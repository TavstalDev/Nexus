# Nexus

![Release (latest by date)](https://img.shields.io/github/v/release/TavstalDev/Nexus?style=plastic-square)
![Workflow Status](https://img.shields.io/github/actions/workflow/status/TavstalDev/Nexus/ghrelease.yml?branch=stable&label=build&style=plastic-square)
![License](https://img.shields.io/github/license/TavstalDev/Nexus)
![Downloads](https://img.shields.io/github/downloads/TavstalDev/Nexus/total?style=plastic-square)
![Issues](https://img.shields.io/github/issues/TavstalDev/Nexus?style=plastic-square)

## Description
Nexus is the core velocity plugin of the MesterMC network. It provides essential features and functionalities required for the smooth operation of the network.

## Features
- Cross-server permission based chats
- Listing online staff members across all servers
- Find and send players to different servers
- Server-wide maintenance mode
- Global announcements to all players on the network
- Helpop & Report system for player support
- Custom MOTD and server list ping responses

## Commands
- `/nexus` - Main command for Nexus plugin
- `/hub`, `/lobby` - Send player to the hub server
- `/report <player> <reason>` - Report a player for misconduct
- `/helpop <message>` - Send a help request to online staff members
- `/stafflist` - List all online staff members across the network
- `/reports <page>` - View reported players
- `/maintenance <add|remove|list|on|off|kickall>` - Manage server maintenance mode
- `/alert <message>` - Send a global announcement to all players
- `/find <player>` - Find which server a player is on
- `/send <player> <server>` - Send a player to a different server on the network

## Permissions
Most permissions are configurable via the `config.yml` file. Below are some of the static permissions:
- `nexus.staff` - Marks a player as a staff member, does not grant any commands by itself.
- `nexus.command.findplayer` - Permission to use the `/find` command.
- `nexus.command.sendplayer` - Permission to use the `/send` command.
- `nexus.command.maintenance` - Permission to use all `/maintenance` subcommands.
- `nexus.command.reportlist` - Permission to use the `/reports` command.
- `nexus.command.stafflist` - Permission to use the `/stafflist` command.

## Contributing

I welcome contributions! If you have ideas for features, bug fixes, or improvements, please consider contributing to the project.

1.  **Fork** the repository on GitHub.
2.  **Create a new branch** for your feature or bug fix (e.g., `feature/add-category` or `fix/gui-bug`).
3.  **Commit your changes** with clear, concise, and descriptive commit messages.
4.  **Push your branch** to your forked repository.
5.  **Open a Pull Request** to the `main` branch of this repository, describing your changes.

## License

This project is licensed under the **GNU General Public License v3.0**. You can find the full license text in the `LICENSE` file within this repository.

## Contact

For any questions, bug reports, or feature requests, please use the [GitHub issue tracker](https://github.com/TavstalDev/Nexus/issues).