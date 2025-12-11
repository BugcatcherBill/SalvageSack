package com.salvagesack;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.awt.Color;

/**
 * Pirate ranks achieved through accumulating booty (high alch value).
 * 100 ranks with smooth exponential progression from 0 GP to max cash stack.
 * Early ranks are quick to achieve, while higher ranks require exponentially more effort.
 * Each rank can have custom colors for the progress arc, circle, and progress bar, plus an icon.
 */
@Getter
@RequiredArgsConstructor
public enum PirateRank
{
	// Ranks 1-10: Quick Start (0 - 5.3M) - Immediate engagement
	// Rank 1: Warm sandy brown with golden yellow (beach/castaway theme)
	RANK_1(0, "Castaway", "Washed up in Lumbridge", "Rank1.png", new Color(139, 90, 43), new Color(255, 215, 0), new Color(255, 215, 0)),
	// Rank 2: Ocean teal with bright aqua (beachcomber/sea shell theme)
	RANK_2(22021, "Barnacle Scraper", "Hey someone has to do it", "Rank2.png", new Color(30, 144, 155), new Color(127, 255, 212), new Color(127, 255, 212)),
	// Rank 3: Deep navy with silver-blue (deck work/nautical theme)
	RANK_3(124570, "Landlubber", "Hates open ocean", "Rank3.png", new Color(25, 25, 112), new Color(176, 196, 222), new Color(176, 196, 222)),
	// Rank 4: Rich mahogany with amber (rope/wood theme)
	RANK_4(343277, "Driftwood", "Useless, but maybe not someday", "Rank4.png", new Color(128, 64, 64), new Color(255, 191, 0), new Color(255, 191, 0)),
	// Rank 5: Dark olive with lime green (cabin boy/grog theme)
	RANK_5(704679, "Stowaway", "You were never here", "Rank5.png", new Color(85, 107, 47), new Color(173, 255, 47), new Color(173, 255, 47)),
	// Rank 6: Dark slate with coral orange (bilge/depths theme)
	RANK_6(1231025, "Plank Walker", "Destined to be one with the ocean", "Rank6.png", new Color(47, 79, 79), new Color(255, 127, 80), new Color(255, 127, 80)),
	RANK_7(1941869, "Chum Bucket", "A certified snack", "Rank10.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_8(3986271, "Prisoner", "Pretty good at being locked up", "Rank2.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_9(3986271, "Anchor Andy", "Swoll enough to lift heavy things", "Rank2.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_10(2854874, "Galley Slave", "Rowing until the sweet release of death", "Rank2.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	
	// Ranks 11-20: Building Skills (5.3M - 34.6M)
	RANK_11(6963729, "Swabbie", "The poop deck wont swab itself", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_12(6963729, "Bailin' Boy", "Go back to your home, water", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_13(6963729, "Cabin Boy", "The Captain requires liquid refreshment", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_14(8837381, "Rope Winder", "Neat and tidy as all things should be", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_15(8837381, "Deck Scrubber", "Stains don't stand a chance", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_16(8837381, "Scallywag", "Where you go, mischief follows", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_17(8837381, "Grog Server Greg", "Ever drank Bailey's from a shoe?", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_18(8837381, "Powder Monkey", "Would never run with scissors", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_19(8837381, "Rat Catcher", "Smarter than your average rat", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_20(8837381, "Deckhand", "Trusted to do at least 1 thing", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),

	// Ranks 21-30: Experienced (34.6M - 99.7M)
	RANK_21(8837381, "Jack Tar", "Sticky hands, dry boat", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_22(8837381, "Knot Master", "Very Knotty", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_23(8837381, "Rigger", "Likes touching ropes, like a lot", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_24(8837381, "Deckhand", "Trusted to do at least 1 thing", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_25(8837381, "Sail Mender", "Has a pretty high crafting level", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_26(10984871, "Topman", "Look at you climb", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_27(13418385, "Lookout", "Eagle eyes", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_28(13418385, "Crow's Nest Carrie", "Always looking down on others", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_29(13418385, "Rigger", "Expert Swinger", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_30(13418385, "Able Seaman", "No comment", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),

	// Ranks 31-40: Notorious (99.7M - 209M)



//RANK_9(3986271, "Ship's Cook", "Burnt fish specialist", "Rank2.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
// RANK_10(5351162, "Able Seaman", "Competent sailor", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
//	RANK_15(16149607, "Navigator", "Chart reader", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
//	RANK_16(19189781, "Gunner", "Cannon operator", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
//	RANK_17(22549755, "Bosun", "Crew supervisor", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
//	RANK_18(26240021, "Master Gunner", "Artillery expert", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
//	RANK_19(30270747, "Sailing Master", "Wind whisperer", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
//	RANK_20(34651806, "Quartermaster", "Supply manager", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
//	RANK_21(39392801, "Carpenter", "Ship builder", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
//	RANK_22(44503084, "Surgeon", "Sawbones", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
//	RANK_23(49991777, "First Mate", "Right hand", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
//	RANK_24(55867787, "Helmsman", "Wheel master", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
//	RANK_25(62139818, "Privateer", "Licensed raider", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
//	RANK_26(68816392, "Buccaneer", "Caribbean terror", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
//	RANK_27(75905850, "Corsair", "Coastal menace", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
//	RANK_28(83416370, "Sea Wolf", "Ocean predator", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
//	RANK_29(91355975, "Marauder", "Island raider", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
//	RANK_30(99732538, "Raider Captain", "Leading raids", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	

	RANK_31(108553796, "Plunderer", "Treasure hunter", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_32(117827350, "Freebooter", "Independent spirit", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_33(127560679, "Brigand", "Lawless bandit", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_34(137761140, "Sea Reaver", "Ocean pillager", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_35(148435975, "Pirate Captain", "Ship commander", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_36(159592320, "Pirate Lord", "Fleet owner", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_37(171237205, "Dread Pirate", "Fear incarnate", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_38(183377559, "Scourge", "Naval nightmare", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_39(196020219, "Terror", "Coastal doom", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_40(209171926, "Pirate King", "Regional ruler", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	
	// Ranks 41-50: Legendary (209M - 370M)
	RANK_41(222839336, "Skeletal Captain", "Undead commander", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_42(237029019, "Ghost Admiral", "Phantom fleet", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_43(251747464, "Treasure Baron", "Wealth hoarder", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_44(267001079, "Fleet Admiral", "Armada leader", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_45(282796200, "Sea Tyrant", "Ocean dictator", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_46(299139086, "Pirate Emperor", "Maritime crown", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_47(316035928, "Kraken Slayer", "Deep hunter", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_48(333492847, "Leviathan Tamer", "Beast master", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_49(351515898, "Storm Caller", "Weather bender", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_50(370111073, "Ocean Sovereign", "Sea lord", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	
	// Ranks 51-60: Mythical (370M - 588M)
	RANK_51(389284300, "Poseidon's Rival", "God challenger", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_52(409041449, "Davy Jones", "Locker keeper", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_53(429388331, "Ancient Mariner", "Timeless sailor", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_54(450330698, "Immortal Captain", "Deathless legend", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_55(471874249, "Phantom King", "Spectral ruler", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_56(494024631, "Cursed One", "Eternal wanderer", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_57(516787435, "Nightmare", "Dream invader", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_58(540168205, "Abyssal Lord", "Deep dweller", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_59(564172434, "Void Pirate", "Darkness master", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_60(588805567, "Cosmic Corsair", "Star sailor", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	
	// Ranks 61-70: Elite (588M - 870M)
	RANK_61(614073003, "Dragon Slayer", "Wyrm bane", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_62(639980094, "Giant Killer", "Titan feller", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_63(666532150, "Demon Hunter", "Hell raider", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_64(693734435, "God Slayer", "Divine bane", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_65(721592173, "Chaos Bringer", "Order breaker", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_66(750110544, "Void Walker", "Reality bender", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_67(779294691, "Time Pirate", "Temporal raider", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_68(809149715, "Dimension Lord", "Plane hopper", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_69(839680679, "Reality Breaker", "Laws defier", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_70(870892610, "Infinity Captain", "Endless voyager", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	
	// Ranks 71-80: Grand Master (870M - 1.22B)
	RANK_71(902790496, "Cosmic Emperor", "Universe ruler", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_72(935379291, "Eternal King", "Timeless sovereign", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_73(968663911, "Omnipotent", "All-powerful", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_74(1002649240, "Transcendent", "Beyond mortal", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_75(1037340127, "Ascended One", "Higher plane", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_76(1072741389, "Divine Pirate", "Godhood achieved", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_77(1108857809, "Supreme Being", "Ultimate power", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_78(1145694140, "Celestial Lord", "Heavenly ruler", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_79(1183255101, "Astral King", "Cosmic sovereign", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_80(1221545384, "Multiverse Pirate", "Reality hopper", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	
	// Ranks 81-90: Insane (1.22B - 1.64B)
	RANK_81(1260569649, "Existence Shaper", "Reality sculptor", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_82(1300332526, "Paradox Master", "Logic breaker", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_83(1340838618, "Entropy Lord", "Chaos embodied", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_84(1382092498, "Singularity", "Unified force", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_85(1424098713, "Origin Pirate", "First raider", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_86(1466861780, "Primordial", "Ancient power", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_87(1510386193, "Harbinger", "End bringer", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_88(1554676415, "Apocalypse", "World ender", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_89(1599736888, "Omega", "Final captain", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_90(1645572024, "Beyond", "Past limits", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	
	// Ranks 91-100: Max Cash Journey (1.64B - 2.14B)
	RANK_91(1692186213, "Unfathomable", "Past comprehension", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_92(1739583820, "Incomprehensible", "Mind breaking", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_93(1787769184, "Ineffable", "Beyond words", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_94(1836746624, "Absolute", "Total power", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_95(1886520430, "Perfect", "Flawless mastery", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_96(1937094875, "Ultimate", "Final form", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_97(1988474204, "Supreme", "Highest peak", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_98(2040662644, "Pinnacle", "Apex achieved", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_99(2093664398, "Zenith", "Maximum height", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32)),
	RANK_100(Integer.MAX_VALUE, "King of the Pirates", "There can be only 1", "Rank1.png", new Color(139, 69, 19), new Color(218, 165, 32), new Color(218, 165, 32));

	private final int bootyRequired;
	private final String displayName;
	private final String description;
	private final String iconFileName;
	private final Color circleColor;
	private final Color arcColor;
	private final Color progressBarColor;

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
