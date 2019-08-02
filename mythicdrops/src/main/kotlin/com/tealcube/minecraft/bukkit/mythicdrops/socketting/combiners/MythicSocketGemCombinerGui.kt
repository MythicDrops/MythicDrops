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
package com.tealcube.minecraft.bukkit.mythicdrops.socketting.combiners

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.ConfigSettings
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SockettingSettings
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketting.combiners.SocketGemCombinerGui
import com.tealcube.minecraft.bukkit.mythicdrops.chatColorize
import com.tealcube.minecraft.bukkit.mythicdrops.isSlotEmpty
import com.tealcube.minecraft.bukkit.mythicdrops.items.setDisplayNameChatColorized
import com.tealcube.minecraft.bukkit.mythicdrops.items.setLoreChatColorized
import com.tealcube.minecraft.bukkit.mythicdrops.logging.JulLoggerFactory
import com.tealcube.minecraft.bukkit.mythicdrops.utils.GemUtil
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class MythicSocketGemCombinerGui(
    private val configSettings: ConfigSettings,
    private val sockettingSettings: SockettingSettings
) : SocketGemCombinerGui {
    companion object {
        const val size = 45
        const val slot1 = 10
        const val slot2 = 12
        const val slot3 = 14
        const val slot4 = 16
        const val resultSlot = 31
        val slots = listOf(slot1, slot2, slot3, slot4)
        private val logger = JulLoggerFactory.getLogger(MythicSocketGemCombinerGui::class)
    }

    private val inv: Inventory = Bukkit.createInventory(
        this, size, sockettingSettings.socketGemCombinerName.chatColorize()
    )
    private val clickToCombineButton = ItemStack(sockettingSettings.socketGemCombinerClickToCombineMaterial).apply {
        setDisplayNameChatColorized(sockettingSettings.socketGemCombinerClickToCombineName)
    }
    private val ineligibleToCombineButton =
        ItemStack(sockettingSettings.socketGemCombinerIneligibleToCombineMaterial).apply {
            setDisplayNameChatColorized(sockettingSettings.socketGemCombinerIneligibleToCombineName)
        }
    private val sameFamilyButton = ineligibleToCombineButton.clone().apply {
        setLoreChatColorized(sockettingSettings.socketGemCombinerSameFamilyLore)
    }
    private val sameLevelButton = ineligibleToCombineButton.clone().apply {
        setLoreChatColorized(sockettingSettings.socketGemCombinerSameLevelLore)
    }
    private val sameFamilyAndLevelButton = ineligibleToCombineButton.clone().apply {
        setLoreChatColorized(sockettingSettings.socketGemCombinerSameFamilyAndLevelLore)
    }

    init {
        val buffer = ItemStack(sockettingSettings.socketGemCombinerBufferMaterial)
        buffer.setDisplayNameChatColorized(sockettingSettings.socketGemCombinerBufferName.chatColorize())
        for (slot in 0 until size) {
            if (slots.plus(resultSlot).contains(slot)) {
                continue
            }
            inv.setItem(slot, buffer)
        }
        inv.maxStackSize = 1
    }

    override fun getInventory(): Inventory = inv

    @EventHandler
    override fun onGuiClick(event: InventoryClickEvent) {
        if (event.isCancelled) {
            logger.fine("event.isCancelled")
            return
        }
        if (event.inventory.holder != this) {
            logger.fine("event.inventory.holder != this")
            return
        }

        event.result = Event.Result.DENY

        val currentItem = event.currentItem
        if (currentItem == null || currentItem.type == Material.AIR) {
            logger.fine("currentItem == null || currentItem.type == Material.AIR")
            return
        }
        val eventInventory = inventory
        val player = event.whoClicked as? Player ?: return
        val clickedInventory = event.clickedInventory
        if (clickedInventory == null) {
            logger.fine("clickedInventory == null")
            return
        }
        when (clickedInventory.holder) {
            is HumanEntity -> {
                logger.fine("clickedInventory.holder is HumanEntity")
                handleAddGemToCombiner(currentItem, player, eventInventory, event.slot)
            }
            is SocketGemCombinerGui -> {
                logger.fine("clickedInventory.holder is SocketGemCombinerGui")
                handleRemoveGemFromCombiner(currentItem, player, eventInventory, event.slot)
            }
        }
    }

    private fun handleAddGemToCombiner(currentItem: ItemStack, player: Player, eventInventory: Inventory, slot: Int) {
        val playerInventory = player.inventory

        val clickedItem = currentItem.clone()
        val resultSlotItem = (eventInventory.getItem(31) ?: ItemStack(Material.BARRIER)).clone()

        // if clicked item is not a socket gem, we don't allow that in the combiner
        if (!isSocketGem(clickedItem)) {
            logger.fine("!isSocketGem(clickedItem)")
            player.sendMessage(configSettings.getFormattedLanguageString("socket.combiner-must-be-gem"))
            return
        }

        // if the result item is already a socket gem, they need to claim it first
        if (isSocketGem(resultSlotItem)) {
            logger.fine("isSocketGem(resultSlotItem)")
            player.sendMessage(configSettings.getFormattedLanguageString("combiner-claim-output"))
            return
        }

        val firstEmptyCombinerSlot = getEmptySocketCombinerSlot(eventInventory)

        if (firstEmptyCombinerSlot == -1) {
            logger.fine("firstEmptyCombinerSlot == -1")
            return
        }
        val newGem = clickedItem.clone()
        val oldGem = clickedItem.clone()

        newGem.amount = 1
        oldGem.amount = oldGem.amount - 1

        eventInventory.setItem(firstEmptyCombinerSlot, newGem)
        playerInventory.setItem(slot, oldGem)

        if (getEmptySocketCombinerSlot(eventInventory) != -1) {
            logger.fine("getEmptySocketCombinerSlot(eventInventory) != -1")
            return
        }

        val requireSameFamily = sockettingSettings.isSocketGemCombinerRequireSameFamily
        val requireSameLevel = sockettingSettings.isSocketGemCombinerRequireSameLevel
        val socketGemsInCombiner = getSocketGemsFromSlots(eventInventory)
        val allHaveSameFamily = GemUtil.doAllGemsHaveSameFamily(socketGemsInCombiner)
        val allHaveSameLevel = GemUtil.doAllGemsHaveSameLevel(socketGemsInCombiner)
        val allHaveSameFamilyAndLevel = allHaveSameFamily && allHaveSameLevel

        if (requireSameFamily && requireSameLevel && !allHaveSameFamilyAndLevel) {
            logger.fine("requireSameFamily && requireSameLevel && !allHaveSameFamilyAndLevel")
            eventInventory.setItem(resultSlot, sameFamilyAndLevelButton)
            return
        }
        if (requireSameFamily && !allHaveSameFamily) {
            logger.fine("requireSameFamily && !allHaveSameFamily")
            eventInventory.setItem(resultSlot, sameFamilyButton)
            return
        }
        if (requireSameLevel && !allHaveSameLevel) {
            logger.fine("requireSameLevel && !allHaveSameLevel")
            eventInventory.setItem(resultSlot, sameLevelButton)
            return
        }
        eventInventory.setItem(resultSlot, clickToCombineButton)
    }

    private fun handleRemoveGemFromCombiner(
        currentItem: ItemStack,
        player: Player,
        eventInventory: Inventory,
        slot: Int
    ) {
        val playerInventory = player.inventory

        val clickedItem = currentItem.clone()

        // if the clicked item is not a socket gem
        if (!isSocketGem(clickedItem) && !isCombineButton(clickedItem)) {
            logger.fine("!isSocketGem(clickedItem) && !isCombineButton(clickedItem)")
            return
        }

        when (slot) {
            resultSlot -> {
                if (isCombineButton(clickedItem)) {
                    logger.fine("clicked combine button!")
                } else {
                    logger.fine("clicked non-combine button in result slot!")
                }
            }
            slot1, slot2, slot3, slot4 -> {
                logger.fine("slot1, slot2, slot3, slot4")
                if (playerInventory.firstEmpty() != -1) {
                    playerInventory.addItem(clickedItem)
                } else {
                    player.world.dropItem(player.location, clickedItem)
                }
                eventInventory.setItem(slot, null)
                if (eventInventory.getItem(resultSlot) != null) {
                    eventInventory.setItem(resultSlot, null)
                }
            }
        }
    }

    @EventHandler
    override fun onGuiClose(event: InventoryCloseEvent) {
        if (event.inventory.holder != this) {
            return
        }

        listOfNotNull(
            event.inventory.getItem(slot1),
            event.inventory.getItem(slot2),
            event.inventory.getItem(slot3),
            event.inventory.getItem(slot4),
            event.inventory.getItem(resultSlot)
        ).filter(this::isSocketGem)
            .forEach {
                if (event.player.inventory.firstEmpty() != -1) {
                    event.player.inventory.addItem(it)
                } else {
                    event.player.world.dropItem(event.player.location, it)
                }
            }

        HandlerList.unregisterAll(this)
    }

    override fun showToPlayer(player: Player) {
        player.openInventory(inv)
    }

    private fun getEmptySocketCombinerSlot(invy: Inventory): Int =
        slots.firstOrNull(invy::isSlotEmpty) ?: -1

    private fun isSocketGem(itemStack: ItemStack) = GemUtil.getSocketGemFromPotentialSocketItem(itemStack) != null

    private fun isCombineButton(itemStack: ItemStack) = itemStack.isSimilar(clickToCombineButton)

    private fun getSocketGemsFromSlots(invy: Inventory) = getSocketGems(slots.mapNotNull { invy.getItem(it) })

    private fun getSocketGems(itemStacks: List<ItemStack>) =
        itemStacks.mapNotNull(GemUtil::getSocketGemFromPotentialSocketItem)
}
