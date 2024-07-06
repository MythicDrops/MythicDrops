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
@file:Suppress("ktlint:standard:annotation")

package com.tealcube.minecraft.bukkit.mythicdrops.items.builders

import com.google.common.base.Joiner
import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDrops
import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDropsApi
import com.tealcube.minecraft.bukkit.mythicdrops.api.attributes.MythicAttribute
import com.tealcube.minecraft.bukkit.mythicdrops.api.events.PreTieredItemGenerationEvent
import com.tealcube.minecraft.bukkit.mythicdrops.api.events.TieredItemGenerationEvent
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGenerationReason
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGroup
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGroupManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.builders.DropBuilder
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.builders.TieredItemGenerationData
import com.tealcube.minecraft.bukkit.mythicdrops.api.names.NameType
import com.tealcube.minecraft.bukkit.mythicdrops.api.relations.RelationManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SettingsManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketExtenderTypeManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketTypeManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.TierManager
import com.tealcube.minecraft.bukkit.mythicdrops.chatColorize
import com.tealcube.minecraft.bukkit.mythicdrops.getThenSetItemMetaAsDamageable
import com.tealcube.minecraft.bukkit.mythicdrops.highestByValue
import com.tealcube.minecraft.bukkit.mythicdrops.merge
import com.tealcube.minecraft.bukkit.mythicdrops.names.NameMap
import com.tealcube.minecraft.bukkit.mythicdrops.replaceArgs
import com.tealcube.minecraft.bukkit.mythicdrops.replaceWithCollections
import com.tealcube.minecraft.bukkit.mythicdrops.safeRandom
import com.tealcube.minecraft.bukkit.mythicdrops.setDisplayNameChatColorized
import com.tealcube.minecraft.bukkit.mythicdrops.setLoreChatColorized
import com.tealcube.minecraft.bukkit.mythicdrops.setRepairCost
import com.tealcube.minecraft.bukkit.mythicdrops.setUnsafeEnchantments
import com.tealcube.minecraft.bukkit.mythicdrops.stripColors
import com.tealcube.minecraft.bukkit.mythicdrops.utils.ItemBuildingUtil
import com.tealcube.minecraft.bukkit.mythicdrops.utils.ItemBuildingUtil.getRelationEnchantments
import com.tealcube.minecraft.bukkit.mythicdrops.utils.ItemBuildingUtil.getRelations
import com.tealcube.minecraft.bukkit.mythicdrops.utils.LeatherArmorUtil
import com.tealcube.minecraft.bukkit.mythicdrops.utils.TemplatingUtil
import com.tealcube.minecraft.bukkit.mythicdrops.choices.WeightedChoice
import io.pixeloutlaw.minecraft.spigot.mythicdrops.addAttributeModifier
import io.pixeloutlaw.minecraft.spigot.mythicdrops.cloneWithDefaultAttributes
import io.pixeloutlaw.minecraft.spigot.mythicdrops.customModelData
import io.pixeloutlaw.minecraft.spigot.mythicdrops.displayName
import io.pixeloutlaw.minecraft.spigot.mythicdrops.getDurabilityInPercentageRange
import io.pixeloutlaw.minecraft.spigot.mythicdrops.getHighestEnchantment
import io.pixeloutlaw.minecraft.spigot.mythicdrops.getMaterials
import io.pixeloutlaw.minecraft.spigot.mythicdrops.isUnbreakable
import io.pixeloutlaw.minecraft.spigot.mythicdrops.itemFlags
import io.pixeloutlaw.minecraft.spigot.mythicdrops.mythicDropsTier
import io.pixeloutlaw.minecraft.spigot.mythicdrops.setPersistentDataString
import io.pixeloutlaw.minecraft.spigot.mythicdrops.toTitleCase
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import java.util.Locale

// MARK AS INTERNAL IN 9.0.0
class MythicDropBuilder @Deprecated(
    "Get via MythicDropsApi instead",
    ReplaceWith(
        "MythicDropsApi.mythicDrops.productionLine.tieredItemFactory.getNewDropBuilder()",
        "com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDropsApi"
    )
) constructor(
    private val itemGroupManager: ItemGroupManager,
    private val relationManager: RelationManager,
    private val settingsManager: SettingsManager,
    private val tierManager: TierManager,
    private val socketTypeManager: SocketTypeManager,
    private val socketExtenderTypeManager: SocketExtenderTypeManager
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
    constructor(
        itemGroupManager: ItemGroupManager,
        relationManager: RelationManager,
        settingsManager: SettingsManager,
        tierManager: TierManager
    ) : this(
        itemGroupManager,
        relationManager,
        settingsManager,
        tierManager,
        MythicDropsApi.mythicDrops.socketTypeManager,
        MythicDropsApi.mythicDrops.socketExtenderTypeManager
    )

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
        mythicDrops.tierManager,
        mythicDrops.socketTypeManager,
        mythicDrops.socketExtenderTypeManager
    )

    private var tier: Tier? = null
    private var material: Material? = null
    private var itemGenerationReason: ItemGenerationReason = ItemGenerationReason.DEFAULT
    private var useDurability: Boolean = false

    override fun withTier(tier: Tier?): DropBuilder {
        this.tier = tier
        return this
    }

    @Deprecated("Use withTier(Tier) instead", replaceWith = ReplaceWith("withTier(x)"))
    override fun withTier(tierName: String?): DropBuilder {
        this.tier =
            tierName?.let { tierManager.getById(tierName) ?: tierManager.getById(tierName.replace(" ", "_")) }
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
        val materialFromTier =
            if (materialsFromTier.isNotEmpty()) {
                materialsFromTier.random()
            } else {
                null
            }
        val chosenMat = material ?: materialFromTier ?: return null
        val durability =
            if (useDurability) {
                chosenMat.getDurabilityInPercentageRange(
                    chosenTier.minimumDurabilityPercentage,
                    chosenTier.maximumDurabilityPercentage
                )
            } else {
                null
            }
        val openSockets =
            if (Math.random() <= chosenTier.chanceToHaveSockets) {
                val numberOfSockets = (chosenTier.minimumSockets..chosenTier.maximumSockets).safeRandom()
                List(numberOfSockets) { socketTypeManager.randomByWeight() }.filterNotNull()
            } else {
                emptyList()
            }
        val socketExtenderSlots =
            if (Math.random() <= chosenTier.chanceToHaveSocketExtenderSlots) {
                val numberOfSocketExtenderSlots =
                    (chosenTier.minimumSocketExtenderSlots..chosenTier.maximumSocketExtenderSlots).safeRandom()
                List(numberOfSocketExtenderSlots) { socketExtenderTypeManager.randomByWeight() }.filterNotNull()
            } else {
                emptyList()
            }

        // this is mostly here because safe enchants requires an ItemStack
        val itemStackOfMat = ItemStack(chosenMat, 1)

        return build(
            MythicTieredItemGenerationData(
                tier = chosenTier,
                material = chosenMat,
                expRepairCost = chosenTier.repairCost,
                durability = durability,
                isUnbreakable = chosenTier.isUnbreakable,
                openSockets = openSockets,
                socketExtenderSlots = socketExtenderSlots,
                baseEnchantments = ItemBuildingUtil.getBaseEnchantments(itemStackOfMat, chosenTier),
                bonusEnchantments = ItemBuildingUtil.getBonusEnchantments(itemStackOfMat, chosenTier),
                baseAttributes = ItemBuildingUtil.getBaseAttributeModifiers(chosenTier),
                bonusAttributes = ItemBuildingUtil.getBonusAttributeModifiers(chosenTier)
            )
        )
    }

    override fun build(tieredItemGenerationData: TieredItemGenerationData): ItemStack? {
        val preTieredItemGenerationEvent = PreTieredItemGenerationEvent(tieredItemGenerationData)
        Bukkit.getPluginManager().callEvent(preTieredItemGenerationEvent)
        if (preTieredItemGenerationEvent.isCancelled) {
            return null
        }

        val itemStack =
            if (settingsManager.configSettings.options.isDisableDefaultTieredItemAttributes) {
                ItemStack(tieredItemGenerationData.material, 1)
            } else {
                ItemStack(tieredItemGenerationData.material, 1).cloneWithDefaultAttributes()
            }

        if (tieredItemGenerationData.expRepairCost > 0) {
            itemStack.setRepairCost(tieredItemGenerationData.expRepairCost)
        }

        tieredItemGenerationData.durability?.let { durability ->
            itemStack.getThenSetItemMetaAsDamageable { this.damage = durability }
        }

        itemStack.isUnbreakable = tieredItemGenerationData.isUnbreakable

        tieredItemGenerationData.baseAttributes.forEach { attribute, attributeModifier ->
            itemStack.addAttributeModifier(
                attribute,
                attributeModifier
            )
        }
        tieredItemGenerationData.bonusAttributes.forEach { attribute, attributeModifier ->
            itemStack.addAttributeModifier(
                attribute,
                attributeModifier
            )
        }

        val enchantments = tieredItemGenerationData.baseEnchantments.merge(tieredItemGenerationData.bonusEnchantments)
        itemStack.setUnsafeEnchantments(enchantments)

        val highestEnchantmentName = getEnchantmentTypeName(enchantments)

        val displayName =
            generateName(
                itemStack = itemStack,
                chosenTier = tieredItemGenerationData.tier,
                enchantmentName = highestEnchantmentName
            )
        val lore =
            generateLore(
                itemStack = itemStack,
                tieredItemGenerationData = tieredItemGenerationData,
                enchantmentName = highestEnchantmentName
            )
        itemStack.setDisplayNameChatColorized(displayName)
        itemStack.setLoreChatColorized(lore)

        val relations = getRelations(displayName, relationManager)
        val relationsEnchantments = getRelationEnchantments(itemStack, relations, tieredItemGenerationData.tier)
        val relationsAttributes =
            relations
                .flatMap { it.attributes }
                .map(MythicAttribute::toAttributeModifier)
        itemStack.setUnsafeEnchantments(itemStack.enchantments.merge(relationsEnchantments))
        relationsAttributes.forEach { (attribute, attributeModifier) ->
            itemStack.addAttributeModifier(attribute, attributeModifier)
        }

        if (settingsManager.configSettings.options.isRandomizeLeatherColors) {
            LeatherArmorUtil.setRandomizedColor(itemStack)
        }

        itemStack.setPersistentDataString(mythicDropsTier, tieredItemGenerationData.tier.name)
        itemStack.itemFlags = tieredItemGenerationData.tier.itemFlags

        val availableCustomModelData =
            tieredItemGenerationData.tier.customModelData.filter {
                it.material == null || it.material == tieredItemGenerationData.material
            }
        WeightedChoice.between(availableCustomModelData).choose()?.modelData?.let { customModelData ->
            itemStack.customModelData = customModelData
        }

        val randomItemGenerationEvent =
            TieredItemGenerationEvent(tieredItemGenerationData.tier, itemStack, itemGenerationReason)
        Bukkit.getPluginManager().callEvent(randomItemGenerationEvent)
        return if (randomItemGenerationEvent.isCancelled) {
            null
        } else {
            randomItemGenerationEvent.itemStack
        }
    }

    private fun getItemGroup(
        itemStack: ItemStack,
        filter: (itemGroup: ItemGroup) -> Boolean = { true }
    ): ItemGroup? =
        itemGroupManager
            .getMatchingItemGroups(itemStack.type)
            .filter { itemGroup -> itemGroup.priority >= 0 }
            .filter(filter)
            .shuffled()
            .minByOrNull { itemGroup -> itemGroup.priority }

    private fun generateLore(
        itemStack: ItemStack,
        tieredItemGenerationData: TieredItemGenerationData,
        enchantmentName: String
    ): List<String> {
        val tooltipFormat =
            tieredItemGenerationData.tier.tooltipFormat ?: settingsManager.configSettings.display.tooltipFormat

        val minecraftName = getMinecraftMaterialName(itemStack.type)
        val mythicName = getMythicMaterialName(itemStack.type)
        val generalLoreString = NameMap.getRandom(NameType.GENERAL_LORE, "")
        val materialLoreString =
            NameMap
                .getRandom(NameType.MATERIAL_LORE, itemStack.type.name.lowercase(Locale.getDefault()))
        val tierLoreString =
            NameMap.getRandom(NameType.TIER_LORE, tieredItemGenerationData.tier.name.lowercase(Locale.getDefault()))
        val enchantmentLoreString =
            NameMap.getRandom(
                NameType.ENCHANTMENT_LORE,
                enchantmentName.lowercase(Locale.getDefault())
            )

        val itemGroupForLore =
            getItemGroup(itemStack) {
                NameMap
                    .getMatchingKeys(NameType.ITEMTYPE_LORE)
                    .map { key -> key.removePrefix(NameType.ITEMTYPE_LORE.format) }
                    .contains(it.name)
            }

        val itemTypeLoreString =
            NameMap
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

        val baseLore =
            tieredItemGenerationData.tier.baseLore.flatMap { lineOfLore ->
                lineOfLore.split(newlineRegex).dropLastWhile { it.isEmpty() }
            }

        val numOfBonusLore =
            (tieredItemGenerationData.tier.minimumBonusLore..tieredItemGenerationData.tier.maximumBonusLore).safeRandom()
        val bonusLore = mutableListOf<String>()
        repeat(numOfBonusLore) {
            val availableBonusLore = tieredItemGenerationData.tier.bonusLore.filter { !bonusLore.contains(it) }
            if (availableBonusLore.isNotEmpty()) {
                val selectedBonusLore = availableBonusLore.random()
                bonusLore.addAll(selectedBonusLore.split(newlineRegex).dropLastWhile { it.isEmpty() })
            }
        }

        val socketLoreGenerationResult = generateSocketLore(tieredItemGenerationData)

        val displayName = itemStack.displayName
        val relationLore =
            displayName?.let { name ->
                name
                    .stripColors()
                    .split(spaceRegex)
                    .dropLastWhile { it.isEmpty() }
                    .mapNotNull { relationManager.getById(it) }
                    .flatMap { it.lore }
            } ?: emptyList()

        val args =
            listOf(
                "%basematerial%" to minecraftName,
                "%mythicmaterial%" to mythicName,
                "%tiername%" to tieredItemGenerationData.tier.displayName,
                "%enchantment%" to enchantmentName,
                "%tiercolor%" to "${tieredItemGenerationData.tier.displayColor}",
                "%itemtype%" to (itemGroupForLore?.name ?: "")
            )

        return tooltipFormat
            .replaceWithCollections(
                "%baselore%" to baseLore,
                "%bonuslore%" to bonusLore,
                "%socketgemlore%" to socketLoreGenerationResult.socketGemLore,
                "%socketablelore%" to socketLoreGenerationResult.socketableLore,
                "%socketlore%" to socketLoreGenerationResult.socketLore,
                "%generallore%" to generalLore,
                "%tierlore%" to tierLore,
                "%materiallore%" to materialLore,
                "%enchantmentlore%" to enchantmentLore,
                "%relationlore%" to relationLore,
                "%itemtypelore%" to itemTypeLore
            ).replaceArgs(args)
            .map { TemplatingUtil.template(it) }
    }

    private fun generateSocketLore(tieredItemGenerationData: TieredItemGenerationData): SocketLoreGenerationResult {
        if (!settingsManager.configSettings.components.isSocketingEnabled) {
            return SocketLoreGenerationResult(emptyList(), emptyList(), emptyList())
        }

        val socketGemLore = mutableListOf<String>()
        val socketableLore = mutableListOf<String>()

        val selectedSocketTypes = tieredItemGenerationData.openSockets.toSet()
        if (tieredItemGenerationData.openSockets.isNotEmpty()) {
            tieredItemGenerationData.openSockets.forEach {
                socketGemLore.add(
                    it.socketStyle.replace(
                        "%tiercolor%",
                        "${tieredItemGenerationData.tier.displayColor}"
                    )
                )
            }
            socketableLore.addAll(
                selectedSocketTypes.fold(emptyList()) { acc, socketType -> acc + socketType.socketHelp }
            )
        }

        val selectedSocketExtenderTypes = tieredItemGenerationData.socketExtenderSlots.toSet()
        if (tieredItemGenerationData.socketExtenderSlots.isNotEmpty()) {
            tieredItemGenerationData.socketExtenderSlots.forEach {
                socketGemLore.add(it.slotStyle)
            }
            socketableLore.addAll(
                selectedSocketExtenderTypes.fold(emptyList()) { acc, socketExtenderType ->
                    acc + socketExtenderType.slotHelp
                }
            )
        }

        val socketLore = socketGemLore.toMutableList()
        socketLore.addAll(socketableLore)
        return SocketLoreGenerationResult(
            socketGemLore = socketGemLore,
            socketableLore = socketableLore,
            socketLore = socketLore
        )
    }

    private fun generateName(
        itemStack: ItemStack,
        chosenTier: Tier,
        enchantmentName: String
    ): String {
        val format = chosenTier.itemDisplayNameFormat ?: settingsManager.configSettings.display.itemDisplayNameFormat
        if (format.isEmpty()) {
            return "Mythic Item"
        }
        val minecraftName = getMinecraftMaterialName(itemStack.type)
        val mythicName = getMythicMaterialName(itemStack.type)
        val generalPrefix = NameMap.getRandom(NameType.GENERAL_PREFIX, "")
        val generalSuffix = NameMap.getRandom(NameType.GENERAL_SUFFIX, "")
        val materialPrefix =
            NameMap
                .getRandom(NameType.MATERIAL_PREFIX, itemStack.type.name.lowercase(Locale.getDefault()))
        val materialSuffix =
            NameMap
                .getRandom(NameType.MATERIAL_SUFFIX, itemStack.type.name.lowercase(Locale.getDefault()))
        val tierPrefix = NameMap.getRandom(NameType.TIER_PREFIX, chosenTier.name.lowercase(Locale.getDefault()))
        val tierSuffix = NameMap.getRandom(NameType.TIER_SUFFIX, chosenTier.name.lowercase(Locale.getDefault()))
        val highestEnch = itemStack.getHighestEnchantment()
        val enchantmentPrefix = getEnchantmentPrefix(highestEnch)
        val enchantmentSuffix = getEnchantmentSuffix(highestEnch)

        val itemGroupForPrefix =
            getItemGroup(itemStack) {
                NameMap
                    .getMatchingKeys(NameType.ITEMTYPE_PREFIX)
                    .map { key -> key.removePrefix(NameType.ITEMTYPE_PREFIX.format) }
                    .contains(it.name)
            }
        val itemGroupForSuffix =
            getItemGroup(itemStack) {
                NameMap
                    .getMatchingKeys(NameType.ITEMTYPE_SUFFIX)
                    .map { key -> key.removePrefix(NameType.ITEMTYPE_SUFFIX.format) }
                    .contains(it.name)
            }

        val itemTypePrefix =
            NameMap.getRandom(NameType.ITEMTYPE_PREFIX, itemGroupForPrefix?.name?.lowercase(Locale.getDefault()) ?: "")
        val itemTypeSuffix =
            NameMap.getRandom(NameType.ITEMTYPE_SUFFIX, itemGroupForSuffix?.name?.lowercase(Locale.getDefault()) ?: "")

        val args =
            listOf(
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

        return "${chosenTier.displayColor}${format.replaceArgs(args).trim()}${chosenTier.identifierColor}"
    }

    private fun getEnchantmentSuffix(highestEnch: Enchantment?): String =
        NameMap.getRandom(
            NameType.ENCHANTMENT_SUFFIX,
            highestEnch?.key?.toString()?.lowercase(Locale.getDefault()) ?: ""
        )

    private fun getEnchantmentPrefix(highestEnch: Enchantment?): String =
        NameMap.getRandom(
            NameType.ENCHANTMENT_PREFIX,
            highestEnch?.key?.toString()?.lowercase(Locale.getDefault()) ?: ""
        )

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

    private fun getEnchantmentTypeName(enchantments: Map<Enchantment, Int>): String {
        val enchantment =
            enchantments.highestByValue()
                ?: return settingsManager.languageSettings.displayNames
                    .getOrDefault("Ordinary", "Ordinary")
                    .chatColorize()

        @Suppress("DEPRECATION")
        val ench =
            try {
                settingsManager.languageSettings.displayNames[enchantment.key.key]
                    ?: settingsManager.languageSettings.displayNames[enchantment.name]
            } catch (ignored: Throwable) {
                settingsManager.languageSettings.displayNames[enchantment.name]
            }
        return ench ?: "Ordinary"
    }
}
