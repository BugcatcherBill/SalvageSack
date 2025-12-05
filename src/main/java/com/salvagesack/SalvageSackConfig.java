package com.salvagesack;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("salvagesack")
public interface SalvageSackConfig extends Config
{
	@ConfigItem(
		keyName = "showPanel",
		name = "Show Panel",
		description = "Show the salvage tracking panel"
	)
	default boolean showPanel()
	{
		return true;
	}

	@ConfigItem(
		keyName = "trackUnknownItems",
		name = "Track Unknown Items",
		description = "Track items that are not in the expected loot table"
	)
	default boolean trackUnknownItems()
	{
		return true;
	}
}
