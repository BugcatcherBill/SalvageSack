# Salvage Sack

A RuneLite plugin for Old School RuneScape that tracks salvage loot from the Sailing skill, organized by shipwreck type.

![RuneLite](https://img.shields.io/badge/RuneLite-Plugin-orange)
![Java](https://img.shields.io/badge/Java-11-blue)
![License](https://img.shields.io/badge/License-BSD--2--Clause-green)

## Features

### 🏴‍☠️ Pirate Rank Microgame
- **100 Unique Ranks**: Progress from "Castaway" (Rank 1) to "Max Cash Stack" (Rank 100)
- **Smooth Progression**: Exponential curve ensures early ranks are quick, later ranks more challenging
- **Booty-Based Progression**: Earn ranks based on the high alchemy value of salvaged items
- **0 to Max Cash**: Journey from 0 GP to 2,147,483,647 GP (Integer.MAX_VALUE)
- **Circular Dial Display**: Visual rank indicator showing current rank and progress
- **Progress Bar**: Track your advancement toward the next rank
- **Rank-Up Effects**: Satisfying visual effects when you achieve a new rank
- **Persistent Progress**: Your rank and booty are saved between sessions
- **Configurable**: Enable or disable the microgame in plugin settings

### 📊 Comprehensive Tracking
- **Track by Shipwreck Type**: Separate tracking for all 8 shipwreck types:
  - Small shipwrecks (Small salvage)
  - Fisherman's shipwrecks (Fishy salvage)
  - Barracuda shipwrecks (Barracuda salvage)
  - Large shipwrecks (Large salvage)
  - Pirate shipwrecks (Plundered salvage)
  - Mercenary shipwrecks (Martial salvage)
  - Fremennik shipwrecks (Fremennik salvage)
  - Merchant shipwrecks (Opulent salvage)

### 📈 Drop Rate Analysis
- **Current Drop Rate**: Your actual drop rate based on your sorts
- **Expected Drop Rate**: Wiki-sourced expected rates for comparison
- **Luck Indicator**: Color-coded display showing if you're running lucky (green), neutral (yellow), or unlucky (red)

### 🎨 Visual Display
- **Item Icons**: Displays actual item icons from the game
- **Quantity Tracking**: Shows total quantity received for stackable items
- **Collapsible Sections**: Accordion-style panels for each shipwreck type
- **Compact Design**: Sections collapse when empty, expand when they have data
- **Auto-Sorted Sections**: Most recently updated shipwreck always appears at the top
- **Flexible Sorting**: Sort items by name, current rate, expected rate, or quantity with ascending/descending options

### 💾 Persistent Data
- All tracking data is automatically saved between sessions
- Sort preferences are saved and restored automatically
- Data stored locally in `.runelite/salvagesack/`

## Installation

### From Plugin Hub (Recommended)
1. Open RuneLite
2. Click the wrench icon to open Configuration
3. Click "Plugin Hub" at the bottom
4. Search for "Salvage Sack"
5. Click Install

### Manual Installation (Development)
1. Clone this repository
2. Build with Gradle: `./gradlew build`
3. Run with: `./gradlew runClient`

## How to Use

### Basic Usage
1. **Enable the Plugin**: After installation, the plugin is enabled by default
2. **Open the Panel**: Click the Salvage Sack icon in the RuneLite sidebar (ship icon)
3. **Go Sailing**: Sort through salvage from shipwrecks as you normally would
4. **View Statistics**: The panel automatically updates with your loot data

### Reading the Panel

#### Pirate Rank Display (if enabled)
The pirate rank panel appears at the top of the interface and shows:
- **Circular Dial**: Displays your current rank number with a progress arc showing advancement to next rank
- **Rank Title**: Your current pirate rank name (e.g., "Quartermaster", "Pirate Lord")
- **Description**: Flavor text describing your rank
- **Total Booty**: Accumulated high alchemy value from all salvaged items
- **Progress Bar**: Visual indicator of progress toward the next rank
- **Progress Label**: Exact amount of booty needed for the next rank

**Rank Progression**:
- Ranks are earned by accumulating "booty" (high alch value of salvaged items)
- 100 ranks total, from Castaway (Rank 1, 0 GP) to Max Cash Stack (Rank 100, 2.147B GP)
- Smooth exponential progression: early ranks are quick to achieve, later ranks require more dedication
- Rank-ups trigger a golden glow effect on the dial
- Your rank persists between sessions

#### Header
- Displays total number of sorts across all shipwreck types
- **Sort Controls**: Dropdown menu and direction toggle for customizing item display order

#### Sorting Options
Items within each shipwreck section can be sorted by:
- **Alphabetical**: Sort by item name (A-Z or Z-A)
- **Current Rate**: Sort by your actual drop rate
- **Expected Rate**: Sort by the wiki expected drop rate
- **Quantity**: Sort by total quantity received
- **Luck**: Sort by luck (how your drop rate compares to expected)
  - Descending: Shows luckiest items (green) first, then neutral (yellow), then unlucky (red)
  - Ascending: Shows unlucky items (red) first, then neutral (yellow), then lucky (green)

Click the arrow button (↑/↓) to toggle between ascending and descending order. Your sort preference is automatically saved.

#### Shipwreck Sections
Each shipwreck type has a collapsible section showing:
- **Section Header**: Shipwreck name and total sorts for that type
- **Arrow Icon**: Click to expand/collapse the section
- **Auto-Sorting**: The most recently updated shipwreck automatically moves to the top of the panel
- **Item Rows**: Each item you've received, showing:
  - Item icon (32x32)
  - Item name
  - Current drop rate (your actual rate)
  - Expected drop rate (from wiki data)
  - Total quantity received

#### Drop Rate Colors
- 🟢 **Green**: You're getting this item more often than expected (lucky!)
- 🟡 **Yellow**: Drop rate is close to expected
- 🔴 **Red**: You're getting this item less often than expected (unlucky)

### Resetting Data

**Right-click** on any shipwreck section header to access reset options:
- **Reset [Shipwreck] Data**: Clears data for just that shipwreck type
- **Reset All Data**: Clears all salvage tracking data

A confirmation dialog will appear before any data is deleted.

## Drop Rate Data

The plugin includes expected drop rates sourced from the OSRS Wiki for all items from each salvage type. This data is stored in `drop_rates.json` and can be customized if needed.

### Customizing Drop Rates
1. Navigate to `.runelite/salvagesack/`
2. Edit `drop_rates.json` to modify expected rates
3. Restart RuneLite to apply changes

## Technical Details

### Requirements
- RuneLite client
- Java 11 or higher
- OSRS membership (Sailing skill access)

### Data Storage
- **Location**: `~/.runelite/salvagesack/`
- **Files**:
  - `salvage-data.json` - Your tracking data
  - `drop_rates.json` - Expected drop rate configuration
  - `pirate_rank.json` - Your pirate rank progression data

### Building from Source

```bash
# Clone the repository
git clone https://github.com/BugcatcherBill/SalvageSack.git
cd SalvageSack

# Build the plugin
./gradlew build

# Run RuneLite with the plugin loaded
./gradlew runClient
```

## FAQ

**Q: Why isn't my loot being tracked?**
A: Make sure you're sorting salvage (not just looting). The plugin detects the chat message "You sort through the [type] salvage and find..."

**Q: Can I export my data?**
A: Yes! Your data is stored in JSON format at `~/.runelite/salvagesack/salvage-data.json`

**Q: The drop rates seem wrong. Can I update them?**
A: Yes, you can edit `drop_rates.json` in the salvagesack folder. The rates are sourced from the OSRS Wiki.

**Q: How do I reset just one shipwreck type?**
A: Right-click on the shipwreck section header in the panel and select "Reset [Shipwreck] Data"

**Q: How do I disable the pirate rank display?**
A: Open the plugin configuration (right-click the wrench icon > "Salvage Sack") and uncheck "Enable Pirate Ranks"

**Q: What is "booty" and how is it calculated?**
A: Booty is the high alchemy value of items you salvage. Higher value items contribute more to your rank progression.

## Contributing

Contributions are welcome! Please feel free to submit issues or pull requests.

## License

This project is licensed under the BSD 2-Clause License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- [RuneLite](https://runelite.net/) - The open source OSRS client
- [OSRS Wiki](https://oldschool.runescape.wiki/) - Drop rate data source
- The OSRS community for feedback and testing
  - Item name
  - Number of times dropped
  - Current drop rate vs Expected drop rate

All data persists between RuneLite sessions.
