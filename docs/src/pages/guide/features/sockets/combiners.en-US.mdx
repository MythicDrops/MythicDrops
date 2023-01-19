# Socket Gem Combiners

Socket Gem Combiners allow players to sacrifice [Socket Gem](/guide/features/sockets/gems)s for the chance at a
better [Socket Gem](/guide/features/sockets/gems).

## How They Work

Each socket gem has a [`family` and `level` field](/config/socketGems.yml.html). As mentioned above in
[Configuration](#configuration), there are configurable options for how these fields are used. If `require-same-family`
is set to `true`, each sacrificed gem must have the same `family` value. If `require-same-level`
is set to `true`, each sacrificed gem must have the same `level` value.

Those values have effects on how the result socket gem is calculated as well.

### No Requirements

If both `require-same-family` and `require-same-level` are `false`, then the resulting gem will be a random socket gem
with a level one higher than the rounded-down average of the sacrificed gems.

For instance, if you sacrifice a level 1 gem, a level 2 gem, a level 3 gem, and a level 4 gem, you will receive a level
3 gem, as the average level (rounded down) is 2.

### Same Family Requirement

If `require-same-family` is `true` and `require-same-level` is `false`, then the resulting gem will be a random socket
gem from the matching family with a level one higher than the rounded-down average of the sacrificed gems.

For instance, if you sacrifice a level 1 gem, a level 2 gem, a level 3 gem, and a level 4 gem, you will receive a level
3 gem from the same family, as the average level (rounded down) is 2.

### Same Level Requirement

If `require-same-level` is `true` and `require-same-family` is `false`, then the resulting gem will be a random socket
gem with a level one higher than the sacrificed gems.

For instance, if you sacrifice 4 level 2 gems, you will receive a level 3 gem.

### Same Family and Same Level Requirement

If `require-same-family` is `true` and `require-same-level` is `true`, then the resulting gem will be a random socket
gem from the matching family with a level one higher than the sacrificed gems.

For instance, if you sacrifice 4 level 2 gems, you will receive a level 3 gem from the same family, as the average
level (rounded down) is 2.

## Usage

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
the [socketing.yml](/config/socketing.yml.html).

The most relevant for functionality is the `combining` section at the bottom that looks something like this:

```yaml
combining:
  ## Do gems being combined require the same gem family?
  require-same-family: false
  ## Do gems being combined require the same gem level?
  require-same-level: false
```
