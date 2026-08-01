# Goetied

[![Minecraft](https://img.shields.io/badge/Minecraft-1.21.1-green.svg)](https://www.minecraft.net/)
[![NeoForge](https://img.shields.io/badge/NeoForge-21.1.34-orange.svg)](https://neoforged.net/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE.txt)

Goetied is a comprehensive magic mod for Minecraft that adds a unique spellcasting system, powerful mobs, and mystical structures to enhance your gameplay experience.

## Features

### Magic System
- **Spellcasting**: Cast a variety of spells across multiple schools of magic including:
  - Necromancy - Raise the dead and command undead servants
  - Geomancy - Manipulate earth and stone
  - Storm Magic - Harness lightning and thunder
  - Nether Magic - Wield fire and destruction
  - Wind Magic - Control air and movement
  - Wild Magic - Command nature and beasts
  - Void Magic - Manipulate the void and ender creatures
  - Utility Spells - Light, crafting, and more

- **Research System**: Learn new spells and abilities through research scrolls
- **Soul Energy**: Collect and manage soul energy to power your magic
- **Rituals**: Perform powerful rituals to transform yourself or craft items
- **Brewing System**: Create custom potions and brews with unique effects

### Mobs & Entities
- **Servants**: Summon and command various allies including:
  - Undead servants (skeletons, zombies, phantoms)
  - Illager servants (raiders, vindicators, evokers)
  - Golems (ice golems, grave golems, redstone golems)
  - Ender creatures (watchlings, snarelings, blastlings)
  - Spiders and other creatures

- **Bosses**: Face challenging boss encounters:
  - Apostle
  - Ender Keeper
  - Vizier
  - And more

- **Hostile Mobs**: Encounter new threats including cultists, hostile servants, and more

### Structures & World Generation
- **Structures**: Discover mystical structures throughout the world:
  - Graveyards
  - Crypts
  - Dark Manors
  - Sorcerous Keeps
  - Spider Dens
  - And more

### Equipment & Items
- **Magic Staves**: Wield powerful staves for spellcasting
- **Armor Sets**: Equip specialized armor sets with unique abilities
- **Curios**: Wear magical accessories for additional powers
- **Totems**: Use totems of souls to store and manage soul energy

### Lich Mode
- Transform into a powerful Lich with unique abilities
- Gain new powers and limitations
- Experience a different playstyle focused on undeath

## Requirements

- **Minecraft**: 1.21.1
- **NeoForge**: 21.1.34 or later
- **Curios API**: 9.5.1 or later (required)
- **Java**: 21 or later

### Optional Dependencies
- **JEI (Just Enough Items)**: For recipe viewing
- **Patchouli**: For in-game documentation

## Installation

1. Install [NeoForge](https://neoforged.net/) for Minecraft 1.21.1
2. Download and install [Curios API](https://www.curseforge.com/minecraft/mc-mods/curios)
3. Download the latest Goetied mod JAR file
4. Place the JAR file in your `mods` folder
5. Launch Minecraft and enjoy!

## Building from Source

### Prerequisites
- Java 21 or later
- Gradle 8.13 or later (included via wrapper)

### Build Instructions

1. Clone the repository:
   ```bash
   git clone https://github.com/imbavirus/Goetied-3.git
   cd Goetied-3
   ```

2. Build the mod:
   ```bash
   ./gradlew build
   ```

3. The built JAR will be in `build/libs/`

### Development Setup

1. Import the project into your IDE (IntelliJ IDEA or Eclipse)
2. Run `./gradlew genIntellijRuns` or `./gradlew genEclipseRuns`
3. Use the generated run configurations to test the mod

## Configuration

Goetied includes extensive configuration options accessible through the config files:
- Spell damage and cooldown settings
- Mob spawn rates and behavior
- Structure generation settings
- Lich mode settings
- And much more

Configuration files are located in your Minecraft instance's `config/goetied/` directory.

## Credits

### Development
- **Author**: Infernos
- **Original Author**: Polarice3

### Special Thanks
- **Tutorial/Help**: GentlemanRevvnar, Technovision, Cy4's Modding, TelepathicGrunt, diesieben07
- **Major Bug Testing**: Mich705, pantman687, glacial_flower, Mephodio

### Art & Assets
- Several textures by zozozrob
- Item retextures by mongoose_artist and amyspiralvt
- Necromancer Servant texture by frosty_gg
- Wither Necromancer Servant texture by xby123896_88452
- Brood Mother Textures by xiaowu
- Void Flame Textures by emptyCoso
- Piker, Conquillager, Inquillager, Maverick, Heretic remodel and base textures by luomu05
- Ender Keeper's design by vulkan9346
- Most animations by joturtleguy21

### Builds
- Blighted Shack, Graveyard, Ominous Blacksmith and Buried Ritual build by boxman
- Dark Manor and Sorcerous Keep build by trufflehistorian

### Translations
- **Chinese**: NUTTAR, Player_7
- **Japanese**: SAGA, 坂上駒子
- **Russian**: .cool_nickname
- **Ukrainian**: catvasilthefirst
- **Polish**: darknetherlord
- **Portuguese**: crougamer.exe

## License

This project is licensed under the MIT License - see the [LICENSE.txt](LICENSE.txt) file for details.

## Links

- **GitHub**: [https://github.com/imbavirus/Goetied-3](https://github.com/imbavirus/Goetied-3)
- **Issues**: [Report bugs or request features](https://github.com/imbavirus/Goetied-3/issues)

## Version History

See [changelogs/README.md](changelogs/README.md) for detailed version history.

### Current Version: 3.0.0
- Initial NeoForge port for Minecraft 1.21.1
- All systems ported and tested to working state
- See [changelogs/3.0.0.md](changelogs/3.0.0.md) for details

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request. For major changes, please open an issue first to discuss what you would like to change.

## Support

If you encounter any issues or have questions, please open an issue on the GitHub repository.

---

**Note**: This mod requires Curios API to function properly. Make sure to install it alongside Goetied.
