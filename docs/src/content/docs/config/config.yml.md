---
title: config.yml
description: config.yml reference
---

MythicDrops has a lot of configuration options. Below is the contents of the config.yml with inline explanations of what
each configuration option does.

```yaml
version: 7.10.0
options:
  ## Should monsters who receive drops be given
  ## random names? Pulls from the resources/mobnames
  ## folder. general.txt applies to all
  ## monsters, while a creeper.txt will only apply to
  ## creepers.
  give-mobs-names: false
  ## Should mobs that are given names get names that are
  ## colored to match the color of the tiered item that they
  ## received?
  give-mobs-colored-names: false
  ## Should all monsters be given random names? This includes
  ## animals like cows and sheep. Pulls from the
  ## resources/mobnames folder. general.txt applies to all
  ## monsters, while a creeper.txt will only apply to
  ## creepers.
  give-all-mobs-names: false
  ## Should mobs be given the equipment when they spawn or
  ## should the items only be dropped on death? This defaults
  ## to true so that players can identify mobs that will drop
  ## sweet loot.
  "display-mob-equipment": true
  ## Should mobs be able to pick up equipment that gets dropped
  ## by a player? This defaults to false as if a player drops
  ## a piece of loot with a low drop rate and a mob picks it
  ## up, the mob might not drop the item when it dies. The laws
  ## of item dropping chance still apply.
  can-mobs-pick-up-equipment: false
  blank-mob-spawn:
    ## Should mobs have any pre-existing equipment removed when
    ## they spawn? This defaults to false as Minecraft now
    ## occasionally will spawn monsters with helmets, chestplates,
    ## leggings, boots, or weapons.
    enabled: false
    ## When performing blank mob spawn, which mobs should spawn with
    ## their default equipment?
    spawn-with-default-equipment:
      - SKELETON
      - WITHER_SKELETON
      - PIGLIN
      - PIGLIN_BRUTE
      - DROWNED
      - ZOMBIFIED_PIGLIN
  ## Allow items to be repaired using an Anvil?
  ## This defaults to false as items can be renamed using the Anvil.
  allow-items-to-be-repaired-by-anvil: false
  ## Should leather colors be randomized?
  randomize-leather-colors: true
  ## Default socket gem name color on items. For power users, mostly.
  default-socket-name-color-on-items: GOLD
  ## If a socket gem gets added to an item AND the item has a tier AND
  ## this is set to true, then use the tier display color as the color
  ## of the socket gem name on the item. Otherwise, use the color from
  ## default-socket-name-color-on-items. For power users, mostly.
  use-tier-color-for-socket-name: true
  ## If mobs need to be killed by a player in order for their items to
  ## drop. This is true by default as it helps nullify mob traps. If
  ## you want mob traps to work, set this to false. This only works with
  ## display-mob-equipment set to false.
  require-player-kill-for-drops: true
  ## Set this to true if you do not want to be able to roll multiple
  ## bonus enchantments with the same enchantment. This will lead to
  ## scenarios where you may have low numbers of enchantments on items.
  only-roll-bonus-enchantments-once: false
  ## Set this to true if you do not want to be able to roll multiple
  ## bonus attributes with the same attribute. This will lead to
  ## scenarios where you may have low numbers of attributes on items.
  only-roll-bonus-attributes-once: false
  ## Allow items to have their repair costs removed by a grindstone?
  ## This only works for Minecraft 1.14+. Set this to false if you
  ## don't want repair costs removed, as that will allow players to
  ## modify their MD items.
  allow-items-to-have-repair-cost-removed-by-grindstone: false
  ## Allow diamond MD items to be upgrade to Netherite?
  ## This only works for Minecraft 1.16+.
  allow-netherite-upgrade: true
  ## Disable checks for items using the legacy chat color and item similarity checks?
  ## This primarily has impacts on dropping items, configuration, socketing, and anvils.
  ## It is much more performant.
  ## This only works for Minecraft 1.16+.
  disable-legacy-item-checks: true
  ## Disable generating default item attributes on tiered items.
  ## Incredibly not recommended as it makes working with attributes more frustrating.
  ## Again, do not change unless you know what you're doing.
  disable-default-tiered-item-attributes: false
multiworld:
  ## Include any worlds where you want to have MythicDrops
  ## create drops here.
  ## Example:
  ##
  ## enabled-worlds:
  ## - world
  ## - world2
  ## - world3
  enabled-worlds:
    - world
## All entries in this section have a maximum value of
## 1.0 (100%) and a minimum value of 0.0 (0%).
drops:
  ## This represents the drop strategy used to determine drops from the plugin.
  ## Can include strategies from other plugins as well.
  ##
  ## Currently provided strategies by MythicDrops:
  ## - single (one drop per mob at most)
  ## - multiple (one drop per mob per type of drop at most)
  strategy: single
  ## This is who will receive a broadcast when an item is found.
  ##
  ## WORLD - Players in the same world.
  ## SERVER - All players on the server.
  ## PLAYER - Only the player finding the item.
  broadcast-target: WORLD
  ## This is the chance for a mob to get items at all.
  item-chance: 1.0
  ## This is the chance for a mob to spawn with a tiered item.
  tiered-item-chance: 0.25
  ## If a mob does not receive an item with a tier,
  ## this is the chance for a mob to spawn with a
  ## custom item.
  custom-item-chance: 0.1
  ## If a mob does not receive an item with a tier
  ## or a custom item, this is the chance for a mob
  ## to spawn with a socket gem.
  socket-gem-chance: 0.2
  ## If a mob does not receive an item with a tier
  ## a custom item, or a socket gem, this is the chance for a mob
  ## to spawn with an unidentified item.
  unidentified-item-chance: 0.1
  ## If a mob does not receive an item with a tier
  ## a custom item, a socket gem, or an unidentified item,
  ## this is the chance for a mob to spawn with an identity tome.
  identity-tome-chance: 0.1
  ## If a mob does not receive an item with a tier
  ## a custom item, a socket gem, an unidentified item,
  ## or an identity tome, this is the chance for a mob to spawn
  ## with a socket extender.
  socket-extender-chance: 0.1
components:
  ## Should creatures be able to spawn with items?
  creature-spawning-enabled: true
  ## Should MythicDrops enable its repairing feature
  ## and disable the vanilla Minecraft repairing
  ## feature (recommended)?
  repairing-enabled: true
  ## Should MythicDrops spawn items with empty sockets
  ## and socket gems?
  socketing-enabled: true
  ## Should MythicDrops enable its identifying feature
  ## and spawn unidentified items and identity tomes?
  identifying-enabled: true
  ## Should MythicDrops enable distance-from-spawn
  ## based drops, i.e., drops that can only spawn within
  ## certain distances from the spawn of a world?
  distance-zones-enabled: false
display:
  ## How should the name of generated items be formatted?
  ## Uses variables contained in variables.txt.
  item-display-name-format: "%generalprefix% %generalsuffix%"
  ## How should the lore of generated items be formatted?
  ## Uses variables contained in variables.txt.
  tooltip-format:
    - "&7Type: %mythicmaterial%"
    - "&7Tier:%tiercolor% %tiername%"
    - "%baselore%"
    - "%bonuslore%"
    - "%socketlore%"
    - "%relationlore%"
```
