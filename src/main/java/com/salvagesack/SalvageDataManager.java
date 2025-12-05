package com.salvagesack;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Handles persistence of salvage tracking data between sessions
 */
@Slf4j
public class SalvageDataManager
{
	private static final String DATA_FILE = "salvage-data.json";
	private final Gson gson;
	private final File dataFile;

	public SalvageDataManager(File dataDirectory)
	{
		this.gson = new GsonBuilder().setPrettyPrinting().create();
		this.dataFile = new File(dataDirectory, DATA_FILE);
	}

	/**
	 * Save salvage data to disk
	 */
	public void saveData(Map<ShipwreckType, SalvageData> dataMap)
	{
		try (FileWriter writer = new FileWriter(dataFile))
		{
			SaveDataWrapper wrapper = new SaveDataWrapper();
			wrapper.shipwrecks = new HashMap<>();

			for (Map.Entry<ShipwreckType, SalvageData> entry : dataMap.entrySet())
			{
				wrapper.shipwrecks.put(entry.getKey().name(), SalvageDataDto.fromSalvageData(entry.getValue()));
			}
			
			gson.toJson(wrapper, writer);
			log.debug("Saved salvage data to {}", dataFile.getAbsolutePath());
		}
		catch (IOException e)
		{
			log.error("Failed to save salvage data", e);
		}
	}

	/**
	 * Load salvage data from disk
	 */
	public Map<ShipwreckType, SalvageData> loadData()
	{
		Map<ShipwreckType, SalvageData> dataMap = new HashMap<>();
		
		if (!dataFile.exists())
		{
			log.debug("No saved data file found, starting fresh");
			return dataMap;
		}

		try (FileReader reader = new FileReader(dataFile))
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
			
			log.debug("Loaded salvage data from {}", dataFile.getAbsolutePath());
		}
		catch (IOException e)
		{
			log.error("Failed to load salvage data", e);
		}

		return dataMap;
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
