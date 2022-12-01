# creatureSpawning.yml

MythicDrops has a lot of configuration options. Below is the contents of the creatureSpawning.yml with inline
explanations of what each configuration option does.

```yaml
version: 5.1.0
## Allows toggling whether or not certain types of mob spawns will actually
## be eligible to receive MythicDrops items. Only works if display-mob-equipment
## is set to true in config.yml.
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
## Multiplier against the percentages (item-chance, tiered-item-chance, etc.)
## from config.yml.
##
## Each mob that you want to drop items needs to be included in this section.
## If the mob is not included in this section, they are treated as having a
## multiplier of 0.0.
## Mob names come from this link:
## https://hub.spigotmc.org/javadocs/spigot/org/bukkit/entity/EntityType.html
dropMultipliers:
  CREEPER: 1.0
  SKELETON: 1.0
  SPIDER: 1.0
  GIANT: 1.0
  ZOMBIE: 1.0
  GHAST: 1.0
  PIG_ZOMBIE: 1.0
  ENDERMAN: 1.0
  CAVE_SPIDER: 1.0
  SILVERFISH: 1.0
  BLAZE: 1.0
  MAGMA_CUBE: 1.0
  ENDER_DRAGON: 1.0
  WITHER: 1.0
  WITCH: 1.0
  DROWNED: 1.0
  ELDER_GUARDIAN: 1.0
  HUSK: 1.0
  SHULKER: 1.0
  VEX: 1.0
  WITHER_SKELETON: 1.0
  PILLAGER: 1.0
## Controls what tiers of items that a mob can drop.
##
## Each mob that you want to drop tiers needs to be included in this section
## in the below format. Mob names come from this link:
## https://hub.spigotmc.org/javadocs/spigot/org/bukkit/entity/EntityType.html
##
## MOBNAME:
## - place tiers
## - to drop
## - in a list
tierDrops:
  CREEPER:
    - common
    - uncommon
    - rare
  SKELETON:
    - common
    - uncommon
  SPIDER:
    - common
    - uncommon
  GIANT:
    - epic
    - exotic
  ZOMBIE:
    - common
  GHAST:
    - rare
    - epic
  PIG_ZOMBIE:
    - common
    - uncommon
    - rare
  ENDERMAN:
    - uncommon
    - rare
  CAVE_SPIDER:
    - common
    - uncommon
  SILVERFISH:
    - common
  BLAZE:
    - uncommon
    - rare
    - epic
    - exotic
  MAGMA_CUBE:
    - common
    - uncommon
    - rare
    - epic
  ENDER_DRAGON:
    - legendary
  WITHER:
    - exotic
    - legendary
  WITCH:
    - artisan
```
