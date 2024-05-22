---
title: customItems.yml
description: customItems.yml reference
---

MythicDrops has a lot of configuration options. Below is the contents of the customItems.yml with inline explanations of
what each configuration option does.

```yaml
version: 5.4.0
## Name of a custom item. Used as an identifier by the plugin, so it needs
## to be unique.
socketsword:
  ## Material of the custom item. You can obtain a list of potential material
  ## names (for 1.14 at time of writing) here:
  ## https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html
  material: WOODEN_SWORD
  ## Name to display on the custom item itself.
  "display-name": "&2Socket Sword (Unique)"
  ## Goes in the description of the custom item.
  lore:
    - "&7This sword has sockets!"
    - "&2(Socket)"
    - "&2(Socket)"
    - "&2(Socket)"
    - "&7Find a &2Socket Gem&7 to fill a &2(Socket)"
  ## Weight of the custom item. See the weights section of the documentation
  ## for more information.
  weight: 100
  ## Durability of the item when spawned. A durability of 1 means that the
  ## item has taken 1 damage. If an item has a max durability of 127,
  ## then setting this to 126 would almost break the item. This is due
  ## to how both Minecraft and Spigot handle durability.
  durability: 1
  ## Chance for this custom item to drop when a monster that is carrying it
  ## dies.
  chance-to-drop-on-monster-death: 1.0
  ## Should a message be sent to every player in the same world
  ## when this custom item is dropped?
  broadcast-on-find: true
  ## Custom model data value. Only supported in 1.14+.
  ## https://www.planetminecraft.com/forums/communities/texturing/new-1-14-custom-item-models-tuto-578834/
  custom-model-data: 0
  ## Should the custom item have the unbreakable NBT tag?
  unbreakable: false
  ## Should the custom item glow even if it doesn't have any enchantments?
  glow: false
  ## Enchantments to go on the custom item. Enchantment names here:
  ## https://hub.spigotmc.org/javadocs/spigot/org/bukkit/enchantments/Enchantment.html
  enchantments:
    ## Standard enchantment name to level mapping.
    DAMAGE_ALL: 5
    LOOTING:
      ## Minimum level of enchantment
      minimum-level: 1
      ## Maximum level of enchantment. Can be higher than normal maximum level
      ## per Minecraft rules.
      maximum-level: 2
  ## Attributes to go on the items.
  attributes:
    ## This needs to be a unique value per custom item. It's used to help identify
    ## the attribute to Minecraft.
    iamarandomuniquekey1:
      ## Attribute for this particular modifier. Attribute names here:
      ## https://hub.spigotmc.org/javadocs/spigot/org/bukkit/attribute/Attribute.html
      attribute: GENERIC_ARMOR
      ## Decimal value for the amount this attribute adds.
      amount: 2.0
      ## Operation for this particular modifier. Operation names and descriptions here:
      ## https://hub.spigotmc.org/javadocs/spigot/org/bukkit/attribute/AttributeModifier.Operation.html
      operation: ADD_NUMBER
    ## This attribute modifier uses a minimum amount and maximum amount to
    ## create a randomized amount to apply for the modifier.
    iamarandomuniquekey2:
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
  ## Flags to control what is displayed on an item.
  ## https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/ItemFlag.html
  item-flags:
    - HIDE_ATTRIBUTES
  ## This is the level cost for an item if it is put inside an anvil.
  repair-cost: 1000
  ## Can enchantments be removed by a grindstone?
  enchantments-removable-by-grindstone: false
  ## Should the default attributes for the custom item's material be added to the custom item?
  add-default-attributes: false
  ## HeadDatabase ID for a head
  hdb-id: "some-id-string"
```
