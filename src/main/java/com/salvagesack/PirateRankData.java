package com.salvagesack;

import lombok.Data;

/**
 * Tracks pirate rank progression based on accumulated booty
 */
@Data
public class PirateRankData
{
	private long totalBooty;
	private PirateRank currentRank;
	private PirateRank previousRank;
	private boolean justRankedUp;

	public PirateRankData()
	{
		this.totalBooty = 0;
		this.currentRank = PirateRank.RANK_1;
		this.previousRank = null;
		this.justRankedUp = false;
	}

	/**
	 * Constructor for deserialization
	 */
	public PirateRankData(long totalBooty, PirateRank currentRank)
	{
		this.totalBooty = totalBooty;
		this.currentRank = currentRank;
		this.previousRank = null;
		this.justRankedUp = false;
	}

	/**
	 * Add booty and check for rank up
	 * @param amount Amount of booty to add (high alch value)
	 * @return true if ranked up, false otherwise
	 */
	public boolean addBooty(long amount)
	{
		this.totalBooty += amount;
		PirateRank newRank = PirateRank.getRankForBooty(this.totalBooty);
		
		if (newRank != this.currentRank)
		{
			this.previousRank = this.currentRank;
			this.currentRank = newRank;
			this.justRankedUp = true;
			return true;
		}
		
		return false;
	}

	/**
	 * Clear the rank up flag (after displaying notification)
	 */
	public void clearRankUpFlag()
	{
		this.justRankedUp = false;
	}

	/**
	 * Get progress to next rank as a percentage (0.0 to 1.0)
	 */
	public double getProgressToNextRank()
	{
		return currentRank.getProgressToNextRank(totalBooty);
	}

	/**
	 * Get booty needed for next rank
	 */
	public long getBootyNeededForNextRank()
	{
		PirateRank nextRank = currentRank.getNextRank();
		if (nextRank == null)
		{
			return 0; // Max rank
		}
		return nextRank.getBootyRequired() - totalBooty;
	}
}
