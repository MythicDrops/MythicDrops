---
title: Integrations
descriptions: Integration with other plugins
---

import { Aside } from "@astrojs/starlight/components";

MythicDrops has integrations with a number of other Spigot plugins.

## WorldGuard

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

# HeadDatabase

Custom items support HeadDatabase with the `hdb-id` key in a custom item. Here's an example:

```yaml
version: 5.4.0
globehead:
  material: PLAYER_HEAD
  "display-name": "Globe!"
  "hdb-id": "93764"
```
