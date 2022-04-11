/*
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2019 Richard Harrah
 *
 * Permission is hereby granted, free of charge,
 * to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.tealcube.minecraft.bukkit.mythicdrops.items.builders

import com.google.common.base.Joiner
import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDrops
import com.tealcube.minecraft.bukkit.mythicdrops.api.attributes.MythicAttribute
import com.tealcube.minecraft.bukkit.mythicdrops.api.events.TieredItemGenerationEvent
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGenerationReason
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGroup
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGroupManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.builders.DropBuilder
import com.tealcube.minecraft.bukkit.mythicdrops.api.names.NameType
import com.tealcube.minecraft.bukkit.mythicdrops.api.relations.RelationManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SettingsManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.TierManager
import com.tealcube.minecraft.bukkit.mythicdrops.chatColorize
import com.tealcube.minecraft.bukkit.mythicdrops.getThenSetItemMetaAsDamageable
import com.tealcube.minecraft.bukkit.mythicdrops.merge
import com.tealcube.minecraft.bukkit.mythicdrops.names.NameMap
import com.tealcube.minecraft.bukkit.mythicdrops.replaceArgs
import com.tealcube.minecraft.bukkit.mythicdrops.replaceWithCollections
import com.tealcube.minecraft.bukkit.mythicdrops.safeRandom
import com.tealcube.minecraft.bukkit.mythicdrops.setDisplayNameChatColorized
import com.tealcube.minecraft.bukkit.mythicdrops.setLoreChatColorized
import com.tealcube.minecraft.bukkit.mythicdrops.setRepairCost
import com.tealcube.minecraft.bukkit.mythicdrops.stripColors
import com.tealcube.minecraft.bukkit.mythicdrops.utils.ChatColorUtil
import com.tealcube.minecraft.bukkit.mythicdrops.utils.ItemBuildingUtil
import com.tealcube.minecraft.bukkit.mythicdrops.utils.LeatherArmorUtil
import com.tealcube.minecraft.bukkit.mythicdrops.utils.TemplatingUtil
import io.pixeloutlaw.minecraft.spigot.mythicdrops.addAttributeModifier
import io.pixeloutlaw.minecraft.spigot.mythicdrops.displayName
import io.pixeloutlaw.minecraft.spigot.mythicdrops.getDurabilityInPercentageRange
import io.pixeloutlaw.minecraft.spigot.mythicdrops.getHighestEnchantment
import io.pixeloutlaw.minecraft.spigot.mythicdrops.getMaterials
import io.pixeloutlaw.minecraft.spigot.mythicdrops.isUnbreakable
import io.pixeloutlaw.minecraft.spigot.mythicdrops.itemFlags
import io.pixeloutlaw.minecraft.spigot.mythicdrops.mythicDropsTier
import io.pixeloutlaw.minecraft.spigot.mythicdrops.setPersistentDataString
import io.pixeloutlaw.minecraft.spigot.mythicdrops.toTitleCase
import io.pixeloutlaw.minecraft.spigot.plumbing.lib.ItemAttributes
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import java.util.Locale

internal class MythicDropBuilder(
    private val itemGroupManager: ItemGroupManager,
    private val relationManager: RelationManager,
    private val settingsManager: SettingsManager,
    private val tierManager: TierManager
) : DropBuilder {
    companion object {
        private val newlineRegex = "/n".toRegex()
        private val spaceRegex = " ".toRegex()
    }

    @Deprecated(
        "Get via MythicDropsApi instead",
        ReplaceWith(
            "MythicDropsApi.mythicDrops.productionLine.tieredItemFactory.getNewDropBuilder()",
            "com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDropsApi"
        )
    )
    constructor(mythicDrops: MythicDrops) : this(
        mythicDrops.itemGroupManager,
        mythicDrops.relationManager,
        mythicDrops.settingsManager,
        mythicDrops.tierManager
    )

    private var tier: Tier? = null
    private var material: Material? = null
    private var itemGenerationReason: ItemGenerationReason = ItemGenerationReason.DEFAULT
    private var useDurability: Boolean = false

    override fun withTier(tier: Tier?): DropBuilder {
        this.tier = tier
        return this
    }

    override fun withMaterial(material: Material?): DropBuilder {
        this.material = material
        return this
    }

    override fun withItemGenerationReason(reason: ItemGenerationReason?): DropBuilder {
        this.itemGenerationReason = reason ?: ItemGenerationReason.DEFAULT
        return this
    }

    override fun useDurability(b: Boolean): DropBuilder {
        this.useDurability = b
        return this
    }

    override fun build(): ItemStack? {
        val chosenTier = tier ?: tierManager.randomByWeight() ?: return null
        val materialsFromTier = chosenTier.getMaterials()
        val materialFromTier = if (materialsFromTier.isNotEmpty()) {
            materialsFromTier.random()
        } else {
            null
        }
        val chosenMat = material ?: materialFromTier ?: return null

        val itemStack = if (settingsManager.configSettings.options.isDisableDefaultTieredItemAttributes) {
            ItemStack(chosenMat, 1)
        } else {
            ItemAttributes.cloneWithDefaultAttributes(ItemStack(chosenMat, 1))
        }
        if (chosenTier.repairCost > 0) {
            itemStack.setRepairCost(chosenTier.repairCost)
        }

        if (useDurability) {
            val durability = chosenMat.getDurabilityInPercentageRange(
                chosenTier.minimumDurabilityPercentage,
                chosenTier.maximumDurabilityPercentage
            )
            itemStack.getThenSetItemMetaAsDamageable { this.damage = durability }
        }

        itemStack.isUnbreakable = chosenTier.isUnbreakable

        val enchantmentName = getEnchantmentTypeName(itemStack)

        val name = generateName(itemStack, chosenTier, enchantmentName)
        itemStack.setDisplayNameChatColorized(name)
        val lore = generateLore(itemStack, chosenTier, enchantmentName)
        itemStack.setLoreChatColorized(lore)

        val baseEnchantments = ItemBuildingUtil.getBaseEnchantments(itemStack, chosenTier)
        val bonusEnchantments = ItemBuildingUtil.getBonusEnchantments(itemStack, chosenTier)
        val relationEnchantments = ItemBuildingUtil.getRelationEnchantments(itemStack, chosenTier, relationManager)
        val baseAttributes = ItemBuildingUtil.getBaseAttributeModifiers(chosenTier)
        val bonusAttributes = ItemBuildingUtil.getBonusAttributeModifiers(chosenTier)
        val relationAttributes = getRelationAttributes(name.chatColorize())

        itemStack.addUnsafeEnchantments(baseEnchantments.merge(bonusEnchantments).merge(relationEnchantments))
        baseAttributes.forEach { attribute, attributeModifier ->
            itemStack.addAttributeModifier(
                attribute,
                attributeModifier
            )
        }
        bonusAttributes.forEach { attribute, attributeModifier ->
            itemStack.addAttributeModifier(
                attribute,
                attributeModifier
            )
        }
        relationAttributes.map { it.toAttributeModifier() }.forEach { (attribute, attributeModifier) ->
            itemStack.addAttributeModifier(attribute, attributeModifier)
        }

        if (settingsManager.configSettings.options.isRandomizeLeatherColors) {
            LeatherArmorUtil.setRandomizedColor(itemStack)
        }

        itemStack.setPersistentDataString(mythicDropsTier, chosenTier.name)
        itemStack.itemFlags = chosenTier.itemFlags

        val randomItemGenerationEvent = TieredItemGenerationEvent(chosenTier, itemStack, itemGenerationReason)
        Bukkit.getPluginManager().callEvent(randomItemGenerationEvent)
        if (randomItemGenerationEvent.isCancelled) {
            return null
        }
        return randomItemGenerationEvent.itemStack
    }

    private fun getItemGroup(itemStack: ItemStack, filter: (itemGroup: ItemGroup) -> Boolean = { true }): ItemGroup? =
        itemGroupManager.getMatchingItemGroups(itemStack.type).filter { itemGroup -> itemGroup.priority >= 0 }
            .filter(filter).shuffled().minByOrNull { itemGroup -> itemGroup.priority }

    private fun generateLore(
        itemStack: ItemStack,
        chosenTier: Tier,
        enchantmentName: String
    ): List<String> {
        val tooltipFormat = chosenTier.tooltipFormat ?: settingsManager.configSettings.display.tooltipFormat

        val minecraftName = getMinecraftMaterialName(itemStack.type)
        val mythicName = getMythicMaterialName(itemStack.type)
        val generalLoreString = NameMap.getRandom(NameType.GENERAL_LORE, "")
        val materialLoreString = NameMap
            .getRandom(NameType.MATERIAL_LORE, itemStack.type.name.lowercase(Locale.getDefault()))
        val tierLoreString = NameMap.getRandom(NameType.TIER_LORE, chosenTier.name.lowercase(Locale.getDefault()))
        val enchantmentLoreString = NameMap.getRandom(
            NameType.ENCHANTMENT_LORE, enchantmentName.lowercase(Locale.getDefault())
        )

        val itemGroupForLore = getItemGroup(itemStack) {
            NameMap.getMatchingKeys(NameType.ITEMTYPE_LORE)
                .map { key -> key.removePrefix(NameType.ITEMTYPE_LORE.format) }.contains(it.name)
        }

        val itemTypeLoreString = NameMap
            .getRandom(NameType.ITEMTYPE_LORE, itemGroupForLore?.name?.lowercase(Locale.getDefault()) ?: "")

        val generalLore =
            generalLoreString.split(newlineRegex).dropLastWhile { it.isEmpty() }
        val materialLore =
            materialLoreString.split(newlineRegex).dropLastWhile { it.isEmpty() }
        val tierLore =
            tierLoreString.split(newlineRegex).dropLastWhile { it.isEmpty() }
        val enchantmentLore =
            enchantmentLoreString.split(newlineRegex).dropLastWhile { it.isEmpty() }
        val itemTypeLore = itemTypeLoreString.split(newlineRegex).dropLastWhile { it.isEmpty() }

        val baseLore = chosenTier.baseLore.flatMap { lineOfLore ->
            lineOfLore.split(newlineRegex).dropLastWhile { it.isEmpty() }
        }

        val numOfBonusLore = (chosenTier.minimumBonusLore..chosenTier.maximumBonusLore).safeRandom()
        val bonusLore = mutableListOf<String>()
        repeat(numOfBonusLore) {
            val availableBonusLore = chosenTier.bonusLore.filter { !bonusLore.contains(it) }
            if (availableBonusLore.isNotEmpty()) {
                val selectedBonusLore = availableBonusLore.random()
                bonusLore.addAll(selectedBonusLore.split(newlineRegex).dropLastWhile { it.isEmpty() })
            }
        }

        val (socketGemLore, socketableLore, socketLore) = generateSocketLore(chosenTier)

        val displayName = itemStack.displayName
        val relationLore = displayName?.let { name ->
            name.stripColors().split(spaceRegex).dropLastWhile { it.isEmpty() }
                .mapNotNull { relationManager.getById(it) }.flatMap { it.lore }
        } ?: emptyList()

        val args = listOf(
            "%basematerial%" to minecraftName,
            "%mythicmaterial%" to mythicName,
            "%tiername%" to chosenTier.displayName,
            "%enchantment%" to enchantmentName,
            "%tiercolor%" to ChatColorUtil.getFirstColors(chosenTier.itemDisplayNameFormat.chatColorize()),
            "%itemtype%" to (itemGroupForLore?.name ?: "")
        )

        return tooltipFormat.replaceWithCollections(
            "%baselore%" to baseLore,
            "%bonuslore%" to bonusLore,
            "%socketgemlore%" to socketGemLore,
            "%socketablelore%" to socketableLore,
            "%socketlore%" to socketLore,
            "%generallore%" to generalLore,
            "%tierlore%" to tierLore,
            "%materiallore%" to materialLore,
            "%enchantmentlore%" to enchantmentLore,
            "%relationlore%" to relationLore,
            "%itemtypelore%" to itemTypeLore
        ).replaceArgs(args).map { TemplatingUtil.template(it) }
    }

    private fun generateSocketLore(chosenTier: Tier): Triple<List<String>, List<String>, List<String>> {
        if (!settingsManager.configSettings.components.isSocketingEnabled) {
            return Triple(emptyList(), emptyList(), emptyList())
        }

        val socketGemLore = mutableListOf<String>()
        val socketableLore = mutableListOf<String>()
        if (Math.random() < chosenTier.chanceToHaveSockets) {
            val numberOfSockets = (chosenTier.minimumSockets..chosenTier.maximumSockets).safeRandom()
            if (numberOfSockets > 0) {
                for (i in 0 until numberOfSockets) {
                    val line = settingsManager.socketingSettings.items.socketedItem.socket
                    socketGemLore.add(line)
                }
                socketableLore.addAll(
                    settingsManager.socketingSettings.items.socketedItem.lore
                )
            }
        }

        if (Math.random() < chosenTier.chanceToHaveSocketExtenderSlots) {
            val numberOfSocketExtenderSlots =
                (chosenTier.minimumSocketExtenderSlots..chosenTier.maximumSocketExtenderSlots).safeRandom()
            if (numberOfSocketExtenderSlots > 0) {
                for (i in 0 until numberOfSocketExtenderSlots) {
                    val line = settingsManager.socketingSettings.items.socketExtender.slot
                    socketGemLore.add(line)
                }
            }
        }

        val socketLore = socketGemLore.toMutableList()
        socketLore.addAll(socketableLore)
        return Triple(socketGemLore, socketableLore, socketLore)
    }

    private fun generateName(
        itemStack: ItemStack,
        chosenTier: Tier,
        enchantmentName: String
    ): String {
        val format = chosenTier.itemDisplayNameFormat
        if (format.isEmpty()) {
            return "Mythic Item"
        }
        val minecraftName = getMinecraftMaterialName(itemStack.type)
        val mythicName = getMythicMaterialName(itemStack.type)
        val generalPrefix = NameMap.getRandom(NameType.GENERAL_PREFIX, "")
        val generalSuffix = NameMap.getRandom(NameType.GENERAL_SUFFIX, "")
        val materialPrefix = NameMap
            .getRandom(NameType.MATERIAL_PREFIX, itemStack.type.name.lowercase(Locale.getDefault()))
        val materialSuffix = NameMap
            .getRandom(NameType.MATERIAL_SUFFIX, itemStack.type.name.lowercase(Locale.getDefault()))
        val tierPrefix = NameMap.getRandom(NameType.TIER_PREFIX, chosenTier.name.lowercase(Locale.getDefault()))
        val tierSuffix = NameMap.getRandom(NameType.TIER_SUFFIX, chosenTier.name.lowercase(Locale.getDefault()))
        val highestEnch = itemStack.getHighestEnchantment()
        val enchantmentPrefix = getEnchantmentPrefix(highestEnch)
        val enchantmentSuffix = getEnchantmentSuffix(highestEnch)

        val itemGroupForPrefix = getItemGroup(itemStack) {
            NameMap.getMatchingKeys(NameType.ITEMTYPE_PREFIX)
                .map { key -> key.removePrefix(NameType.ITEMTYPE_PREFIX.format) }.contains(it.name)
        }
        val itemGroupForSuffix = getItemGroup(itemStack) {
            NameMap.getMatchingKeys(NameType.ITEMTYPE_SUFFIX)
                .map { key -> key.removePrefix(NameType.ITEMTYPE_SUFFIX.format) }.contains(it.name)
        }

        val itemTypePrefix =
            NameMap.getRandom(NameType.ITEMTYPE_PREFIX, itemGroupForPrefix?.name?.lowercase(Locale.getDefault()) ?: "")
        val itemTypeSuffix =
            NameMap.getRandom(NameType.ITEMTYPE_SUFFIX, itemGroupForSuffix?.name?.lowercase(Locale.getDefault()) ?: "")

        val args = listOf(
            "%basematerial%" to minecraftName,
            "%mythicmaterial%" to mythicName,
            "%generalprefix%" to generalPrefix,
            "%generalsuffix%" to generalSuffix,
            "%materialprefix%" to materialPrefix,
            "%materialsuffix%" to materialSuffix,
            "%itemtypeprefix%" to itemTypePrefix,
            "%itemtypesuffix%" to itemTypeSuffix,
            "%tierprefix%" to tierPrefix,
            "%tiersuffix%" to tierSuffix,
            "%tiername%" to chosenTier.displayName,
            "%enchantment%" to enchantmentName,
            "%enchantmentprefix%" to enchantmentPrefix,
            "%enchantmentsuffix%" to enchantmentSuffix
        )

        return format.replaceArgs(args).trim()
    }

    @Suppress("DEPRECATION")
    private fun getEnchantmentSuffix(highestEnch: Enchantment?): String {
        return NameMap.getRandom(
            NameType.ENCHANTMENT_SUFFIX,
            highestEnch?.key?.toString()?.lowercase(Locale.getDefault()) ?: ""
        )
    }

    @Suppress("DEPRECATION")
    private fun getEnchantmentPrefix(highestEnch: Enchantment?): String {
        return NameMap.getRandom(
            NameType.ENCHANTMENT_PREFIX,
            highestEnch?.key?.toString()?.lowercase(Locale.getDefault()) ?: ""
        )
    }

    private fun getMythicMaterialName(type: Material): String {
        val comb = type.name
        var mythicMatName: String? = settingsManager.languageSettings.displayNames[comb]
        if (mythicMatName == null || mythicMatName == comb) {
            mythicMatName = getMinecraftMaterialName(type)
        }
        return mythicMatName.toTitleCase()
    }

    private fun getMinecraftMaterialName(type: Material): String {
        val matName = type.name
        val split = matName.split("_".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val prettyMaterialName = Joiner.on(" ").skipNulls().join(split)
        return prettyMaterialName.toTitleCase()
    }

    private fun getEnchantmentTypeName(itemStack: ItemStack): String {
        val enchantment = itemStack.getHighestEnchantment()
            ?: return settingsManager.languageSettings.displayNames
                .getOrDefault("Ordinary", "Ordinary")
                .chatColorize()

        @Suppress("DEPRECATION")
        val ench = try {
            settingsManager.languageSettings.displayNames[enchantment.key.key]
                ?: settingsManager.languageSettings.displayNames[enchantment.name]
        } catch (throwable: Throwable) {
            settingsManager.languageSettings.displayNames[enchantment.name]
        }
        return ench ?: "Ordinary"
    }

    private fun getRelationAttributes(name: String): List<MythicAttribute> {
        return name.stripColors().split(spaceRegex).dropLastWhile { it.isEmpty() }
            .mapNotNull { relationManager.getById(it) }.flatMap { it.attributes }
    }
}
