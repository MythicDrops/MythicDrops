package com.tealcube.minecraft.bukkit.mythicdrops.items;

/*
 * #%L
 * MythicDrops
 * %%
 * Copyright (C) 2013 - 2015 TealCube
 * %%
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
 * provided that the above copyright notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
 * INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF
 * THIS SOFTWARE.
 * #L%
 */


import com.tealcube.minecraft.bukkit.mythicdrops.api.items.CustomItem;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.material.MaterialData;

import java.util.List;
import java.util.Map;

public final class CustomItemBuilder {

    public final MythicCustomItem customItem;

    public CustomItemBuilder(String name) {
        customItem = new MythicCustomItem(name);
    }

    public CustomItemBuilder withDisplayName(String displayName) {
        customItem.setDisplayName(displayName);
        return this;
    }

    public CustomItemBuilder withLore(List<String> lore) {
        customItem.setLore(lore);
        return this;
    }

    public CustomItemBuilder withEnchantments(Map<Enchantment, Integer> enchantments) {
        customItem.setEnchantments(enchantments);
        return this;
    }

    @Deprecated
    public CustomItemBuilder withMaterialData(MaterialData materialData) {
        // do nothing
        return this;
    }

    public CustomItemBuilder withMaterial(Material material) {
        customItem.setMaterial(material);
        return this;
    }

    public CustomItemBuilder withChanceToBeGivenToMonster(double chance) {
        customItem.setChanceToBeGivenToAMonster(chance);
        return this;
    }

    public CustomItemBuilder withChanceToDropOnDeath(double chance) {
        customItem.setChanceToDropOnDeath(chance);
        return this;
    }

    public CustomItemBuilder withBroadcastOnFind(boolean b) {
        customItem.setBroadcastOnFind(b);
        return this;
    }

    public CustomItem build() {
        return customItem;
    }
}
