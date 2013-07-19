/*
 * Copyright (C) 2013 Richard Harrah
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated 
 * documentation files (the "Software"), to deal in the Software without restriction, 
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, 
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.nunnerycode.bukkit.mythicdrops.api.tiers;

import net.nunnerycode.bukkit.mythicdrops.api.items.MythicEnchantment;
import org.bukkit.ChatColor;

import java.util.Set;

public interface Tier {

    /**
     * Gets the Set of item groups that can spawn for this Tier.
     *
     * @return allowed item groups
     */
    Set<String> getAllowedGroups();

    /**
     * Gets the Set of item groups that cannot spawn for this Tier.
     *
     * @return disallowed item groups
     */
    Set<String> getDisallowedGroups();

    /**
     * Gets the Set of item ids that can spawn for this Tier.
     *
     * @return allowed item ids
     */
    Set<String> getAllowedIds();

    /**
     * Gets the Set of item ids that cannot spawn for this Tier.
     *
     * @return disallowed ids
     */
    Set<String> getDisallowedIds();

    /**
     * Gets the name of the Tier.
     *
     * @return name of the Tier
     */
    String getTierName();

    /**
     * Gets the display name of the Tier.
     *
     * @return the display name of the Tier
     */
    String getTierDisplayName();

    /**
     * Gets the display color for the Tier.
     *
     * @return the display color of the Tier
     */
    ChatColor getTierDisplayColor();

    /**
     * Gets the identification color for the Tier.
     *
     * @return the identification color of the Tier
     */
    ChatColor getTierIdentificationColor();

    /**
     * Returns true if base {@link org.bukkit.enchantments.Enchantment}s have to be able to go on the item naturally to
     * be applied.
     *
     * @return if base Enchantments must be safe
     */
    boolean isSafeBaseEnchantments();

    /**
     * Returns true if bonus {@link org.bukkit.enchantments.Enchantment}s have to be able to go on the item naturally
     * tobe applied.
     *
     * @return if bonus Enchantments must be safe
     */
    boolean isSafeBonusEnchantments();

    /**
     * Returns true if base {@link org.bukkit.enchantments.Enchantment}s can go above their normal level limit.
     *
     * @return if base Enchantments can be high
     */
    boolean isAllowHighBaseEnchantments();

    /**
     * Returns true if bonus {@link org.bukkit.enchantments.Enchantment}s can go above their normal level limit.
     *
     * @return if bonus Enchantments can be high
     */
    boolean isAllowHighBonusEnchantments();

    /**
     * Returns true if the Tier can spawn with sockets.
     *
     * @return if Tier can spawn with sockets
     */
    boolean isAllowSpawningWithSockets();

    /**
     * Gets the smallest amount of bonus {@link org.bukkit.enchantments.Enchantment}s that are possible.
     *
     * @return smallest amount of bonus Enchantments
     */
    int getMinimumAmountOfBonusEnchantments();

    /**
     * Gets the largest amount of bonus {@link org.bukkit.enchantments.Enchantment}s that are possible.
     *
     * @return largest amount of bonus Enchantments
     */
    int getMaximumAmountOfBonusEnchantments();

    /**
     * Gets the smallest amount of sockets that can spawn for the Tier.
     *
     * @return smallest amount of sockets possible
     */
    int getMinimumAmountOfSockets();

    /**
     * Gets the largest amount of sockets that can spawn for the Tier.
     *
     * @return largest amount of sockets possible
     */
    int getMaximumAmountOfSockets();

    /**
     * Gets the chance for the Tier to spawn on a monster.
     *
     * @return chance for the Tier to spawn
     */
    double getChanceToSpawnOnAMonster();

    /**
     * Gets the chance for the Tier to drop on a monster's death.
     *
     * @return chance for the Tier to drop
     */
    double getChanceToDropOnMonsterDeath();

    /**
     * Gets the chance for the Tier to be identified with an Identity Tome.
     *
     * @return chance for the Tier to be identified
     */
    double getChanceToBeIdentified();

    /**
     * Gets the smallest percentage of durability an item of this Tier can spawn with.
     *
     * @return smallest percentage of durability
     */
    double getMinimumDurabilityPercentage();

    /**
     * Gets the largest percentage of durability an item of this Tier can spawn with.
     *
     * @return largest percentage of durability
     */
    double getMaximumDurabilityPercentage();

    Set<MythicEnchantment> getBaseEnchantments();

    Set<MythicEnchantment> getBonusEnchantments();
}
