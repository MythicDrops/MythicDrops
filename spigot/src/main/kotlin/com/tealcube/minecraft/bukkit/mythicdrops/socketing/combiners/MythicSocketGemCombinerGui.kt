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
package com.tealcube.minecraft.bukkit.mythicdrops.socketing.combiners

import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDropsApi
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SettingsManager
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketGem
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.combiners.SocketGemCombinerGui
import com.tealcube.minecraft.bukkit.mythicdrops.chatColorize
import com.tealcube.minecraft.bukkit.mythicdrops.isSlotEmpty
import com.tealcube.minecraft.bukkit.mythicdrops.setDisplayNameChatColorized
import com.tealcube.minecraft.bukkit.mythicdrops.setLoreChatColorized
import com.tealcube.minecraft.bukkit.mythicdrops.utils.GemUtil
import io.pixeloutlaw.kindling.Log
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
import org.bukkit.inventory.PlayerInventory
import java.util.UUID

internal class MythicSocketGemCombinerGui(
    private val settingsManager: SettingsManager
) : SocketGemCombinerGui {
    private companion object {
        const val SIZE = 45
        const val SLOT1 = 10
        const val SLOT2 = 12
        const val SLOT3 = 14
        const val SLOT4 = 16
        const val RESULT_SLOT = 31
        val slots = listOf(SLOT1, SLOT2, SLOT3, SLOT4)
    }

    private val socketingSettings by lazy { settingsManager.socketingSettings }
    private val socketGemCombinerOptions by lazy { socketingSettings.items.socketGemCombiner }

    private val uuid = UUID.randomUUID()
    private val inv: Inventory =
        Bukkit.createInventory(
            this,
            SIZE,
            socketGemCombinerOptions.name.chatColorize()
        )
    private val clickToCombineButton =
        ItemStack(socketGemCombinerOptions.clickToCombine.material).apply {
            setDisplayNameChatColorized(socketGemCombinerOptions.clickToCombine.name)
        }
    private val ineligibleToCombineButton =
        ItemStack(socketGemCombinerOptions.ineligibleToCombineOptions.material).apply {
            setDisplayNameChatColorized(socketGemCombinerOptions.ineligibleToCombineOptions.name)
        }
    private val sameFamilyButton =
        ineligibleToCombineButton.clone().apply {
            setLoreChatColorized(socketGemCombinerOptions.ineligibleToCombineOptions.sameFamilyLore)
        }
    private val sameLevelButton =
        ineligibleToCombineButton.clone().apply {
            setLoreChatColorized(socketGemCombinerOptions.ineligibleToCombineOptions.sameLevelLore)
        }
    private val sameFamilyAndLevelButton =
        ineligibleToCombineButton.clone().apply {
            setLoreChatColorized(socketGemCombinerOptions.ineligibleToCombineOptions.sameFamilyAndLevelLore)
        }
    private val noGemFoundButton =
        ineligibleToCombineButton.clone().apply {
            setLoreChatColorized(socketGemCombinerOptions.ineligibleToCombineOptions.noGemFoundLore)
        }
    private var combinedGem: SocketGem? = null

    init {
        val buffer = ItemStack(socketGemCombinerOptions.buffer.material)
        buffer.setDisplayNameChatColorized(socketGemCombinerOptions.buffer.name.chatColorize())
        for (slot in 0 until SIZE) {
            if (slots.plus(RESULT_SLOT).contains(slot)) {
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
            Log.debug("event.isCancelled uuid=$uuid")
            return
        }
        if (event.inventory.holder != this) {
            Log.debug("event.inventory.holder != this uuid=$uuid")
            return
        }

        event.result = Event.Result.DENY

        val currentItem = event.currentItem
        if (currentItem == null || currentItem.type == Material.AIR) {
            Log.debug("currentItem == null || currentItem.type == Material.AIR uuid=$uuid")
            return
        }
        val eventInventory = inventory
        val player = event.whoClicked as? Player ?: return
        val clickedInventory = event.clickedInventory
        if (clickedInventory == null) {
            Log.debug("clickedInventory == null uuid=$uuid")
            return
        }
        when (clickedInventory.holder) {
            is HumanEntity -> {
                Log.debug("clickedInventory.holder is HumanEntity uuid=$uuid")
                handleAddGemToCombiner(currentItem, player, eventInventory, event.slot)
            }

            is SocketGemCombinerGui -> {
                Log.debug("clickedInventory.holder is SocketGemCombinerGui uuid=$uuid")
                handleRemoveGemFromCombiner(currentItem, player, eventInventory, event.slot)
            }
        }
    }

    @EventHandler
    override fun onGuiClose(event: InventoryCloseEvent) {
        if (event.inventory.holder != this) {
            return
        }

        listOfNotNull(
            event.inventory.getItem(SLOT1),
            event.inventory.getItem(SLOT2),
            event.inventory.getItem(SLOT3),
            event.inventory.getItem(SLOT4),
            event.inventory.getItem(RESULT_SLOT)
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

    private fun handleAddGemToCombiner(
        currentItem: ItemStack,
        player: Player,
        eventInventory: Inventory,
        slot: Int
    ) {
        val playerInventory = player.inventory

        val clickedItem = currentItem.clone()
        val resultSlotItem = (eventInventory.getItem(31) ?: ItemStack(Material.BARRIER)).clone()

        // if clicked item is not a socket gem, we don't allow that in the combiner
        if (!isSocketGem(clickedItem)) {
            Log.debug("!isSocketGem(clickedItem) uuid=$uuid")
            player.sendMessage(settingsManager.languageSettings.socketing.combinerMustBeGem.chatColorize())
            return
        }

        // if the result item is already a socket gem, they need to claim it first
        if (isSocketGem(resultSlotItem)) {
            Log.debug("isSocketGem(resultSlotItem) uuid=$uuid")
            player.sendMessage(settingsManager.languageSettings.socketing.combinerClaimOutput.chatColorize())
            return
        }

        val firstEmptyCombinerSlot = getEmptySocketCombinerSlot(eventInventory)

        if (firstEmptyCombinerSlot == -1) {
            Log.debug("firstEmptyCombinerSlot == -1 uuid=$uuid")
            return
        }
        val newGem = clickedItem.clone()
        val oldGem = clickedItem.clone()

        newGem.amount = 1
        oldGem.amount = oldGem.amount - 1

        eventInventory.setItem(firstEmptyCombinerSlot, newGem)
        playerInventory.setItem(slot, oldGem)

        if (getEmptySocketCombinerSlot(eventInventory) != -1) {
            Log.debug("getEmptySocketCombinerSlot(eventInventory) != -1 uuid=$uuid")
            return
        }

        val requireSameFamily = socketingSettings.combining.isRequireSameFamily
        val requireSameLevel = socketingSettings.combining.isRequireSameLevel
        val socketGemsInCombiner = getSocketGemsFromSlots(eventInventory)
        val allHaveSameFamily = GemUtil.doAllGemsHaveSameFamily(socketGemsInCombiner)
        val allHaveSameLevel = GemUtil.doAllGemsHaveSameLevel(socketGemsInCombiner)
        val allHaveSameFamilyAndLevel = allHaveSameFamily && allHaveSameLevel

        if (requireSameFamily && requireSameLevel) {
            handleSameFamilyAndSameLevel(allHaveSameFamilyAndLevel, socketGemsInCombiner, eventInventory)
            return
        }
        if (requireSameFamily) {
            handleSameFamily(allHaveSameFamily, socketGemsInCombiner, eventInventory)
            return
        }
        if (requireSameLevel) {
            handleSameLevel(allHaveSameLevel, socketGemsInCombiner, eventInventory)
            return
        }
        handleNoRequirements(socketGemsInCombiner, eventInventory)
    }

    private fun handleNoRequirements(
        socketGemsInCombiner: List<SocketGem>,
        eventInventory: Inventory
    ) {
        Log.debug("no requirements uuid=$uuid")
        val averageLevel = socketGemsInCombiner.map { it.level }.average().toInt()
        val foundGem = GemUtil.getRandomSocketGemByWeightWithLevel(averageLevel + 1)
        handleGemIfFound(foundGem, eventInventory)
    }

    private fun handleSameLevel(
        allHaveSameLevel: Boolean,
        socketGemsInCombiner: List<SocketGem>,
        eventInventory: Inventory
    ) {
        Log.debug("requireSameLevel uuid=$uuid")
        if (allHaveSameLevel) {
            Log.debug("allHaveSameLevel uuid=$uuid")
            val firstGem = socketGemsInCombiner.first()
            val foundGem = GemUtil.getRandomSocketGemByWeightWithLevel(firstGem.level + 1)
            handleGemIfFound(foundGem, eventInventory)
        } else {
            Log.debug("!allHaveSameLevel uuid=$uuid")
            combinedGem = null
            eventInventory.setItem(RESULT_SLOT, sameLevelButton)
        }
    }

    private fun handleSameFamily(
        allHaveSameFamily: Boolean,
        socketGemsInCombiner: List<SocketGem>,
        eventInventory: Inventory
    ) {
        Log.debug("requireSameFamily uuid=$uuid")
        if (allHaveSameFamily) {
            Log.debug("allHaveSameFamily uuid=$uuid")
            val firstGem = socketGemsInCombiner.first()
            val averageLevel = socketGemsInCombiner.map { it.level }.average().toInt()
            val foundGem = GemUtil.getRandomSocketGemByWeightFromFamilyWithLevel(firstGem.family, averageLevel + 1)
            handleGemIfFound(foundGem, eventInventory)
        } else {
            Log.debug("!allHaveSameFamily uuid=$uuid")
            combinedGem = null
            eventInventory.setItem(RESULT_SLOT, sameFamilyButton)
        }
    }

    private fun handleSameFamilyAndSameLevel(
        allHaveSameFamilyAndLevel: Boolean,
        socketGemsInCombiner: List<SocketGem>,
        eventInventory: Inventory
    ) {
        Log.debug("requireSameFamily && requireSameLevel uuid=$uuid")
        if (allHaveSameFamilyAndLevel) {
            Log.debug("allHaveSameFamilyAndLevel uuid=$uuid")
            val firstGem = socketGemsInCombiner.first()
            val foundGem = GemUtil.getRandomSocketGemByWeightFromFamilyWithLevel(firstGem.family, firstGem.level + 1)
            handleGemIfFound(foundGem, eventInventory)
        } else {
            Log.debug("!allHaveSameFamilyAndLevel uuid=$uuid")
            combinedGem = null
            eventInventory.setItem(RESULT_SLOT, sameFamilyAndLevelButton)
        }
        return
    }

    private fun handleGemIfFound(
        foundGem: SocketGem?,
        eventInventory: Inventory
    ) {
        if (foundGem != null) {
            Log.debug("foundGem != null uuid=$uuid")
            combinedGem = foundGem
            eventInventory.setItem(RESULT_SLOT, clickToCombineButton)
        } else {
            Log.debug("foundGem == null uuid=$uuid")
            eventInventory.setItem(RESULT_SLOT, noGemFoundButton)
        }
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
            Log.debug("!isSocketGem(clickedItem) && !isCombineButton(clickedItem) uuid=$uuid")
            return
        }

        when (slot) {
            RESULT_SLOT -> {
                val combinedGemAtTimeOfClick = combinedGem // combined gem is mutable so we need to hold onto the value
                if (
                    isCombineButton(clickedItem) && combinedGemAtTimeOfClick != null &&
                    getEmptySocketCombinerSlot(eventInventory) == -1
                ) {
                    handleCombineClick(combinedGemAtTimeOfClick, eventInventory)
                    return
                }
                if (isSocketGem(currentItem)) {
                    handleClaimSocketGemClick(eventInventory, slot, playerInventory, player, currentItem)
                    return
                }
            }

            SLOT1, SLOT2, SLOT3, SLOT4 -> {
                handleRemoveGemClick(playerInventory, clickedItem, player, eventInventory, slot)
            }
        }
    }

    private fun handleRemoveGemClick(
        playerInventory: PlayerInventory,
        clickedItem: ItemStack,
        player: Player,
        eventInventory: Inventory,
        slot: Int
    ) {
        Log.debug("removing gem slot=$slot uuid=$uuid")
        if (playerInventory.firstEmpty() != -1) {
            playerInventory.addItem(clickedItem)
        } else {
            player.world.dropItem(player.location, clickedItem)
        }
        eventInventory.setItem(slot, null)
        if (eventInventory.getItem(RESULT_SLOT) != null) {
            eventInventory.setItem(RESULT_SLOT, null)
        }
    }

    private fun handleClaimSocketGemClick(
        eventInventory: Inventory,
        slot: Int,
        playerInventory: PlayerInventory,
        player: Player,
        currentItem: ItemStack
    ) {
        Log.debug("clicked combined socket gem!")
        eventInventory.setItem(slot, null)
        if (playerInventory.firstEmpty() == -1) {
            player.world.dropItem(player.location, currentItem)
        } else {
            playerInventory.addItem(currentItem)
        }
    }

    private fun handleCombineClick(
        combinedGemAtTimeOfClick: SocketGem,
        eventInventory: Inventory
    ) {
        Log.debug("clicked combine button! uuid=$uuid")
        GemUtil.getRandomSocketGemMaterial()?.let {
            val socketItem =
                MythicDropsApi.mythicDrops.productionLine.socketGemItemFactory.toItemStack(combinedGemAtTimeOfClick)
            eventInventory.setItem(SLOT1, null)
            eventInventory.setItem(SLOT2, null)
            eventInventory.setItem(SLOT3, null)
            eventInventory.setItem(SLOT4, null)
            eventInventory.setItem(RESULT_SLOT, socketItem)
        }
    }

    private fun getEmptySocketCombinerSlot(invy: Inventory): Int = slots.firstOrNull(invy::isSlotEmpty) ?: -1

    private fun isSocketGem(itemStack: ItemStack) = GemUtil.getSocketGemFromPotentialSocketItem(itemStack) != null

    private fun isCombineButton(itemStack: ItemStack) = itemStack.isSimilar(clickToCombineButton)

    private fun getSocketGemsFromSlots(invy: Inventory) = getSocketGems(slots.mapNotNull { invy.getItem(it) })

    private fun getSocketGems(itemStacks: List<ItemStack>) = itemStacks.mapNotNull(GemUtil::getSocketGemFromPotentialSocketItem)
}
