package com.tealcube.minecraft.bukkit.mythicdrops.attributes

import com.tealcube.minecraft.bukkit.mythicdrops.api.attributes.MythicAttribute
import com.tealcube.minecraft.bukkit.mythicdrops.getNonNullString
import com.tealcube.minecraft.bukkit.mythicdrops.safeRandom
import io.pixeloutlaw.minecraft.spigot.mythicdrops.enumValueOrNull
import io.pixeloutlaw.minecraft.spigot.mythicdrops.mythicDrops
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.EquipmentSlotGroup
import java.util.Locale
import kotlin.math.max
import kotlin.math.min

internal data class MythicMythicAttribute(
    override val attribute: Attribute,
    override val minimumAmount: Double,
    override val maximumAmount: Double,
    override val name: String,
    override val operation: AttributeModifier.Operation,
    override val equipmentSlot: EquipmentSlot?
) : MythicAttribute {
    companion object {
        fun fromConfigurationSection(
            configurationSection: ConfigurationSection,
            key: String
        ): MythicAttribute? {
            val attributeKey =
                NamespacedKey.fromString(
                    configurationSection.getNonNullString("attribute").lowercase()
                ) ?: return null
            val attribute = Registry.ATTRIBUTE.get(attributeKey) ?: return null
            val attributeOperation =
                enumValueOrNull<AttributeModifier.Operation>(
                    configurationSection.getNonNullString("operation")
                ) ?: return null
            val equipmentSlot =
                enumValueOrNull<EquipmentSlot>(
                    configurationSection.getNonNullString("slot")
                )
            val (minimumAmount, maximumAmount) =
                if (configurationSection.contains("amount")) {
                    val amount = configurationSection.getDouble("amount")
                    amount to amount
                } else {
                    configurationSection.getDouble("minimum-amount") to configurationSection.getDouble("maximum-amount")
                }
            return MythicMythicAttribute(
                attribute = attribute,
                minimumAmount = minimumAmount,
                maximumAmount = maximumAmount,
                name = key,
                operation = attributeOperation,
                equipmentSlot = equipmentSlot
            )
        }
    }

    override fun getAmount(): Double {
        val minimum = min(minimumAmount, maximumAmount)
        val maximum =
            max(
                minimumAmount,
                maximumAmount
            )
        return (minimum..maximum).safeRandom()
    }

    override fun toAttributeModifier(): Pair<Attribute, AttributeModifier> {
        val name = "$name.${equipmentSlot?.name?.lowercase(Locale.ROOT)}"
        return attribute to
            AttributeModifier(
                mythicDrops(name),
                getAmount(),
                operation,
                equipmentSlot?.group ?: EquipmentSlotGroup.ANY
            )
    }
}
