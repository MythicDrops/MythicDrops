---
id: tier_yml
title: tier.yml
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

## Configuration

MythicDrops has a lot of configuration options. Below is the contents of a
tier YAML with inline explanations of what each configuration option does.

Display color and identifier color combinations must be unique.

<Tabs
defaultValue="1.1.0"
values={[
{ label: '1.1.0 (MD 6.1.x)', value: '1.1.0', },
{ label: '1.0.1 (MD 6.0.x)', value: '1.0.1', }
]
}>
<TabItem value="1.1.0">

```yaml
version: 1.1.0
## Display name for the tier. Used for display on items in lore.
display-name: Legendary
## Color of the tier when used in names. Prefixed to the name of the randomized item as well.
display-color: GOLD
## Used in combination with the display color to identify the tier based on item name. Added to
## the end of the item name.
identifier-color: DARK_RED
attributes:
  ## These attributes will go on every item from this tier.
  base-attributes:
    ## This needs to be a unique value per attribute/tier. It's used to help identify
    ## the attribute to Minecraft.
    legendaryuniquekey1:
      ## Attribute for this particular modifier. Attribute names here:
      ## https://hub.spigotmc.org/javadocs/spigot/org/bukkit/attribute/Attribute.html
      attribute: GENERIC_ARMOR
      ## Decimal value for the amount this attribute adds.
      amount: 2.0
      ## Operation for this particular modifier. Operation names and descriptions here:
      ## https://hub.spigotmc.org/javadocs/spigot/org/bukkit/attribute/AttributeModifier.Operation.html
      operation: ADD_NUMBER
  bonus-attributes:
    legendaryuniquekey2:
      attribute: GENERIC_ARMOR
      ## Decimal value for the minimum amount this attribute adds.
      minimum-amount: 4.0
      ## Decimal value for the maximum amount this attribute adds.
      maximum-amount: 6.0
      operation: ADD_NUMBER
      ## Which equipment slot should this modifier apply to? Not including
      ## this field makes it apply to every equipment slot. Slot names here:
      ## https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/EquipmentSlot.html
      slot: OFF_HAND
  ## The minimum number of bonus attributes to go on items from this tier.
  minimum-bonus-attributes: 0
  ## The maximum number of bonus attributes to go on items from this tier.
  maximum-bonus-attributes: 0
enchantments:
  ## If set to true, only base enchantments that could naturally appear on the item will be put on the item.
  ## For instance, with this on, a sword could not get the Feather Falling enchantment.
  safe-base-enchantments: true
  ## If set to true, only bonus enchantments that could naturally appear on the item will be put on the item.
  ## For instance, with this on, a sword could not get the Feather Falling enchantment.
  safe-bonus-enchantments: false
  ## If set to true, base enchantments are allowed to roll numbers higher than naturally obtainable.
  allow-high-base-enchantments: true
  ## If set to true, bonus enchantments are allowed to roll numbers higher than naturally obtainable.
  allow-high-bonus-enchantments: true
  ## This is a list of enchantments that will go on every item of this tier. They can be specified in
  ## the below format:
  ## < > - mandatory
  ## [ ] - optional
  ##
  ## <ENCHANTMENT NAME>:<MINIMUM LEVEL>:[MAXIMUM LEVEL]
  ##
  ## See below for a couple of examples. Make sure to not end the value with the colon.
  base-enchantments:
    - ARROW_DAMAGE:1:1
    - DURABILITY:10
    - ARROW_INFINITE:1:1
    - DAMAGE_ALL:1:5
  ## This is a list of enchantments that can be rolled onto items in this tier. Note that it has a different
  ## shape than the base-enchantments section. Both base-enchantments and bonus-enchantments can follow either
  ## pattern. You can have a list (like in the above section) or you can have sub-sections (like below).
  ##
  ## In this section, like above, minimum-level is mandatory while maximum-level is optional.
  bonus-enchantments:
    PROTECTION_FIRE:
      minimum-level: 1
      maximum-level: 2
    ARROW_FIRE:
      minimum-level: 1
      maximum-level: 1
    DAMAGE_ARTHROPODS:
      minimum-level: 1
      maximum-level: 4
  ## This is the minimum number of bonus-enchantments that can be rolled on an item
  ## from this tier. In order to meet this number, multiple of the same bonus enchantment
  ## can be rolled.
  minimum-bonus-enchantments: 7
  ## This is the maximum number of bonus-enchantments that can be rolled on an item
  ## from this tier. In order to meet this number, multiple of the same bonus enchantment
  ## can be rolled.
  maximum-bonus-enchantments: 10
lore:
  ## This is the minimum number of bonus-lore that can be rolled on an item
  ## from this tier. In order to meet this number, multiple of the same bonus lore
  ## can be rolled.
  minimum-bonus-lore: 0
  ## This is the maximum number of bonus-lore that can be rolled on an item
  ## from this tier. In order to meet this number, multiple of the same bonus lore
  ## can be rolled.
  maximum-bonus-lore: 0
  ## This is a list of lore that will go on every item from this tier.
  base-lore:
    - "&6An item only the worthy can hold."
  ## This is a list of lore that will be selected from when applying bonus lore. You can add
  ## multiple lines in the bonus lore by adding a linebreak character in the value: \n
  bonus-lore:
    - "&6Test Lore"
    - "&6Please Ignore"
## This is the maximum durability percentage that an item of this tier can spawn
## at. 1.0 = 100%, 0.0 = 0%
maximum-durability: 0.8
## This is the minimum durability percentage that an item of this tier can spawn
## at. 1.0 = 100%, 0.0 = 0%
minimum-durability: 0.75
## This is the minimum number of sockets that can be rolled on an item of this tier.
minimum-sockets: 0
## This is the maximum number of sockets that can be rolled on an item of this tier.
maximum-sockets: 6
## This is the weight of this tier. See the weight page for more details.
weight: 0.01
## This is the percentage chance for a mob to drop this item if they're holding it
## when they die. 1.0 = 100%, 0.0 = 0%
## Note that the above percentages only work for when a player has recently damaged the mob
## before it dies (~1 second). If you want it to work for fall damage, bump it to 1.1.
chance-to-drop-on-monster-death: 1.0
## This is the weight of this tier when identifying an item. See the weight page for more details.
identity-weight: 0.01
## This is the percentage chance for an item of this tier to have sockets. 1.0 = 100%, 0.0 = 0%
chance-to-have-sockets: 0.6
## Should a broadcast be sent to all players on the same world when an item of this tier is found?
broadcast-on-find: true
item-types:
  ## Item groups containing materials that items from this tier can use.
  allowed-groups:
    - pickaxe
    - sword
    - shears
    - axe
    - hoe
    - shovel
    - bow
    - fishing rod
    - helmet
    - boots
    - chestplate
    - leggings
  ## Item groups containing materials that items from this tier cannot use. Any materials in both this list
  ## and allowed-groups will not be used.
  disallowed-groups:
    - leather
    - chainmail
    - gold
    - iron
    - wood
    - stone
  ## Explicit material IDs that items from this tier can use. Combined with allowed-groups.
  allowed-material-ids: []
  ## Explicit material IDs that items from this tier cannot use. Combined with disallowed-groups.
  disallowed-material-ids: []
## Minimum distance from spawn that a mob must be in order to get this item. -1 disables.
minimum-distance-from-spawn: -1
## Maximum distance from spawn that a mob must be in order to get this item. -1 disables.
maximum-distance-from-spawn: -1
## Should the item spawn with the unbreakable flag?
unbreakable: false
```

</TabItem>
<TabItem value="1.0.1">

```yaml
version: 1.0.1
## Display name for the tier. Used for display on items in lore.
display-name: Legendary
## Color of the tier when used in names. Prefixed to the name of the randomized item as well.
display-color: GOLD
## Used in combination with the display color to identify the tier based on item name. Added to
## the end of the item name.
identifier-color: DARK_RED
enchantments:
  ## If set to true, only base enchantments that could naturally appear on the item will be put on the item.
  ## For instance, with this on, a sword could not get the Feather Falling enchantment.
  safe-base-enchantments: true
  ## If set to true, only bonus enchantments that could naturally appear on the item will be put on the item.
  ## For instance, with this on, a sword could not get the Feather Falling enchantment.
  safe-bonus-enchantments: false
  ## If set to true, base enchantments are allowed to roll numbers higher than naturally obtainable.
  allow-high-base-enchantments: true
  ## If set to true, bonus enchantments are allowed to roll numbers higher than naturally obtainable.
  allow-high-bonus-enchantments: true
  ## This is a list of enchantments that will go on every item of this tier. They can be specified in
  ## the below format:
  ## < > - mandatory
  ## [ ] - optional
  ##
  ## <ENCHANTMENT NAME>:<MINIMUM LEVEL>:[MAXIMUM LEVEL]
  ##
  ## See below for a couple of examples. Make sure to not end the value with the colon.
  base-enchantments:
    - ARROW_DAMAGE:1:1
    - DURABILITY:10
    - ARROW_INFINITE:1:1
    - DAMAGE_ALL:1:5
  ## This is a list of enchantments that can be rolled onto items in this tier. Note that it has a different
  ## shape than the base-enchantments section. Both base-enchantments and bonus-enchantments can follow either
  ## pattern. You can have a list (like in the above section) or you can have sub-sections (like below).
  ##
  ## In this section, like above, minimum-level is mandatory while maximum-level is optional.
  bonus-enchantments:
    PROTECTION_FIRE:
      minimum-level: 1
      maximum-level: 2
    ARROW_FIRE:
      minimum-level: 1
      maximum-level: 1
    DAMAGE_ARTHROPODS:
      minimum-level: 1
      maximum-level: 4
  ## This is the minimum number of bonus-enchantments that can be rolled on an item
  ## from this tier. In order to meet this number, multiple of the same bonus enchantment
  ## can be rolled.
  minimum-bonus-enchantments: 7
  ## This is the maximum number of bonus-enchantments that can be rolled on an item
  ## from this tier. In order to meet this number, multiple of the same bonus enchantment
  ## can be rolled.
  maximum-bonus-enchantments: 10
lore:
  ## This is the minimum number of bonus-lore that can be rolled on an item
  ## from this tier. In order to meet this number, multiple of the same bonus lore
  ## can be rolled.
  minimum-bonus-lore: 0
  ## This is the maximum number of bonus-lore that can be rolled on an item
  ## from this tier. In order to meet this number, multiple of the same bonus lore
  ## can be rolled.
  maximum-bonus-lore: 0
  ## This is a list of lore that will go on every item from this tier.
  base-lore:
    - "&6An item only the worthy can hold."
  ## This is a list of lore that will be selected from when applying bonus lore. You can add
  ## multiple lines in the bonus lore by adding a linebreak character in the value: \n
  bonus-lore:
    - "&6Test Lore"
    - "&6Please Ignore"
## This is the maximum durability percentage that an item of this tier can spawn
## at. 1.0 = 100%, 0.0 = 0%
maximum-durability: 0.8
## This is the minimum durability percentage that an item of this tier can spawn
## at. 1.0 = 100%, 0.0 = 0%
minimum-durability: 0.75
## This is the minimum number of sockets that can be rolled on an item of this tier.
minimum-sockets: 0
## This is the maximum number of sockets that can be rolled on an item of this tier.
maximum-sockets: 6
## This is the weight of this tier. See the weight page for more details.
weight: 0.01
## This is the percentage chance for a mob to drop this item if they're holding it
## when they die. 1.0 = 100%, 0.0 = 0%
chance-to-drop-on-monster-death: 1.0
## This is the weight of this tier when identifying an item. See the weight page for more details.
identity-weight: 0.01
## This is the percentage chance for an item of this tier to have sockets. 1.0 = 100%, 0.0 = 0%
chance-to-have-sockets: 0.6
## Should a broadcast be sent to all players on the same world when an item of this tier is found?
broadcast-on-find: true
item-types:
  ## Item groups containing materials that items from this tier can use.
  allowed-groups:
    - pickaxe
    - sword
    - shears
    - axe
    - hoe
    - shovel
    - bow
    - fishing rod
    - helmet
    - boots
    - chestplate
    - leggings
  ## Item groups containing materials that items from this tier cannot use. Any materials in both this list
  ## and allowed-groups will not be used.
  disallowed-groups:
    - leather
    - chainmail
    - gold
    - iron
    - wood
    - stone
  ## Explicit material IDs that items from this tier can use. Combined with allowed-groups.
  allowed-material-ids: []
  ## Explicit material IDs that items from this tier cannot use. Combined with disallowed-groups.
  disallowed-material-ids: []
## Minimum distance from spawn that a mob must be in order to get this item. -1 disables.
minimum-distance-from-spawn: -1
## Maximum distance from spawn that a mob must be in order to get this item. -1 disables.
maximum-distance-from-spawn: -1
## Should the item spawn with the unbreakable flag?
unbreakable: false
```

</TabItem>
</Tabs>
