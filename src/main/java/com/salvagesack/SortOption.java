package com.salvagesack;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Sorting options for item display
 */
@Getter
@RequiredArgsConstructor
public enum SortOption
{
	ALPHABETICAL("Alphabetical"),
	CURRENT_RATE("Current Rate"),
	EXPECTED_RATE("Expected Rate"),
	QUANTITY("Quantity");

	private final String displayName;

	@Override
	public String toString()
	{
		return displayName;
	}
}
