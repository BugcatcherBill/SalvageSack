package com.salvagesack;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.util.ImageUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

/**
 * Panel that displays salvage tracking information
 */
@Slf4j
public class SalvageSackPanel extends PluginPanel
{
	private final JPanel contentPanel;
	private Map<ShipwreckType, SalvageData> salvageDataMap;

	public SalvageSackPanel()
	{
		super(false);
		setBackground(ColorScheme.DARK_GRAY_COLOR);
		setLayout(new BorderLayout());

		JPanel titlePanel = new JPanel(new BorderLayout());
		titlePanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		titlePanel.setBorder(new EmptyBorder(10, 10, 10, 10));

		JLabel title = new JLabel("Salvage Sack");
		title.setForeground(Color.WHITE);
		title.setFont(new Font("Arial", Font.BOLD, 16));
		titlePanel.add(title, BorderLayout.WEST);

		add(titlePanel, BorderLayout.NORTH);

		contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		contentPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);

		JScrollPane scrollPane = new JScrollPane(contentPanel);
		scrollPane.setBackground(ColorScheme.DARK_GRAY_COLOR);
		scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		add(scrollPane, BorderLayout.CENTER);
	}

	/**
	 * Update the panel with new salvage data
	 */
	public void updateData(Map<ShipwreckType, SalvageData> dataMap)
	{
		this.salvageDataMap = dataMap;
		rebuild();
	}

	/**
	 * Rebuild the entire panel
	 */
	private void rebuild()
	{
		SwingUtilities.invokeLater(() -> {
			contentPanel.removeAll();

			if (salvageDataMap == null || salvageDataMap.isEmpty())
			{
				JLabel emptyLabel = new JLabel("No salvage data yet");
				emptyLabel.setForeground(Color.GRAY);
				emptyLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
				contentPanel.add(emptyLabel);
			}
			else
			{
				// Add each shipwreck type section
				for (ShipwreckType type : ShipwreckType.values())
				{
					if (type == ShipwreckType.UNKNOWN)
					{
						continue;
					}

					SalvageData data = salvageDataMap.get(type);
					if (data != null && data.getTotalLoots() > 0)
					{
						contentPanel.add(createShipwreckPanel(data));
						contentPanel.add(Box.createVerticalStrut(10));
					}
				}

				// Add unknown shipwrecks at the end if any
				SalvageData unknownData = salvageDataMap.get(ShipwreckType.UNKNOWN);
				if (unknownData != null && unknownData.getTotalLoots() > 0)
				{
					contentPanel.add(createShipwreckPanel(unknownData));
				}
			}

			contentPanel.revalidate();
			contentPanel.repaint();
		});
	}

	/**
	 * Create a panel for a specific shipwreck type
	 */
	private JPanel createShipwreckPanel(SalvageData data)
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));

		// Header with shipwreck type and total loots
		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		JLabel typeLabel = new JLabel(data.getShipwreckType().getDisplayName());
		typeLabel.setForeground(Color.WHITE);
		typeLabel.setFont(new Font("Arial", Font.BOLD, 14));

		JLabel totalLabel = new JLabel("Total: " + data.getTotalLoots());
		totalLabel.setForeground(Color.LIGHT_GRAY);

		headerPanel.add(typeLabel, BorderLayout.WEST);
		headerPanel.add(totalLabel, BorderLayout.EAST);
		panel.add(headerPanel);
		panel.add(Box.createVerticalStrut(5));

		// Add each item
		for (SalvageItem item : data.getItems().values())
		{
			panel.add(createItemPanel(item, data.getTotalLoots()));
		}

		return panel;
	}

	/**
	 * Create a panel for a specific item
	 */
	private JPanel createItemPanel(SalvageItem item, int totalLoots)
	{
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		panel.setBorder(new EmptyBorder(2, 10, 2, 5));
		panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

		// Left side: item icon and name
		JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
		leftPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		// Item icon placeholder (will be replaced with actual wiki images)
		JLabel iconLabel = new JLabel();
		iconLabel.setPreferredSize(new Dimension(24, 24));
		iconLabel.setBackground(ColorScheme.DARK_GRAY_COLOR);
		iconLabel.setOpaque(true);
		leftPanel.add(iconLabel);

		JLabel nameLabel = new JLabel(item.getItemName());
		nameLabel.setForeground(Color.WHITE);
		leftPanel.add(nameLabel);

		// Right side: drop count and rates
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		JLabel countLabel = new JLabel(String.format("x%d", item.getDropCount()));
		countLabel.setForeground(Color.WHITE);
		countLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

		double currentRate = item.getCurrentDropRate(totalLoots) * 100;
		double expectedRate = item.getExpectedDropRate() * 100;
		
		String rateText = String.format("%.2f%% (%.2f%%)", currentRate, expectedRate);
		JLabel rateLabel = new JLabel(rateText);
		rateLabel.setForeground(Color.LIGHT_GRAY);
		rateLabel.setFont(new Font("Arial", Font.PLAIN, 10));
		rateLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

		rightPanel.add(countLabel);
		rightPanel.add(rateLabel);

		panel.add(leftPanel, BorderLayout.WEST);
		panel.add(rightPanel, BorderLayout.EAST);

		return panel;
	}
}
