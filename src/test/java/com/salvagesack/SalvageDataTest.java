package com.salvagesack;

import org.junit.Test;
import static org.junit.Assert.*;

public class SalvageDataTest
{
	@Test
	public void testSalvageItemTracking()
	{
		SalvageItem item = new SalvageItem(1, "Test Item", 0.25);
		assertEquals("Test Item", item.getItemName());
		assertEquals(1, item.getItemId());
		assertEquals(0, item.getDropCount());
		assertEquals(0.25, item.getExpectedDropRate(), 0.001);

		item.incrementDropCount();
		assertEquals(1, item.getDropCount());

		// Current drop rate should be 50% after 2 loots with 1 drop
		assertEquals(0.5, item.getCurrentDropRate(2), 0.001);
	}

	@Test
	public void testSalvageDataRecording()
	{
		SalvageData data = new SalvageData(ShipwreckType.SMALL);
		assertEquals(ShipwreckType.SMALL, data.getShipwreckType());
		assertEquals(0, data.getTotalLoots());
		assertTrue(data.getItems().isEmpty());

		data.incrementTotalLoots();
		data.recordLoot(1, "Item 1", 0.5);
		
		assertEquals(1, data.getTotalLoots());
		assertEquals(1, data.getItems().size());
		assertEquals(1, data.getItems().get(1).getDropCount());

		data.incrementTotalLoots();
		data.recordLoot(1, "Item 1", 0.5);
		data.recordLoot(2, "Item 2", 0.25);

		assertEquals(2, data.getTotalLoots());
		assertEquals(2, data.getItems().size());
		assertEquals(2, data.getItems().get(1).getDropCount());
		assertEquals(1, data.getItems().get(2).getDropCount());
	}

	@Test
	public void testShipwreckTypeFromString()
	{
		assertEquals(ShipwreckType.SMALL, ShipwreckType.fromString("Small Shipwreck"));
		assertEquals(ShipwreckType.MEDIUM, ShipwreckType.fromString("Medium Shipwreck"));
		assertEquals(ShipwreckType.LARGE, ShipwreckType.fromString("Large Shipwreck"));
		assertEquals(ShipwreckType.UNKNOWN, ShipwreckType.fromString("Random Text"));
	}

	@Test
	public void testDropRateCalculation()
	{
		SalvageItem item = new SalvageItem(1, "Test", 0.2);
		
		// No loots yet
		assertEquals(0.0, item.getCurrentDropRate(0), 0.001);
		
		// 1 drop in 5 loots = 20%
		item.incrementDropCount();
		assertEquals(0.2, item.getCurrentDropRate(5), 0.001);
		
		// 3 drops in 10 loots = 30%
		item.incrementDropCount();
		item.incrementDropCount();
		assertEquals(0.3, item.getCurrentDropRate(10), 0.001);
	}
}
