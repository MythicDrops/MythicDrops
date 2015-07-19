package com.tealcube.minecraft.bukkit.mythicdrops.items;

/*
 * #%L
 * MythicDrops
 * %%
 * Copyright (C) 2013 - 2015 TealCube
 * %%
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby
 * granted,
 * provided that the above copyright notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
 * INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER
 * IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
 * PERFORMANCE OF
 * THIS SOFTWARE.
 * #L%
 */


import com.tealcube.minecraft.bukkit.mythicdrops.MythicDropsPlugin;
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.CustomItem;
import org.apache.commons.lang.math.RandomUtils;

import java.util.concurrent.ConcurrentHashMap;

/**
 * An extension of {@link ConcurrentHashMap} designed to allow easy developer access to {@link CustomItem}s.
 */
public final class CustomItemMap extends ConcurrentHashMap<String, CustomItem> {

    private static final CustomItemMap _INSTANCE = new CustomItemMap();

    private CustomItemMap() {
        // do nothing
    }

    /**
     * Gets the instance of CustomItemMap running on the server.
     *
     * @return instance running on the server
     */
    public static CustomItemMap getInstance() {
        return _INSTANCE;
    }

    /**
     * Gets a random {@link CustomItem} out of the ones loaded on the server using chance.
     *
     * @return random CustomItem
     */
    public CustomItem getRandom() {
        CustomItem[] valueArray = values().toArray(new CustomItem[values().size()]);
        return valueArray[RandomUtils.nextInt(values().size())];
    }

    /**
     * Gets a random {@link CustomItem} out of the ones loaded on the server using chance. Returns null if none found.
     *
     * @return random CustomItem
     */
    public CustomItem getRandomWithChance() {
        double totalWeight = 0;
        for (CustomItem ci : values()) {
            totalWeight += ci.getChanceToBeGivenToAMonster();
        }

        double chosenWeight = MythicDropsPlugin.getInstance().getRandom().nextDouble() * totalWeight;

        double currentWeight = 0;
        for (CustomItem ci : values()) {
            currentWeight += ci.getChanceToBeGivenToAMonster();

            if (currentWeight >= chosenWeight) {
                return ci;
            }
        }

        return null;
    }

}
