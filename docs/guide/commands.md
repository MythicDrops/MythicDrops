# Commands

MythicDrops has multiple commands available in order to make administering your setup easy. Below is a comprehensive
list of commands that are available. You can also print out this list of commands in game by running `/md help`.

## Command List

This command list is searchable in game by running variants of almost any of the commands. For instance, you can find
any commands that involve socket gems by running `/md socket`.

Arguments surrounded by `< >` are mandatory, while arguments surrounded by `[ ]`
are optional.

| Command                                                                  | Command Description                                                                           |
| ------------------------------------------------------------------------ | --------------------------------------------------------------------------------------------- |
| /mythicdrops combiners add                                               | Adds a socket gem combiner at the block the sender is looking at.                             |
| /mythicdrops combiners list                                              | Lists the socket gem combiners and their locations.                                           |
| /mythicdrops combiners remove                                            | Removes a socket gem combiner from the block the sender is looking at.                        |
| /mythicdrops customcreate <weight\>                                      | Creates a new custom item based on the item in your main hand.                                |
| /mythicdrops customitems                                                 | Prints the custom items that the plugin is aware of.                                          |
| /mythicdrops debug                                                       | Prints information to log. Useful for getting help in the Discord.                            |
| /mythicdrops drop custom <customItem\> \[world] \[x] \[y] \[z] \[amount] | Spawns a tiered item in the player's inventory. Use "\*" to drop any custom item.             |
| /mythicdrops drop gem <socketGem\> \[world] \[x] \[y] \[z] \[amount]     | Spawns a Socket Gem in the player's inventory. Use "\*" to drop any Socket Gem.               |
| /mythicdrops drop tier <tier\> \[world] \[x] \[y] \[z] \[amount]         | Spawns a tiered item in the player's inventory. Use "\*" to drop any tier.                    |
| /mythicdrops drop tome \[world] \[x] \[y] \[z] \[amount]                 | Spawns an Identity Tome in the player's inventory.                                            |
| /mythicdrops drop unidentified \[world] \[x] \[y] \[z] \[amount]         | Spawns an Unidentified Item in the player's inventory.                                        |
| /mythicdrops errors                                                      | Prints any loading errors. Useful for getting help in the Discord.                            |
| /mythicdrops give custom \[player] \[customItem] \[amount]               | Spawns a tiered item in the player's inventory. Use "\*" to give any custom item.             |
| /mythicdrops give gem \[player] \[socketGem] \[amount]                   | Spawns a Socket Gem in the player's inventory. Use "\*" to give any Socket Gem.               |
| /mythicdrops give tier \[player] \[tier] \[amount]                       | Spawns a tiered item in the player's inventory. Use "\*" to give any tier.                    |
| /mythicdrops give tome \[player] \[amount]                               | Spawns an Identity Tome in the player's inventory.                                            |
| /mythicdrops give unidentified \[player] \[amount]                       | Spawns an Unidentified Item in the player's inventory.                                        |
| /mythicdrops help \[help]                                                | Prints this help text.                                                                        |
| /mythicdrops modify enchantment add <enchantment\> \[level]              | Adds an enchantment to the item in the main hand of the player.                               |
| /mythicdrops modify enchantment remove <enchantment\>                    | Removes an enchantment from the item in the main hand of the player.                          |
| /mythicdrops modify lore add \[args]                                     | Adds a line of lore to the item in the main hand of the player.                               |
| /mythicdrops modify lore insert <index\> \[args]                         | Inserts a line of lore at index (starting at 1) to the item in the main hand of the player.   |
| /mythicdrops modify lore remove \[index]                                 | Removes a line of lore at index (starting at 1) from the item in the main hand of the player. |
| /mythicdrops modify lore set <index\> \[args]                            | Sets a line of lore at index (starting at 1) to the item in the main hand of the player.      |
| /mythicdrops modify name \[args]                                         | Sets the name of the item in the main hand of the player.                                     |
| /mythicdrops reload                                                      | Reloads the configuration and data of the plugin                                              |
| /mythicdrops socketgems                                                  | Prints the socket gems that the plugin is aware of.                                           |
| /mythicdrops spawn custom \[customItem] \[amount]                        | Spawns a tiered item in the player's inventory. Use "\*" to spawn any custom item.            |
| /mythicdrops spawn gem \[socketGem] \[amount]                            | Spawns a Socket Gem in the player's inventory. Use "\*" to spawn any Socket Gem.              |
| /mythicdrops spawn tier \[tier] \[amount]                                | Spawns a tiered item in the player's inventory. Use "\*" to spawn any tier.                   |
| /mythicdrops spawn tome \[amount]                                        | Spawns an Identity Tome in the player's inventory.                                            |
| /mythicdrops spawn unidentified \[amount]                                | Spawns an Unidentified Item in the player's inventory.                                        |
| /mythicdrops tiers                                                       | Prints the tiers that the plugin is aware of.                                                 |
