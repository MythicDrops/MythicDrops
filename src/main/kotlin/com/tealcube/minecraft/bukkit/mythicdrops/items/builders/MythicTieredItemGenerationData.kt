package com.tealcube.minecraft.bukkit.mythicdrops.items.builders

import com.google.common.collect.Multimap
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.builders.TieredItemGenerationData
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketExtenderType
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketType
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment

internal data class MythicTieredItemGenerationData(
    override val tier: Tier,
    override val material: Material,
    override val expRepairCost: Int,
    override val durability: Int?,
    override val isUnbreakable: Boolean,
    override val openSockets: List<SocketType>,
    override val socketExtenderSlots: List<SocketExtenderType>,
    override val baseEnchantments: Map<Enchantment, Int>,
    override val bonusEnchantments: Map<Enchantment, Int>,
    override val baseAttributes: Multimap<Attribute, AttributeModifier>,
    override val bonusAttributes: Multimap<Attribute, AttributeModifier>
): TieredItemGenerationData
