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
