package com.salvagesack;

import org.junit.Test;
import static org.junit.Assert.*;

public class SortOptionTest
{
	@Test
	public void testSortOptionDisplayNames()
	{
		assertEquals("Alphabetical", SortOption.ALPHABETICAL.getDisplayName());
		assertEquals("Current Rate", SortOption.CURRENT_RATE.getDisplayName());
		assertEquals("Expected Rate", SortOption.EXPECTED_RATE.getDisplayName());
		assertEquals("Quantity", SortOption.QUANTITY.getDisplayName());
		assertEquals("Luck", SortOption.LUCK.getDisplayName());
	}

	@Test
	public void testSortOptionToString()
	{
		assertEquals("Alphabetical", SortOption.ALPHABETICAL.toString());
		assertEquals("Current Rate", SortOption.CURRENT_RATE.toString());
		assertEquals("Expected Rate", SortOption.EXPECTED_RATE.toString());
		assertEquals("Quantity", SortOption.QUANTITY.toString());
		assertEquals("Luck", SortOption.LUCK.toString());
	}

	@Test
	public void testSortOptionValues()
	{
		SortOption[] values = SortOption.values();
		assertEquals(5, values.length);
		assertEquals(SortOption.ALPHABETICAL, values[0]);
		assertEquals(SortOption.CURRENT_RATE, values[1]);
		assertEquals(SortOption.EXPECTED_RATE, values[2]);
		assertEquals(SortOption.QUANTITY, values[3]);
		assertEquals(SortOption.LUCK, values[4]);
	}
}
