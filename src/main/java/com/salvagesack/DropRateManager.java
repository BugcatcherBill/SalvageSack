package com.salvagesack;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages loading and accessing expected drop rates from configuration
 */
@Slf4j
public class DropRateManager
{
	private final Map<ShipwreckType, Map<String, Double>> dropRates = new HashMap<>();
	private final Gson gson;
	private final File userConfigFile;

	public DropRateManager(File dataDirectory, Gson gson)
	{
		this.gson = gson;
		this.userConfigFile = new File(dataDirectory, "drop_rates.json");
		loadDropRates();
	}

	/**
	 * Load drop rates from bundled resource and user config
	 */
	private void loadDropRates()
	{
		// First load bundled defaults
		loadBundledRates();

		// Then override with user config if it exists
		if (userConfigFile.exists())
		{
			loadUserRates();
		}
		else
		{
			// Copy bundled config to user directory for editing
			copyBundledToUser();
		}
	}

	/**
	 * Load rates from bundled resource
	 */
	private void loadBundledRates()
	{
		try (InputStream is = getClass().getResourceAsStream("/drop_rates.json"))
		{
			if (is != null)
			{
				parseDropRates(new InputStreamReader(is, StandardCharsets.UTF_8));
				log.info("Loaded bundled drop rates");
			}
		}
		catch (Exception e)
		{
			log.warn("Failed to load bundled drop rates: {}", e.getMessage());
		}
	}

	/**
	 * Load rates from user config file
	 */
	private void loadUserRates()
	{
		try (FileReader reader = new FileReader(userConfigFile))
		{
			parseDropRates(reader);
			log.info("Loaded user drop rates from {}", userConfigFile.getAbsolutePath());
		}
		catch (Exception e)
		{
			log.warn("Failed to load user drop rates: {}", e.getMessage());
		}
	}

	/**
	 * Copy bundled config to user directory
	 */
	private void copyBundledToUser()
	{
		try (InputStream is = getClass().getResourceAsStream("/drop_rates.json"))
		{
			if (is != null)
			{
				File parentDir = userConfigFile.getParentFile();
				if (parentDir != null && !parentDir.exists() && !parentDir.mkdirs())
				{
					log.warn("Failed to create directory: {}", parentDir.getAbsolutePath());
					return;
				}
				try (FileOutputStream fos = new FileOutputStream(userConfigFile))
				{
					byte[] buffer = new byte[1024];
					int bytesRead;
					while ((bytesRead = is.read(buffer)) != -1)
					{
						fos.write(buffer, 0, bytesRead);
					}
				}
				log.info("Copied drop rates config to {}", userConfigFile.getAbsolutePath());
			}
		}
		catch (Exception e)
		{
			log.warn("Failed to copy drop rates config: {}", e.getMessage());
		}
	}

	/**
	 * Parse drop rates from a reader
	 */
	private void parseDropRates(Reader reader)
	{
		JsonObject root = gson.fromJson(reader, JsonObject.class);
		JsonObject shipwrecks = root.getAsJsonObject("shipwrecks");

		if (shipwrecks == null)
		{
			return;
		}

		for (String shipwreckKey : shipwrecks.keySet())
		{
			try
			{
				ShipwreckType type = ShipwreckType.valueOf(shipwreckKey);
				JsonObject shipwreckData = shipwrecks.getAsJsonObject(shipwreckKey);
				JsonObject items = shipwreckData.getAsJsonObject("items");

				if (items != null)
				{
					Map<String, Double> itemRates = dropRates.computeIfAbsent(type, k -> new HashMap<>());

					for (String itemName : items.keySet())
					{
						JsonElement rateElement = items.get(itemName);
						if (rateElement != null && rateElement.isJsonPrimitive())
						{
							double rate = rateElement.getAsDouble();
							itemRates.put(itemName.toLowerCase(), rate);
						}
					}
				}
			}
			catch (IllegalArgumentException e)
			{
				log.debug("Unknown shipwreck type in config: {}", shipwreckKey);
			}
		}
	}

	/**
	 * Get the expected drop rate for an item from a specific shipwreck type
	 * @param shipwreckType The type of shipwreck
	 * @param itemName The name of the item
	 * @return The expected drop rate (0.0 to 1.0), or 0.0 if unknown
	 */
	public double getExpectedDropRate(ShipwreckType shipwreckType, String itemName)
	{
		Map<String, Double> itemRates = dropRates.get(shipwreckType);
		if (itemRates == null)
		{
			log.debug("No rates found for shipwreck type: {}", shipwreckType);
			return 0.0;
		}

		double rate = itemRates.getOrDefault(itemName.toLowerCase(), 0.0);
		log.info("Drop rate lookup: type={}, item='{}', rate={}", shipwreckType, itemName, rate);
		return rate;
	}
}

