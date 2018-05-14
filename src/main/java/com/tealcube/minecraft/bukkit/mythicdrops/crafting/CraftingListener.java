/*
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
package com.tealcube.minecraft.bukkit.mythicdrops.crafting;

import com.tealcube.minecraft.bukkit.mythicdrops.MythicDropsPlugin;
import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDrops;
import com.tealcube.minecraft.bukkit.mythicdrops.socketting.SocketGem;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.SocketGemUtil;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.StringUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class CraftingListener implements Listener {

    private MythicDrops plugin;

    public CraftingListener(MythicDrops plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onItemCraftEvent(CraftItemEvent event) {
        String replaceString = plugin.getSockettingSettings().getSocketGemName().replace('&',
                '\u00A7')
                .replace("\u00A7\u00A7", "&").replaceAll("%(?s)(.*?)%", "").replaceAll("\\s+", " ")
                .trim();
        String[] splitString = ChatColor.stripColor(replaceString).split(" ");
        for (ItemStack is : event.getInventory().getMatrix()) {
            if (is == null) {
                continue;
            }
            if (is.getType() == Material.AIR) {
                continue;
            }
            if (!is.hasItemMeta()) {
                continue;
            }
            ItemMeta im = is.getItemMeta();
            if (!im.hasDisplayName()) {
                continue;
            }
            String displayName = im.getDisplayName();
            String colorlessName = ChatColor.stripColor(displayName);

            for (String s : splitString) {
                if (colorlessName.contains(s)) {
                    colorlessName = colorlessName.replace(s, "");
                }
            }

            colorlessName = colorlessName.replaceAll("\\s+", " ").trim();

            SocketGem socketGem = SocketGemUtil.getSocketGemFromName(colorlessName);
            if (socketGem == null) {
                continue;
            }
            String otherName = StringUtil.replaceArgs(MythicDropsPlugin
                            .getInstance()
                            .getSockettingSettings()
                            .getSocketGemName(),
                    new String[][]{
                            {"%socketgem%",
                                    socketGem
                                            .getName()}}
            )
                    .replace('&', '\u00A7')
                    .replace("\u00A7\u00A7", "&");
            if (displayName.equals(otherName)) {
                event.setCancelled(true);
                return;
            }
        }
    }

}
