# NetherRoofBlocker Plugin
**NetherRoofBlocker** is a Minecraft Paper 1.21.1 plugin that prevents players from accessing the Nether roof by automatically teleporting them back down to a safe location.

## Features
- Automatically detects players on the Nether roof (Y >= 128).
- Teleports players to a safe location below the bedrock ceiling.
- Searches nearby coordinates if no safe spot is found directly below.
- Configurable unsafe blocks to avoid when finding safe locations.
- Toggle command to enable/disable blocking at runtime.

## Installation
1. Download the latest release [here](https://github.com/Jelly-Pudding/NetherRoofBlocker/releases/latest).
2. Place the `.jar` file in your Minecraft server's `plugins` folder.
3. Restart your server.

## Important Configuration
Ensure `nether-ceiling-void-damage-height` is set to `disabled` in your Paper config to prevent players taking damage before being teleported:

In `config/paper-world-defaults.yml`:
```yaml
environment:
  nether-ceiling-void-damage-height: disabled
```

## Configuration
In `config.yml`, you can configure:
```yaml
enabled: true

unsafe-blocks:
  - LAVA
  - FIRE
  - SOUL_FIRE
  - CAMPFIRE
  - SOUL_CAMPFIRE
  - MAGMA_BLOCK
  - CACTUS
  - SWEET_BERRY_BUSH
  - WITHER_ROSE
  - POWDER_SNOW
```

## Commands
- `/netherroofblocker reload`: Reloads the plugin configuration (requires `netherroofblocker.admin` permission)
- `/netherroofblocker toggle`: Toggles blocking on/off at runtime (requires `netherroofblocker.admin` permission)

## Permissions
- `netherroofblocker.admin`: Allows use of admin commands (default: op)

## Support Me
[![ko-fi](https://ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/K3K715TC1R)
