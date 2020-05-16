---
id: socketing_yml
title: socketing.yml
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

import useBaseUrl from "@docusaurus/useBaseUrl";

## Configuration

MythicDrops has a lot of configuration options. Below is the contents of the
socketing.yml with inline explanations of what each configuration option does.

<Tabs
defaultValue="5.3.0"
values={[
{ label: '5.3.0 (MD 6.1.x)', value: '5.3.0', },
{ label: '5.1.0 (MD 6.0.x)', value: '5.1.0', }
]
}>
<TabItem value="5.3.0">

```yaml
version: 5.3.0
options:
  ## Should gems be prevented from being used in crafting? true is a recommended default.
  prevent-crafting-with-gems: true
  ## Should gems be prevented from adding prefixes/suffixes if an item already has one?
  prevent-multiple-name-changes-from-sockets: false
  ## When applying ON_HIT/ON_HIT_AND_WHEN_HIT gems, use the attacker's items in their hands?
  use-attacker-item-in-hand: true
  ## When applying ON_HIT/ON_HIT_AND_WHEN_HIT gems, use the attacker's equipped armor?
  use-attacker-armor-equipped: false
  ## When applying WHEN_HIT/ON_HIT_AND_WHEN_HIT gems, use the defender's items in their hands?
  use-defender-item-in-hand: false
  ## When applying WHEN_HIT/ON_HIT_AND_WHEN_HIT gems, use the defender's equipped armor?
  use-defender-armor-equipped: true
  ## Materials that can be used as socket gems.
  ## https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html
  socket-gem-material-ids:
    - DIAMOND
    - EMERALD
    - NETHER_STAR
  ## Default color for socket gem names when applied to items.
  ## https://hub.spigotmc.org/javadocs/spigot/org/bukkit/ChatColor.html
  default-socket-name-color-on-items: GOLD
  ## Should socket gem names on items attempt to use the display color
  ## from a tier before falling back to default-socket-name-color-on-items?
  use-tier-color-for-socket-name: true
  ## Sets the number of seconds between AURA gem refreshes. This is intentionally set
  ## high as aura gems can be very computationally expensive due to distance checking.
  aura-gem-refresh-in-seconds: 30
items:
  socketed-item:
    ## Text to display for an open socket.
    ## %tiercolor% will use the display color of the tier of the item
    ## if available and default-socket-name-color-on-items if not.
    socket: "%tiercolor%(Socket)"
    ## Text to add to an item with an open socket.
    lore:
      - "&7Find a &6Socket Gem&7 to fill a &F%tiercolor%(Socket)&7"
  socket-gem:
    ## Should socket gems glow?
    glow: false
    ## Display name of a socket gem.
    name: "&6Socket Gem - %socketgem%"
    ## Lore/description of a socket gem.
    ## Available placeholders:
    ## - %sockettypelore% - combination of all-of, any-of, none-of item group lore
    ## - %socketfamilylore% - shows gem family and level
    ## - %socketgemlore% - lore from the socket gem's configuration
    lore:
      - "%sockettypelore%"
      - "%socketfamilylore%"
      - ""
      - "&7Right-click while holding this gem"
      - "&7over another item in"
      - "&7your inventory to socket it!"
      - ""
      - "%socketgemlore%"
    ## Customize %socketfamilylore%
    family-lore:
      - "&7Family: &F%family% &7(&F%level%&7)"
    ## Customize prefix of %sockettypelore%
    socket-type-lore:
      - "&7Type(s): &F%type%"
    ## Customize second part of %sockettypelore%
    any-of-socket-type-lore:
      - "  &7Requires One Of: &F%type%"
    ## Customize first part of %sockettypelore%
    all-of-socket-type-lore:
      - "  &7Requires All: &F%type%"
    ## Customize third part of %sockettypelore%
    none-of-socket-type-lore:
      - "  &7Requires None: &F%type%"
  socket-gem-combiner:
    ## Name displayed in socket gem combiner GUI
    name: "&4Socket Gem Combiner"
    ## Item to show in non-clickable spots in socket gem combiner GUI
    buffer:
      name: "&aClick a &6Socket Gem &ato begin!"
      material: "IRON_BARS"
      lore: []
    ## Item to show when socket gems are able to be combined in GUI
    click-to-combine:
      name: "&E&LClick to combine!"
      material: "NETHER_STAR"
      lore: []
    ## Item to show when socket gems are not able to be combined in GUI
    ineligible-to-combine:
      name: "&C&LIneligible to combine!"
      material: "BARRIER"
      ## Message to show when unable to be combined due to not being in
      ## same socket gem family
      same-family-lore:
        - "&7You need to have four &6Socket Gems"
        - "&7from the same family"
        - "&7in order to combine them!"
      ## Message to show when unable to be combined due to not being in
      ## same socket gem level
      same-level-lore:
        - "&7You need to have four &6Socket Gems"
        - "&7from the same level"
        - "&7in order to combine them!"
      ## Message to show when unable to be combined due to not being in
      ## same socket gem family and level
      same-family-and-level-lore:
        - "&7You need to have four &6Socket Gems"
        - "&7from the same family and level"
        - "&7in order to combine them!"
      ## Message to show when unable to be combined due to not being able
      ## to find a gem based on the gems available.
      no-gem-found-lore:
        - "&7Unable to find a gem for that"
        - "&7combination of gems!"
combining:
  ## Do gems being combined require the same gem family?
  require-same-family: false
  ## Do gems being combined require the same gem level?
  require-same-level: false
```

<img alt="Example of Gem Group Combinations" src={useBaseUrl("img/allofanyofnoneof.png")} />;

</TabItem>
<TabItem value="5.1.0">

```yaml
version: 5.1.0
options:
  ## Should gems be prevented from being used in crafting? true is a recommended default.
  prevent-crafting-with-gems: true
  ## Should gems be prevented from adding prefixes/suffixes if an item already has one?
  prevent-multiple-name-changes-from-sockets: false
  ## When applying ON_HIT/ON_HIT_AND_WHEN_HIT gems, use the attacker's items in their hands?
  use-attacker-item-in-hand: true
  ## When applying ON_HIT/ON_HIT_AND_WHEN_HIT gems, use the attacker's equipped armor?
  use-attacker-armor-equipped: false
  ## When applying WHEN_HIT/ON_HIT_AND_WHEN_HIT gems, use the defender's items in their hands?
  use-defender-item-in-hand: false
  ## When applying WHEN_HIT/ON_HIT_AND_WHEN_HIT gems, use the defender's equipped armor?
  use-defender-armor-equipped: true
  ## Materials that can be used as socket gems.
  ## https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html
  socket-gem-material-ids:
    - DIAMOND
    - EMERALD
    - NETHER_STAR
  ## Default color for socket gem names when applied to items.
  ## https://hub.spigotmc.org/javadocs/spigot/org/bukkit/ChatColor.html
  default-socket-name-color-on-items: GOLD
  ## Should socket gem names on items attempt to use the display color
  ## from a tier before falling back to default-socket-name-color-on-items?
  use-tier-color-for-socket-name: true
items:
  socketed-item:
    ## Text to display for an open socket.
    ## %tiercolor% will use the display color of the tier of the item
    ## if available and default-socket-name-color-on-items if not.
    socket: "%tiercolor%(Socket)"
    ## Text to add to an item with an open socket.
    lore:
      - "&7Find a &6Socket Gem&7 to fill a &F%tiercolor%(Socket)&7"
  socket-gem:
    ## Display name of a socket gem.
    name: "&6Socket Gem - %socketgem%"
    ## Lore/description of a socket gem.
    ## Available placeholders:
    ## - %sockettypelore% - combination of all-of, any-of, none-of item group lore
    ## - %socketfamilylore% - shows gem family and level
    ## - %socketgemlore% - lore from the socket gem's configuration
    lore:
      - "%sockettypelore%"
      - "%socketfamilylore%"
      - ""
      - "&7Right-click while holding this gem"
      - "&7over another item in"
      - "&7your inventory to socket it!"
      - ""
      - "%socketgemlore%"
    ## Customize %socketfamilylore%
    family-lore:
      - "&7Family: &F%family% &7(&F%level%&7)"
    ## Customize prefix of %sockettypelore%
    socket-type-lore:
      - "&7Type(s): &F%type%"
    ## Customize second part of %sockettypelore%
    any-of-socket-type-lore:
      - "  &7Requires One Of: &F%type%"
    ## Customize first part of %sockettypelore%
    all-of-socket-type-lore:
      - "  &7Requires All: &F%type%"
    ## Customize third part of %sockettypelore%
    none-of-socket-type-lore:
      - "  &7Requires None: &F%type%"
  socket-gem-combiner:
    ## Name displayed in socket gem combiner GUI
    name: "&4Socket Gem Combiner"
    ## Item to show in non-clickable spots in socket gem combiner GUI
    buffer:
      name: "&aClick a &6Socket Gem &ato begin!"
      material: "IRON_BARS"
      lore: []
    ## Item to show when socket gems are able to be combined in GUI
    click-to-combine:
      name: "&E&LClick to combine!"
      material: "NETHER_STAR"
      lore: []
    ## Item to show when socket gems are not able to be combined in GUI
    ineligible-to-combine:
      name: "&C&LIneligible to combine!"
      material: "BARRIER"
      ## Message to show when unable to be combined due to not being in
      ## same socket gem family
      same-family-lore:
        - "&7You need to have four &6Socket Gems"
        - "&7from the same family"
        - "&7in order to combine them!"
      ## Message to show when unable to be combined due to not being in
      ## same socket gem level
      same-level-lore:
        - "&7You need to have four &6Socket Gems"
        - "&7from the same level"
        - "&7in order to combine them!"
      ## Message to show when unable to be combined due to not being in
      ## same socket gem family and level
      same-family-and-level-lore:
        - "&7You need to have four &6Socket Gems"
        - "&7from the same family and level"
        - "&7in order to combine them!"
      ## Message to show when unable to be combined due to not being able
      ## to find a gem based on the gems available.
      no-gem-found-lore:
        - "&7Unable to find a gem for that"
        - "&7combination of gems!"
combining:
  ## Do gems being combined require the same gem family?
  require-same-family: false
  ## Do gems being combined require the same gem level?
  require-same-level: false
```

<img alt="Example of Gem Group Combinations" src={useBaseUrl("img/allofanyofnoneof.png")} />;

</TabItem>
</Tabs>
