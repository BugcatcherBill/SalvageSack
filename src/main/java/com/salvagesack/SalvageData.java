package com.salvagesack;

import lombok.Data;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Stores all salvage tracking data for a specific shipwreck type.
 * <p>
 * Each shipwreck type maintains its own loot statistics including total sorts,
 * individual item drop counts, and a timestamp for UI ordering.
 * </p>
 * <p>
 * The panel displays shipwreck sections sorted by {@link #lastUpdated} so that
 * the most recently active shipwreck always appears at the top, regardless of
 * whether it is expanded or collapsed.
 * </p>
 */
@Data
public class SalvageData
{
	private final ShipwreckType shipwreckType;
	private int totalLoots;
	private final Map<Integer, SalvageItem> items; // itemId -> SalvageItem

	/**
	 * Timestamp (milliseconds since epoch) of the last loot recorded for this shipwreck.
	 * Used to sort shipwreck panels in the UI with most recently updated at the top.
	 */
	private long lastUpdated;

	public SalvageData(ShipwreckType shipwreckType)
	{
		this.shipwreckType = shipwreckType;
		this.totalLoots = 0;
		this.items = new ConcurrentHashMap<>();
		this.lastUpdated = 0;
	}

	/**
	 * Constructor for deserialization
	 */
	public SalvageData(ShipwreckType shipwreckType, int totalLoots, Map<Integer, SalvageItem> items)
	{
		this.shipwreckType = shipwreckType;
		this.totalLoots = totalLoots;
		this.items = items;
		this.lastUpdated = 0;
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
	 * Increment total loot count and update the last modified timestamp.
	 * <p>
	 * This method should be called once per salvage sort action. The timestamp
	 * update ensures this shipwreck type will be displayed at the top of the
	 * panel, as shipwrecks are sorted by most recently updated.
	 * </p>
	 */
	public void incrementTotalLoots()
	{
		this.totalLoots++;
		this.lastUpdated = System.currentTimeMillis();
	}
}
