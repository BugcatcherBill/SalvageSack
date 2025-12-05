# Development Notes

## Architecture

The Salvage Sack plugin consists of several key components:

### Data Models
- **ShipwreckType**: Enum defining different types of shipwrecks (Small, Fisherman's, Barracuda, Large, Pirate, Mercenary, Fremennik, Merchant)
- **SalvageItem**: Represents an individual item with drop tracking
- **SalvageData**: Aggregates all salvage data for a specific shipwreck type, includes a `lastUpdated` timestamp for UI ordering

### Core Components
- **SalvageSackPlugin**: Main plugin class that handles chat messages and coordinates data flow
- **SalvageDataManager**: Manages persistence of data to disk using JSON
- **DropRateManager**: Loads and provides expected drop rates from JSON configuration
- **ItemIconManager**: Fetches and caches item icons using RuneLite's built-in item manager
- **SalvageSackPanel**: UI panel that displays the tracked data

### Data Persistence
Data is saved automatically:
- After every loot drop
- When the plugin shuts down

Data is stored in: `<runelite-config-directory>/salvagesack/salvage-data.json`

### Chat Message Pattern
The plugin listens for chat messages matching the pattern:
```
You sort through the <salvage type> salvage and find: <quantity> x <item>.
```

### Expected Drop Rates
Drop rates are loaded from `drop_rates.json` in the resources folder. The rates are sourced from the OSRS Wiki and organized by shipwreck type.

### Item Icons
Item icons are fetched using RuneLite's built-in `ItemManager.getImage()` API, which retrieves icons from the game cache. Icons are cached in memory to avoid repeated lookups.

### UI Ordering
Shipwreck sections in the panel are automatically sorted by the `lastUpdated` timestamp on each `SalvageData` object. This ensures the most recently active shipwreck always appears at the top of the panel, regardless of whether the section is expanded or collapsed.

When `SalvageData.incrementTotalLoots()` is called, it updates the timestamp to `System.currentTimeMillis()`, triggering a re-sort on the next panel rebuild.
