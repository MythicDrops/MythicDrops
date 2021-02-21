---
id: relation_yml
title: relation.yml
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

## Configuration

MythicDrops has a lot of configuration options. Below is the contents of the
relation.yml with inline explanations of what each configuration option does.

<Tabs
defaultValue="1.0.0"
values={[
{ label: '1.0.0 (MD 6.3.x)', value: '1.0.0', },
{ label: '0.0.1 (MD 6.0.x)', value: '0.0.1', }
]
}>
<TabItem value="1.0.0">

```yaml
version: "1.0.0"
attributed:
  attributes:
    exampleattributes:
      ## Attribute for this particular modifier. Attribute names here:
      ## https://hub.spigotmc.org/javadocs/spigot/org/bukkit/attribute/Attribute.html
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
binding:
  enchantments:
    BINDING_CURSE:
      minimum_level: 1
      maximum_level: 1
glacier:
  lore:
    - "&bSpeed: -30%"
```

</TabItem>
<TabItem value="0.0.1">

```yaml
version: "0.0.1"
## Relations are incredibly simplified sets.
## Any randomized item that has one of the below keys
## in its display name will spawn with an extra line of lore,
## assuming that "%relationlore%" is available in the tooltip-format
## in config.yml.
##
## In this example, an item named "&BSlow Glacier" would spawn with
## an extra line of lore "&bSpeed: -30%"
glacier:
  - "&bSpeed: -30%"
```

</TabItem>
</Tabs>
