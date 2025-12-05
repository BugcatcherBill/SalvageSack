package com.salvagesack;

import lombok.Data;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Stores all salvage tracking data for a specific shipwreck type
 */
@Data
public class SalvageData
{
	private final ShipwreckType shipwreckType;
	private int totalLoots;
	private final Map<Integer, SalvageItem> items; // itemId -> SalvageItem

	public SalvageData(ShipwreckType shipwreckType)
	{
		this.shipwreckType = shipwreckType;
		this.totalLoots = 0;
		this.items = new ConcurrentHashMap<>();
	}

	/**
	 * Constructor for deserialization
	 */
	public SalvageData(ShipwreckType shipwreckType, int totalLoots, Map<Integer, SalvageItem> items)
	{
		this.shipwreckType = shipwreckType;
		this.totalLoots = totalLoots;
		this.items = items;
	}

	/**
	 * Record a loot drop
	 * @param itemId The item ID that was looted
	 * @param itemName The item name
	 * @param expectedDropRate Expected drop rate for this item
	 */
	public void recordLoot(int itemId, String itemName, double expectedDropRate)
	{
		SalvageItem item = items.computeIfAbsent(itemId, 
			id -> new SalvageItem(id, itemName, expectedDropRate));
		item.incrementDropCount();
	}

	/**
	 * Increment total loot count
	 */
	public void incrementTotalLoots()
	{
		this.totalLoots++;
	}

	/**
	 * Get or create a salvage item
	 */
	public SalvageItem getOrCreateItem(int itemId, String itemName, double expectedDropRate)
	{
		return items.computeIfAbsent(itemId, 
			id -> new SalvageItem(id, itemName, expectedDropRate));
	}
}
