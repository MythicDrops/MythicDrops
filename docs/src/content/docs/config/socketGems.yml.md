---
title: socketGems.yml
description: socketGems.yml reference
---

MythicDrops has a lot of configuration options. Below is the shortened contents of the socketGems.yml with inline
explanations of what each configuration option does.

```yaml
version: 5.5.0
socket-gems:
  ## Name of the socket gem. It's displayed on items when the socket gem
  ## is added.
  Harden I:
    ## When does this get triggered? Available options:
    ## - ON_HIT
    ## - WHEN_HIT
    ## - ON_HIT_AND_WHEN_HIT
    ## - AURA
    ## - RIGHT_CLICK
    ##
    ## Defaults to ON_HIT_AND_WHEN_HIT.
    trigger-type: WHEN_HIT
    ## Item groups that this gem can be applied to. Defaults to empty,
    ## which means that it can be applied to any item.
    ## In 6.1.0, this maps to all-of-item-groups.
    item-groups:
      - armor
    ## This gem can only be applied to items that match all of
    ## the below item groups. Defaults to empty,
    ## which means that it can be applied to any item.
    ## Only works in 6.1.x+.
    all-of-item-groups:
      - armor
    ## This gem can only be applied to items that match any of
    ## the below item groups. Defaults to empty,
    ## which means that it can be applied to any item.
    ## Only works in 6.1.x+.
    any-of-item-groups:
      - armor
    ## This gem can only be applied to items that match none of
    ## the below item groups. Defaults to empty,
    ## which means that it can be applied to any item.
    ## Only works in 6.1.x+.
    none-of-item-groups:
      - armor
    ## Potion effects that can be applied.
    potion-effects:
      ## Name of the potion effect type.
      ## https://hub.spigotmc.org/javadocs/spigot/org/bukkit/potion/PotionEffectType.html
      DAMAGE_RESISTANCE:
        ## Intensity of the effect. 0 is level 1 due to Minecraft. Defaults to 0.
        intensity: 0
        ## Duration of the effect in milliseconds. Defaults to 0.
        duration: 600
        ## Target of the potion effect.
        ## Available options:
        ## - SELF
        ## - OTHER
        ## - NONE
        ## - AREA
        ##
        ## Defaults to NONE.
        target: SELF
        ## Radius of the effect. Only used for AREA targets and AURA triggers. Defaults to 0.
        radius: 0
        ## Chance for the effect to trigger. Defaults to 1.0.
        chance-to-trigger: 1.0
        ## Does the potion effect affect the wielder of the socket gem? Only used for
        ## AURA trigger types. Defaults to false.
        affects-wielder: true
        ## Does the potion effect affect not the wielder of the socket gem? Only used for
        ## AURA trigger types. Defaults to false.
        ##
        ## "not the wielder" meaning any other entities within the given radius.
        affects-target: true
    ## Particle effects that can be applied.
    particle-effects:
      ## Name of the particle type.
      ## https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Particle.html
      FIREWORKS_SPARK:
        ## Number of particles to spawn. Defaults to 0.
        intensity: 1
        ## Duration of the effect in milliseconds. Defaults to 0.
        duration: 2000
        ## Target of the potion effect.
        ## Available options:
        ## - SELF
        ## - OTHER
        ## - NONE
        ## - AREA
        ##
        ## Defaults to NONE.
        target: SELF
        ## Radius of the effect. Only used for AREA targets and AURA triggers. Defaults to 0.
        radius: 0
        ## Chance for the effect to trigger. Defaults to 1.0.
        chance-to-trigger: 1.0
        ## Does the particle effect affect the wielder of the socket gem? Only used for
        ## AURA trigger types. Defaults to false.
        affects-wielder: true
        ## Does the particle effect affect not the wielder of the socket gem? Only used for
        ## AURA trigger types. Defaults to false.
        ##
        ## "not the wielder" meaning any other entities within the given radius.
        affects-target: true
    ## Commands to be run by the socket gem when triggered. Not able to be triggered by AURA.
    ## "CONSOLE:" will cause the command to be run by the console.
    ## "PLAYER:" will cause the command to be run by the player if possible.
    commands:
      - "CONSOLE:version MythicDrops"
      - "PLAYER:spawn"
    ## Enchantments to be added to the item when the gem is applied. Supports both single level
    ## and level ranges.
    enchantments:
      DAMAGE_ALL: 2
      DAMAGE_UNDEAD:
        minimum-level: 1
        maximum-level: 2
    ## Attributes to be added to the item when the gem is applied.
    attributes:
      uniquehardenIkey1:
        ## Attribute for this particular modifier. Attribute names here:
        ## https://hub.spigotmc.org/javadocs/spigot/org/bukkit/attribute/Attribute.html
        attribute: GENERIC_ARMOR
        ## Decimal value for the amount this attribute adds.
        amount: 2.0
        ## Operation for this particular modifier. Operation names and descriptions here:
        ## https://hub.spigotmc.org/javadocs/spigot/org/bukkit/attribute/AttributeModifier.Operation.html
        operation: ADD_NUMBER
      uniquehardenIkey2:
        ## Attribute for this particular modifier. Attribute names here:
        ## https://hub.spigotmc.org/javadocs/spigot/org/bukkit/attribute/Attribute.html
        attribute: GENERIC_ARMOR
        ## Decimal value for the minimum amount this attribute adds.
        minimum-amount: 4.0
        ## Decimal value for the maximum amount this attribute adds.
        maximum-amount: 6.0
        ## Operation for this particular modifier. Operation names and descriptions here:
        ## https://hub.spigotmc.org/javadocs/spigot/org/bukkit/attribute/AttributeModifier.Operation.html
        operation: ADD_NUMBER
    ## Weight of the socket gem. See the weights section of the documentation
    ## for more information. Defaults to 0.
    weight: 400
    ## Prefix to add to an item's name when this gem is applied. Defaults to no prefix.
    prefix: Hard
    ## Suffix to add to an item's name when this gem is applied. Defaults to no prefix.
    suffix: Hard
    ## Lore to add to an item's description when this gem is applied. Defaults to no lore.
    lore:
      - "&4Boosts defense for 0.5s when hit"
    ## Family of the socket gem. This is primarily used for Socket Gem Combining.
    family: Harden
    ## Level of the socket gem. This is primarily used for Socket Gem Combining.
    level: 1
    ## Should the gem be broadcast to the server when found?
    broadcast-on-find: false
    ## Custom model data to be set on the Socket Gem item.
    custom-model-data: 0
    ## Type of Socket Gem. See socketing.yml for details.
    socket-type: any
    ## Item Flags to add to the item. Flags here:
    ## https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/ItemFlag.html
    item-flags:
      - HIDE_ATTRIBUTES
  # other socket gems down here...
```

## Commands

Recommended command format:

```yaml
commands:
  thisisauniqueidentifierkey:
    runner: SUDO # CONSOLE and PLAYER work here too
    command: mythicdrops spawn tier # Put the command you want the player to run here
    # Put any permissions you want granted temporarily (just as long as it takes to run the command) here
    # only used when runner is SUDO
    permissions:
      - mythicdrops.command.spawn.tier
```

## Item Groups

You can now combine the fields `all-of-item-groups`, `any-of-item-groups`, and `none-of-item-groups` like below.

This example will only allow the item to be applied to an item with a material that is:

- in the armor item group
- in the chest or head or leggings item groups
- not in the leather item group

```yaml
Harden I:
  trigger-type: WHEN_HIT
  item-groups:
    all-of-item-groups:
      - armor
    any-of-item-groups:
      - chest
      - head
      - leggings
    none-of-item-groups:
      - leather
  # other configuration...
```

![Example of Gem Group Combinations](../../../assets/allofanyofnoneof.png)
