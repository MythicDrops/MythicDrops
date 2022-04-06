# repairCosts.yml

MythicDrops has a lot of configuration options. Below is the shortened contents of the repairCosts.yml with inline
explanations of what each configuration option does.

```yaml
version: 1.0.0
## Identifier for an item that can be repaired. Needs to be unique.
## Does NOT have to be anything sensible, it can be gibberish like
## asijsoiadsnansga.
wooden-axe:
  ## Name of the material of the item that can be repaired.
  ## https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html
  material-name: WOODEN_AXE
  ## You can specify an item name to create custom repair costs for items
  ## with a given name. This defaults to being undefined and that's usually
  ## fine for most usages.
  item-name: Example
  ## Behaves identically to item-name, but with the item's lore instead.
  item-lore:
    - Example
  ## Costs to repair this item.
  costs:
    ## Identifier for a repair cost. Needs to be unique per costs section.
    ## You can have multiple.
    ## Does NOT have to be anything sensible, it can be gibberish like
    ## asijsoiadsnansga.
    default:
      ## Name of the material of the item that is required to do the repairing.
      ## https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html
      material-name: OAK_WOOD
      ## Priority of this particular cost. If you have multiple matching costs
      ## in your inventory, the one with the highest valued priority wins (1 > 0).
      priority: 0
      ## How many of this item are required to perform the repair?
      amount: 1
      ## How much experience is required to perform the repair?
      experience-cost: 0
      ## What percentage of the item's durability should be repaired?
      repair-per-cost: 0.1
      ## The item used to perform the repair must have a name matching this
      ## value if it is defined.
      ## This defaults to being undefined and that's usually
      ## fine for most usages.
      item-name: Example
      ## Behaves identically to item-name, but with the item's lore instead.
      item-lore:
        - Example
      ## Behaves identically to item-name, but with the item's enchantments instead.
      enchantments:
        MENDING: 1
# other repair costs down here...
```
