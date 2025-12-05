package com.salvagesack;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("salvagesack")
@SuppressWarnings("unused") // Methods are used by RuneLite's config system via reflection
public interface SalvageSackConfig extends Config
{
	@ConfigSection(
		name = "Reset Data",
		description = "Reset tracking data for specific shipwreck types",
		position = 0,
		closedByDefault = true
	)
	String resetSection = "resetSection";

	@ConfigItem(
		keyName = "resetSmall",
		name = "Reset Small Shipwreck",
		description = "Click to reset tracking data for Small shipwrecks",
		section = resetSection,
		position = 0
	)
	default boolean resetSmall()
	{
		return false;
	}

	@ConfigItem(
		keyName = "resetFishermans",
		name = "Reset Fisherman's Shipwreck",
		description = "Click to reset tracking data for Fisherman's shipwrecks",
		section = resetSection,
		position = 1
	)
	default boolean resetFishermans()
	{
		return false;
	}

	@ConfigItem(
		keyName = "resetBarracuda",
		name = "Reset Barracuda Shipwreck",
		description = "Click to reset tracking data for Barracuda shipwrecks",
		section = resetSection,
		position = 2
	)
	default boolean resetBarracuda()
	{
		return false;
	}

	@ConfigItem(
		keyName = "resetLarge",
		name = "Reset Large Shipwreck",
		description = "Click to reset tracking data for Large shipwrecks",
		section = resetSection,
		position = 3
	)
	default boolean resetLarge()
	{
		return false;
	}

	@ConfigItem(
		keyName = "resetPirate",
		name = "Reset Pirate Shipwreck",
		description = "Click to reset tracking data for Pirate shipwrecks",
		section = resetSection,
		position = 4
	)
	default boolean resetPirate()
	{
		return false;
	}

	@ConfigItem(
		keyName = "resetMercenary",
		name = "Reset Mercenary Shipwreck",
		description = "Click to reset tracking data for Mercenary shipwrecks",
		section = resetSection,
		position = 5
	)
	default boolean resetMercenary()
	{
		return false;
	}

	@ConfigItem(
		keyName = "resetFremennik",
		name = "Reset Fremennik Shipwreck",
		description = "Click to reset tracking data for Fremennik shipwrecks",
		section = resetSection,
		position = 6
	)
	default boolean resetFremennik()
	{
		return false;
	}

	@ConfigItem(
		keyName = "resetMerchant",
		name = "Reset Merchant Shipwreck",
		description = "Click to reset tracking data for Merchant shipwrecks",
		section = resetSection,
		position = 7
	)
	default boolean resetMerchant()
	{
		return false;
	}

	@ConfigItem(
		keyName = "resetAll",
		name = "Reset All Data",
		description = "Click to reset all salvage tracking data",
		section = resetSection,
		position = 8
	)
	default boolean resetAll()
	{
		return false;
	}
}
