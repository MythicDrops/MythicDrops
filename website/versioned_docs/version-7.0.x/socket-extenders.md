---
id: socket-extenders
title: Socket Extenders
---

import useBaseUrl from "@docusaurus/useBaseUrl";

Socket Extenders support adding empty sockets to an item. These sockets can go on MythicDrops items, items
from other plugins, or any other item in the game.

## How To Use

**Note that this assumes that you are in Survival. Creative will not work.**

In order to use a Socket Extender on an item, open your Minecraft inventory. Once in your inventory,
pick up the item with your cursor. Hover your cursor over the item to which you want to add the socket.
Right click on the item. The Socket Extender will be consumed; the item will now have an extra socket.

As you'll see how to configure in one of the below sections, you can also configure Socket Extenders to
have a limited number of uses on an item. Once you use all the slots on the item, Socket Extenders will
no longer work with that item. This functionality is optional and turned off by default.

## Configuration

### Tiers

If you want to limit the number of uses of Socket Extenders, you can use the below values in a tier's
YAML file to control their use with Socket Extenders:

```yaml
## This is the percentage chance for an item of this tier to have sockets. 1.0 = 100%, 0.0 = 0%
chance-to-have-socket-extender-slots: 0.0
## This is the minimum number of socket extender slots that can be rolled on an item of this tier.
minimum-socket-extender-slots: 0
## This is the maximum number of socket extender slots that can be rolled on an item of this tier.
maximum-socket-extender-slots: 0
```

Note that the above configuration doesn't really mean much unless you're also setting the value in the
socketing.yml.

### socketing.yml

If you're familiar with Socket Gems, you'll be familiar with how to configure the materials for a
Socket Extender:

```yaml
## Materials that can be used as socket extenders.
## https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html
socket-extender-material-ids:
  - NETHER_STAR
```

This is where the magic happens for limiting the use of Socket Extenders. If the below value is true, there
must be an empty socket extender slot on the item in order to use a Socket Extender on the item. You'll see
what a socket extender slot looks like down below.

```yaml
## Should an item require an unused socket extender slot on the item in order
## for a socket extender to be able to add an empty socket to the item?
require-extender-slots-to-add-sockets: false
```

This is where you'd configure what a Socket Extender looks like. This is pretty straightforward, there are
no placeholders currently supported for Socket Extenders.

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

## Examples

### No Limit

<img
alt="Item without Socket Extender Slot"
src={useBaseUrl("img/itemwithoutsocketextenderslot.png")}
/>
<img
alt="Socket Extender"
src={useBaseUrl("img/itemwithoutsocketextenderslot.png")}
/>
<img
alt="Item with New Socket"
src={useBaseUrl("img/itemwithnewsocket.png")}
/>

### Limited Number of Socket Extender Slots

<img
alt="Limited Number of Socket Extender Slots"
src={useBaseUrl("img/limitednumberofsocketextenderslots.png")}
/>
<img
alt="One Socket Extender Slot Taken Up"
src={useBaseUrl("img/onesocketextenderslottakenup.png")}
/>
