# Aura Gems

## Use Case

Aura gems are gems that can be used to provide continuous buffs or debuffs. These buffs or debuffs can be applied to the
player with the gem or to other entities nearby.

Auras replenish every 30s.

## Buff Self Gem

Below is an example of a Socket Gem that applies a [potion effect] that grants increased damage to the wielding player.

```yaml
Enraging I:
  trigger-type: AURA
  all-of-item-groups:
    - armor
  potion-effects:
    INCREASE_DAMAGE:
      intensity: 0
      duration: 30000
      affects-wielder: true
  weight: 400
  prefix: Enraging
  lore:
    - "&4Increases damage dealt while item is equipped."
  family: Rage
  level: 1
```

## Buff Others Gem

Below is an example of a Socket Gem that applies a [potion effect] that grants increased damage to other players, but
not the wielding player.

```yaml
Enraging I:
  trigger-type: AURA
  all-of-item-groups:
    - armor
  potion-effects:
    INCREASE_DAMAGE:
      intensity: 0
      duration: 30000
      affects-wielder: false
      affects-target: true
  weight: 400
  prefix: Enraging
  lore:
    - "&4Increases damage dealt by others while item is equipped."
  family: Rage
  level: 1
```

## Debuff Self Gem

Below is an example of a Socket Gem that applies a [potion effect] that applies confusion to the wielding player.

```yaml
Confusion I:
  trigger-type: AURA
  all-of-item-groups:
    - armor
  potion-effects:
    CONFUSION:
      intensity: 0
      duration: 30000
      affects-wielder: true
  weight: 400
  prefix: Confusing
  lore:
    - "&4Confuses the wielder while item is equipped."
  family: Psychic
  level: 1
```

## Debuff Others Gem

Below is an example of a Socket Gem that applies a [potion effect] that applies confusion to other players, but not the
wielding player.

```yaml
Confusing I:
  trigger-type: AURA
  all-of-item-groups:
    - armor
  potion-effects:
    CONFUSION:
      intensity: 0
      duration: 30000
      affects-wielder: false
      affects-target: true
  weight: 400
  prefix: Confusing
  lore:
    - "&4Confuses other nearby players while item is equipped."
  family: Psychic
  level: 1
```

[potion effect]: https://hub.spigotmc.org/javadocs/spigot/org/bukkit/potion/PotionEffectType.html
