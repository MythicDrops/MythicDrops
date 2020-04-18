/*
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2020 Richard Harrah
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
package com.tealcube.minecraft.bukkit.mythicdrops.api.attributes

import com.tealcube.minecraft.bukkit.mythicdrops.getNonNullString
import com.tealcube.minecraft.bukkit.mythicdrops.safeRandom
import java.lang.Double.max
import java.lang.Double.min
import java.util.UUID
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.EquipmentSlot

data class MythicAttribute(
    val attribute: Attribute,
    val minimumAmount: Double,
    val maximumAmount: Double,
    val name: String,
    val operation: AttributeModifier.Operation,
    val equipmentSlot: EquipmentSlot?
) {
    companion object {
        @JvmStatic
        fun fromConfigurationSection(configurationSection: ConfigurationSection, key: String): MythicAttribute? {
            val attribute = try {
                Attribute.valueOf(configurationSection.getNonNullString("attribute"))
            } catch (ex: Exception) {
                return null
            }
            val attributeOperation = try {
                AttributeModifier.Operation.valueOf(configurationSection.getNonNullString("operation"))
            } catch (ex: Exception) {
                return null
            }
            val equipmentSlot = try {
                EquipmentSlot.valueOf(configurationSection.getNonNullString("slot"))
            } catch (ex: Exception) {
                null
            }
            val (minimumAmount, maximumAmount) = if (configurationSection.contains("amount")) {
                val amount = configurationSection.getDouble("amount")
                amount to amount
            } else {
                configurationSection.getDouble("minimum-amount") to configurationSection.getDouble("maximum-amount")
            }
            return MythicAttribute(
                attribute = attribute,
                minimumAmount = minimumAmount,
                maximumAmount = maximumAmount,
                name = key,
                operation = attributeOperation,
                equipmentSlot = equipmentSlot
            )
        }
    }

    fun getAmount() = (min(minimumAmount, maximumAmount)..max(minimumAmount, maximumAmount)).safeRandom()

    fun toAttributeModifier(): Pair<Attribute, AttributeModifier> =
        attribute to AttributeModifier(UUID.randomUUID(), name, getAmount(), operation, equipmentSlot)
}
