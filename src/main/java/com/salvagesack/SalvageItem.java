package com.salvagesack;

import lombok.Data;

/**
 * Represents a salvage item drop with tracking information
 */
@Data
public class SalvageItem
{
	private final int itemId;
	private final String itemName;
	private int dropCount;
	private final double expectedDropRate; // Expected rate from wiki (e.g., 0.1 for 10%)

	public SalvageItem(int itemId, String itemName, double expectedDropRate)
	{
		this.itemId = itemId;
		this.itemName = itemName;
		this.dropCount = 0;
		this.expectedDropRate = expectedDropRate;
	}

	/**
	 * Constructor for deserialization
	 */
	public SalvageItem(int itemId, String itemName, int dropCount, double expectedDropRate)
	{
		this.itemId = itemId;
		this.itemName = itemName;
		this.dropCount = dropCount;
		this.expectedDropRate = expectedDropRate;
	}

	/**
	 * Increment the drop count for this item
	 */
	public void incrementDropCount()
	{
		this.dropCount++;
	}

	/**
	 * Calculate current drop rate based on total loots
	 * @param totalLoots Total number of loots from this shipwreck type
	 * @return Current drop rate as a decimal (e.g., 0.15 for 15%)
	 */
	public double getCurrentDropRate(int totalLoots)
	{
		if (totalLoots == 0)
		{
			return 0.0;
		}
		return (double) dropCount / totalLoots;
	}
}
