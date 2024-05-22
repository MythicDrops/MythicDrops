---
title: language.yml
description: language.yml reference
---

MythicDrops has a lot of configuration options. Below is the contents of the language.yml. Most of the options are just
used for various command outputs and the names of the keys are fairly self-explanatory.

There are a couple of explanations below, but most options are self-explanatory.

```yaml
version: 3.11.0
# A bunch of different and modifiable messages
#   used in the "/md" commands.
general:
  found-item-broadcast: "&6[MythicDrops] &F%receiver%&A has found a %item%!"
  prevented-netherite-upgrade: "&6[MythicDrops] You cannot upgrade this item to Netherite!"
command:
  only-players: "&6[MythicDrops] &COnly players can run this command."
  no-access: "&6[MythicDrops] &CYou don't have permission to do that."
  reload-config: "&6[MythicDrops] &AMythicDrops configs reloaded."
  reload-plugin: "&6[MythicDrops] &AMythicDrops reloaded."
  save-config: "&6[MythicDrops] &AMythicDrops configs saved."
  tier-does-not-exist: "&6[MythicDrops] &CThat tier does not exist."
  custom-item-does-not-exist: "&6[MythicDrops] &CThat custom item does not exist."
  player-does-not-exist: "&6[MythicDrops] &CThat player does not exist or is not online."
  world-does-not-exist: "&6[MythicDrops] &CThat world does not exist."
  socket-gem-does-not-exist: "&6[MythicDrops] &CThat Socket Gem does not exist."
  item-group-does-not-exist: "&6[MythicDrops] &CThat item group does not exist."
  unknown-player: "unknown player"
  custom-item-list: "&6[MythicDrops] &CCustom Items: &F%customitems%"
  tier-list: "&6[MythicDrops] &7Tiers: &F%tiers%"
  debug: "&6[MythicDrops] &7Debug printed."
  help: "&7/%command% &F- &7%help%"
  custom-create:
    success: "&6[MythicDrops] &ACreated a custom item with id &F%name%&A."
    failure: "&6[MythicDrops] &CUnable to create custom item."
    requires-item: "&6[MythicDrops] &CUnable to create custom item because there is no item in your main hand."
    requires-item-meta: "&6[MythicDrops] &CUnable to create custom item because item has no display name, lore, or enchantments."
    requires-display-name: "&6[MythicDrops] &CUnable to create custom item because item has no display name."
  drop-custom:
    success: "&6[MythicDrops] &AYou dropped &F%amount%&A custom MythicDrops item(s)."
    failure: "&6[MythicDrops] &CYou were unable to drop &F%amount%&C custom MythicDrops item(s)."
  drop-extender:
    success: "&6[MythicDrops] &AYou dropped &F%amount%&A &3Socket Extender(s)&A."
    failure: "&6[MythicDrops] &CYou were unable to drop &F%amount%&C &3Socket Extender(s)&C."
  drop-gem:
    success: "&6[MythicDrops] &AYou dropped &F%amount%&A &6Socket Gem(s)&A."
    failure: "&6[MythicDrops] &CYou were unable to drop &F%amount%&C &6Socket Gem(s)&C."
  drop-random:
    success: "&6[MythicDrops] &AYou dropped &F%amount%&A MythicDrops item(s)."
    failure: "&6[MythicDrops] &CYou were unable to drop &F%amount%&C MythicDrops item(s)."
  drop-tome:
    success: "&6[MythicDrops] &AYou dropped &F%amount%&A &5Identity Tome(s)&A."
    failure: "&6[MythicDrops] &CYou were unable to drop &F%amount%&C &5Identity Tome(s)&C."
  drop-unidentified:
    success: "&6[MythicDrops] &AYou dropped &F%amount%&A &DUnidentified Item(s)&A."
    failure: "&6[MythicDrops] &CYou were unable to drop &F%amount%&C &DUnidentified Item(s)&C."
  give-custom:
    sender-success: "&6[MythicDrops] &F%receiver%&A was given &F%amount%&A custom MythicDrops item(s)."
    sender-failure: "&6[MythicDrops] &F%receiver%&C was unable to be given &F%amount%&C custom MythicDrops item(s)."
    receiver-success: "&6[MythicDrops] &AYou have received &F%amount%&A MythicDrops item(s)!"
    receiver-failure: "&6[MythicDrops] &CYou were unable to be given &F%amount%&C custom MythicDrops item(s)."
  give-extender:
    sender-success: "&6[MythicDrops] &F%receiver%&A was given &F%amount%&A &3Socket Extender(s)&A."
    sender-failure: "&6[MythicDrops] &F%receiver%&C was unable to be given &F%amount%&C &3Socket Extender(s)&C."
    receiver-success: "&6[MythicDrops] &AYou have received &F%amount%&A &3Socket Extender(s)&A!"
    receiver-failure: "&6[MythicDrops] &CYou were unable to be given &F%amount%&C &3Socket Extender(s)&C."
  give-gem:
    sender-success: "&6[MythicDrops] &F%receiver%&A was given &F%amount%&A &6Socket Gem(s)&A."
    sender-failure: "&6[MythicDrops] &F%receiver%&C was unable to be given &F%amount%&C &6Socket Gem(s)&C."
    receiver-success: "&6[MythicDrops] &AYou have received &F%amount%&A &6Socket Gem(s)&A!"
    receiver-failure: "&6[MythicDrops] &CYou were unable to be given &F%amount%&C &6Socket Gem(s)&C."
  give-random:
    sender-success: "&6[MythicDrops] &F%receiver%&A was given &F%amount%&A MythicDrops item(s)."
    sender-failure: "&6[MythicDrops] &F%receiver%&C was unable to be given &F%amount%&C MythicDrops item(s)."
    receiver-success: "&6[MythicDrops] &AYou were given &F%amount%&A MythicDrops item(s)."
    receiver-failure: "&6[MythicDrops] &CYou were unable to be given &F%amount%&C MythicDrops item(s)."
  give-tome:
    sender-success: "&6[MythicDrops] &F%receiver%&A was given &F%amount%&A &5Identity Tome(s)&A."
    sender-failure: "&6[MythicDrops] &F%receiver%&C was unable to be given &F%amount%&C &5Identity Tome(s)&C."
    receiver-success: "&6[MythicDrops] &AYou have received &F%amount%&A &5Identity Tome(s)&A!"
    receiver-failure: "&6[MythicDrops] &CYou were unable to be given &F%amount%&C &5Identity Tome(s)&C."
  give-unidentified:
    sender-success: "&6[MythicDrops] &F%receiver%&A was given &F%amount%&A &DUnidentified Item(s)&A."
    sender-failure: "&6[MythicDrops] &F%receiver%&C was unable to be given &F%amount%&C &DUnidentified Item(s)&C."
    receiver-success: "&6[MythicDrops] &AYou have received &F%amount%&A &DUnidentified Item(s)&A!"
    receiver-failure: "&6[MythicDrops] &CYou were unable to be given &F%amount%&C &DUnidentified Item(s)&C."
  item-groups:
    list: "&6[MythicDrops] &6Item Groups: &F%itemgroups%"
    materials-list: "&6[MythicDrops] &6Item Group (&F%itemgroup%&6) materials: &F%materials%"
    priority: "&6[MythicDrops] &6Item Group (&F%itemgroup%&6) priority: &F%priority%"
  socket-gems:
    list: "&6[MythicDrops] &6Socket Gems: &F%socketgems%"
    commands: "&6[MythicDrops] &6Socket Gem (&F%socketgem%&6) commands: &F%commands%"
    effects: "&6[MythicDrops] &6Socket Gem (&F%socketgem%&6) effects: &F%effects%"
    enchantments: "&6[MythicDrops] &6Socket Gem (&F%socketgem%&6) enchantments: &F%enchantments%"
  modify:
    failure: "&6[MythicDrops] &CCannot modify this item!"
    name: "&6[MythicDrops] &ASuccessfully modified the name of the item in your hand!"
    lore:
      add: "&6[MythicDrops] &ASuccessfully added lore to the item in your hand!"
      remove: "&6[MythicDrops] &ASuccessfully removed lore from the item in your hand!"
      insert: "&6[MythicDrops] &ASuccessfully inserted lore on the item in your hand!"
      set: "&6[MythicDrops] &ASuccessfully set lore on the item in your hand!"
    enchantment:
      add: "&6[MythicDrops] &ASuccessfully added an enchantment to the item in your hand!"
      remove: "&6[MythicDrops] &ASuccessfully removed an enchantment from the item in your hand!"
  spawn-custom:
    success: "&6[MythicDrops] &AYou have received &F%amount%&A custom MythicDrops item(s)."
    failure: "&6[MythicDrops] &CYou were unable to be given &F%amount%&C custom MythicDrops item(s)."
  spawn-extender:
    success: "&6[MythicDrops] &AYou have received &F%amount%&A &3Socket Extender(s)&A."
    failure: "&6[MythicDrops] &CYou were unable to be given &F%amount%&C &3Socket Extender(s)&C."
  spawn-gem:
    success: "&6[MythicDrops] &AYou have received &F%amount%&A &6Socket Gem(s)&A."
    failure: "&6[MythicDrops] &CYou were unable to be given &F%amount%&C &6Socket Gem(s)&C."
  spawn-random:
    success: "&6[MythicDrops] &AYou were given &F%amount%&A MythicDrops item(s)."
    failure: "&6[MythicDrops] &CYou were unable to be given &F%amount%&C MythicDrops item(s)."
  spawn-tome:
    success: "&6[MythicDrops] &AYou have received &F%amount%&A &5Identity Tome(s)&A."
    failure: "&6[MythicDrops] &CYou were unable to be given &F%amount%&C &5Identity Tome(s)&C."
  spawn-unidentified:
    success: "&6[MythicDrops] &AYou have received &F%amount%&A &DUnidentified Item(s)&A."
    failure: "&6[MythicDrops] &CYou were unable to be given &F%amount%&C &DUnidentified Item(s)&C."
  socket-gem-combiner-add:
    success: "&6[MythicDrops] &AYou successfully added a socket gem combiner!"
    failure: "&6[MythicDrops] &CYou failed to add a socket gem combiner!"
  socket-gem-combiner-remove:
    success: "&6[MythicDrops] &AYou successfully removed a &4Socket Gem Combiner&A!"
    failure: "&6[MythicDrops] &CYou failed to remove a socket gem combiner!"
  socket-gem-combiner-open: "&6[MythicDrops] &AOpened socket gem combiner GUI for &F%player%&A."
socketing:
  success: "&6[MythicDrops] &AYou successfully added a socket gem to your item!"
  not-in-item-group: "&6[MythicDrops] &CYou cannot use that gem on that type of item!"
  no-open-sockets: "&6[MythicDrops] &CThat item does not have any open sockets!"
  prevented-crafting: "&6[MythicDrops] &CYou cannot craft items with &6Socket Gems&C or &3Socket Extenders&C!"
  combiner-must-be-gem: "&6[MythicDrops] &CYou can only put &6Socket Gems &Cinside the combiner!"
  combiner-claim-output: "&6[MythicDrops] &CPlease claim your combined gem before adding more gems!"
  added-socket: "&6[MythicDrops] &AYou successfully added a socket to your item!"
  no-socket-extender-slots: "&6[MythicDrops] &CThere are no open socket extender slots on your item."
  maximum-socket-extender-slots: "&6[MythicDrops] &CYour item has reached the maximum number of sockets."
identification:
  success: "&6[MythicDrops] &AYou successfully identified your item!"
  failure: "&6[MythicDrops] &CYou cannot identify that item!"
  not-unidentified-item: "&6[MythicDrops] &CYou cannot identify an already identified item!"
repairing:
  cannot-use: "&6[MythicDrops] &CYou cannot repair that item!"
  do-not-have: "&6[MythicDrops] &CYou do not have enough materials to repair that item!"
  success: "&6[MythicDrops] &AYou successfully repaired your item!"
  instructions: "&6[MythicDrops] &ASmack this item on an anvil again to repair it!"
# Various display names that are used for
#   different itemNameFormat variables
"display-names":
  IRON_SHOVEL: Iron Shovel
  IRON_PICKAXE: Iron Pickaxe
  IRON_AXE: Iron Axe
  BOW: Bow
  IRON_SWORD: Iron Sword
  WOODEN_SWORD: Wood Sword
  WOODEN_SHOVEL: Wooden Shovel
  WOODEN_PICKAXE: Wood Pickaxe
  WOODEN_AXE: Wood Axe
  STONE_SWORD: Stone Sword
  STONE_SHOVEL: Stone Shovel
  STONE_PICKAXE: Stone Pickaxe
  STONE_AXE: Stone Axe
  DIAMOND_SWORD: Diamond Sword
  DIAMOND_SHOVEL: Diamond Shovel
  DIAMOND_PICKAXE: Diamond Pickaxe
  DIAMOND_AXE: Diamond Axe
  GOLDEN_SWORD: Golden Sword
  GOLDEN_SHOVEL: Golden Shovel
  GOLDEN_PICKAXE: Golden Pickaxe
  GOLDEN_AXE: Golden Axe
  WOODEN_HOE: Wooden Hoe
  STONE_HOE: Stone Hoe
  IRON_HOE: Iron Hoe
  DIAMOND_HOE: Diamond Hoe
  GOLDEN_HOE: Golden Hoe
  LEATHER_HELMET: Leather Cap
  LEATHER_CHESTPLATE: Leather Tunic
  LEATHER_LEGGINGS: Leather Pants
  LEATHER_BOOTS: Leather Boots
  CHAINMAIL_HELMET: Chainmail Helmet
  CHAINMAIL_CHESTPLATE: Chainmail Chestplate
  CHAINMAIL_LEGGINGS: Chainmail Leggings
  CHAINMAIL_BOOTS: Chainmail Boots
  IRON_HELMET: Iron Helmet
  IRON_CHESTPLATE: Iron Chestplate
  IRON_LEGGINGS: Iron Leggings
  IRON_BOOTS: Iron Boots
  DIAMOND_HELMET: Diamond Helmet
  DIAMOND_CHESTPLATE: Diamond Chestplate
  DIAMOND_LEGGINGS: Diamond Leggings
  DIAMOND_BOOTS: Diamond Boots
  GOLDEN_HELMET: Golden Helmet
  GOLDEN_CHESTPLATE: Golden Chestplate
  GOLDEN_LEGGINGS: Golden Leggings
  GOLDEN_BOOTS: Golden Boots
  FISHING_ROD: Fishing Rod
  SHEARS: Shears
  BOOK: Book
  ELYTRA: Elytra
  TRIDENT: Trident
  CROSSBOW: Crossbow
  ARROW: Arrow
  TIPPED_ARROW: Tipped Arrow
  SPECTRAL_ARROW: Spectral Arrow
  SHIELD: Shield
  Ordinary: Ordinary
  DURABILITY: Durable
  PROTECTION_ENVIRONMENTAL: Safe
  LOOT_BONUS_BLOCKS: Fortunate
  PROTECTION_FIRE: Fireproof
  DIG_SPEED: Miner's
  PROTECTION_FALL: Feathery
  SILK_TOUCH: Careful
  PROTECTION_EXPLOSIONS: Wary
  PROTECTION_PROJECTILE: Dodger's
  OXYGEN: Merman's
  WATER_WORKER: Atlantean
  THORNS: Prickly
  ARROW_INFINITE: Quivered
  DAMAGE_UNDEAD: Holy
  ARROW_FIRE: Flaming
  DAMAGE_ALL: Sharp
  ARROW_KNOCKBACK: Broadhead
  KNOCKBACK: Heavy
  ARROW_DAMAGE: Sharpened
  DAMAGE_ARTHROPODS: Squisher's
  LOOT_BONUS_MOBS: Looter's
  FIRE_ASPECT: Burning
  CREEPER: Creeper
  ZOMBIE: Zombie
```
