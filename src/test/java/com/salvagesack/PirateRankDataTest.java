package com.salvagesack;

import org.junit.Test;
import static org.junit.Assert.*;

public class PirateRankDataTest
{
	@Test
	public void testInitialization()
	{
		PirateRankData data = new PirateRankData();
		
		assertEquals(0, data.getTotalBooty());
		assertEquals(PirateRank.RANK_1, data.getCurrentRank());
		assertNull(data.getPreviousRank());
		assertFalse(data.isJustRankedUp());
	}

	@Test
	public void testAddBootyNoRankUp()
	{
		PirateRankData data = new PirateRankData();
		
		// Add booty but not enough to rank up (RANK_2 requires 22,021)
		boolean rankedUp = data.addBooty(10000);
		
		assertFalse(rankedUp);
		assertEquals(10000, data.getTotalBooty());
		assertEquals(PirateRank.RANK_1, data.getCurrentRank());
		assertFalse(data.isJustRankedUp());
	}

	@Test
	public void testAddBootyWithRankUp()
	{
		PirateRankData data = new PirateRankData();
		
		// Add enough booty to rank up (RANK_2 requires 22,021)
		boolean rankedUp = data.addBooty(22021);
		
		assertTrue(rankedUp);
		assertEquals(22021, data.getTotalBooty());
		assertEquals(PirateRank.RANK_2, data.getCurrentRank());
		assertEquals(PirateRank.RANK_1, data.getPreviousRank());
		assertTrue(data.isJustRankedUp());
	}

	@Test
	public void testMultipleRankUps()
	{
		PirateRankData data = new PirateRankData();
		
		// First rank up (RANK_2 at 22,021)
		data.addBooty(22021);
		assertEquals(PirateRank.RANK_2, data.getCurrentRank());
		
		// Clear the flag
		data.clearRankUpFlag();
		assertFalse(data.isJustRankedUp());
		
		// Second rank up (RANK_3 at 124,570, need 102,549 more)
		data.addBooty(102549);
		assertEquals(PirateRank.RANK_3, data.getCurrentRank());
		assertTrue(data.isJustRankedUp());
	}

	@Test
	public void testSkipMultipleRanks()
	{
		PirateRankData data = new PirateRankData();
		
		// Add enough booty to skip several ranks (RANK_10 at 5,351,162)
		boolean rankedUp = data.addBooty(5351162);
		
		assertTrue(rankedUp);
		assertEquals(5351162, data.getTotalBooty());
		assertEquals(PirateRank.RANK_10, data.getCurrentRank());
	}

	@Test
	public void testGetProgressToNextRank()
	{
		PirateRankData data = new PirateRankData();
		
		// At start, progress should be 0
		assertEquals(0.0, data.getProgressToNextRank(), 0.001);
		
		// Add half the required booty (RANK_2 requires 22,021)
		data.addBooty(11010);
		assertEquals(0.5, data.getProgressToNextRank(), 0.001);
		
		// Add more to reach next rank
		data.addBooty(11011); // Now at 22,021, should be at RANK_2
		assertEquals(0.0, data.getProgressToNextRank(), 0.001);
		
		// Add partial progress toward next rank (RANK_3 at 124,570, delta = 102,549)
		data.addBooty(51274); // Now at 73,295, halfway between 22,021 and 124,570
		assertEquals(0.5, data.getProgressToNextRank(), 0.01);
	}

	@Test
	public void testGetBootyNeededForNextRank()
	{
		PirateRankData data = new PirateRankData();
		
		// At start, need 22,021 for RANK_2
		assertEquals(22021, data.getBootyNeededForNextRank());
		
		// Add 11,010 (half way)
		data.addBooty(11010);
		assertEquals(11011, data.getBootyNeededForNextRank());
		
		// Reach RANK_2 (need 102,549 more for RANK_3 at 124,570)
		data.addBooty(11011);
		assertEquals(102549, data.getBootyNeededForNextRank());
	}

	@Test
	public void testMaxRank()
	{
		PirateRankData data = new PirateRankData();
		
		// Add enough to reach max rank
		data.addBooty(Integer.MAX_VALUE);
		
		assertEquals(PirateRank.RANK_100, data.getCurrentRank());
		assertEquals(1.0, data.getProgressToNextRank(), 0.001);
		assertEquals(0, data.getBootyNeededForNextRank());
		
		// Adding more booty shouldn't rank up
		boolean rankedUp = data.addBooty(1000000);
		assertFalse(rankedUp);
		assertEquals(PirateRank.RANK_100, data.getCurrentRank());
	}

	@Test
	public void testClearRankUpFlag()
	{
		PirateRankData data = new PirateRankData();
		
		data.addBooty(2000);
		assertTrue(data.isJustRankedUp());
		
		data.clearRankUpFlag();
		assertFalse(data.isJustRankedUp());
	}

	@Test
	public void testDeserializationConstructor()
	{
		PirateRankData data = new PirateRankData(50000, PirateRank.RANK_20);
		
		assertEquals(50000, data.getTotalBooty());
		assertEquals(PirateRank.RANK_20, data.getCurrentRank());
		assertNull(data.getPreviousRank());
		assertFalse(data.isJustRankedUp());
	}
}
