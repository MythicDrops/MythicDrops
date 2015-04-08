package com.tealcube.minecraft.bukkit.mythicdrops.api.items.builders;

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


import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGenerationReason;
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public interface DropBuilder {

    DropBuilder withTier(Tier tier);

    DropBuilder withTier(String tierName);

    DropBuilder withMaterial(Material material);

    @Deprecated
    DropBuilder withMaterialData(MaterialData materialData);

    @Deprecated
    DropBuilder withMaterialData(String materialDataString);

    DropBuilder withItemGenerationReason(ItemGenerationReason reason);

    @Deprecated
    DropBuilder inWorld(World world);

    @Deprecated
    DropBuilder inWorld(String worldName);

    DropBuilder useDurability(boolean b);

    ItemStack build();

}
