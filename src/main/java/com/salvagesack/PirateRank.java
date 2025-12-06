package com.salvagesack;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Pirate ranks achieved through accumulating booty (high alch value).
 * 100 ranks with smooth exponential progression from 0 GP to max cash stack.
 * Early ranks are quick to achieve, while higher ranks require exponentially more effort.
 */
@Getter
@RequiredArgsConstructor
public enum PirateRank
{
	// Ranks 1-10: Quick Start (0 - 5.3M) - Immediate engagement
	RANK_1(0, "Castaway", "Washed up in Lumbridge"),
	RANK_2(22021, "Beachcomber", "Collecting seashells"),
	RANK_3(124570, "Deck Swabber", "Cleaning barnacles"),
	RANK_4(343277, "Rope Hauler", "Learning the ropes"),
	RANK_5(704679, "Cabin Boy", "Fetching grog"),
	RANK_6(1231025, "Bilge Rat", "Working the depths"),
	RANK_7(1941869, "Powder Monkey", "Handling cannons"),
	RANK_8(2854874, "Galley Hand", "Peeling potatoes"),
	RANK_9(3986271, "Ship's Cook", "Burnt fish specialist"),
	RANK_10(5351162, "Able Seaman", "Competent sailor"),
	
	// Ranks 11-20: Building Skills (5.3M - 34.6M)
	RANK_11(6963729, "Deck Hand", "Trusted crewmate"),
	RANK_12(8837381, "Rigger", "Rope master"),
	RANK_13(10984871, "Topman", "Climbing high"),
	RANK_14(13418385, "Lookout", "Eagle eyes"),
	RANK_15(16149607, "Navigator", "Chart reader"),
	RANK_16(19189781, "Gunner", "Cannon operator"),
	RANK_17(22549755, "Bosun", "Crew supervisor"),
	RANK_18(26240021, "Master Gunner", "Artillery expert"),
	RANK_19(30270747, "Sailing Master", "Wind whisperer"),
	RANK_20(34651806, "Quartermaster", "Supply manager"),
	
	// Ranks 21-30: Experienced (34.6M - 99.7M)
	RANK_21(39392801, "Carpenter", "Ship builder"),
	RANK_22(44503084, "Surgeon", "Sawbones"),
	RANK_23(49991777, "First Mate", "Right hand"),
	RANK_24(55867787, "Helmsman", "Wheel master"),
	RANK_25(62139818, "Privateer", "Licensed raider"),
	RANK_26(68816392, "Buccaneer", "Caribbean terror"),
	RANK_27(75905850, "Corsair", "Coastal menace"),
	RANK_28(83416370, "Sea Wolf", "Ocean predator"),
	RANK_29(91355975, "Marauder", "Island raider"),
	RANK_30(99732538, "Raider Captain", "Leading raids"),
	
	// Ranks 31-40: Notorious (99.7M - 209M)
	RANK_31(108553796, "Plunderer", "Treasure hunter"),
	RANK_32(117827350, "Freebooter", "Independent spirit"),
	RANK_33(127560679, "Brigand", "Lawless bandit"),
	RANK_34(137761140, "Sea Reaver", "Ocean pillager"),
	RANK_35(148435975, "Pirate Captain", "Ship commander"),
	RANK_36(159592320, "Pirate Lord", "Fleet owner"),
	RANK_37(171237205, "Dread Pirate", "Fear incarnate"),
	RANK_38(183377559, "Scourge", "Naval nightmare"),
	RANK_39(196020219, "Terror", "Coastal doom"),
	RANK_40(209171926, "Pirate King", "Regional ruler"),
	
	// Ranks 41-50: Legendary (209M - 370M)
	RANK_41(222839336, "Skeletal Captain", "Undead commander"),
	RANK_42(237029019, "Ghost Admiral", "Phantom fleet"),
	RANK_43(251747464, "Treasure Baron", "Wealth hoarder"),
	RANK_44(267001079, "Fleet Admiral", "Armada leader"),
	RANK_45(282796200, "Sea Tyrant", "Ocean dictator"),
	RANK_46(299139086, "Pirate Emperor", "Maritime crown"),
	RANK_47(316035928, "Kraken Slayer", "Deep hunter"),
	RANK_48(333492847, "Leviathan Tamer", "Beast master"),
	RANK_49(351515898, "Storm Caller", "Weather bender"),
	RANK_50(370111073, "Ocean Sovereign", "Sea lord"),
	
	// Ranks 51-60: Mythical (370M - 588M)
	RANK_51(389284300, "Poseidon's Rival", "God challenger"),
	RANK_52(409041449, "Davy Jones", "Locker keeper"),
	RANK_53(429388331, "Ancient Mariner", "Timeless sailor"),
	RANK_54(450330698, "Immortal Captain", "Deathless legend"),
	RANK_55(471874249, "Phantom King", "Spectral ruler"),
	RANK_56(494024631, "Cursed One", "Eternal wanderer"),
	RANK_57(516787435, "Nightmare", "Dream invader"),
	RANK_58(540168205, "Abyssal Lord", "Deep dweller"),
	RANK_59(564172434, "Void Pirate", "Darkness master"),
	RANK_60(588805567, "Cosmic Corsair", "Star sailor"),
	
	// Ranks 61-70: Elite (588M - 870M)
	RANK_61(614073003, "Dragon Slayer", "Wyrm bane"),
	RANK_62(639980094, "Giant Killer", "Titan feller"),
	RANK_63(666532150, "Demon Hunter", "Hell raider"),
	RANK_64(693734435, "God Slayer", "Divine bane"),
	RANK_65(721592173, "Chaos Bringer", "Order breaker"),
	RANK_66(750110544, "Void Walker", "Reality bender"),
	RANK_67(779294691, "Time Pirate", "Temporal raider"),
	RANK_68(809149715, "Dimension Lord", "Plane hopper"),
	RANK_69(839680679, "Reality Breaker", "Laws defier"),
	RANK_70(870892610, "Infinity Captain", "Endless voyager"),
	
	// Ranks 71-80: Grand Master (870M - 1.22B)
	RANK_71(902790496, "Cosmic Emperor", "Universe ruler"),
	RANK_72(935379291, "Eternal King", "Timeless sovereign"),
	RANK_73(968663911, "Omnipotent", "All-powerful"),
	RANK_74(1002649240, "Transcendent", "Beyond mortal"),
	RANK_75(1037340127, "Ascended One", "Higher plane"),
	RANK_76(1072741389, "Divine Pirate", "Godhood achieved"),
	RANK_77(1108857809, "Supreme Being", "Ultimate power"),
	RANK_78(1145694140, "Celestial Lord", "Heavenly ruler"),
	RANK_79(1183255101, "Astral King", "Cosmic sovereign"),
	RANK_80(1221545384, "Multiverse Pirate", "Reality hopper"),
	
	// Ranks 81-90: Insane (1.22B - 1.64B)
	RANK_81(1260569649, "Existence Shaper", "Reality sculptor"),
	RANK_82(1300332526, "Paradox Master", "Logic breaker"),
	RANK_83(1340838618, "Entropy Lord", "Chaos embodied"),
	RANK_84(1382092498, "Singularity", "Unified force"),
	RANK_85(1424098713, "Origin Pirate", "First raider"),
	RANK_86(1466861780, "Primordial", "Ancient power"),
	RANK_87(1510386193, "Harbinger", "End bringer"),
	RANK_88(1554676415, "Apocalypse", "World ender"),
	RANK_89(1599736888, "Omega", "Final captain"),
	RANK_90(1645572024, "Beyond", "Past limits"),
	
	// Ranks 91-100: Max Cash Journey (1.64B - 2.14B)
	RANK_91(1692186213, "Unfathomable", "Past comprehension"),
	RANK_92(1739583820, "Incomprehensible", "Mind breaking"),
	RANK_93(1787769184, "Ineffable", "Beyond words"),
	RANK_94(1836746624, "Absolute", "Total power"),
	RANK_95(1886520430, "Perfect", "Flawless mastery"),
	RANK_96(1937094875, "Ultimate", "Final form"),
	RANK_97(1988474204, "Supreme", "Highest peak"),
	RANK_98(2040662644, "Pinnacle", "Apex achieved"),
	RANK_99(2093664398, "Zenith", "Maximum height"),
	RANK_100(Integer.MAX_VALUE, "King of the Pirates", "2,147,483,647 GP");

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
		PirateRank currentRank = RANK_1;
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
