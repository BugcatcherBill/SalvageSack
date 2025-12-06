package com.salvagesack;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Pirate ranks achieved through accumulating booty (high alch value).
 * Ranks progress from humble beginnings to legendary pirate status.
 */
@Getter
@RequiredArgsConstructor
public enum PirateRank
{
	// Beginner ranks (0-100k)
	CASTAWAY(0, "Castaway", "Cast ashore with nothing"),
	DECK_SWABBER(2000, "Deck Swabber", "The lowest of the low"),
	CABIN_BOY(4000, "Cabin Boy", "Learning the ropes"),
	POWDER_MONKEY(6000, "Powder Monkey", "Handling explosives"),
	SHIPS_COOK(8000, "Ship's Cook", "Feeding the crew"),
	
	// Junior ranks (10k-50k)
	ABLE_SEAMAN(10000, "Able Seaman", "Competent sailor"),
	RIGGER(15000, "Rigger", "Master of the rigging"),
	LOOKOUT(20000, "Lookout", "Eagle-eyed watcher"),
	NAVIGATOR(25000, "Navigator", "Charting the course"),
	GUNNER(30000, "Gunner", "Manning the cannons"),
	BOSUN(35000, "Bosun", "Ship's supervisor"),
	MASTER_GUNNER(40000, "Master Gunner", "Artillery expert"),
	SAILING_MASTER(45000, "Sailing Master", "Navigation specialist"),
	
	// Intermediate ranks (50k-150k)
	QUARTERMASTER(50000, "Quartermaster", "Supply master"),
	CARPENTER(60000, "Carpenter", "Ship's builder"),
	SURGEON(70000, "Surgeon", "Sawbones of the sea"),
	FIRST_MATE(80000, "First Mate", "Captain's right hand"),
	HELMSMAN(90000, "Helmsman", "Master of the wheel"),
	PRIVATEER(100000, "Privateer", "Licensed plunderer"),
	BUCCANEER(120000, "Buccaneer", "Caribbean raider"),
	CORSAIR(140000, "Corsair", "Mediterranean menace"),
	
	// Advanced ranks (150k-500k)
	SEA_WOLF(150000, "Sea Wolf", "Feared predator"),
	MARAUDER(175000, "Marauder", "Ruthless raider"),
	RAIDER(200000, "Raider", "Coastal terror"),
	PLUNDERER(225000, "Plunderer", "Master looter"),
	FREEBOOTER(250000, "Freebooter", "Independent pirate"),
	BRIGAND(275000, "Brigand", "Lawless bandit"),
	SEA_REAVER(300000, "Sea Reaver", "Ocean pillager"),
	PIRATE_LORD(350000, "Pirate Lord", "Regional power"),
	CUTTHROAT_CAPTAIN(400000, "Cutthroat Captain", "Merciless leader"),
	SCOURGE(450000, "Scourge", "Naval nightmare"),
	
	// Elite ranks (500k-2M)
	DREAD_CAPTAIN(500000, "Dread Captain", "Feared commander"),
	SKULL_KING(600000, "Skull King", "Death's ambassador"),
	TREASURE_BARON(700000, "Treasure Baron", "Wealthy plunderer"),
	FLEET_ADMIRAL(800000, "Fleet Admiral", "Multi-ship commander"),
	SEA_TYRANT(900000, "Sea Tyrant", "Ocean dictator"),
	PIRATE_EMPEROR(1000000, "Pirate Emperor", "Ruler of pirates"),
	LEGENDARY_CORSAIR(1200000, "Legendary Corsair", "Living legend"),
	KRAKEN_MASTER(1400000, "Kraken Master", "Beast tamer"),
	NEPTUNES_NIGHTMARE(1600000, "Neptune's Nightmare", "God's enemy"),
	LEVIATHAN_LORD(1800000, "Leviathan Lord", "Sea monster"),
	
	// Legendary ranks (2M+)
	IMMORTAL_CAPTAIN(2000000, "Immortal Captain", "Deathless legend"),
	GHOST_ADMIRAL(2500000, "Ghost Admiral", "Phantom fleet commander"),
	STORM_BRINGER(3000000, "Storm Bringer", "Weather controller"),
	CURSE_BEARER(3500000, "Curse Bearer", "Eternally damned"),
	KINGS_BANE(4000000, "King's Bane", "Crown's nightmare"),
	OCEAN_SOVEREIGN(5000000, "Ocean Sovereign", "Master of seas"),
	DAVY_JONES(6000000, "Davy Jones", "Locker keeper"),
	POSEIDON_SLAYER(7500000, "Poseidon Slayer", "God killer"),
	ETERNAL_LEGEND(10000000, "Eternal Legend", "Forever remembered");

	private final int bootyRequired;
	private final String displayName;
	private final String description;

	/**
	 * Get the pirate rank for a given amount of booty
	 * @param totalBooty Total booty accumulated
	 * @return The highest rank achieved
	 */
	public static PirateRank getRankForBooty(long totalBooty)
	{
		PirateRank currentRank = CASTAWAY;
		for (PirateRank rank : values())
		{
			if (totalBooty >= rank.getBootyRequired())
			{
				currentRank = rank;
			}
			else
			{
				break;
			}
		}
		return currentRank;
	}

	/**
	 * Get the next rank after this one
	 * @return The next rank, or null if this is the highest rank
	 */
	public PirateRank getNextRank()
	{
		int nextOrdinal = ordinal() + 1;
		PirateRank[] ranks = values();
		if (nextOrdinal < ranks.length)
		{
			return ranks[nextOrdinal];
		}
		return null;
	}

	/**
	 * Calculate progress to next rank as a percentage
	 * @param totalBooty Current total booty
	 * @return Progress from 0.0 to 1.0, or 1.0 if at max rank
	 */
	public double getProgressToNextRank(long totalBooty)
	{
		PirateRank nextRank = getNextRank();
		if (nextRank == null)
		{
			return 1.0; // Max rank
		}

		long currentRequirement = this.bootyRequired;
		long nextRequirement = nextRank.getBootyRequired();
		long bootyInRange = totalBooty - currentRequirement;
		long rangeSize = nextRequirement - currentRequirement;

		if (rangeSize <= 0)
		{
			return 1.0;
		}

		return Math.min(1.0, Math.max(0.0, (double) bootyInRange / rangeSize));
	}

	@Override
	public String toString()
	{
		return displayName;
	}
}
