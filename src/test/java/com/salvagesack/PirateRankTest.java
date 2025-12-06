package com.salvagesack;

import org.junit.Test;
import static org.junit.Assert.*;

public class PirateRankTest
{
	@Test
	public void testRankEnumValues()
	{
		// Test first and last ranks
		assertEquals("Castaway", PirateRank.CASTAWAY.getDisplayName());
		assertEquals("Wise Old Pirate", PirateRank.WISE_OLD_PIRATE.getDisplayName());
		
		// Verify we have 50 ranks
		assertEquals(50, PirateRank.values().length);
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
		assertEquals(PirateRank.CASTAWAY, PirateRank.getRankForBooty(0));
		assertEquals(PirateRank.CASTAWAY, PirateRank.getRankForBooty(1999));
		
		// Second rank
		assertEquals(PirateRank.DECK_SWABBER, PirateRank.getRankForBooty(2000));
		assertEquals(PirateRank.DECK_SWABBER, PirateRank.getRankForBooty(3999));
		
		// Test some intermediate ranks
		assertEquals(PirateRank.ABLE_SEAMAN, PirateRank.getRankForBooty(10000));
		assertEquals(PirateRank.QUARTERMASTER, PirateRank.getRankForBooty(50000));
		assertEquals(PirateRank.PIRATE_EMPEROR, PirateRank.getRankForBooty(1000000));
		
		// Maximum rank
		assertEquals(PirateRank.WISE_OLD_PIRATE, PirateRank.getRankForBooty(10000000));
		assertEquals(PirateRank.WISE_OLD_PIRATE, PirateRank.getRankForBooty(999999999));
	}

	@Test
	public void testGetNextRank()
	{
		// First rank should have next rank
		assertEquals(PirateRank.DECK_SWABBER, PirateRank.CASTAWAY.getNextRank());
		
		// Middle rank should have next rank
		assertEquals(PirateRank.QUARTERMASTER, PirateRank.SAILING_MASTER.getNextRank());
		
		// Last rank should have no next rank
		assertNull(PirateRank.WISE_OLD_PIRATE.getNextRank());
	}

	@Test
	public void testProgressToNextRank()
	{
		// At exact rank requirement, progress should be 0
		assertEquals(0.0, PirateRank.CASTAWAY.getProgressToNextRank(0), 0.001);
		
		// Halfway to next rank (1000 / 2000)
		assertEquals(0.5, PirateRank.CASTAWAY.getProgressToNextRank(1000), 0.001);
		
		// Almost at next rank
		assertEquals(0.99, PirateRank.CASTAWAY.getProgressToNextRank(1980), 0.01);
		
		// Past the rank requirement (should still calculate correctly)
		assertEquals(1.0, PirateRank.CASTAWAY.getProgressToNextRank(2000), 0.001);
		
		// Max rank should always be 100% progress
		assertEquals(1.0, PirateRank.WISE_OLD_PIRATE.getProgressToNextRank(10000000), 0.001);
		assertEquals(1.0, PirateRank.WISE_OLD_PIRATE.getProgressToNextRank(999999999), 0.001);
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
		assertEquals("Castaway", PirateRank.CASTAWAY.toString());
		assertEquals("Pirate Lord", PirateRank.PIRATE_LORD.toString());
		assertEquals("Wise Old Pirate", PirateRank.WISE_OLD_PIRATE.toString());
	}
}
