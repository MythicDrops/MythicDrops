# identifying.yml

MythicDrops has a lot of configuration options. Below is the contents of the identifying.yml with inline explanations of
what each configuration option does.

```yaml
version: 5.2.0
options:
  ## Should unidentified items respect the tier drops of the mob that they
  ## originally dropped from? Does not work in 6.0.0. See
  ## https://github.com/PixelOutlaw/MythicDrops/issues/396
  ##
  ## Will require the use of "%droppedby%" or "%allowabletiers%" from below.
  unidentified-items-respect-tier-drops: true
  ## What type of click is used for identifying an item?
  ## https://hub.spigotmc.org/javadocs/spigot/org/bukkit/event/inventory/ClickType.html
  click-type-to-identify: RIGHT
items:
  identity-tome:
    ## Name of the identity tome item.
    name: "&5Identity Tome"
    ## Description of the identity tome item.
    lore:
      - "&7Right-click while holding this tome"
      - "&7over an &dUnidentified Item"
      - "&7to identify it!"
    ## Material of the identity tome item.
    material: ENCHANTED_BOOK
  unidentified-item:
    ## Name of the unidentified item.
    name: "&dUnidentified Item"
    ## Description of the unidentified item.
    ##
    ## You can use the following variables in the lore of an unidentified item:
    ## %droppedby% - composed of `dropped-by-prefix`, then the mob type, then `dropped-by-suffix`
    ## %allowabletiers% - composed of `allowable-tiers-prefix`, then allowable tiers separated by
    ##                    `allowable-tiers-separator`, then `allowable-tiers-suffix`
    ## %tier% - composed of `tier-prefix`, then the tier name, then `tier-suffix`
    lore:
      - "&7Find an &5Identity Tome &7to"
      - "&7identify this item!"
      - ""
      - "%droppedby%"
    ## Used to compose the "%allowabletiers%" variable above.
    allowable-tiers-prefix: "&7Tiers: (&F"
    ## Used to compose the "%allowabletiers%" variable above.
    allowable-tiers-separator: "&7, &F"
    ## Used to compose the "%allowabletiers%" variable above.
    allowable-tiers-suffix: "&7)"
    ## Used to compose the "%droppedby%" variable above.
    dropped-by-prefix: "&7Dropped by: &F"
    ## Used to compose the "%droppedby%" variable above.
    dropped-by-suffix: ""
    ## Used to compose the "%tier%" variable above.
    tier-prefix: "&7Tier: &F"
    ## Used to compose the "%tier%" variable above.
    tier-suffix: ""
```
