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
	SMALL("Small shipwreck", "Small"),
	FISHERMANS("Fisherman's shipwreck", "Fishy"),
	BARRACUDA("Barracuda shipwreck", "Barracuda"),
	LARGE("Large shipwreck", "Large"),
	PIRATE("Pirate shipwreck", "Plundered"),
	MERCENARY("Mercenary shipwreck", "Martial"),
	FREMENNIK("Fremennik shipwreck", "Fremennik"),
	MERCHANT("Merchant shipwreck", "Opulent"),
	UNKNOWN("Unknown", "unknown");

	private final String displayName;
	private final String salvageType;

	public static ShipwreckType fromString(String text)
	{
		if (text == null)
		{
			return UNKNOWN;
		}

		String lowerText = text.toLowerCase().trim();
		for (ShipwreckType type : ShipwreckType.values())
		{
			if (type.salvageType.toLowerCase().equals(lowerText) ||
			    type.displayName.equalsIgnoreCase(text) ||
			    lowerText.contains(type.salvageType.toLowerCase()))
			{
				return type;
			}
		}
		return UNKNOWN;
	}
}
