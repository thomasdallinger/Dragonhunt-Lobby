# Dragonhunt-Lobby

The lobby server plugin for the [Dragonhunt](https://github.com/thomasdallinger/Dragonhunt) Minecraft network, built for Spigot.

Players wait in the lobby and play a SkyPvP warm-up mode while they queue for the next Dragonhunt match. Once the queue fills or the countdown runs out, the lobby boots a game node and moves the players there through BungeeCord.

Dragonhunt was a personal project and ran publicly on `playdragonhunt.net`. The server is no longer online. The code is published here for reference.

More about the project: [thomasdallinger.com/dragonhunt.html](https://thomasdallinger.com/dragonhunt.html)

## Features

- **Match queue:**
  - Right-click to join or leave the queue, for up to 8 players per match.
  - A boss bar shows a 2-minute countdown and the player count.
  - Shortly before the countdown ends, the lobby asks the node control service to start the game node.
  - Players are then moved to `MhNode<id>` one at a time.
- **SkyPvP lobby:**
  - Kits (Warrior, Sniper, Healer, Grappler, Wizard, ...).
  - Loot blocks in five tiers (uncommon → mythic), viewable with `/tier`.
  - Supply drops (respawn anchors and smithing tables) marked by a beacon.
  - Hidden beacons to collect across the maps. The count is shown on the scoreboard.
  - Elytra swapping, enchanting tables, anvils and brewing stands.
  - Kill and death stats on the scoreboard.
- **Map rotation** across the lobby's Overworld, Nether and End maps.
- **Profiles:** per-player stats from the shared Dragonhunt database.
- **Social links:** `/website`, `/discord`, `/youtube`, `/twitter`.

## Commands

| Command | Description |
| --- | --- |
| `/spawn` | Teleport back to spawn |
| `/tier <tier>` | Show the loot table of a tier |
| `/beacon <number> <key>` | Claim a hidden beacon. Run by command blocks with `beacon-key` from `config.yml`, not by players. |
| `/forcestart` | Start the queued match immediately (operators only) |
| `/date` | Show the current date |
| `/website`, `/discord`, `/youtube`, `/twitter` | Social links |

## Architecture

```
            BungeeCord proxy
           /                \
   Lobby server         Game nodes "MhNode<id>"
   (this plugin)         (Dragonhunt plugin)
           \                /
             MySQL database
```

- **Lobby → node:** when a match starts, the lobby:
  - inserts a row into `games` and one row per player into `plays`,
  - marks the node as running in `servers`,
  - sends each player to the node with the BungeeCord `Connect` message.
- **Node control service:** a small HTTP service (not part of this repository) that boots and shuts down game nodes. The lobby calls `<url><start|stop>/<serverID>?accessToken=<token>`.
- **Node → lobby:** after the match, the [Dragonhunt](https://github.com/thomasdallinger/Dragonhunt) plugin sends players back to the `lobby` server.

## Building

Requirements: Java 8+ and Maven. The plugin targets the Spigot API **1.18**.

```bash
mvn package
```

`pom.xml` writes the jar to a hard-coded local plugin folder (`maven-jar-plugin` → `outputDirectory`). Change or remove that line before building on your machine.

## Configuration

### `config.yml`
```yaml
server-control:
  url: "http://localhost:5000/"   # base URL of the node control service
  access-token: ""                # token expected by that service

beacon-key: ""                    # secret used by the hidden-beacon command blocks; /beacon is off while empty
```
The rest of `config.yml` defines the loot tables (`uncommon`, `rare`, `epic`, `exotic`, `mythic`).

### `src/main/resources/db.txt`
Database credentials. The file is bundled into the jar and ignored by git. It contains three lines:
```
jdbc:mysql://<host>:3306/<database>
<user>
<password>
```

### Game settings
The mode, version label, Elo toggle and free Kit for new matches are constants in `game/GameState.java`.

## Operations notes

Starting a new SkyPvP Season:
- Reset the SkyPvP stats and the SkyPvP equipment in the database.
- Update the `season` string in `Scoreboard.update()` to the new season number.

Every map spawn has:
- Social media and website holograms
- Loot holograms
- Ender chests
- 3 enchanting tables
- Anvils
- Fletching tables
- Magma blocks
- An End portal leading to a jump 'n' run
- A Nether portal leading to the leaderboards

## License

[MIT](LICENSE) © Thomas Dallinger
