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
		recordLoot(itemId, itemName, expectedDropRate, 1);
	}

	/**
	 * Record a loot drop with quantity
	 * Counts as 1 drop for rate calculation, but tracks full quantity
	 * @param itemId The item ID that was looted
	 * @param itemName The item name
	 * @param expectedDropRate Expected drop rate for this item
	 * @param quantity Number of items looted (for display, not rate calc)
	 */
	public void recordLoot(int itemId, String itemName, double expectedDropRate, int quantity)
	{
		SalvageItem item = items.computeIfAbsent(itemId,
			id -> new SalvageItem(id, itemName, expectedDropRate));
		item.recordDrop(quantity);  // 1 drop for rate, full quantity for display
	}

	/**
	 * Increment total loot count
	 */
	public void incrementTotalLoots()
	{
		this.totalLoots++;
	}
}
