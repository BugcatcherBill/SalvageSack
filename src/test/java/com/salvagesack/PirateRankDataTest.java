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
		
		// Add booty but not enough to rank up
		boolean rankedUp = data.addBooty(500);
		
		assertFalse(rankedUp);
		assertEquals(500, data.getTotalBooty());
		assertEquals(PirateRank.RANK_1, data.getCurrentRank());
		assertFalse(data.isJustRankedUp());
	}

	@Test
	public void testAddBootyWithRankUp()
	{
		PirateRankData data = new PirateRankData();
		
		// Add enough booty to rank up
		boolean rankedUp = data.addBooty(1000);
		
		assertTrue(rankedUp);
		assertEquals(1000, data.getTotalBooty());
		assertEquals(PirateRank.RANK_2, data.getCurrentRank());
		assertEquals(PirateRank.RANK_1, data.getPreviousRank());
		assertTrue(data.isJustRankedUp());
	}

	@Test
	public void testMultipleRankUps()
	{
		PirateRankData data = new PirateRankData();
		
		// First rank up
		data.addBooty(1000);
		assertEquals(PirateRank.RANK_2, data.getCurrentRank());
		
		// Clear the flag
		data.clearRankUpFlag();
		assertFalse(data.isJustRankedUp());
		
		// Second rank up
		data.addBooty(1000); // Total: 2000
		assertEquals(PirateRank.RANK_3, data.getCurrentRank());
		assertTrue(data.isJustRankedUp());
	}

	@Test
	public void testSkipMultipleRanks()
	{
		PirateRankData data = new PirateRankData();
		
		// Add enough booty to skip several ranks
		boolean rankedUp = data.addBooty(100000);
		
		assertTrue(rankedUp);
		assertEquals(100000, data.getTotalBooty());
		assertEquals(PirateRank.RANK_25, data.getCurrentRank());
	}

	@Test
	public void testGetProgressToNextRank()
	{
		PirateRankData data = new PirateRankData();
		
		// At start, progress should be 0
		assertEquals(0.0, data.getProgressToNextRank(), 0.001);
		
		// Add half the required booty
		data.addBooty(500); // Need 1000 total for next rank
		assertEquals(0.5, data.getProgressToNextRank(), 0.001);
		
		// Add more to reach next rank
		data.addBooty(500); // Now at 1000, should be at RANK_2
		assertEquals(0.0, data.getProgressToNextRank(), 0.001);
		
		// Add partial progress toward next rank (RANK_3 at 2000)
		data.addBooty(500); // Now at 1500, halfway between 1000 and 2000
		assertEquals(0.5, data.getProgressToNextRank(), 0.001);
	}

	@Test
	public void testGetBootyNeededForNextRank()
	{
		PirateRankData data = new PirateRankData();
		
		// At start, need 1000 for RANK_2
		assertEquals(1000, data.getBootyNeededForNextRank());
		
		// Add 500
		data.addBooty(500);
		assertEquals(500, data.getBootyNeededForNextRank());
		
		// Reach RANK_2 (need 2000 for RANK_3)
		data.addBooty(500);
		assertEquals(1000, data.getBootyNeededForNextRank());
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
