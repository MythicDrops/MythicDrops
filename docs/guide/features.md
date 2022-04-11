# Features

## Tiered Items

## Custom Items

## Socket Gems

## Socket Extenders

Socket Extenders support adding empty sockets to an item. These sockets can go on MythicDrops items, items from other
plugins, or any other item in the game.

### How To Use

**Note that this assumes that you are in Survival. Creative will not work.**

In order to use a Socket Extender on an item, open your Minecraft inventory. Once in your inventory, pick up the item
with your cursor. Hover your cursor over the item to which you want to add the socket. Right-click on the item. The
Socket Extender will be consumed; the item will now have an extra socket.

As you'll see how to configure in one of the below sections, you can also configure Socket Extenders to have a limited
number of uses on an item. Once you use all the slots on the item, Socket Extenders will no longer work with that item.
This functionality is optional and turned off by default.

### Configuration

#### Tiers

If you want to limit the number of uses of Socket Extenders, you can use the below values in a tier's YAML file to
control their use with Socket Extenders:

```yaml
## This is the percentage chance for an item of this tier to have sockets. 1.0 = 100%, 0.0 = 0%
chance-to-have-socket-extender-slots: 0.0
## This is the minimum number of socket extender slots that can be rolled on an item of this tier.
minimum-socket-extender-slots: 0
## This is the maximum number of socket extender slots that can be rolled on an item of this tier.
maximum-socket-extender-slots: 0
```

Note that the above configuration doesn't really mean much unless you're also setting
`require-extender-slots-to-add-sockets` to `true` in the socketing.yml.

#### socketing.yml

If you're familiar with Socket Gems, you'll be familiar with how to configure the materials for a Socket Extender:

```yaml
## Materials that can be used as socket extenders.
## https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html
socket-extender-material-ids:
  - NETHER_STAR
```

This is where the magic happens for limiting the use of Socket Extenders. If the below value is true, there must be an
empty socket extender slot on the item in order to use a Socket Extender on the item. You'll see what a socket extender
slot looks like down below.

```yaml
## Should an item require an unused socket extender slot on the item in order
## for a socket extender to be able to add an empty socket to the item?
require-extender-slots-to-add-sockets: false
```

This is where you'd configure what a Socket Extender looks like. This is pretty straightforward, there are no
placeholders currently supported for Socket Extenders.

```yaml
socket-extender:
  ## Text to display for an open extender slot. Only necessary if the above
  ## `require-extender-slots-to-add-sockets` is true.
  slot: "&3(+)"
  ## Name to display on the socket extender.
  name: "&3Socket Extender"
  ## Lore to display on the socket extender. No placeholders.
  lore:
    - "&7Right-click while holding this item"
    - "&7over another item in"
    - "&7your inventory to add a socket to it!"
```

### Examples

#### No Limit

![Item without Socket Extender Slot](/itemwithoutsocketextenderslot.png)
![Socket Extender](/socketextender.png)
![Item with New Socket](/itemwithnewsocket.png)

#### Limited Number of Socket Extender Slots

![Limited Number of Socket Extender Slots](/limitednumberofsocketextenderslots.png)
![One Socket Extender Slot Taken Up](/onesocketextenderslottakenup.png)

## Socket Gem Combiners

Socket Gem Combiners allow players to sacrifice socket gems for the chance at a better socket gem.

### Usage

Whenever a Socket Gem Combiner is opened, it will open a custom inventory for the player that opened it and will only
allow them to place socket gems in the open slots. Once all 4 socket gems have been placed, they will be able to click
an item in the inventory to receive their new socket gem.

### Creation

In order to create a Socket Gem Combiner, look at a chest and run `/md combiners add`.

### Listing

In order to list existing Socket Gem Combiners and their locations, run `/md combiners list`.

### Deletion

In order to delete a Socket Gem Combiner, look at a Socket Gem Combiner and run `/md combiners delete`.

### Configuration

You can change multiple things about Socket Gem Combiners, including the items used in the inventory GUI, by editing
the [socketing.yml](socketing.yml.html).

The most relevant for functionality is the `combining` section at the bottom that looks something like this:

```yaml
combining:
  ## Do gems being combined require the same gem family?
  require-same-family: false
  ## Do gems being combined require the same gem level?
  require-same-level: false
```

### How Socket Gem Combiners Work

Each socket gem has a [`family` and `level` field](socketGems.yml.html). As mentioned above in
[Configuration](#configuration), there are configurable options for how these fields are used. If `require-same-family`
is set to `true`, each sacrificed gem must have the same `family` value. If `require-same-level`
is set to `true`, each sacrificed gem must have the same `level` value.

Those values have effects on how the result socket gem is calculated as well.

#### No Requirements

If both `require-same-family` and `require-same-level` are `false`, then the resulting gem will be a random socket gem
with a level one higher than the rounded-down average of the sacrificed gems.

For instance, if you sacrifice a level 1 gem, a level 2 gem, a level 3 gem, and a level 4 gem, you will receive a level
3 gem, as the average level (rounded down) is 2.

#### Same Family Requirement

If `require-same-family` is `true` and `require-same-level` is `false`, then the resulting gem will be a random socket
gem from the matching family with a level one higher than the rounded-down average of the sacrificed gems.

For instance, if you sacrifice a level 1 gem, a level 2 gem, a level 3 gem, and a level 4 gem, you will receive a level
3 gem from the same family, as the average level (rounded down) is 2.

#### Same Level Requirement

If `require-same-level` is `true` and `require-same-family` is `false`, then the resulting gem will be a random socket
gem with a level one higher than the sacrificed gems.

For instance, if you sacrifice 4 level 2 gems, you will receive a level 3 gem.

#### Same Family and Same Level Requirement

If `require-same-family` is `true` and `require-same-level` is `true`, then the resulting gem will be a random socket
gem from the matching family with a level one higher than the sacrificed gems.

For instance, if you sacrifice 4 level 2 gems, you will receive a level 3 gem from the same family, as the average
level (rounded down) is 2.

## Unidentified Items

## Identity Tomes

## Repairing

## Relations (mini-sets)

## Weight

MythicDrops uses a weight-based randomization system for determining tier drops, socket gem drops, and custom item
drops.

### Explanation

Weight based systems make it easier to expand upon items available in a randomized system. Instead of having to ensure
that each item's chance to be chosen all adds up to `1.0` (100%), weight based systems allow each item to contribute a
value to a total and each item therefore has an automatically determinable "chance" to be chosen.

If you have an item with a weight of 20 and all the items in the system have their weight add up to 200, the
aforementioned item has a 10% chance to be chosen.

This does have the side effect of making it a bit tedious to enforce that a specific item in the system has a static
percentage chance to be chosen. You end up having to do a bit of math based off of the weight of all of your items.

Ultimately, however, the weight based system is easier to expand and add more items to, as it will automatically
re-balance the percentage chances of the existing items in the system.

### Example

Think of it like a deck of cards.

If you have a suit of cards, Ace - King, and assign each of them a weight equal to their face value (1 - 13), you have a
total weight of 91.

You pick a random number between 0 and 91. Let's say it's 14.

You shuffle the cards from the suit and begin to deal them one at a time, adding each card to the previous cards you
dealt. If the total is 14 or greater, you choose the card you just dealt. Example of the series:

You flip over 4. Your total is 4, which is not greater than or equal to 14. You flip over 2. Your total is 6, which is
not greater than or equal to 14. You flip over 10. Your total is 16, which is greater than 14, so you choose 10.

## WorldGuard Support

MythicDrops supports disabling every type of drop within a WorldGuard region. You can do this via a few different flags,
all available and visible via the `/rg flags` command from WorldGuard.

| Flag Name                      | Flag Description                                                             |
| ------------------------------ | ---------------------------------------------------------------------------- |
| mythic-drops                   | If DENY, prevents any and all drops from MythicDrops in the region           |
| mythic-drops-tiered            | If DENY, prevents any tiered drops from MythicDrops in the region            |
| mythic-drops-custom            | If DENY, prevents any custom item drops from MythicDrops in the region       |
| mythic-drops-socket-gem        | If DENY, prevents any socket gem drops from MythicDrops in the region        |
| mythic-drops-identity-tome     | If DENY, prevents any identity tome drops from MythicDrops in the region     |
| mythic-drops-unidentified-item | If DENY, prevents any unidentified item drops from MythicDrops in the region |
