/*
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2021 Richard Harrah
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
package com.tealcube.minecraft.bukkit.mythicdrops.items.factories

import com.tealcube.minecraft.bukkit.mythicdrops.DEFAULT_REPAIR_COST
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.factories.IdentificationItemFactory
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SettingsManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.TierManager
import com.tealcube.minecraft.bukkit.mythicdrops.replaceArgs
import com.tealcube.minecraft.bukkit.mythicdrops.setDisplayNameChatColorized
import com.tealcube.minecraft.bukkit.mythicdrops.setLoreChatColorized
import com.tealcube.minecraft.bukkit.mythicdrops.setRepairCost
import com.tealcube.minecraft.bukkit.mythicdrops.trimEmpty
import io.pixeloutlaw.minecraft.spigot.mythicdrops.cloneWithDefaultAttributes
import io.pixeloutlaw.minecraft.spigot.mythicdrops.getApplicableTiers
import io.pixeloutlaw.minecraft.spigot.mythicdrops.getMaterials
import io.pixeloutlaw.minecraft.spigot.mythicdrops.isZero
import io.pixeloutlaw.minecraft.spigot.mythicdrops.mythicDropsTier
import io.pixeloutlaw.minecraft.spigot.mythicdrops.setPersistentDataString
import io.pixeloutlaw.minecraft.spigot.mythicdrops.toTitleCase
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack
import org.koin.core.annotation.Single

@Single
@Suppress("detekt.TooManyFunctions")
class MythicIdentificationItemFactory(
    private val settingsManager: SettingsManager,
    private val tierManager: TierManager
) : IdentificationItemFactory {
    override fun buildIdentityTome(): ItemStack =
        ItemStack(settingsManager.identifyingSettings.items.identityTome.material).cloneWithDefaultAttributes().apply {
            setDisplayNameChatColorized(settingsManager.identifyingSettings.items.identityTome.name)
            setLoreChatColorized(settingsManager.identifyingSettings.items.identityTome.lore)
            setRepairCost(DEFAULT_REPAIR_COST)
        }

    override fun buildUnidentifiedItem(entityType: EntityType): ItemStack? {
        val materialsFromEntityType =
            settingsManager.creatureSpawningSettings.creatures[entityType]
                ?.tierDrops
                ?.mapNotNull { tierManager.getByName(it) }
                ?.flatMap { it.getMaterials() } ?: emptyList()
        val material = materialsFromEntityType.randomOrNull() ?: return null
        return buildUnidentifiedItem(material, entityType)
    }

    override fun buildUnidentifiedItem(tierName: String): ItemStack? = tierManager.getByName(tierName)?.let { buildUnidentifiedItem(it) }

    override fun buildUnidentifiedItem(tier: Tier): ItemStack? {
        val material = tier.getMaterials().randomOrNull() ?: return null
        return buildUnidentifiedItem(material, null, tier)
    }

    override fun buildUnidentifiedItem(material: Material): ItemStack = buildUnidentifiedItem(material, null)

    override fun buildUnidentifiedItem(
        material: Material,
        entityType: EntityType?
    ): ItemStack = buildUnidentifiedItem(material, entityType, null)

    override fun buildUnidentifiedItem(
        material: Material,
        entityType: EntityType?,
        tier: Tier?
    ): ItemStack = buildUnidentifiedItem(material, entityType, tier, null)

    override fun buildUnidentifiedItem(
        material: Material,
        entityType: EntityType?,
        tier: Tier?,
        allowableTiers: List<Tier>?
    ): ItemStack {
        val verifiedTiers = allowableTiers ?: getTiersForUnidentifiedItem(material, entityType)
        val filteredAllowableTiers = verifiedTiers.filter { !it.identityWeight.isZero() }
        val lore = getUnidentifiedItemLore(filteredAllowableTiers, entityType, tier)
        return ItemStack(material).cloneWithDefaultAttributes().apply {
            setDisplayNameChatColorized(settingsManager.identifyingSettings.items.unidentifiedItem.name)
            setLoreChatColorized(lore)
            setRepairCost(DEFAULT_REPAIR_COST)
            tier?.let {
                setPersistentDataString(mythicDropsTier, tier.name)
            }
        }
    }

    private fun getUnidentifiedItemLore(
        filteredAllowableTiers: List<Tier>,
        entityType: EntityType?,
        tier: Tier?
    ): List<String> {
        val unidentifiedItemOptions = settingsManager.identifyingSettings.items.unidentifiedItem
        val allowableTiersJoined =
            filteredAllowableTiers.joinToString(unidentifiedItemOptions.allowableTiersSeparator) { it.displayName }
        val allowableTiersLore =
            if (filteredAllowableTiers.isNotEmpty()) {
                val allowableTiersPrefix = unidentifiedItemOptions.allowableTiersPrefix
                val allowableTiersSuffix = unidentifiedItemOptions.allowableTiersSuffix
                "${allowableTiersPrefix}$allowableTiersJoined$allowableTiersSuffix"
            } else {
                ""
            }
        val droppedByLore =
            entityType?.let {
                val fromLocalization = settingsManager.languageSettings.displayNames[it.name]
                val prettyEntityTypeName = it.name.replace("_", " ").toTitleCase()
                val entityName = fromLocalization ?: prettyEntityTypeName
                "${unidentifiedItemOptions.droppedByPrefix}${entityName}${unidentifiedItemOptions.droppedBySuffix}"
            } ?: ""
        val tierLore =
            tier?.let {
                "${unidentifiedItemOptions.tierPrefix}${it.displayName}${unidentifiedItemOptions.tierSuffix}"
            } ?: ""
        return unidentifiedItemOptions.lore
            .replaceArgs(
                "%droppedby%" to droppedByLore,
                "%allowabletiers%" to allowableTiersLore,
                "%tier%" to tierLore
            ).trimEmpty()
    }

    private fun getTiersForUnidentifiedItem(
        material: Material,
        entityType: EntityType?
    ): Collection<Tier> {
        val tiersForMaterial = material.getApplicableTiers(tierManager)
        val tiersForEntityType = getTiersForEntityType(entityType, material)
        return if (entityType != null) {
            tiersForMaterial.union(tiersForEntityType)
        } else {
            tiersForMaterial
        }
    }

    private fun getTiersForEntityType(
        entityType: EntityType?,
        material: Material
    ) = if (entityType == null) {
        emptyList()
    } else {
        settingsManager.creatureSpawningSettings.creatures[entityType]
            ?.tierDrops
            ?.mapNotNull { tierManager.getByName(it) }
            ?.filter {
                it.getMaterials().contains(material)
            } ?: emptyList()
    }
}
