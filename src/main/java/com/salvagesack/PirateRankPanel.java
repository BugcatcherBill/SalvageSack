package com.salvagesack;

import lombok.Setter;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.util.ImageUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Panel that displays pirate rank progression with a circular dial and progress bar
 */
public class PirateRankPanel extends JPanel
{
	private static final int DIAL_SIZE = 120;
	private static final Color RANK_UP_COLOR = new Color(255, 215, 0); // Bright gold (level up glow)
	private static final Map<String, BufferedImage> iconCache = new HashMap<>();

	@Setter
	private PirateRankData rankData;
	
	private final JLabel rankTitleLabel;
	private final JLabel rankDescriptionLabel;
	private final JLabel bootyLabel;
	private final JLabel progressLabel;
	private final JProgressBar progressBar;
	private final DialPanel dialPanel;
	
	private boolean showRankUpEffect = false;
	private long rankUpEffectStartTime = 0;
	private Timer rankUpEffectTimer = null;
	private static final long RANK_UP_EFFECT_DURATION = 2000; // 2 seconds
	private static final int ANIMATION_FRAME_DELAY_MS = 50; // 20 FPS

	public PirateRankPanel()
	{
		setLayout(new BorderLayout(0, 8));
		setBackground(ColorScheme.DARKER_GRAY_COLOR);
		setBorder(new EmptyBorder(10, 10, 10, 10));

		// Dial panel
		dialPanel = new DialPanel();
		dialPanel.setPreferredSize(new Dimension(DIAL_SIZE, DIAL_SIZE));
		dialPanel.setMaximumSize(new Dimension(DIAL_SIZE, DIAL_SIZE));
		dialPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Rank info panel
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		infoPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		infoPanel.setBorder(new EmptyBorder(5, 0, 5, 0));

		rankTitleLabel = new JLabel("Castaway");
		rankTitleLabel.setForeground(Color.WHITE);
		rankTitleLabel.setFont(new Font("Serif", Font.BOLD, 18));
		rankTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		rankTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

		rankDescriptionLabel = new JLabel("Cast ashore with nothing");
		rankDescriptionLabel.setForeground(Color.LIGHT_GRAY);
		rankDescriptionLabel.setFont(new Font("Arial", Font.ITALIC, 11));
		rankDescriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		rankDescriptionLabel.setHorizontalAlignment(SwingConstants.CENTER);

		bootyLabel = new JLabel("Booty: 0 gp");
		bootyLabel.setForeground(new Color(218, 165, 32));
		bootyLabel.setFont(new Font("Arial", Font.BOLD, 12));
		bootyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		infoPanel.add(rankTitleLabel);
		infoPanel.add(Box.createVerticalStrut(2));
		infoPanel.add(rankDescriptionLabel);
		infoPanel.add(Box.createVerticalStrut(5));
		infoPanel.add(bootyLabel);

		// Progress section
		JPanel progressPanel = new JPanel();
		progressPanel.setLayout(new BoxLayout(progressPanel, BoxLayout.Y_AXIS));
		progressPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		progressPanel.setBorder(new EmptyBorder(5, 0, 0, 0));

		progressLabel = new JLabel("Next rank: 22,021 gp");
		progressLabel.setForeground(Color.LIGHT_GRAY);
		progressLabel.setFont(new Font("Arial", Font.PLAIN, 10));
		progressLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		progressBar = new JProgressBar(0, 1000);
		progressBar.setValue(0);
		progressBar.setStringPainted(false);
		progressBar.setPreferredSize(new Dimension(200, 20));
		progressBar.setMaximumSize(new Dimension(200, 20));
		progressBar.setBackground(new Color(60, 60, 60));
		progressBar.setForeground(new Color(218, 165, 32));
		progressBar.setBorderPainted(true);
		progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);

		progressPanel.add(progressLabel);
		progressPanel.add(Box.createVerticalStrut(3));
		progressPanel.add(progressBar);

		// Add all components
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		centerPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		centerPanel.add(dialPanel);
		centerPanel.add(Box.createVerticalStrut(10));
		centerPanel.add(infoPanel);
		centerPanel.add(progressPanel);

		add(centerPanel, BorderLayout.CENTER);

		// Initialize with default data
		if (rankData == null)
		{
			rankData = new PirateRankData();
		}
		updateDisplay();
	}

	/**
	 * Update the display with current rank data
	 */
	public void updateDisplay()
	{
		if (rankData == null)
		{
			return;
		}

		SwingUtilities.invokeLater(() -> {
			PirateRank rank = rankData.getCurrentRank();
			
			// Update labels
			rankTitleLabel.setText(rank.getDisplayName());
			rankDescriptionLabel.setText(rank.getDescription());
			
			NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
			bootyLabel.setText("Booty: " + numberFormat.format(rankData.getTotalBooty()) + " gp");

			// Update progress bar with rank's arc color
			double progress = rankData.getProgressToNextRank();
			progressBar.setValue((int) (progress * 1000));
			progressBar.setForeground(rank.getArcColor());

			// Update progress label
			PirateRank nextRank = rank.getNextRank();
			if (nextRank != null)
			{
				long needed = rankData.getBootyNeededForNextRank();
				progressLabel.setText("Next rank: " + numberFormat.format(needed) + " gp");
			}
			else
			{
				progressLabel.setText("Maximum rank achieved!");
			}

			// Check for rank up effect
			if (rankData.isJustRankedUp())
			{
				triggerRankUpEffect();
				rankData.clearRankUpFlag();
			}

			dialPanel.repaint();
		});
	}

	/**
	 * Trigger the rank up visual effect
	 */
	private void triggerRankUpEffect()
	{
		stopRankUpEffect();
		startRankUpEffect();
	}

	/**
	 * Stop any existing rank up animation
	 */
	private void stopRankUpEffect()
	{
		if (rankUpEffectTimer != null && rankUpEffectTimer.isRunning())
		{
			rankUpEffectTimer.stop();
		}
	}

	/**
	 * Start the rank up animation
	 */
	private void startRankUpEffect()
	{
		showRankUpEffect = true;
		rankUpEffectStartTime = System.currentTimeMillis();
		
		rankUpEffectTimer = new Timer(ANIMATION_FRAME_DELAY_MS, null);
		rankUpEffectTimer.addActionListener(e -> {
			long elapsed = System.currentTimeMillis() - rankUpEffectStartTime;
			if (elapsed >= RANK_UP_EFFECT_DURATION)
			{
				showRankUpEffect = false;
				rankUpEffectTimer.stop();
			}
			dialPanel.repaint();
		});
		rankUpEffectTimer.start();
	}

	/**
	 * Inner class for the circular dial display
	 */
	private class DialPanel extends JPanel
	{
		@Override
		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g.create();
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			int width = getWidth();
			int height = getHeight();
			int centerX = width / 2;
			int centerY = height / 2;
			int diameter = Math.min(width, height) - 10;
			int x = centerX - diameter / 2;
			int y = centerY - diameter / 2;

			// Get current rank colors
			Color circleColor = new Color(139, 69, 19); // Default brown
			Color arcColor = new Color(218, 165, 32); // Default gold
			
			if (rankData != null)
			{
				PirateRank rank = rankData.getCurrentRank();
				circleColor = rank.getCircleColor();
				arcColor = rank.getArcColor();
			}

			// Draw background circle
			g2d.setColor(new Color(40, 40, 40));
			g2d.fill(new Ellipse2D.Double(x, y, diameter, diameter));

			// Draw progress arc
			if (rankData != null)
			{
				double progress = rankData.getProgressToNextRank();
				double angle = 360.0 * progress;

				// Draw progress arc using rank's arc color
				g2d.setColor(arcColor);
				g2d.setStroke(new BasicStroke(6));
				Arc2D.Double arc = new Arc2D.Double(x, y, diameter, diameter, 90, -angle, Arc2D.OPEN);
				g2d.draw(arc);
			}

			// Draw border using rank's circle color
			g2d.setColor(circleColor);
			g2d.setStroke(new BasicStroke(3));
			g2d.draw(new Ellipse2D.Double(x, y, diameter, diameter));

			// Rank up glow effect
			if (showRankUpEffect)
			{
				long elapsed = System.currentTimeMillis() - rankUpEffectStartTime;
				float alpha = 1.0f - ((float) elapsed / RANK_UP_EFFECT_DURATION);
				alpha = Math.max(0, Math.min(1, alpha));
				
				g2d.setColor(new Color(RANK_UP_COLOR.getRed(), RANK_UP_COLOR.getGreen(), 
					RANK_UP_COLOR.getBlue(), (int) (alpha * 150)));
				g2d.setStroke(new BasicStroke(8));
				g2d.draw(new Ellipse2D.Double(x - 2, y - 2, diameter + 4, diameter + 4));
			}

			// Draw rank icon
			if (rankData != null)
			{
				PirateRank rank = rankData.getCurrentRank();
				BufferedImage icon = loadRankIcon(rank.getIconFileName());
				
				if (icon != null)
				{
					// Draw icon centered in the dial with more precise centering
					int iconSize = diameter - 30; // Leave some padding from the arc
					// Use floating point for precise centering, then convert to int
					double iconXDouble = centerX - (iconSize / 2.0);
					double iconYDouble = centerY - (iconSize / 2.0);
					int iconX = (int) Math.round(iconXDouble);
					int iconY = (int) Math.round(iconYDouble);
					g2d.drawImage(icon, iconX, iconY, iconSize, iconSize, null);
				}
				else
				{
					// Fallback: draw rank number if icon not available
					String rankOrdinal = String.valueOf(rank.ordinal() + 1);
					g2d.setColor(Color.WHITE);
					g2d.setFont(new Font("Serif", Font.BOLD, 36));
					FontMetrics fm = g2d.getFontMetrics();
					int textWidth = fm.stringWidth(rankOrdinal);
					int textHeight = fm.getAscent();
					g2d.drawString(rankOrdinal, centerX - textWidth / 2, centerY + textHeight / 2);
				}
			}

			g2d.dispose();
		}
	}

	/**
	 * Load a rank icon from resources with caching
	 * @param iconFileName The icon file name
	 * @return The icon image, or null if not found
	 */
	private BufferedImage loadRankIcon(String iconFileName)
	{
		if (iconFileName == null || iconFileName.isEmpty())
		{
			return null;
		}

		// Check cache first
		if (iconCache.containsKey(iconFileName))
		{
			return iconCache.get(iconFileName);
		}

		// Try to load the icon
		try
		{
			String iconPath = "/PirateRankIcons/" + iconFileName;
			BufferedImage icon = ImageUtil.loadImageResource(getClass(), iconPath);
			if (icon != null)
			{
				iconCache.put(iconFileName, icon);
				return icon;
			}
		}
		catch (Exception e)
		{
			// Icon not found, return null (will use fallback rendering)
		}

		// Cache the null result to avoid repeated load attempts
		iconCache.put(iconFileName, null);
		return null;
	}
}
