---
title: itemGroups.yml
description: itemGroups.yml reference
---

MythicDrops has a lot of configuration options. Below is the contents of the itemGroups.yml with inline explanations of
what each configuration option does.

```yaml
# You can create your own groups here.
version: 6.0.0
sword:
  # Materials that belong in this item group.
  materials:
    - WOODEN_SWORD
    - STONE_SWORD
    - IRON_SWORD
    - GOLDEN_SWORD
    - DIAMOND_SWORD
  # Priority of this item group for name/lore generation.
  # Negative numbers are ignored. 0 is most important.
  priority: 0
axe:
  materials:
    - WOODEN_AXE
    - STONE_AXE
    - IRON_AXE
    - GOLDEN_AXE
    - DIAMOND_AXE
  priority: 0
pickaxe:
  materials:
    - WOODEN_PICKAXE
    - STONE_PICKAXE
    - IRON_PICKAXE
    - GOLDEN_PICKAXE
    - DIAMOND_PICKAXE
  priority: 0
shovel:
  materials:
    - WOODEN_SHOVEL
    - STONE_SHOVEL
    - IRON_SHOVEL
    - GOLDEN_SHOVEL
    - DIAMOND_SHOVEL
  priority: 0
hoe:
  materials:
    - WOODEN_HOE
    - STONE_HOE
    - IRON_HOE
    - GOLDEN_HOE
    - DIAMOND_HOE
  priority: 0
shears:
  materials:
    - SHEARS
  priority: 0
bow:
  materials:
    - BOW
    - CROSSBOW
  priority: 0
fishing rod:
  materials:
    - FISHING_ROD
  priority: 0
arrow:
  materials:
    - ARROW
    - SPECTRAL_ARROW
    - TIPPED_ARROW
  priority: 0
trident:
  materials:
    - TRIDENT
  priority: 0
melee:
  materials:
    - WOODEN_SWORD
    - STONE_SWORD
    - IRON_SWORD
    - GOLDEN_SWORD
    - DIAMOND_SWORD
    - WOODEN_AXE
    - STONE_AXE
    - IRON_AXE
    - GOLDEN_AXE
    - DIAMOND_AXE
    - WOODEN_PICKAXE
    - STONE_PICKAXE
    - IRON_PICKAXE
    - GOLDEN_PICKAXE
    - DIAMOND_PICKAXE
    - WOODEN_SHOVEL
    - STONE_SHOVEL
    - IRON_SHOVEL
    - GOLDEN_SHOVEL
    - DIAMOND_SHOVEL
    - WOODEN_HOE
    - STONE_HOE
    - IRON_HOE
    - GOLDEN_HOE
    - DIAMOND_HOE
    - SHEARS
    - TRIDENT
  priority: 0
ranged:
  materials:
    - BOW
    - CROSSBOW
    - FISHING_ROD
    - ARROW
    - SPECTRAL_ARROW
    - TIPPED_ARROW
    - TRIDENT
  priority: 0
weapon:
  materials:
    - WOODEN_SWORD
    - STONE_SWORD
    - IRON_SWORD
    - GOLDEN_SWORD
    - DIAMOND_SWORD
    - WOODEN_AXE
    - STONE_AXE
    - IRON_AXE
    - GOLDEN_AXE
    - DIAMOND_AXE
    - WOODEN_PICKAXE
    - STONE_PICKAXE
    - IRON_PICKAXE
    - GOLDEN_PICKAXE
    - DIAMOND_PICKAXE
    - WOODEN_SHOVEL
    - STONE_SHOVEL
    - IRON_SHOVEL
    - GOLDEN_SHOVEL
    - DIAMOND_SHOVEL
    - WOODEN_HOE
    - STONE_HOE
    - IRON_HOE
    - GOLDEN_HOE
    - DIAMOND_HOE
    - SHEARS
    - TRIDENT
    - BOW
    - CROSSBOW
    - FISHING_ROD
    - ARROW
    - SPECTRAL_ARROW
    - TIPPED_ARROW
  priority: 0
helmet:
  materials:
    - LEATHER_HELMET
    - CHAINMAIL_HELMET
    - IRON_HELMET
    - GOLDEN_HELMET
    - DIAMOND_HELMET
  priority: 0
chestplate:
  materials:
    - LEATHER_CHESTPLATE
    - CHAINMAIL_CHESTPLATE
    - IRON_CHESTPLATE
    - GOLDEN_CHESTPLATE
    - DIAMOND_CHESTPLATE
  priority: 0
leggings:
  materials:
    - LEATHER_LEGGINGS
    - CHAINMAIL_LEGGINGS
    - IRON_LEGGINGS
    - GOLDEN_LEGGINGS
    - DIAMOND_LEGGINGS
  priority: 0
boots:
  materials:
    - LEATHER_BOOTS
    - CHAINMAIL_BOOTS
    - IRON_BOOTS
    - GOLDEN_BOOTS
    - DIAMOND_BOOTS
  priority: 0
armor:
  materials:
    - LEATHER_HELMET
    - CHAINMAIL_HELMET
    - IRON_HELMET
    - GOLDEN_HELMET
    - DIAMOND_HELMET
    - LEATHER_CHESTPLATE
    - CHAINMAIL_CHESTPLATE
    - IRON_CHESTPLATE
    - GOLDEN_CHESTPLATE
    - DIAMOND_CHESTPLATE
    - LEATHER_LEGGINGS
    - CHAINMAIL_LEGGINGS
    - IRON_LEGGINGS
    - GOLDEN_LEGGINGS
    - DIAMOND_LEGGINGS
    - LEATHER_BOOTS
    - CHAINMAIL_BOOTS
    - IRON_BOOTS
    - GOLDEN_BOOTS
    - DIAMOND_BOOTS
  priority: 0
shield:
  materials:
    - SHIELD
  priority: 0
elytra:
  materials:
    - ELYTRA
  priority: 0
wood:
  materials:
    - WOODEN_AXE
    - WOODEN_HOE
    - WOODEN_PICKAXE
    - WOODEN_SHOVEL
    - WOODEN_SWORD
    - BOW
    - CROSSBOW
    - FISHING_ROD
  priority: 0
wooden:
  materials:
    - WOODEN_AXE
    - WOODEN_HOE
    - WOODEN_PICKAXE
    - WOODEN_SHOVEL
    - WOODEN_SWORD
    - BOW
    - CROSSBOW
    - FISHING_ROD
  priority: 0
stone:
  materials:
    - STONE_AXE
    - STONE_PICKAXE
    - STONE_HOE
    - STONE_SWORD
    - STONE_SHOVEL
  priority: 0
leather:
  materials:
    - LEATHER_BOOTS
    - LEATHER_CHESTPLATE
    - LEATHER_HELMET
    - LEATHER_LEGGINGS
  priority: 0
gold:
  materials:
    - GOLDEN_AXE
    - GOLDEN_BOOTS
    - GOLDEN_CHESTPLATE
    - GOLDEN_HELMET
    - GOLDEN_LEGGINGS
    - GOLDEN_PICKAXE
    - GOLDEN_HOE
    - GOLDEN_SHOVEL
    - GOLDEN_SWORD
  priority: 0
iron:
  materials:
    - IRON_AXE
    - IRON_BOOTS
    - IRON_CHESTPLATE
    - IRON_HELMET
    - IRON_LEGGINGS
    - IRON_PICKAXE
    - IRON_HOE
    - IRON_SHOVEL
    - IRON_SWORD
  priority: 0
diamond:
  materials:
    - DIAMOND_AXE
    - DIAMOND_BOOTS
    - DIAMOND_CHESTPLATE
    - DIAMOND_HELMET
    - DIAMOND_HOE
    - DIAMOND_LEGGINGS
    - DIAMOND_PICKAXE
    - DIAMOND_SHOVEL
    - DIAMOND_SWORD
  priority: 0
chainmail:
  materials:
    - CHAINMAIL_BOOTS
    - CHAINMAIL_CHESTPLATE
    - CHAINMAIL_HELMET
    - CHAINMAIL_LEGGINGS
  priority: 0
```
