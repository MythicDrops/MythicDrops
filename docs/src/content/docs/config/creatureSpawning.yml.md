---
title: creatureSpawning.yml
description: creatureSpawning.yml reference
---

MythicDrops has a lot of configuration options. Below is the contents of the creatureSpawning.yml with inline
explanations of what each configuration option does.

```yaml
version: 6.0.0
spawnPrevention:
  ## Should mobs spawned by a spawn egg be prevented from receiving items?
  spawnEgg: true
  ## Should mobs spawned by a mob spawner be prevented from receiving items?
  spawner: true
  ## Should mobs spawned by a custom reason be prevented from receiving items?
  ## This would typically be mobs spawned by plugins.
  custom: true
  ## Should mobs spawned by reinforcements (zombie mechanic) be prevented from
  ## receiving items?
  reinforcements: true
  ## Should mobs spawned by drowning be prevented from receiving items?
  drowned: true
  ## Allows you to set a Y level per world where mobs will not receive items
  ## if they spawn above it.
  ## In the default config, mobs spawned above 255 in a world named "world"
  ## will not receive items.
  above-y:
    world: 255
## Each mob that you want to drop items needs to be included in this section.
## If the mob is not included in this section, they are treated as having a
## multiplier of 0.0 and will not drop tiered items.
## Mob names come from this link:
## https://hub.spigotmc.org/javadocs/spigot/org/bukkit/entity/EntityType.html
creatures:
  CREEPER:
    ## Multiplier against the percentages (item-chance, tiered-item-chance, etc.)
    ## from config.yml.
    drop-multiplier: 1.0
    ## Tiers that this type of mob can drop.
    tier-drops:
      - common
      - uncommon
      - rare
    ## Number of passes against the drop strategy this mob gets. You can bump this number up to
    ## get loot explosions.
    number-of-loot-passes: 1
  ZOMBIE:
    ## Multiplier against the percentages (item-chance, tiered-item-chance, etc.)
    ## from config.yml.
    drop-multiplier: 1.0
    ## Tiers that this type of mob can drop.
    tier-drops:
      - common
      - uncommon
      - rare
    ## Number of passes against the drop strategy this mob gets. This is an alternative way to
    ## control the amount of loot that this creature can get.
    number-of-loot-passes:
      minimum: 1
      maximum: 3
```
