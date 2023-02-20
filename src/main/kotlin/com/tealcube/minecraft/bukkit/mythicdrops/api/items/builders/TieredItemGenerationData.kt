package com.tealcube.minecraft.bukkit.mythicdrops.api.items.builders

import com.google.common.collect.Multimap
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketExtenderType
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketType
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment

interface TieredItemGenerationData {
    val tier: Tier
    val material: Material
    val expRepairCost: Int
    val durability: Int?
    val isUnbreakable: Boolean
    val openSockets: List<SocketType>
    val socketExtenderSlots: List<SocketExtenderType>
    val baseEnchantments: Map<Enchantment, Int>
    val bonusEnchantments: Map<Enchantment, Int>
    val baseAttributes: Multimap<Attribute, AttributeModifier>
    val bonusAttributes: Multimap<Attribute, AttributeModifier>
}
