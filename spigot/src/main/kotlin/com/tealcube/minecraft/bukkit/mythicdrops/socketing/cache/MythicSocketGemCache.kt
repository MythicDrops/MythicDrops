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
package com.tealcube.minecraft.bukkit.mythicdrops.socketing.cache

import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.GemTriggerType
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketCommand
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketEffect
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.SocketGem
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.cache.SocketCache
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketing.cache.SocketGemCache
import com.tealcube.minecraft.bukkit.mythicdrops.bifold
import com.tealcube.minecraft.bukkit.mythicdrops.utils.GemUtil
import io.pixeloutlaw.kindling.Log
import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack
import java.util.UUID

internal data class MythicSocketGemCache(
    override val owner: UUID,
    val socketEffectCache: SocketCache<SocketEffect> = MythicSocketEffectCache(),
    val socketCommandCache: SocketCache<SocketCommand> = MythicSocketCommandCache()
) : SocketGemCache {
    override fun getArmorSocketEffects(gemTriggerType: GemTriggerType): Set<SocketEffect> = socketEffectCache.getArmor(gemTriggerType)

    override fun getMainHandSocketEffects(gemTriggerType: GemTriggerType): Set<SocketEffect> = socketEffectCache.getMainHand(gemTriggerType)

    override fun getOffHandSocketEffects(gemTriggerType: GemTriggerType): Set<SocketEffect> = socketEffectCache.getOffHand(gemTriggerType)

    override fun setArmorSocketEffects(
        gemTriggerType: GemTriggerType,
        set: Set<SocketEffect>
    ): SocketGemCache = copy(socketEffectCache = socketEffectCache.setArmor(gemTriggerType, set))

    override fun setMainHandSocketEffects(
        gemTriggerType: GemTriggerType,
        set: Set<SocketEffect>
    ): SocketGemCache = copy(socketEffectCache = socketEffectCache.setMainHand(gemTriggerType, set))

    override fun setOffHandSocketEffects(
        gemTriggerType: GemTriggerType,
        set: Set<SocketEffect>
    ): SocketGemCache = copy(socketEffectCache = socketEffectCache.setOffHand(gemTriggerType, set))

    override fun getArmorSocketCommands(gemTriggerType: GemTriggerType): Set<SocketCommand> = socketCommandCache.getArmor(gemTriggerType)

    override fun getMainHandSocketCommands(gemTriggerType: GemTriggerType): Set<SocketCommand> =
        socketCommandCache.getMainHand(gemTriggerType)

    override fun getOffHandSocketCommands(gemTriggerType: GemTriggerType): Set<SocketCommand> =
        socketCommandCache.getOffHand(gemTriggerType)

    override fun setArmorSocketCommands(
        gemTriggerType: GemTriggerType,
        set: Set<SocketCommand>
    ): SocketGemCache = copy(socketCommandCache = socketCommandCache.setArmor(gemTriggerType, set))

    override fun setMainHandSocketCommands(
        gemTriggerType: GemTriggerType,
        set: Set<SocketCommand>
    ): SocketGemCache = copy(socketCommandCache = socketCommandCache.setMainHand(gemTriggerType, set))

    override fun setOffHandSocketCommands(
        gemTriggerType: GemTriggerType,
        set: Set<SocketCommand>
    ): SocketGemCache = copy(socketCommandCache = socketCommandCache.setOffHand(gemTriggerType, set))

    override fun updateArmor(): SocketGemCache {
        val clearedSocketCommandCache = socketCommandCache.clearArmor()
        val clearedSocketEffectCache = socketEffectCache.clearArmor()
        Log.debug("Cleared armor socket command and effect cache. owner=$owner")
        val player = Bukkit.getPlayer(owner)
        if (player == null) {
            Log.debug("Could not find player matching owner: owner=$owner")
            return copy(
                socketCommandCache = clearedSocketCommandCache,
                socketEffectCache = clearedSocketEffectCache
            )
        }
        val socketGems: List<SocketGem> =
            player.equipment?.armorContents?.filterNotNull()?.flatMap(GemUtil::getSocketGemsFromItemStackLore)
                ?: emptyList()
        Log.debug("Updating armor socket command and effect cache. owner=$owner gems=${socketGems.map { it.name }}")
        val updatedCaches =
            socketGems.bifold(clearedSocketCommandCache, clearedSocketEffectCache) { cacheAccums, socketGem ->
                val gemTriggerType = socketGem.gemTriggerType
                val socketCommands = socketGem.commands
                val socketEffects = socketGem.socketEffects

                cacheAccums.copy(
                    first =
                        cacheAccums.first.setArmor(
                            gemTriggerType,
                            cacheAccums.first.getArmor(gemTriggerType) + socketCommands
                        ),
                    second =
                        cacheAccums.second.setArmor(
                            gemTriggerType,
                            cacheAccums.second.getArmor(gemTriggerType) + socketEffects
                        )
                )
            }
        Log.debug("Updated armor socket command cache. owner=$owner socketCommandCache=${updatedCaches.first}")
        Log.debug("Updated armor socket effect cache. owner=$owner socketEffectCache=${updatedCaches.second}")
        return copy(socketCommandCache = updatedCaches.first, socketEffectCache = updatedCaches.second)
    }

    override fun updateMainHand(): SocketGemCache {
        val clearedSocketCommandCache = socketCommandCache.clearMainHand()
        val clearedSocketEffectCache = socketEffectCache.clearMainHand()
        Log.debug("Cleared main hand socket command and effect cache. owner=$owner")
        val player =
            Bukkit.getPlayer(owner) ?: return copy(
                socketCommandCache = clearedSocketCommandCache,
                socketEffectCache = clearedSocketEffectCache
            )
        val socketGems = GemUtil.getSocketGemsFromItemStackLore(player.equipment?.itemInMainHand)
        return calculateUpdatedMainHandCache(socketGems, clearedSocketCommandCache, clearedSocketEffectCache)
    }

    override fun updateMainHand(itemInMainHand: ItemStack?): SocketGemCache {
        val clearedSocketCommandCache = socketCommandCache.clearMainHand()
        val clearedSocketEffectCache = socketEffectCache.clearMainHand()
        Log.debug("Cleared main hand socket command and effect cache. owner=$owner")
        val socketGems = GemUtil.getSocketGemsFromItemStackLore(itemInMainHand)
        return calculateUpdatedMainHandCache(socketGems, clearedSocketCommandCache, clearedSocketEffectCache)
    }

    override fun updateOffHand(): SocketGemCache {
        val clearedSocketCommandCache = socketCommandCache.clearOffHand()
        val clearedSocketEffectCache = socketEffectCache.clearOffHand()
        Log.debug("Cleared off hand socket command and effect cache. owner=$owner")
        val player =
            Bukkit.getPlayer(owner) ?: return copy(
                socketCommandCache = clearedSocketCommandCache,
                socketEffectCache = clearedSocketEffectCache
            )
        val socketGems = GemUtil.getSocketGemsFromItemStackLore(player.equipment?.itemInOffHand)
        return calculateUpdatedOffHandCache(socketGems, clearedSocketCommandCache, clearedSocketEffectCache)
    }

    override fun updateOffHand(itemInOffHand: ItemStack?): SocketGemCache {
        val clearedSocketCommandCache = socketCommandCache.clearMainHand()
        val clearedSocketEffectCache = socketEffectCache.clearMainHand()
        Log.debug("Cleared off hand socket command and effect cache. owner=$owner")
        val socketGems = GemUtil.getSocketGemsFromItemStackLore(itemInOffHand)
        return calculateUpdatedOffHandCache(socketGems, clearedSocketCommandCache, clearedSocketEffectCache)
    }

    private fun calculateUpdatedMainHandCache(
        socketGems: List<SocketGem>,
        clearedSocketCommandCache: SocketCache<SocketCommand>,
        clearedSocketEffectCache: SocketCache<SocketEffect>
    ): MythicSocketGemCache {
        Log.debug("Updating main hand socket command and effect cache. owner=$owner gems=${socketGems.map { it.name }}")
        val updatedCaches =
            socketGems.bifold(clearedSocketCommandCache, clearedSocketEffectCache) { cacheAccums, socketGem ->
                val gemTriggerType = socketGem.gemTriggerType
                val socketCommands = socketGem.commands
                val socketEffects = socketGem.socketEffects

                cacheAccums.copy(
                    first =
                        cacheAccums.first.setMainHand(
                            gemTriggerType,
                            cacheAccums.first.getMainHand(gemTriggerType) + socketCommands
                        ),
                    second =
                        cacheAccums.second.setMainHand(
                            gemTriggerType,
                            cacheAccums.second.getMainHand(gemTriggerType) + socketEffects
                        )
                )
            }
        Log.debug("Updated main hand socket command cache. owner=$owner socketCommandCache=${updatedCaches.first}")
        Log.debug("Updated main hand socket effect cache. owner=$owner socketEffectCache=${updatedCaches.second}")
        return copy(socketCommandCache = updatedCaches.first, socketEffectCache = updatedCaches.second)
    }

    private fun calculateUpdatedOffHandCache(
        socketGems: List<SocketGem>,
        clearedSocketCommandCache: SocketCache<SocketCommand>,
        clearedSocketEffectCache: SocketCache<SocketEffect>
    ): MythicSocketGemCache {
        Log.debug("Updating off hand socket command and effect cache. owner=$owner gems=${socketGems.map { it.name }}")
        val updatedCaches =
            socketGems.bifold(clearedSocketCommandCache, clearedSocketEffectCache) { cacheAccums, socketGem ->
                val gemTriggerType = socketGem.gemTriggerType
                val socketCommands = socketGem.commands
                val socketEffects = socketGem.socketEffects

                cacheAccums.copy(
                    first =
                        cacheAccums.first.setOffHand(
                            gemTriggerType,
                            cacheAccums.first.getOffHand(gemTriggerType) + socketCommands
                        ),
                    second =
                        cacheAccums.second.setOffHand(
                            gemTriggerType,
                            cacheAccums.second.getOffHand(gemTriggerType) + socketEffects
                        )
                )
            }
        Log.debug("Updated off hand socket command cache. owner=$owner socketCommandCache=${updatedCaches.first}")
        Log.debug("Updated off hand socket effect cache. owner=$owner socketEffectCache=${updatedCaches.second}")
        return copy(socketCommandCache = updatedCaches.first, socketEffectCache = updatedCaches.second)
    }
}
