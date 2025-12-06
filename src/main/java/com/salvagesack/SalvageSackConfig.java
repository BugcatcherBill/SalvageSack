package com.salvagesack;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("salvagesack")
public interface SalvageSackConfig extends Config
{
	@ConfigItem(
		keyName = "sortOption",
		name = "Sort Items By",
		description = "Choose how to sort items within each shipwreck type"
	)
	default SortOption sortOption()
	{
		return SortOption.ALPHABETICAL;
	}

	@ConfigItem(
		keyName = "sortDescending",
		name = "Sort Descending",
		description = "Sort items in descending order (highest to lowest)"
	)
	default boolean sortDescending()
	{
		return false;
	}

	@ConfigItem(
		keyName = "enablePirateRanks",
		name = "Enable Pirate Ranks",
		description = "Enable the pirate rank microgame that tracks your booty and progression"
	)
	default boolean enablePirateRanks()
	{
		return true;
	}
}
