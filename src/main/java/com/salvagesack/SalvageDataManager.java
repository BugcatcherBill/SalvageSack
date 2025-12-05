package com.salvagesack;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
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
			// Convert to a serializable format
			Map<String, SalvageDataDto> dtoMap = new HashMap<>();
			for (Map.Entry<ShipwreckType, SalvageData> entry : dataMap.entrySet())
			{
				dtoMap.put(entry.getKey().name(), SalvageDataDto.fromSalvageData(entry.getValue()));
			}
			
			gson.toJson(dtoMap, writer);
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
			Type type = new TypeToken<Map<String, SalvageDataDto>>(){}.getType();
			Map<String, SalvageDataDto> dtoMap = gson.fromJson(reader, type);
			
			if (dtoMap != null)
			{
				for (Map.Entry<String, SalvageDataDto> entry : dtoMap.entrySet())
				{
					try
					{
						ShipwreckType type1 = ShipwreckType.valueOf(entry.getKey());
						SalvageData data = entry.getValue().toSalvageData(type1);
						dataMap.put(type1, data);
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
	 * Data Transfer Object for serialization
	 */
	private static class SalvageDataDto
	{
		int totalLoots;
		Map<Integer, SalvageItemDto> items;

		static SalvageDataDto fromSalvageData(SalvageData data)
		{
			SalvageDataDto dto = new SalvageDataDto();
			dto.totalLoots = data.getTotalLoots();
			dto.items = new HashMap<>();
			
			for (Map.Entry<Integer, SalvageItem> entry : data.getItems().entrySet())
			{
				dto.items.put(entry.getKey(), SalvageItemDto.fromSalvageItem(entry.getValue()));
			}
			
			return dto;
		}

		SalvageData toSalvageData(ShipwreckType type)
		{
			Map<Integer, SalvageItem> itemsMap = new ConcurrentHashMap<>();
			
			if (items != null)
			{
				for (Map.Entry<Integer, SalvageItemDto> entry : items.entrySet())
				{
					SalvageItem item = entry.getValue().toSalvageItem(entry.getKey());
					itemsMap.put(entry.getKey(), item);
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
		double expectedDropRate;

		static SalvageItemDto fromSalvageItem(SalvageItem item)
		{
			SalvageItemDto dto = new SalvageItemDto();
			dto.itemName = item.getItemName();
			dto.dropCount = item.getDropCount();
			dto.expectedDropRate = item.getExpectedDropRate();
			return dto;
		}

		SalvageItem toSalvageItem(int itemId)
		{
			return new SalvageItem(itemId, itemName, dropCount, expectedDropRate);
		}
	}
}
