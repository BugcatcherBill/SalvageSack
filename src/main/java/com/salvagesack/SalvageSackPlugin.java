package com.salvagesack;

import com.google.gson.Gson;
import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.RuneLite;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;
import net.runelite.http.api.item.ItemPrice;

import javax.inject.Inject;
import java.awt.*;
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
@SuppressWarnings("unused") // Fields and methods are used by RuneLite's dependency injection and event system
public class SalvageSackPlugin extends Plugin
{
	// Pattern to match salvage loot messages
	// Salvage types: Small, Fishy, Barracuda, Large, Pirate, Martial, Fremennik, Opulent
	// Example: "You sort through the Martial salvage and find: 1 x Adamant 2h sword."
	private static final Pattern SALVAGE_PATTERN = Pattern.compile(
		"You sort through the (.+?) salvage and find: (\\d+) x (.+?)\\.",
		Pattern.CASE_INSENSITIVE
	);

	@Inject
	private ClientToolbar clientToolbar;

	@Inject
	private ItemManager itemManager;

	@Inject
	private Gson gson;

	@Inject
	private SalvageSackConfig config;

	@Inject
	private ConfigManager configManager;

	private SalvageSackPanel panel;
	private NavigationButton navButton;
	private SalvageDataManager dataManager;
	private DropRateManager dropRateManager;
	private PirateRankDataManager pirateRankDataManager;
	private Map<ShipwreckType, SalvageData> salvageDataMap;
	private PirateRankData pirateRankData;

	@Override
	protected void startUp()
	{
		log.info("Salvage Sack started!");

		// Initialize data structures
		salvageDataMap = new HashMap<>();
		
		// Initialize icon manager
		ItemIconManager iconManager = new ItemIconManager();
		iconManager.setItemManager(itemManager);

		// Initialize data manager
		File runeliteDir = RuneLite.RUNELITE_DIR;
		if (runeliteDir == null)
		{
			runeliteDir = new File(System.getProperty("user.home"), ".runelite");
		}
		File dataDirectory = new File(runeliteDir, "salvagesack");
		if (!dataDirectory.exists())
		{
			boolean created = dataDirectory.mkdirs();
			if (!created)
			{
				log.warn("Failed to create data directory: {}", dataDirectory.getAbsolutePath());
			}
		}
		dataManager = new SalvageDataManager(dataDirectory, gson);

		// Initialize drop rate manager
		dropRateManager = new DropRateManager(dataDirectory, gson);

		// Initialize pirate rank data manager
		pirateRankDataManager = new PirateRankDataManager(dataDirectory, gson);

		// Load saved data
		Map<ShipwreckType, SalvageData> loadedData = dataManager.loadData();
		if (loadedData != null && !loadedData.isEmpty())
		{
			salvageDataMap.putAll(loadedData);
			log.info("Loaded {} shipwreck types from saved data", loadedData.size());
		}

		// Load pirate rank data
		pirateRankData = pirateRankDataManager.loadData();
		log.info("Loaded pirate rank data: {} with {} booty", 
			pirateRankData.getCurrentRank().getDisplayName(), pirateRankData.getTotalBooty());

		// Initialize panel
		panel = new SalvageSackPanel(iconManager, config);
		panel.setDropRateManager(dropRateManager);
		panel.setConfigManager(configManager);
		panel.setOnResetShipwreck(this::resetShipwreckData);
		panel.setOnResetAll(this::resetAllData);
		panel.setPirateRankData(pirateRankData);
		panel.updateData(salvageDataMap);

		// Set up icon loaded callback to repaint panel when async icons finish loading
		iconManager.setOnIconLoaded(() -> {
			if (panel != null)
			{
				panel.repaint();
			}
		});

		// Create navigation button
		BufferedImage icon;
		try
		{
			icon = ImageUtil.loadImageResource(getClass(), "/icon.png");
		}
		catch (Exception e)
		{
			log.warn("Failed to load icon, using fallback", e);
			// Create a visible fallback icon
			icon = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = icon.createGraphics();
			g.setColor(new Color(66, 134, 244)); // Blue color
			g.fillRect(0, 0, 16, 16);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", Font.BOLD, 12));
			g.drawString("S", 4, 13);
			g.dispose();
		}

		navButton = NavigationButton.builder()
			.tooltip("Salvage Sack")
			.icon(icon)
			.priority(5)
			.panel(panel)
			.build();

		clientToolbar.addNavigation(navButton);
		log.info("Navigation button added to toolbar");
	}

	@Override
	protected void shutDown()
	{
		log.info("Salvage Sack stopped!");

		// Save data before shutdown
		if (dataManager != null && salvageDataMap != null)
		{
			dataManager.saveData(salvageDataMap);
			log.info("Saved salvage data");
		}

		// Save pirate rank data
		if (pirateRankDataManager != null && pirateRankData != null)
		{
			pirateRankDataManager.saveData(pirateRankData);
			log.info("Saved pirate rank data");
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

		// Strip HTML tags from the message
		String message = event.getMessage()
			.replaceAll("<[^>]*>", "")
			.replaceAll("&lt;", "<")
			.replaceAll("&gt;", ">")
			.replaceAll("&amp;", "&");

		// Log all game messages for debugging (can be removed later)
		if (message.toLowerCase().contains("salvage"))
		{
			log.info("Salvage message detected: {}", message);
		}

		Matcher matcher = SALVAGE_PATTERN.matcher(message);

		if (matcher.find())
		{
			String salvageType = matcher.group(1).trim();  // e.g., "martial"
			int quantity = Integer.parseInt(matcher.group(2).trim());  // e.g., 1
			String itemName = matcher.group(3).trim();  // e.g., "Adamant 2h sword"

			log.info("Parsed salvage: type='{}' quantity={} item='{}'", salvageType, quantity, itemName);

			// Determine shipwreck type from salvage type
			ShipwreckType shipwreckType = ShipwreckType.fromString(salvageType);

			// Get or create salvage data for this shipwreck type
			SalvageData data = salvageDataMap.computeIfAbsent(
				shipwreckType, 
				SalvageData::new
			);

			// Increment total loots
			data.incrementTotalLoots();

			// Look up item ID using ItemManager
			int itemId = lookupItemId(itemName);

			// Get expected drop rate (default to 0 for unknown items)
			double expectedRate = getExpectedDropRate(shipwreckType, itemName);

			// Record the loot with quantity
			data.recordLoot(itemId, itemName, expectedRate, quantity);

			// Update pirate rank if enabled
			if (config.enablePirateRanks() && pirateRankData != null && itemManager != null)
			{
				int highAlchValue = getHighAlchValue(itemId);
				// Validate quantity is reasonable (prevent DoS with extreme values)
				if (highAlchValue > 0 && quantity > 0 && quantity <= 1000000)
				{
					// Cast to long before multiplication to prevent overflow
					long bootyGained = ((long) highAlchValue) * quantity;
					boolean rankedUp = pirateRankData.addBooty(bootyGained);
					
					if (rankedUp)
					{
						log.info("Ranked up to {}!", pirateRankData.getCurrentRank().getDisplayName());
					}
					
					// Save pirate rank data
					pirateRankDataManager.saveData(pirateRankData);
					
					// Update panel
					if (panel != null)
					{
						panel.updatePirateRankDisplay();
					}
				}
			}

			// Update the panel
			panel.updateData(salvageDataMap);

			// Save data
			dataManager.saveData(salvageDataMap);

			log.info("Recorded salvage: {}x {} (ID: {}) from {}", quantity, itemName, itemId, shipwreckType);
		}
	}

	/**
	 * Look up an item ID by name using the ItemManager
	 */
	private int lookupItemId(String itemName)
	{
		if (itemManager != null)
		{
			try
			{
				// Search for the item by name
				int itemId = itemManager.search(itemName).stream()
					.findFirst()
					.map(ItemPrice::getId)
					.orElse(-1);

				if (itemId != -1)
				{
					log.debug("Found item ID {} for '{}'", itemId, itemName);
					return itemId;
				}
			}
			catch (Exception e)
			{
				log.debug("Failed to look up item ID for '{}': {}", itemName, e.getMessage());
			}
		}

		// Fallback to hash-based ID if lookup fails
		return itemName.hashCode() & 0x7FFFFFFF;
	}

	/**
	 * Get expected drop rate for an item from a specific shipwreck type
	 * Loads rates from drop_rates.json configuration file
	 */
	private double getExpectedDropRate(ShipwreckType shipwreckType, String itemName)
	{
		if (dropRateManager != null)
		{
			return dropRateManager.getExpectedDropRate(shipwreckType, itemName);
		}
		return 0.0;
	}

	/**
	 * Get the high alch value of an item
	 * @param itemId The item ID
	 * @return High alch value in GP, or 0 if unknown
	 */
	private int getHighAlchValue(int itemId)
	{
		if (itemManager != null && itemId > 0)
		{
			try
			{
				// ItemManager provides item stats which include high alch value
				return itemManager.getItemComposition(itemId).getHaPrice();
			}
			catch (Exception e)
			{
				log.debug("Failed to get high alch value for item {}: {}", itemId, e.getMessage());
			}
		}
		return 0;
	}


	private void resetShipwreckData(ShipwreckType type)
	{
		salvageDataMap.remove(type);
		panel.updateData(salvageDataMap);
		dataManager.saveData(salvageDataMap);
		log.info("Reset data for {}", type.getDisplayName());
	}

	private void resetAllData()
	{
		salvageDataMap.clear();
		panel.updateData(salvageDataMap);
		dataManager.saveData(salvageDataMap);
		log.info("Reset all salvage data");
	}

	@Provides
	SalvageSackConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(SalvageSackConfig.class);
	}
}
