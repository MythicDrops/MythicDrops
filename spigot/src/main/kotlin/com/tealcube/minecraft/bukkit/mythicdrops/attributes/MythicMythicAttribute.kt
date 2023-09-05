package com.tealcube.minecraft.bukkit.mythicdrops.attributes

import com.tealcube.minecraft.bukkit.mythicdrops.api.attributes.MythicAttribute
import com.tealcube.minecraft.bukkit.mythicdrops.getNonNullString
import com.tealcube.minecraft.bukkit.mythicdrops.safeRandom
import io.pixeloutlaw.minecraft.spigot.mythicdrops.enumValueOrNull
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.EquipmentSlot
import java.util.UUID
import kotlin.math.max
import kotlin.math.min

internal data class MythicMythicAttribute(
    override val attribute: Attribute,
    override val minimumAmount: Double,
    override val maximumAmount: Double,
    override val name: String,
    override val operation: AttributeModifier.Operation,
    override val equipmentSlot: EquipmentSlot?
): MythicAttribute {
    companion object {
        @JvmStatic
        fun fromConfigurationSection(configurationSection: ConfigurationSection, key: String): MythicAttribute? {
            val attribute = enumValueOrNull<Attribute>(
                configurationSection.getNonNullString("attribute")
            )
            val attributeOperation =
                enumValueOrNull<AttributeModifier.Operation>(
                    configurationSection.getNonNullString("operation")
                )
            if (attribute == null || attributeOperation == null) {
                return null
            }
            val equipmentSlot =
                enumValueOrNull<EquipmentSlot>(
                    configurationSection.getNonNullString("slot")
                )
            val (minimumAmount, maximumAmount) = if (configurationSection.contains("amount")) {
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

    override fun getAmount() = (min(minimumAmount, maximumAmount)..max(
        minimumAmount,
        maximumAmount
    )).safeRandom()

    override fun toAttributeModifier(): Pair<Attribute, AttributeModifier> =
        attribute to AttributeModifier(UUID.randomUUID(), name, getAmount(), operation, equipmentSlot)
}
