---
title: socketTypes.yml
description: socketTypes.yml reference
---

MythicDrops has a lot of configuration options. Below is the contents of the socketTypes.yml with inline explanations of
what each configuration option does.

```yaml
version: 1.0.0
socket-types:
  ## Identifier for the Socket Type.
  any:
    ## Weight of the socket type. See the weights section of the documentation
    ## for more information. Defaults to 0.
    weight: 2
    ## What the name for Socket Gems of this type looks like.
    socket-gem-style: "&6Socket Gem - %socketgem%"
    ## What the empty socket looks like on an item. Goes in %socketablelore%.
    socket-style: "&F(Socket)"
    ## Should checking for empty sockets ignore colors? This value should be false for
    ## any socket type other than a "default" socket type. Defaults to false.
    ignore-colors: true
    ## Help text added to the lore of an item. Goes in %socketablelore%.
    socket-help:
      - "&7Find a &6Socket Gem&7 to fill a &F(Socket)"
  # other socket types down here...
socket-extender-types:
  default:
    ## Weight of the socket extender type. See the weights section of the documentation
    ## for more information. Defaults to 0.
    weight: 10
    ## Custom model data applied to the Socket Extender item. Defaults to 0.
    custom-model-data: 100
    ## Socket Type added by the Socket Extender. Must match a value in `socket-types`.
    applied-socket-type: "any"
    ## The display name of the Socket Extender item.
    socket-extender-style: "&3Socket Extender"
    ## Help text added to the lore of the Socket Extender item.
    socket-extender-help:
      - "&7Use on an item with a %slot-style%&7"
      - "&7in order to add a %applied-socket-type.socket-style%&7."
    ## What the empty socket extender slot looks like on an item. Goes in %socketablelore%.
    slot-style: "&3(+)"
    ## Help text added to the lore of an item. Goes in %socketablelore%.
    slot-help:
      - "&7Find a &3Socket Extender&7 to fill a &3(+)&7."
    ## Should checking for empty slots ignore colors? This value should be false for
    ## any socket extender type other than a "default" socket extender type. Defaults to false.
    ignore-colors: true
  # other socket extender types down here...
```
