package com.salvagesack;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Represents different types of shipwrecks in OSRS Sailing
 */
@Getter
@RequiredArgsConstructor
public enum ShipwreckType
{
	SMALL("Small Shipwreck"),
	MEDIUM("Medium Shipwreck"),
	LARGE("Large Shipwreck"),
	UNKNOWN("Unknown");

	private final String displayName;

	public static ShipwreckType fromString(String text)
	{
		for (ShipwreckType type : ShipwreckType.values())
		{
			if (type.displayName.equalsIgnoreCase(text))
			{
				return type;
			}
		}
		return UNKNOWN;
	}
}
