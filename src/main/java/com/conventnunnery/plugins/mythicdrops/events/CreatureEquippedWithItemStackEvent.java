/*
 * Copyright (c) 2013. ToppleTheNun
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.conventnunnery.plugins.mythicdrops.events;

import com.conventnunnery.plugins.mythicdrops.events.parents.MythicDropsCancellableEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public class CreatureEquippedWithItemStackEvent extends MythicDropsCancellableEvent {

    private final LivingEntity entity;
    private ItemStack itemStack;

    public CreatureEquippedWithItemStackEvent(LivingEntity entity, ItemStack itemStack) {
        this.entity = entity;
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public LivingEntity getEntity() {
        return entity;
    }

}
