---
title: Weight
description: Weight based RNG
---

MythicDrops uses a weight-based randomization system for determining tier drops, socket gem drops, and custom item
drops.

## Explanation

Weight based systems make it easier to expand upon items available in a randomized system. Instead of having to ensure
that each item's chance to be chosen all adds up to `1.0` (100%), weight based systems allow each item to contribute a
value to a total and each item therefore has an automatically determinable "chance" to be chosen.

If you have an item with a weight of 20 and all the items in the system have their weight add up to 200, the
aforementioned item has a 10% chance to be chosen.

This does have the side effect of making it a bit tedious to enforce that a specific item in the system has a static
percentage chance to be chosen. You end up having to do a bit of math based off of the weight of all of your items.

Ultimately, however, the weight based system is easier to expand and add more items to, as it will automatically
re-balance the percentage chances of the existing items in the system.

## Example

Think of it like a deck of cards.

If you have a suit of cards, Ace - King, and assign each of them a weight equal to their face value (1 - 13), you have a
total weight of 91.

You pick a random number between 0 and 91. Let's say it's 14.

You shuffle the cards from the suit and begin to deal them one at a time, adding each card to the previous cards you
dealt. If the total is 14 or greater, you choose the card you just dealt. Example of the series:

You flip over 4. Your total is 4, which is not greater than or equal to 14. You flip over 2. Your total is 6, which is
not greater than or equal to 14. You flip over 10. Your total is 16, which is greater than 14, so you choose 10.
