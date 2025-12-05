# Development Notes

## Architecture

The Salvage Sack plugin consists of several key components:

### Data Models
- **ShipwreckType**: Enum defining different types of shipwrecks (Small, Medium, Large)
- **SalvageItem**: Represents an individual item with drop tracking
- **SalvageData**: Aggregates all salvage data for a specific shipwreck type

### Core Components
- **SalvageSackPlugin**: Main plugin class that handles chat messages and coordinates data flow
- **SalvageDataManager**: Manages persistence of data to disk using JSON
- **ItemIconManager**: Fetches and caches item icons from the OSRS wiki
- **SalvageSackPanel**: UI panel that displays the tracked data

### Data Persistence
Data is saved automatically:
- After every loot drop
- When the plugin shuts down

Data is stored in: `<runelite-config-directory>/salvagesack/salvage-data.json`

### Chat Message Pattern
The plugin listens for chat messages matching the pattern:
```
You salvage [a/an] <item> from the <shipwreck type>.
```

### Expected Drop Rates
Currently, the expected drop rates are hardcoded placeholders in the plugin.
In a production implementation, these should be:
1. Loaded from a configuration file
2. Fetched from the OSRS Wiki API
3. Made configurable by the user

### Item Icons
Item icons are fetched from the OSRS Wiki using the pattern:
```
https://oldschool.runescape.wiki/images/<ItemName>_detail.png
```

Icons are cached in memory to avoid repeated network requests.

## Future Enhancements

1. **Wiki Integration**: Fetch drop rates from the OSRS Wiki API
2. **Export/Import**: Allow users to export/import their data
3. **Statistics View**: Show aggregate statistics across all shipwreck types
4. **Reset Function**: Allow users to reset tracking data
5. **Filters**: Filter items by rarity or drop rate
6. **Item Lookup**: Use RuneLite's ItemManager to get accurate item IDs
7. **Dry Streak Tracking**: Track longest dry streaks for rare items
