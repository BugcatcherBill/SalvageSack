package com.salvagesack;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.ConfigManager;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Handles persistence of salvage tracking data between sessions using RSProfile configuration.
 * <p>
 * On first load, checks for existing file-based data and migrates it to the RSProfile format.
 * This ensures backwards compatibility with previous plugin versions.
 * </p>
 */
@Slf4j
public class SalvageDataManager
{
	private static final String CONFIG_GROUP = "salvagesack";
	private static final String DATA_KEY = "salvageData";
	private static final String DATA_FILE = "salvage-data.json";

	private final Gson gson;
	private final ConfigManager configManager;
	private final File legacyDataFile;

	/**
	 * Create a SalvageDataManager using RSProfile configuration for persistence.
	 *
	 * @param configManager RuneLite's ConfigManager for RSProfile storage
	 * @param legacyDataDirectory Directory where legacy file-based data may exist (for migration)
	 * @param gson Gson instance for JSON serialization
	 */
	public SalvageDataManager(ConfigManager configManager, File legacyDataDirectory, Gson gson)
	{
		this.configManager = configManager;
		this.gson = gson;
		this.legacyDataFile = legacyDataDirectory != null ? new File(legacyDataDirectory, DATA_FILE) : null;
	}

	/**
	 * Save salvage data to RSProfile configuration
	 */
	public void saveData(Map<ShipwreckType, SalvageData> dataMap)
	{
		SaveDataWrapper wrapper = new SaveDataWrapper();
		wrapper.shipwrecks = new HashMap<>();

		for (Map.Entry<ShipwreckType, SalvageData> entry : dataMap.entrySet())
		{
			wrapper.shipwrecks.put(entry.getKey().name(), SalvageDataDto.fromSalvageData(entry.getValue()));
		}

		String jsonData = gson.toJson(wrapper);
		configManager.setRSProfileConfiguration(CONFIG_GROUP, DATA_KEY, jsonData);
		log.debug("Saved salvage data to RSProfile configuration");
	}

	/**
	 * Load salvage data from RSProfile configuration.
	 * If no RSProfile data exists, attempts to migrate from legacy file format.
	 */
	public Map<ShipwreckType, SalvageData> loadData()
	{
		// First try to load from RSProfile configuration
		String jsonData = configManager.getRSProfileConfiguration(CONFIG_GROUP, DATA_KEY);

		if (jsonData != null && !jsonData.isEmpty())
		{
			Map<ShipwreckType, SalvageData> dataMap = parseJsonData(jsonData);
			if (!dataMap.isEmpty())
			{
				log.debug("Loaded salvage data from RSProfile configuration");
				return dataMap;
			}
		}

		// Check for legacy file-based data and migrate if found
		Map<ShipwreckType, SalvageData> legacyData = loadLegacyData();
		if (!legacyData.isEmpty())
		{
			log.info("Migrating legacy file data to RSProfile configuration");
			saveData(legacyData);
			// Rename the old file to indicate migration is complete
			renameLegacyFile();
			return legacyData;
		}

		log.debug("No saved data found, starting fresh");
		return new HashMap<>();
	}

	/**
	 * Parse JSON data into a salvage data map
	 */
	private Map<ShipwreckType, SalvageData> parseJsonData(String jsonData)
	{
		Map<ShipwreckType, SalvageData> dataMap = new HashMap<>();

		try
		{
			SaveDataWrapper wrapper = gson.fromJson(jsonData, SaveDataWrapper.class);

			if (wrapper != null && wrapper.shipwrecks != null)
			{
				for (Map.Entry<String, SalvageDataDto> entry : wrapper.shipwrecks.entrySet())
				{
					try
					{
						ShipwreckType type = ShipwreckType.valueOf(entry.getKey());
						SalvageData data = entry.getValue().toSalvageData(type);
						dataMap.put(type, data);
					}
					catch (IllegalArgumentException e)
					{
						log.warn("Unknown shipwreck type: {}", entry.getKey());
					}
				}
			}
		}
		catch (Exception e)
		{
			log.error("Failed to parse salvage data JSON", e);
		}

		return dataMap;
	}

	/**
	 * Load data from legacy file-based storage (for migration)
	 */
	private Map<ShipwreckType, SalvageData> loadLegacyData()
	{
		Map<ShipwreckType, SalvageData> dataMap = new HashMap<>();

		if (legacyDataFile == null || !legacyDataFile.exists())
		{
			return dataMap;
		}

		try (FileReader reader = new FileReader(legacyDataFile))
		{
			SaveDataWrapper wrapper = gson.fromJson(reader, SaveDataWrapper.class);

			if (wrapper != null && wrapper.shipwrecks != null)
			{
				for (Map.Entry<String, SalvageDataDto> entry : wrapper.shipwrecks.entrySet())
				{
					try
					{
						ShipwreckType type = ShipwreckType.valueOf(entry.getKey());
						SalvageData data = entry.getValue().toSalvageData(type);
						dataMap.put(type, data);
					}
					catch (IllegalArgumentException e)
					{
						log.warn("Unknown shipwreck type: {}", entry.getKey());
					}
				}
			}

			log.info("Loaded {} shipwreck types from legacy file: {}", dataMap.size(), legacyDataFile.getAbsolutePath());
		}
		catch (IOException e)
		{
			log.error("Failed to load legacy salvage data file", e);
		}

		return dataMap;
	}

	/**
	 * Rename the legacy file to indicate migration is complete
	 */
	private void renameLegacyFile()
	{
		if (legacyDataFile == null || !legacyDataFile.exists())
		{
			return;
		}

		File migratedFile = new File(legacyDataFile.getParentFile(), DATA_FILE + ".migrated");
		if (legacyDataFile.renameTo(migratedFile))
		{
			log.info("Renamed legacy file to {} to indicate migration complete", migratedFile.getName());
		}
		else
		{
			log.warn("Failed to rename legacy file. It will be migrated again on next startup.");
		}
	}

	/**
	 * Wrapper class for the root JSON object
	 */
	private static class SaveDataWrapper
	{
		Map<String, SalvageDataDto> shipwrecks;
	}

	/**
	 * Data Transfer Object for serialization
	 */
	private static class SalvageDataDto
	{
		int totalLoots;
		Map<String, SalvageItemDto> items;

		static SalvageDataDto fromSalvageData(SalvageData data)
		{
			SalvageDataDto dto = new SalvageDataDto();
			dto.totalLoots = data.getTotalLoots();
			dto.items = new HashMap<>();
			
			for (Map.Entry<Integer, SalvageItem> entry : data.getItems().entrySet())
			{
				dto.items.put(String.valueOf(entry.getKey()), SalvageItemDto.fromSalvageItem(entry.getValue()));
			}
			
			return dto;
		}

		SalvageData toSalvageData(ShipwreckType type)
		{
			Map<Integer, SalvageItem> itemsMap = new ConcurrentHashMap<>();
			
			if (items != null)
			{
				for (Map.Entry<String, SalvageItemDto> entry : items.entrySet())
				{
					try
					{
						int itemId = Integer.parseInt(entry.getKey());
						SalvageItem item = entry.getValue().toSalvageItem(itemId);
						itemsMap.put(itemId, item);
					}
					catch (NumberFormatException e)
					{
						log.warn("Invalid item ID format: {}", entry.getKey());
					}
				}
			}
			
			return new SalvageData(type, totalLoots, itemsMap);
		}
	}

	/**
	 * Data Transfer Object for SalvageItem
	 */
	private static class SalvageItemDto
	{
		String itemName;
		int dropCount;
		int totalQuantity;
		double expectedDropRate;

		static SalvageItemDto fromSalvageItem(SalvageItem item)
		{
			SalvageItemDto dto = new SalvageItemDto();
			dto.itemName = item.getItemName();
			dto.dropCount = item.getDropCount();
			dto.totalQuantity = item.getTotalQuantity();
			dto.expectedDropRate = item.getExpectedDropRate();
			return dto;
		}

		SalvageItem toSalvageItem(int itemId)
		{
			int qty = totalQuantity > 0 ? totalQuantity : dropCount;
			return new SalvageItem(itemId, itemName, dropCount, qty, expectedDropRate);
		}
	}
}
