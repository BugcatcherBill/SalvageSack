package com.salvagesack;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Sorting options for item display in the salvage panel.
 * <p>
 * Each option determines how items are ordered within each shipwreck section:
 * <ul>
 *   <li>ALPHABETICAL - Sort items by name (A-Z or Z-A)</li>
 *   <li>CURRENT_RATE - Sort by actual drop rate calculated from player's data</li>
 *   <li>EXPECTED_RATE - Sort by wiki-sourced expected drop rates</li>
 *   <li>QUANTITY - Sort by total quantity received</li>
 *   <li>LUCK - Sort by luck ratio (current/expected rate), grouping by color:
 *       <ul>
 *         <li>Green: ratio ≥ 1.1 (lucky - getting items more often)</li>
 *         <li>Yellow: 0.9 ≤ ratio < 1.1 (neutral - at expected rate)</li>
 *         <li>Red: ratio < 0.9 (unlucky - getting items less often)</li>
 *         <li>Unknown: no expected rate data available</li>
 *       </ul>
 *   </li>
 * </ul>
 * The sort direction (ascending/descending) can be toggled independently.
 * </p>
 */
@Getter
@RequiredArgsConstructor
public enum SortOption
{
	ALPHABETICAL("Alphabetical"),
	CURRENT_RATE("Current Rate"),
	EXPECTED_RATE("Expected Rate"),
	QUANTITY("Quantity"),
	LUCK("Luck");

	private final String displayName;

	@Override
	public String toString()
	{
		return displayName;
	}
}
