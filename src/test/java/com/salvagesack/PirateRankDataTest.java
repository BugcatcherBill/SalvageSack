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
		assertEquals(PirateRank.CASTAWAY, data.getCurrentRank());
		assertNull(data.getPreviousRank());
		assertFalse(data.isJustRankedUp());
	}

	@Test
	public void testAddBootyNoRankUp()
	{
		PirateRankData data = new PirateRankData();
		
		// Add booty but not enough to rank up
		boolean rankedUp = data.addBooty(1000);
		
		assertFalse(rankedUp);
		assertEquals(1000, data.getTotalBooty());
		assertEquals(PirateRank.CASTAWAY, data.getCurrentRank());
		assertFalse(data.isJustRankedUp());
	}

	@Test
	public void testAddBootyWithRankUp()
	{
		PirateRankData data = new PirateRankData();
		
		// Add enough booty to rank up
		boolean rankedUp = data.addBooty(2000);
		
		assertTrue(rankedUp);
		assertEquals(2000, data.getTotalBooty());
		assertEquals(PirateRank.DECK_SWABBER, data.getCurrentRank());
		assertEquals(PirateRank.CASTAWAY, data.getPreviousRank());
		assertTrue(data.isJustRankedUp());
	}

	@Test
	public void testMultipleRankUps()
	{
		PirateRankData data = new PirateRankData();
		
		// First rank up
		data.addBooty(2000);
		assertEquals(PirateRank.DECK_SWABBER, data.getCurrentRank());
		
		// Clear the flag
		data.clearRankUpFlag();
		assertFalse(data.isJustRankedUp());
		
		// Second rank up
		data.addBooty(2000); // Total: 4000
		assertEquals(PirateRank.CABIN_BOY, data.getCurrentRank());
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
		assertEquals(PirateRank.PRIVATEER, data.getCurrentRank());
	}

	@Test
	public void testGetProgressToNextRank()
	{
		PirateRankData data = new PirateRankData();
		
		// At start, progress should be 0
		assertEquals(0.0, data.getProgressToNextRank(), 0.001);
		
		// Add half the required booty
		data.addBooty(1000); // Need 2000 total for next rank
		assertEquals(0.5, data.getProgressToNextRank(), 0.001);
		
		// Add more to reach next rank
		data.addBooty(1000); // Now at 2000, should be at DECK_SWABBER
		assertEquals(0.0, data.getProgressToNextRank(), 0.001);
		
		// Add partial progress toward next rank (CABIN_BOY at 4000)
		data.addBooty(1000); // Now at 3000, halfway between 2000 and 4000
		assertEquals(0.5, data.getProgressToNextRank(), 0.001);
	}

	@Test
	public void testGetBootyNeededForNextRank()
	{
		PirateRankData data = new PirateRankData();
		
		// At start, need 2000 for DECK_SWABBER
		assertEquals(2000, data.getBootyNeededForNextRank());
		
		// Add 1000
		data.addBooty(1000);
		assertEquals(1000, data.getBootyNeededForNextRank());
		
		// Reach DECK_SWABBER (need 4000 for CABIN_BOY)
		data.addBooty(1000);
		assertEquals(2000, data.getBootyNeededForNextRank());
	}

	@Test
	public void testMaxRank()
	{
		PirateRankData data = new PirateRankData();
		
		// Add enough to reach max rank
		data.addBooty(10000000);
		
		assertEquals(PirateRank.WISE_OLD_PIRATE, data.getCurrentRank());
		assertEquals(1.0, data.getProgressToNextRank(), 0.001);
		assertEquals(0, data.getBootyNeededForNextRank());
		
		// Adding more booty shouldn't rank up
		boolean rankedUp = data.addBooty(1000000);
		assertFalse(rankedUp);
		assertEquals(PirateRank.WISE_OLD_PIRATE, data.getCurrentRank());
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
		PirateRankData data = new PirateRankData(50000, PirateRank.QUARTERMASTER);
		
		assertEquals(50000, data.getTotalBooty());
		assertEquals(PirateRank.QUARTERMASTER, data.getCurrentRank());
		assertNull(data.getPreviousRank());
		assertFalse(data.isJustRankedUp());
	}
}
