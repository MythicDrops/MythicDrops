/**
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2013 Richard Harrah
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
package com.tealcube.minecraft.bukkit.mythicdrops.events;

import com.tealcube.minecraft.bukkit.mythicdrops.api.events.MythicDropsCancellableEvent;
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGenerationReason;
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier;
import org.bukkit.inventory.ItemStack;

public class RandomItemGenerationEvent extends MythicDropsCancellableEvent {

    private Tier tier;
    private ItemStack itemStack;
    private ItemGenerationReason reason;
    private boolean modified;

    public RandomItemGenerationEvent(Tier tier, ItemStack itemStack, ItemGenerationReason reason) {
        this.tier = tier;
        this.itemStack = itemStack;
        this.reason = reason;
        modified = false;
    }

    public Tier getTier() {
        return tier;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        setItemStack(itemStack, true);
    }

    public void setItemStack(ItemStack itemStack, boolean modified) {
        this.itemStack = itemStack;
        if (modified) {
            this.modified = true;
        }
    }

    public ItemGenerationReason getReason() {
        return reason;
    }

    public boolean isModified() {
        return modified;
    }
}
