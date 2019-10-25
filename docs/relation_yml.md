---
id: relation_yml
title: relation.yml
---

## Configuration

MythicDrops has a lot of configuration options. Below is the contents of the
relation.yml with inline explanations of what each configuration option does.

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
