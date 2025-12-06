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
	// Beginner ranks (0-100k) - Starting your journey
	CASTAWAY(0, "Castaway", "Washed up in Lumbridge"),
	DECK_SWABBER(2000, "Deck Swabber", "Cleaning barnacles at Port Sarim"),
	CABIN_BOY(4000, "Cabin Boy", "Fetching grog for Bill Teach"),
	POWDER_MONKEY(6000, "Powder Monkey", "Handling dwarf cannons"),
	SHIPS_COOK(8000, "Ship's Cook", "Burnt fish specialist"),
	
	// Junior ranks (10k-50k) - Port Sarim connections
	ABLE_SEAMAN(10000, "Able Seaman", "Redberry pie approved"),
	RIGGER(15000, "Rigger", "Master of agility shortcuts"),
	LOOKOUT(20000, "Lookout", "Spying from Eagles' Peak"),
	NAVIGATOR(25000, "Navigator", "Reading ancient charts"),
	GUNNER(30000, "Gunner", "Wannabe Cannonballer"),
	BOSUN(35000, "Bosun", "Brimhaven's finest"),
	MASTER_GUNNER(40000, "Master Gunner", "Artisan of explosions"),
	SAILING_MASTER(45000, "Sailing Master", "Charting the Arc"),
	
	// Intermediate ranks (50k-150k) - Pirate legends and locations
	QUARTERMASTER(50000, "Quartermaster", "Trading at Mos Le'Harmless"),
	CARPENTER(60000, "Carpenter", "Fixing up your POH ship"),
	SURGEON(70000, "Surgeon", "4 HP healer"),
	FIRST_MATE(80000, "First Mate", "Bill Teach's rival"),
	HELMSMAN(90000, "Helmsman", "Steering through Crandor"),
	PRIVATEER(100000, "Privateer", "Royal charter holder"),
	BUCCANEER(120000, "Buccaneer", "Scourge of Karamja"),
	CORSAIR(140000, "Corsair", "Terror of Corsair Cove"),
	
	// Advanced ranks (150k-500k) - Wilderness pirates and dangers
	SEA_WOLF(150000, "Sea Wolf", "Mage Arena II survivor"),
	MARAUDER(175000, "Marauder", "Fossil Island raider"),
	RAIDER(200000, "Raider", "Looting Barrows chests"),
	PLUNDERER(225000, "Plunderer", "Pyramid Plunder expert"),
	FREEBOOTER(250000, "Freebooter", "Answerable to none"),
	ROCK_CRAB_CRUSHER(275000, "Rock Crab Crusher", "Slayer of Waterbirth"),
	SEA_REAVER(300000, "Sea Reaver", "Fremennik approved"),
	PIRATE_LORD(350000, "Pirate Lord", "Ruler of Braindeath Island"),
	RABID_JACK_RIVAL(400000, "Rabid Jack Rival", "Worthy adversary"),
	SCOURGE(450000, "Scourge of the Seas", "Feared across Gielinor"),
	
	// Elite ranks (500k-2M) - Legendary bosses and quests
	DREAD_CAPTAIN(500000, "Dread Captain", "Haunting the seven seas"),
	SKELETAL_ADMIRAL(600000, "Skeletal Admiral", "Undead fleet commander"),
	TREASURE_BARON(700000, "Treasure Baron", "Casket hoarder"),
	FLEET_ADMIRAL(800000, "Fleet Admiral", "Leading the armada"),
	ECTOFUNTUS_DEVOTEE(900000, "Ectofuntus Devotee", "Worshipping for booty"),
	PIRATE_EMPEROR(1000000, "Pirate Emperor", "Crown of the ocean"),
	BARRELCHEST_SLAYER(1200000, "Barrelchest Slayer", "Defeated the legend"),
	KRAKEN_TAMER(1400000, "Kraken Tamer", "87 Slayer achieved"),
	JORMUNGAND_HUNTER(1600000, "Jormungand Hunter", "Snake slayer supreme"),
	LEVIATHAN_LORD(1800000, "Leviathan Lord", "DT2 conqueror"),
	
	// Legendary ranks (2M+) - God-tier OSRS references
	GHOST_CAPTAIN(2000000, "Ghost Captain", "Ectoplasm incarnate"),
	PHANTOM_ADMIRAL(2500000, "Phantom Admiral", "Port Phasmatys legend"),
	TEMPOROS_TAMER(3000000, "Temporos Tamer", "Storm god subdued"),
	MONKEY_MADNESS(3500000, "Monkey Madness", "Ape Atoll survivor"),
	PIRATE_PETE_RIVAL(4000000, "Pirate Pete Rival", "Rum Deal champion"),
	OCEAN_SOVEREIGN(5000000, "Ocean Sovereign", "Master of all waters"),
	JONES_LOCKER_KEEPER(6000000, "Jones' Locker Keeper", "Guardian of the deep"),
	DRAGON_SLAYER(7500000, "Dragon Slayer", "Elvarg's bane"),
	WISE_OLD_PIRATE(10000000, "Wise Old Pirate", "Bank standing legend");

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
