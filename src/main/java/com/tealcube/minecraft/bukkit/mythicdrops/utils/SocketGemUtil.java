package com.tealcube.minecraft.bukkit.mythicdrops.utils;

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


import com.tealcube.minecraft.bukkit.mythicdrops.MythicDropsPlugin;
import com.tealcube.minecraft.bukkit.mythicdrops.socketting.SocketGem;

import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class SocketGemUtil {

    private SocketGemUtil() {
        // do nothing;
    }

    public static SocketGem getSocketGemFromItemStack(ItemStack itemStack) {
        SocketGem sg;
        if (!MythicDropsPlugin.getInstance().getSockettingSettings().getSocketGemMaterials().contains
            (itemStack.getType())) {
            return null;
        }
        if (!itemStack.hasItemMeta() || !itemStack.getItemMeta().hasDisplayName()) {
            return null;
        }
        String
            replacedArgs =
            ChatColor.stripColor(StringUtil.replaceArgs(MythicDropsPlugin.getInstance()
                                                            .getSockettingSettings()
                                                            .getSocketGemName(),
                                                        new String[][]{{"%socketgem%", ""}}
            )
                                     .replace('&', '\u00A7')
                                     .replace("\u00A7\u00A7", "&"));
        String type = ChatColor.stripColor(
            itemStack.getItemMeta().getDisplayName().replace(replacedArgs, ""));
        if (type == null) {
            return null;
        }
        sg = MythicDropsPlugin.getInstance().getSockettingSettings().getSocketGemMap().get(type);
        if (sg == null) {
            sg = SocketGemUtil.getSocketGemFromName(type);
        }
        return sg;
    }

    public static SocketGem getSocketGemFromName(String name) {
        for (SocketGem sg : MythicDropsPlugin.getInstance().getSockettingSettings().getSocketGemMap()
            .values()) {
            if (sg.getName().equalsIgnoreCase(name) || sg.getName().equalsIgnoreCase(name.replace("_", " "))) {
                return sg;
            }
        }
        return null;
    }

    public static SocketGem getRandomSocketGemWithChance() {
        Map<String, SocketGem>
            socketGemMap =
            MythicDropsPlugin.getInstance().getSockettingSettings().getSocketGemMap
                ();
        if (socketGemMap == null || socketGemMap.isEmpty()) {
            return null;
        }
        double totalWeight = 0;
        for (SocketGem sg : socketGemMap.values()) {
            totalWeight += sg.getChance();
        }

        double chosenWeight = MythicDropsPlugin.getInstance().getRandom().nextDouble() * totalWeight;

        double currentWeight = 0;

        List<SocketGem> l = new ArrayList<>(socketGemMap.values());
        Collections.shuffle(l);

        for (SocketGem sg : socketGemMap.values()) {
            currentWeight += sg.getChance();

            if (currentWeight >= chosenWeight) {
                return sg;
            }
        }
        return null;
    }

    public static Material getRandomSocketGemMaterial() {
        List<Material> materialDatas = MythicDropsPlugin.getInstance().getSockettingSettings()
            .getSocketGemMaterials();
        if (materialDatas == null || materialDatas.isEmpty()) {
            return null;
        }
        return materialDatas.get(RandomUtils.nextInt(materialDatas.size()));
    }

}
