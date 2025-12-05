package com.salvagesack;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;

import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@PluginDescriptor(
	name = "Salvage Sack",
	description = "Tracks salvage loot from the Sailing skill by shipwreck type",
	tags = {"sailing", "salvage", "tracking", "loot"}
)
public class SalvageSackPlugin extends Plugin
{
	// Pattern to match salvage loot messages
	// Example: "You salvage a Plank from the Small Shipwreck."
	private static final Pattern SALVAGE_PATTERN = Pattern.compile(
		"You salvage (?:an? )?(.+?) from the (.+?)\\.",
		Pattern.CASE_INSENSITIVE
	);

	@Inject
	private Client client;

	@Inject
	private SalvageSackConfig config;

	@Inject
	private ClientToolbar clientToolbar;

	@Inject
	private ConfigManager configManager;

	private SalvageSackPanel panel;
	private NavigationButton navButton;
	private SalvageDataManager dataManager;
	private Map<ShipwreckType, SalvageData> salvageDataMap;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Salvage Sack started!");

		// Initialize data structures
		salvageDataMap = new HashMap<>();
		
		// Initialize data manager
		File dataDirectory = new File(configManager.getConfigurationDirectory(), "salvagesack");
		if (!dataDirectory.exists())
		{
			dataDirectory.mkdirs();
		}
		dataManager = new SalvageDataManager(dataDirectory);
		
		// Load saved data
		Map<ShipwreckType, SalvageData> loadedData = dataManager.loadData();
		if (loadedData != null && !loadedData.isEmpty())
		{
			salvageDataMap.putAll(loadedData);
			log.info("Loaded {} shipwreck types from saved data", loadedData.size());
		}

		// Initialize panel
		panel = new SalvageSackPanel();
		panel.updateData(salvageDataMap);

		// Create navigation button
		final BufferedImage icon = ImageUtil.loadImageResource(getClass(), "/icon.png");
		navButton = NavigationButton.builder()
			.tooltip("Salvage Sack")
			.icon(icon)
			.priority(5)
			.panel(panel)
			.build();

		clientToolbar.addNavigation(navButton);
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Salvage Sack stopped!");

		// Save data before shutdown
		if (dataManager != null && salvageDataMap != null)
		{
			dataManager.saveData(salvageDataMap);
			log.info("Saved salvage data");
		}

		// Clean up UI
		if (navButton != null)
		{
			clientToolbar.removeNavigation(navButton);
		}
	}

	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		if (event.getType() != ChatMessageType.GAMEMESSAGE && 
		    event.getType() != ChatMessageType.SPAM)
		{
			return;
		}

		String message = event.getMessage();
		Matcher matcher = SALVAGE_PATTERN.matcher(message);

		if (matcher.find())
		{
			String itemName = matcher.group(1).trim();
			String shipwreckName = matcher.group(2).trim();

			log.debug("Detected salvage: {} from {}", itemName, shipwreckName);

			// Determine shipwreck type
			ShipwreckType shipwreckType = ShipwreckType.fromString(shipwreckName);
			
			// Get or create salvage data for this shipwreck type
			SalvageData data = salvageDataMap.computeIfAbsent(
				shipwreckType, 
				type -> new SalvageData(type)
			);

			// Increment total loots
			data.incrementTotalLoots();

			// Get item ID (for now, we'll use a hash of the name as placeholder)
			// In a real implementation, this would query the item database
			int itemId = itemName.hashCode() & 0x7FFFFFFF;

			// Get expected drop rate (default to 0 for unknown items)
			double expectedRate = getExpectedDropRate(shipwreckType, itemName);

			// Record the loot
			data.recordLoot(itemId, itemName, expectedRate);

			// Update the panel
			panel.updateData(salvageDataMap);

			// Save data
			dataManager.saveData(salvageDataMap);

			log.debug("Recorded salvage: {} (ID: {}) from {}", itemName, itemId, shipwreckType);
		}
	}

	/**
	 * Get expected drop rate for an item from a specific shipwreck type
	 * This should ideally be loaded from a configuration or wiki data
	 */
	private double getExpectedDropRate(ShipwreckType shipwreckType, String itemName)
	{
		// Placeholder expected drop rates
		// In a real implementation, these would come from wiki data or configuration
		
		// Common items (~50%)
		if (itemName.equalsIgnoreCase("Plank") || 
		    itemName.equalsIgnoreCase("Logs") ||
		    itemName.equalsIgnoreCase("Rope"))
		{
			return 0.50;
		}
		
		// Uncommon items (~25%)
		if (itemName.equalsIgnoreCase("Steel bar") || 
		    itemName.equalsIgnoreCase("Coal") ||
		    itemName.equalsIgnoreCase("Iron ore"))
		{
			return 0.25;
		}
		
		// Rare items (~10%)
		if (itemName.equalsIgnoreCase("Gold ore") || 
		    itemName.equalsIgnoreCase("Mithril bar"))
		{
			return 0.10;
		}
		
		// Very rare items (~5%)
		if (itemName.equalsIgnoreCase("Adamant bar") || 
		    itemName.equalsIgnoreCase("Rune ore"))
		{
			return 0.05;
		}

		// Default for unknown items
		return 0.0;
	}

	@Provides
	SalvageSackConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(SalvageSackConfig.class);
	}
}
