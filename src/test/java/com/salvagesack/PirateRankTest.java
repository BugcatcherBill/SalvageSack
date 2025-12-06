package com.salvagesack;

import org.junit.Test;
import static org.junit.Assert.*;

public class PirateRankTest
{
	@Test
	public void testRankEnumValues()
	{
		// Test first and last ranks
		assertEquals("Castaway", PirateRank.RANK_1.getDisplayName());
		assertEquals("Max Cash Stack", PirateRank.RANK_100.getDisplayName());
		
		// Verify we have 100 ranks
		assertEquals(100, PirateRank.values().length);
	}

	@Test
	public void testBootyRequirements()
	{
		// Verify ranks are ordered by booty requirement
		PirateRank[] ranks = PirateRank.values();
		for (int i = 1; i < ranks.length; i++)
		{
			assertTrue("Rank " + ranks[i].getDisplayName() + " should require more booty than " + ranks[i-1].getDisplayName(),
				ranks[i].getBootyRequired() > ranks[i-1].getBootyRequired());
		}
	}

	@Test
	public void testGetRankForBooty()
	{
		// Starting rank
		assertEquals(PirateRank.RANK_1, PirateRank.getRankForBooty(0));
		assertEquals(PirateRank.RANK_1, PirateRank.getRankForBooty(999));
		
		// Second rank
		assertEquals(PirateRank.RANK_2, PirateRank.getRankForBooty(1000));
		assertEquals(PirateRank.RANK_2, PirateRank.getRankForBooty(1999));
		
		// Test some intermediate ranks
		assertEquals(PirateRank.RANK_10, PirateRank.getRankForBooty(10000));
		assertEquals(PirateRank.RANK_20, PirateRank.getRankForBooty(50000));
		assertEquals(PirateRank.RANK_40, PirateRank.getRankForBooty(1000000));
		
		// Maximum rank
		assertEquals(PirateRank.RANK_100, PirateRank.getRankForBooty(Integer.MAX_VALUE));
		assertEquals(PirateRank.RANK_100, PirateRank.getRankForBooty(Long.MAX_VALUE));
	}

	@Test
	public void testGetNextRank()
	{
		// First rank should have next rank
		assertEquals(PirateRank.RANK_2, PirateRank.RANK_1.getNextRank());
		
		// Middle rank should have next rank
		assertEquals(PirateRank.RANK_21, PirateRank.RANK_20.getNextRank());
		
		// Last rank should have no next rank
		assertNull(PirateRank.RANK_100.getNextRank());
	}

	@Test
	public void testProgressToNextRank()
	{
		// At exact rank requirement, progress should be 0
		assertEquals(0.0, PirateRank.RANK_1.getProgressToNextRank(0), 0.001);
		
		// Halfway to next rank (500 / 1000)
		assertEquals(0.5, PirateRank.RANK_1.getProgressToNextRank(500), 0.001);
		
		// Almost at next rank
		assertEquals(0.99, PirateRank.RANK_1.getProgressToNextRank(990), 0.01);
		
		// Past the rank requirement (should still calculate correctly)
		assertEquals(1.0, PirateRank.RANK_1.getProgressToNextRank(1000), 0.001);
		
		// Max rank should always be 100% progress
		assertEquals(1.0, PirateRank.RANK_100.getProgressToNextRank(Integer.MAX_VALUE), 0.001);
		assertEquals(1.0, PirateRank.RANK_100.getProgressToNextRank(Long.MAX_VALUE), 0.001);
	}

	@Test
	public void testRankDescriptions()
	{
		// Verify all ranks have non-null descriptions
		for (PirateRank rank : PirateRank.values())
		{
			assertNotNull("Rank " + rank.getDisplayName() + " should have a description", 
				rank.getDescription());
			assertFalse("Rank " + rank.getDisplayName() + " description should not be empty", 
				rank.getDescription().isEmpty());
		}
	}

	@Test
	public void testToString()
	{
		assertEquals("Castaway", PirateRank.RANK_1.toString());
		assertEquals("Pirate Captain", PirateRank.RANK_35.toString());
		assertEquals("Max Cash Stack", PirateRank.RANK_100.toString());
	}
}
