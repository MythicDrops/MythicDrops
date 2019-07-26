/*
 * The MIT License
 * Copyright © 2013 Richard Harrah
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.tealcube.minecraft.bukkit.mythicdrops.utils

import org.bukkit.Material

object DefaultItemGroups {
    // Weapons
    val sword = listOf(
        Material.WOODEN_SWORD,
        Material.STONE_SWORD,
        Material.IRON_SWORD,
        Material.GOLDEN_SWORD,
        Material.DIAMOND_SWORD
    )
    val axe = listOf(
        Material.WOODEN_AXE,
        Material.STONE_AXE,
        Material.IRON_AXE,
        Material.GOLDEN_AXE,
        Material.DIAMOND_AXE
    )
    val pickaxe = listOf(
        Material.WOODEN_PICKAXE,
        Material.STONE_PICKAXE,
        Material.IRON_PICKAXE,
        Material.GOLDEN_PICKAXE,
        Material.DIAMOND_PICKAXE
    )
    val shovel = listOf(
        Material.WOODEN_SHOVEL,
        Material.STONE_SHOVEL,
        Material.IRON_SHOVEL,
        Material.GOLDEN_SHOVEL,
        Material.DIAMOND_SHOVEL
    )
    val hoe = listOf(
        Material.WOODEN_HOE,
        Material.STONE_HOE,
        Material.IRON_HOE,
        Material.GOLDEN_HOE,
        Material.DIAMOND_HOE
    )
    val shears = listOf(
        Material.SHEARS
    )
    val bow = listOf(
        Material.BOW,
        Material.CROSSBOW
    )
    val fishingRod = listOf(
        Material.FISHING_ROD
    )
    val arrows = listOf(
        Material.ARROW,
        Material.SPECTRAL_ARROW,
        Material.TIPPED_ARROW
    )
    val trident = listOf(
        Material.TRIDENT
    )
    val melee = sword + axe + pickaxe + shovel + hoe + shears + trident
    val ranged = bow + fishingRod + arrows + trident
    val weapons = sword + axe + pickaxe + shovel + hoe + shears + trident + bow + fishingRod + arrows

    // Armor
    val helmet = listOf(
        Material.LEATHER_HELMET,
        Material.CHAINMAIL_HELMET,
        Material.IRON_HELMET,
        Material.GOLDEN_HELMET,
        Material.DIAMOND_HELMET
    )
    val chestplate = listOf(
        Material.LEATHER_CHESTPLATE,
        Material.CHAINMAIL_CHESTPLATE,
        Material.IRON_CHESTPLATE,
        Material.GOLDEN_CHESTPLATE,
        Material.DIAMOND_CHESTPLATE
    )
    val leggings = listOf(
        Material.LEATHER_LEGGINGS,
        Material.CHAINMAIL_LEGGINGS,
        Material.IRON_LEGGINGS,
        Material.GOLDEN_LEGGINGS,
        Material.DIAMOND_LEGGINGS
    )
    val boots = listOf(
        Material.LEATHER_BOOTS,
        Material.CHAINMAIL_BOOTS,
        Material.IRON_BOOTS,
        Material.GOLDEN_BOOTS,
        Material.DIAMOND_BOOTS
    )
    val shield = listOf(
        Material.SHIELD
    )
    val elytra = listOf(
        Material.ELYTRA
    )
    val armor = helmet + chestplate + leggings + boots

    // Materials
    val wood = listOf(
        Material.WOODEN_AXE,
        Material.WOODEN_HOE,
        Material.WOODEN_PICKAXE,
        Material.WOODEN_SHOVEL,
        Material.WOODEN_SWORD,
        Material.BOW,
        Material.CROSSBOW,
        Material.FISHING_ROD
    )
    val wooden = wood
    val stone = listOf(
        Material.STONE_AXE,
        Material.STONE_PICKAXE,
        Material.STONE_HOE,
        Material.STONE_SWORD,
        Material.STONE_SHOVEL
    )
    val leather = listOf(
        Material.LEATHER_BOOTS,
        Material.LEATHER_CHESTPLATE,
        Material.LEATHER_HELMET,
        Material.LEATHER_LEGGINGS
    )
    val chainmail = listOf(
        Material.CHAINMAIL_BOOTS,
        Material.CHAINMAIL_CHESTPLATE,
        Material.CHAINMAIL_HELMET,
        Material.CHAINMAIL_LEGGINGS
    )
    val iron = listOf(
        Material.IRON_AXE,
        Material.IRON_BOOTS,
        Material.IRON_CHESTPLATE,
        Material.IRON_HELMET,
        Material.IRON_LEGGINGS,
        Material.IRON_PICKAXE,
        Material.IRON_HOE,
        Material.IRON_SHOVEL,
        Material.IRON_SWORD
    )
    val gold = listOf(
        Material.GOLDEN_AXE,
        Material.GOLDEN_BOOTS,
        Material.GOLDEN_CHESTPLATE,
        Material.GOLDEN_HELMET,
        Material.GOLDEN_LEGGINGS,
        Material.GOLDEN_PICKAXE,
        Material.GOLDEN_HOE,
        Material.GOLDEN_SHOVEL,
        Material.GOLDEN_SWORD
    )
    val diamond = listOf(
        Material.DIAMOND_AXE,
        Material.DIAMOND_BOOTS,
        Material.DIAMOND_CHESTPLATE,
        Material.DIAMOND_HELMET,
        Material.DIAMOND_HOE,
        Material.DIAMOND_LEGGINGS,
        Material.DIAMOND_PICKAXE,
        Material.DIAMOND_SHOVEL,
        Material.DIAMOND_SWORD
    )

    val groups = mapOf(
        "sword" to sword,
        "axe" to axe,
        "pickaxe" to pickaxe,
        "shovel" to shovel,
        "hoe" to hoe,
        "shears" to shears,
        "bow" to bow,
        "fishing rod" to fishingRod,
        "arrow" to arrows,
        "trident" to trident,
        "melee" to melee,
        "ranged" to ranged,
        "weapon" to weapons,
        "helmet" to helmet,
        "chestplate" to chestplate,
        "leggings" to leggings,
        "boots" to boots,
        "armor" to armor,
        "shield" to shield,
        "elytra" to elytra,
        "wood" to wood,
        "wooden" to wooden,
        "stone" to stone,
        "leather" to leather,
        "gold" to gold,
        "iron" to iron,
        "diamond" to diamond
    )
}