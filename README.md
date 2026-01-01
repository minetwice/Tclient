
# TCosmatic - Advanced Minecraft Cape Mod

A client-side Fabric mod for Minecraft 1.21.1 that allows players to use custom capes with advanced editing features.

## Features

### 🎨 Core Features
1. **Custom Cape Support** - Load PNG capes (64x32 or 22x17) from the game directory
2. **Live Preview** - See your cape on a 3D character model before applying
3. **Advanced Editor** - Full-featured cape editor with multiple tools
4. **Multiplayer Sync** - Share your cape with friends who have the mod
5. **Transform Controls** - Adjust scale, rotation, and position
6. **Texture Editing** - Direct texture manipulation with brush, bucket, and more

### 🛠️ Editor Tools
- **Brush Tool** - Paint directly on cape texture
- **Fill Bucket** - Fill areas with color
- **Eraser** - Remove parts of the texture
- **Eyedropper** - Pick colors from the texture
- **Line Tool** - Draw straight lines
- **Rectangle Tool** - Draw rectangles

### ⚙️ Additional Features
1. **Auto-Detection** - Automatically detects new capes added to folder
2. **Physics Simulation** - Realistic cape movement and animation
3. **Opacity Control** - Adjust cape transparency
4. **First Person View** - Toggle cape visibility in first person
5. **Config System** - Persistent settings saved between sessions
6. **Main Menu Button** - Easy access from title screen

## Installation

### Prerequisites
- Minecraft 1.21.1
- Fabric Loader 0.16.5+
- Fabric API 0.105.0+
- Java 21+

### Steps
1. Download the latest release JAR from [Releases](https://github.com/yourusername/tcosmatic/releases)
2. Place the JAR file in your `.minecraft/mods` folder
3. Launch Minecraft with Fabric profile
4. A `TCosmatic` folder will be created in your game directory

## Usage

### Adding Capes
1. Navigate to `.minecraft/TCosmatic/capes/`
2. Place your PNG cape files (64x32 or 22x17 pixels)
3. Capes will be automatically detected

### Applying Capes
1. Press **Right Shift** or click **TCosmatic** on the main menu
2. Select a cape from the list
3. Click **Apply Cape** to equip it
4. Click **Cape Editor** for advanced customization

### Editor Controls
- **Left Panel** - Tool selection and color picker
- **Center** - 3D preview with your character
- **Right Panel** - Transform controls (scale, rotation, position)
- Use sliders to adjust values in real-time

### Keybinds
- **Right Shift** - Open TCosmatic menu (configurable)

## Building from Source

### Prerequisites
- JDK 21
- Git

### Clone and Build
```bash
git clone https://github.com/yourusername/tcosmatic.git
cd tcosmatic
./gradlew build
```

The compiled JAR will be in `build/libs/`

### Development Setup
```bash
# Generate IDE project files
./gradlew idea     # For IntelliJ IDEA
./gradlew eclipse  # For Eclipse

# Run client
./gradlew runClient
```

## Configuration

Config file location: `.minecraft/TCosmatic/config/config.json`

```json
{
  "selectedCape": "my_cape",
  "showCapeInFirstPerson": false,
  "showOtherPlayersCapes": true,
  "capeOpacity": 1.0,
  "animateCape": true,
  "capePhysics": true
}
```

## Multiplayer

### How It Works
- TCosmatic is **client-side only**
- Your cape is visible to **you** always
- Your cape is visible to **others** only if they also have TCosmatic installed
- No server-side plugin required

### Sharing Capes
To see each other's capes:
1. Both players must have TCosmatic installed
2. Capes sync automatically when joining servers
3. Players without the mod won't see custom capes

## Cape Specifications

### Supported Formats
- **Standard Cape**: 64x32 pixels
- **Elytra-Style**: 22x17 pixels
- Format: PNG with transparency support

### Creating Capes
1. Use any image editor (Photoshop, GIMP, Paint.NET)
2. Create a 64x32 or 22x17 canvas
3. Design your cape
4. Export as PNG
5. Place in `.minecraft/TCosmatic/capes/`

### Texture Mapping
```
Standard Cape (64x32):
[0-10, 1-17]   - Cape front
[11-21, 1-17]  - Cape back
```

## API for Developers

### Accessing Cape Manager
```java
CapeManager manager = TCosmaticClient.getCapeManager();
CapeData selectedCape = manager.getSelectedCape();
```

### Adding Custom Features
```java
// Listen for cape changes
TCosmaticClient.getCapeManager().getConfig().setSelectedCape("cape_name");
```

## Troubleshooting

### Cape Not Loading
- Check file format (must be PNG)
- Verify dimensions (64x32 or 22x17)
- Check console for error messages

### Cape Not Visible
- Ensure config has correct cape selected
- Check opacity settings
- Verify Fabric API is installed

### Performance Issues
- Disable cape physics in config
- Reduce cape opacity
- Check for mod conflicts

## Contributing

Contributions are welcome! Please:
1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request

## Credits

- **Mod Author**: Your Name
- **Fabric API**: FabricMC Team
- **Minecraft**: Mojang Studios

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Support

- **Issues**: [GitHub Issues](https://github.com/yourusername/tcosmatic/issues)
- **Discord**: [Join our Discord](#)
- **Wiki**: [Documentation](#)

## Changelog

### v1.0.0 (Initial Release)
- Custom cape support
- Advanced editor with 6 tools
- Transform controls
- Multiplayer sync
- Auto-detection system
- Physics simulation

---

**Made with ❤️ for the Minecraft community**
