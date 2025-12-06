package com.salvagesack;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Panel that displays salvage tracking information
 * <p>
 * The panel includes sorting controls that allow users to organize items
 * by different criteria including alphabetical order, current drop rate,
 * expected drop rate, and quantity. Sort direction can be toggled between
 * ascending and descending.
 * </p>
 */
@Slf4j
public class SalvageSackPanel extends PluginPanel
{
	private static final String CONFIG_GROUP = "salvagesack";
	private static final Color LUCK_GOOD = new Color(0, 200, 83);      // Green - lucky
	private static final Color LUCK_NEUTRAL = new Color(255, 214, 0); // Yellow - expected
	private static final Color LUCK_BAD = new Color(255, 68, 68);     // Red - unlucky
	private static final String ARROW_RIGHT = "▶";
	private static final String ARROW_DOWN = "▼";

	private final JPanel contentPanel;
	private final ItemIconManager iconManager;
	private final SalvageSackConfig config;
	private final Map<ShipwreckType, Boolean> expandedState = new HashMap<>();
	private final JLabel totalOpensLabel;
	private Map<ShipwreckType, SalvageData> salvageDataMap;
	private SortOption currentSortOption;
	private boolean currentSortDescending;
	private PirateRankPanel pirateRankPanel;

	@lombok.Setter
	private DropRateManager dropRateManager;

	@lombok.Setter
	private Consumer<ShipwreckType> onResetShipwreck;

	@lombok.Setter
	private Runnable onResetAll;

	@lombok.Setter
	private ConfigManager configManager;

	@lombok.Setter
	private PirateRankData pirateRankData;

	public SalvageSackPanel(ItemIconManager iconManager, SalvageSackConfig config)
	{
		super(false);
		this.iconManager = iconManager;
		this.config = config;
		this.currentSortOption = config.sortOption();
		this.currentSortDescending = config.sortDescending();
		setBackground(ColorScheme.DARK_GRAY_COLOR);
		setLayout(new BorderLayout());

		JPanel titlePanel = new JPanel(new BorderLayout());
		titlePanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		titlePanel.setBorder(new EmptyBorder(10, 12, 10, 12));

		// Right side: Stats and controls organized in a grid
		JPanel infoPanel = new JPanel(new GridBagLayout());
		infoPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(0, 8, 0, 0);
		
		// Total salvage label
		totalOpensLabel = new JLabel("0 Total Salvage Sorted");
		totalOpensLabel.setForeground(Color.WHITE);
		totalOpensLabel.setFont(new Font("Arial", Font.BOLD, 11));
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(0, 0, 4, 0);
		infoPanel.add(totalOpensLabel, gbc);
		
		// Sort dropdown
		JComboBox<SortOption> sortComboBox = new JComboBox<>(SortOption.values());
		sortComboBox.setSelectedItem(currentSortOption);
		sortComboBox.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		sortComboBox.setForeground(Color.WHITE);
		sortComboBox.setFont(new Font("Arial", Font.PLAIN, 10));
		sortComboBox.setFocusable(false);
		sortComboBox.setBorder(new CompoundBorder(
			new LineBorder(ColorScheme.MEDIUM_GRAY_COLOR, 1),
			new EmptyBorder(2, 4, 2, 4)
		));
		sortComboBox.addActionListener(e -> {
			SortOption selected = (SortOption) sortComboBox.getSelectedItem();
			if (selected != null)
			{
				currentSortOption = selected;
				if (configManager != null)
				{
					configManager.setConfiguration(CONFIG_GROUP, "sortOption", selected);
				}
				rebuild();
			}
		});
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(0, 0, 0, 4);
		infoPanel.add(sortComboBox, gbc);
		
		// Sort direction button
		JButton sortDirectionButton = new JButton(currentSortDescending ? "↓" : "↑");
		sortDirectionButton.setFont(new Font("Arial", Font.BOLD, 14));
		sortDirectionButton.setPreferredSize(new Dimension(28, 24));
		sortDirectionButton.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		sortDirectionButton.setForeground(Color.WHITE);
		sortDirectionButton.setFocusable(false);
		sortDirectionButton.setBorder(new LineBorder(ColorScheme.MEDIUM_GRAY_COLOR, 1));
		sortDirectionButton.setToolTipText(currentSortDescending ? "Descending" : "Ascending");
		sortDirectionButton.addActionListener(e -> {
			currentSortDescending = !currentSortDescending;
			sortDirectionButton.setText(currentSortDescending ? "↓" : "↑");
			sortDirectionButton.setToolTipText(currentSortDescending ? "Descending" : "Ascending");
			if (configManager != null)
			{
				configManager.setConfiguration(CONFIG_GROUP, "sortDescending", currentSortDescending);
			}
			rebuild();
		});
		// Add hover effect
		sortDirectionButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				sortDirectionButton.setBackground(ColorScheme.DARK_GRAY_HOVER_COLOR);
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				sortDirectionButton.setBackground(ColorScheme.DARKER_GRAY_COLOR);
			}
		});
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.insets = new Insets(0, 0, 0, 0);
		infoPanel.add(sortDirectionButton, gbc);
		
		titlePanel.add(infoPanel, BorderLayout.EAST);

		add(titlePanel, BorderLayout.NORTH);

		contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		contentPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		JScrollPane scrollPane = new JScrollPane(contentPanel);
		scrollPane.setBackground(ColorScheme.DARK_GRAY_COLOR);
		scrollPane.getViewport().setBackground(ColorScheme.DARK_GRAY_COLOR);
		scrollPane.setBorder(null);
		scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		add(scrollPane, BorderLayout.CENTER);

		// Initialize pirate rank panel but don't add it yet (will be added in rebuild based on config)
		pirateRankPanel = new PirateRankPanel();

		rebuild();
	}


	public void updateData(Map<ShipwreckType, SalvageData> dataMap)
	{
		this.salvageDataMap = dataMap;

		// Initialize expanded state for new shipwreck types (expanded if has data)
		if (dataMap != null)
		{
			for (ShipwreckType type : dataMap.keySet())
			{
				if (!expandedState.containsKey(type))
				{
					SalvageData data = dataMap.get(type);
					expandedState.put(type, data != null && data.getTotalLoots() > 0);
				}
			}
		}

		// Update pirate rank display if data is available
		if (pirateRankData != null && pirateRankPanel != null)
		{
			pirateRankPanel.setRankData(pirateRankData);
			pirateRankPanel.updateDisplay();
		}

		rebuild();
	}

	private void rebuild()
	{
		SwingUtilities.invokeLater(() -> {
			contentPanel.removeAll();

			// Add pirate rank panel at the top if enabled
			if (config.enablePirateRanks() && pirateRankPanel != null)
			{
				pirateRankPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
				contentPanel.add(pirateRankPanel);
				contentPanel.add(Box.createVerticalStrut(10));
			}

			// Calculate total opens across all shipwreck types
			int totalOpens = 0;
			if (salvageDataMap != null)
			{
				for (SalvageData data : salvageDataMap.values())
				{
					totalOpens += data.getTotalLoots();
				}
			}
			totalOpensLabel.setText(totalOpens + " Total Salvage Sorted");

			if (salvageDataMap == null || salvageDataMap.isEmpty())
			{
				JLabel emptyLabel = new JLabel("No salvage data yet");
				emptyLabel.setForeground(Color.GRAY);
				emptyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
				emptyLabel.setBorder(new EmptyBorder(10, 5, 10, 5));
				contentPanel.add(emptyLabel);
			}
			else
			{
				// Collect all shipwrecks with data and sort by lastUpdated (most recent first)
				List<SalvageData> shipwrecksWithData = new ArrayList<>();
				for (ShipwreckType type : ShipwreckType.values())
				{
					SalvageData data = salvageDataMap.get(type);
					if (data != null && data.getTotalLoots() > 0)
					{
						shipwrecksWithData.add(data);
					}
				}

				// Sort by lastUpdated descending (most recently updated first)
				shipwrecksWithData.sort(Comparator.comparingLong(SalvageData::getLastUpdated).reversed());

				for (SalvageData data : shipwrecksWithData)
				{
					contentPanel.add(createShipwreckPanel(data));
					contentPanel.add(Box.createVerticalStrut(4));
				}
			}


			contentPanel.revalidate();
			contentPanel.repaint();
		});
	}

	private JPanel createShipwreckPanel(SalvageData data)
	{
		ShipwreckType type = data.getShipwreckType();
		boolean isExpanded = expandedState.getOrDefault(type, data.getTotalLoots() > 0);

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		panel.setBorder(new LineBorder(ColorScheme.MEDIUM_GRAY_COLOR, 1));
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);

		// Header with arrow
		JPanel headerPanel = new JPanel(new BorderLayout(6, 0));
		headerPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		headerPanel.setBorder(new EmptyBorder(4, 6, 4, 6));
		headerPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));

		JLabel arrowLabel = new JLabel(isExpanded ? ARROW_DOWN : ARROW_RIGHT);
		arrowLabel.setForeground(Color.LIGHT_GRAY);
		arrowLabel.setFont(new Font("Arial", Font.PLAIN, 10));

		JLabel typeLabel = new JLabel(data.getShipwreckType().getDisplayName());
		typeLabel.setForeground(Color.WHITE);
		typeLabel.setFont(new Font("Arial", Font.BOLD, 12));

		JLabel totalLabel = new JLabel("Sorts: " + data.getTotalLoots());
		totalLabel.setForeground(Color.LIGHT_GRAY);
		totalLabel.setFont(new Font("Arial", Font.PLAIN, 11));

		JPanel leftHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
		leftHeader.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		leftHeader.add(arrowLabel);
		leftHeader.add(typeLabel);

		headerPanel.add(leftHeader, BorderLayout.WEST);
		headerPanel.add(totalLabel, BorderLayout.EAST);

		// Items panel
		JPanel itemsPanel = new JPanel();
		itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
		itemsPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		itemsPanel.setBorder(new EmptyBorder(2, 4, 4, 4));
		itemsPanel.setVisible(isExpanded);

		// Sort items based on config
		List<SalvageItem> sortedItems = getSortedItems(data);
		for (SalvageItem item : sortedItems)
		{
			itemsPanel.add(createItemPanel(item, data.getTotalLoots(), data.getShipwreckType()));
			itemsPanel.add(Box.createVerticalStrut(2));
		}

		// Click handler for accordion
		headerPanel.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (SwingUtilities.isLeftMouseButton(e))
				{
					boolean newState = !expandedState.getOrDefault(type, true);
					expandedState.put(type, newState);
					arrowLabel.setText(newState ? ARROW_DOWN : ARROW_RIGHT);
					itemsPanel.setVisible(newState);

					// Revalidate the entire content panel to recalculate sizes
					contentPanel.revalidate();
					contentPanel.repaint();
				}
			}

			@Override
			public void mousePressed(MouseEvent e)
			{
				if (SwingUtilities.isRightMouseButton(e))
				{
					showContextMenu(e, type);
				}
			}

			@Override
			public void mouseEntered(MouseEvent e)
			{
				headerPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);
				leftHeader.setBackground(ColorScheme.DARK_GRAY_COLOR);
			}

			@Override
			public void mouseExited(MouseEvent e)
			{
				headerPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
				leftHeader.setBackground(ColorScheme.DARKER_GRAY_COLOR);
			}
		});

		panel.add(headerPanel);
		panel.add(itemsPanel);

		return panel;
	}

	private JPanel createItemPanel(SalvageItem item, int totalLoots, ShipwreckType shipwreckType)
	{
		// Look up expected rate from DropRateManager (dynamically, not from stored value)
		double expectedRate = 0.0;
		if (dropRateManager != null)
		{
			expectedRate = dropRateManager.getExpectedDropRate(shipwreckType, item.getItemName());
		}

		JPanel panel = new JPanel(new BorderLayout(6, 0));
		panel.setBackground(new Color(40, 40, 40));
		panel.setBorder(new CompoundBorder(
			new LineBorder(new Color(60, 60, 60), 1),
			new EmptyBorder(4, 6, 4, 6)
		));
		// Height depends on whether we have expected rate (3 lines) or not (2 lines)
		int panelHeight = expectedRate > 0 ? 54 : 44;
		panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, panelHeight));
		panel.setPreferredSize(new Dimension(0, panelHeight));

		// Left: Icon - give it proper size for 32x32 icons
		BufferedImage icon = iconManager.getItemIcon(item.getItemId());
		JLabel iconLabel = new JLabel();
		if (icon != null)
		{
			iconLabel.setIcon(new ImageIcon(icon));
		}
		iconLabel.setPreferredSize(new Dimension(32, 32));
		iconLabel.setMinimumSize(new Dimension(32, 32));

		// Center: Name and rates
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		infoPanel.setBackground(new Color(40, 40, 40));

		JLabel nameLabel = new JLabel(item.getItemName());
		nameLabel.setForeground(Color.WHITE);
		nameLabel.setFont(new Font("Arial", Font.PLAIN, 11));
		nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

		// Calculate luck color
		double currentRate = item.getCurrentDropRate(totalLoots);
		Color luckColor = getLuckColor(currentRate, expectedRate);

		// Current rate with label
		double currentPct = currentRate * 100;
		JLabel currentLabel = new JLabel(String.format("Current: %.2f%%", currentPct));
		currentLabel.setForeground(luckColor);
		currentLabel.setFont(new Font("Arial", Font.BOLD, 9));
		currentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

		infoPanel.add(nameLabel);
		infoPanel.add(currentLabel);

		// Expected rate with label (only if we have data)
		if (expectedRate > 0)
		{
			double expectedPct = expectedRate * 100;
			JLabel expectedLabel = new JLabel(String.format("Expected: %.2f%%", expectedPct));
			expectedLabel.setForeground(Color.LIGHT_GRAY);
			expectedLabel.setFont(new Font("Arial", Font.PLAIN, 9));
			expectedLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
			infoPanel.add(expectedLabel);
		}

		// Right: Count (show total quantity received)
		JLabel countLabel = new JLabel("x" + item.getTotalQuantity());
		countLabel.setForeground(Color.WHITE);
		countLabel.setFont(new Font("Arial", Font.BOLD, 11));

		panel.add(iconLabel, BorderLayout.WEST);
		panel.add(infoPanel, BorderLayout.CENTER);
		panel.add(countLabel, BorderLayout.EAST);

		return panel;
	}

	/**
	 * Returns a sorted list of SalvageItems based on the current sort option and direction.
	 * Sorting can be by alphabetical order, current drop rate, expected drop rate, or quantity.
	 */
	private List<SalvageItem> getSortedItems(SalvageData data)
	{
		List<SalvageItem> items = new ArrayList<>(data.getItems().values());
		
		Comparator<SalvageItem> comparator;
		
		switch (currentSortOption)
		{
			case ALPHABETICAL:
				comparator = Comparator.comparing(SalvageItem::getItemName);
				break;
			
			case CURRENT_RATE:
				comparator = Comparator.comparingDouble(item -> item.getCurrentDropRate(data.getTotalLoots()));
				break;
			
			case EXPECTED_RATE:
				comparator = Comparator.comparingDouble(SalvageItem::getExpectedDropRate);
				break;
			
			case QUANTITY:
				comparator = Comparator.comparingInt(SalvageItem::getTotalQuantity);
				break;
			
			default:
				comparator = Comparator.comparing(SalvageItem::getItemName);
				break;
		}
		
		if (currentSortDescending)
		{
			comparator = comparator.reversed();
		}
		
		items.sort(comparator);
		return items;
	}

	private Color getLuckColor(double currentRate, double expectedRate)
	{
		if (expectedRate <= 0)
		{
			return Color.LIGHT_GRAY;
		}

		double luckRatio = currentRate / expectedRate;

		if (luckRatio >= 1.5)
		{
			return LUCK_GOOD;
		}
		else if (luckRatio >= 1.1)
		{
			float t = (float) ((luckRatio - 1.1) / 0.4);
			return interpolateColor(LUCK_NEUTRAL, LUCK_GOOD, t);
		}
		else if (luckRatio >= 0.9)
		{
			return LUCK_NEUTRAL;
		}
		else if (luckRatio >= 0.5)
		{
			float t = (float) ((luckRatio - 0.5) / 0.4);
			return interpolateColor(LUCK_BAD, LUCK_NEUTRAL, t);
		}
		else
		{
			return LUCK_BAD;
		}
	}

	private Color interpolateColor(Color c1, Color c2, float t)
	{
		t = Math.max(0, Math.min(1, t));
		int r = (int) (c1.getRed() + t * (c2.getRed() - c1.getRed()));
		int g = (int) (c1.getGreen() + t * (c2.getGreen() - c1.getGreen()));
		int b = (int) (c1.getBlue() + t * (c2.getBlue() - c1.getBlue()));
		return new Color(r, g, b);
	}

	/**
	 * Update the pirate rank display
	 */
	public void updatePirateRankDisplay()
	{
		if (pirateRankPanel != null && pirateRankData != null)
		{
			pirateRankPanel.setRankData(pirateRankData);
			pirateRankPanel.updateDisplay();
		}
	}

	/**
	 * Show context menu for shipwreck section
	 */
	private void showContextMenu(MouseEvent e, ShipwreckType type)
	{
		JPopupMenu menu = new JPopupMenu();

		JMenuItem resetItem = new JMenuItem("Reset " + type.getDisplayName() + " Data");
		resetItem.addActionListener(ev -> {
			int confirm = JOptionPane.showConfirmDialog(
				this,
				"Are you sure you want to reset all data for " + type.getDisplayName() + "?",
				"Confirm Reset",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE
			);
			if (confirm == JOptionPane.YES_OPTION && onResetShipwreck != null)
			{
				onResetShipwreck.accept(type);
			}
		});
		menu.add(resetItem);

		menu.addSeparator();

		JMenuItem resetAllItem = new JMenuItem("Reset All Data");
		resetAllItem.addActionListener(ev -> {
			int confirm = JOptionPane.showConfirmDialog(
				this,
				"Are you sure you want to reset ALL salvage data?",
				"Confirm Reset All",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE
			);
			if (confirm == JOptionPane.YES_OPTION && onResetAll != null)
			{
				onResetAll.run();
			}
		});
		menu.add(resetAllItem);

		menu.show(e.getComponent(), e.getX(), e.getY());
	}
}
