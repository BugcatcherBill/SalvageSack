package com.salvagesack;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Manages persistence of pirate rank data
 */
@Slf4j
public class PirateRankDataManager
{
	private static final String PIRATE_RANK_FILE = "pirate_rank.json";
	
	private final File dataDirectory;
	private final Gson gson;

	public PirateRankDataManager(File dataDirectory, Gson gson)
	{
		this.dataDirectory = dataDirectory;
		this.gson = gson;
	}

	/**
	 * Load pirate rank data from file
	 * @return Loaded data, or new instance if file doesn't exist
	 */
	public PirateRankData loadData()
	{
		File file = new File(dataDirectory, PIRATE_RANK_FILE);
		
		if (!file.exists())
		{
			log.debug("Pirate rank file does not exist, creating new data");
			return new PirateRankData();
		}

		try (FileReader reader = new FileReader(file))
		{
			PirateRankData data = gson.fromJson(reader, PirateRankData.class);
			if (data == null)
			{
				log.warn("Failed to deserialize pirate rank data, deleting corrupted file");
				file.delete();
				return new PirateRankData();
			}
			
			// Check if deserialization resulted in null rank (e.g., from old enum values)
			if (data.getCurrentRank() == null)
			{
				log.warn("Old pirate rank save file detected with incompatible enum values, deleting and starting fresh");
				file.delete();
				return new PirateRankData();
			}
			
			log.debug("Loaded pirate rank data: {} with {} booty", 
				data.getCurrentRank().getDisplayName(), data.getTotalBooty());
			return data;
		}
		catch (Exception e)
		{
			log.error("Failed to load pirate rank data, deleting corrupted file: {}", e.getMessage());
			file.delete();
			return new PirateRankData();
		}
	}

	/**
	 * Save pirate rank data to file
	 * @param data Data to save
	 */
	public void saveData(PirateRankData data)
	{
		if (data == null)
		{
			log.warn("Attempted to save null pirate rank data");
			return;
		}

		File file = new File(dataDirectory, PIRATE_RANK_FILE);
		
		try (FileWriter writer = new FileWriter(file))
		{
			gson.toJson(data, writer);
			log.debug("Saved pirate rank data: {} with {} booty", 
				data.getCurrentRank().getDisplayName(), data.getTotalBooty());
		}
		catch (IOException e)
		{
			log.error("Failed to save pirate rank data", e);
		}
	}
}
