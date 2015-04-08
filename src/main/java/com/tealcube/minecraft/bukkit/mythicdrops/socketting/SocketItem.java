package com.tealcube.minecraft.bukkit.mythicdrops.socketting;

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
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.MythicItemStack;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.StringListUtil;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.StringUtil;

import org.bukkit.Material;
import org.bukkit.material.MaterialData;

public final class SocketItem extends MythicItemStack {

    public SocketItem(Material material, SocketGem socketGem) {
        super(material, 1, (short) 0, StringUtil.replaceArgs(MythicDropsPlugin
                                                                 .getInstance()
                                                                 .getSockettingSettings()
                                                                 .getSocketGemName(),
                                                             new String[][]{
                                                                 {"%socketgem%",
                                                                  socketGem
                                                                      .getName()}}
              ),
              StringListUtil.replaceArgs(MythicDropsPlugin.getInstance()
                                             .getSockettingSettings().getSocketGemLore(),
                                         new String[][]{{"%type%", socketGem.getPresentableType()}}
              )
        );
    }

    @Deprecated
    public SocketItem(MaterialData materialData, SocketGem socketGem) {
        super(materialData.getItemType(), 1, (short) 0, StringUtil.replaceArgs(MythicDropsPlugin
                                                                                   .getInstance()
                                                                                   .getSockettingSettings()
                                                                                   .getSocketGemName(),
                                                                               new String[][]{
                                                                                   {"%socketgem%",
                                                                                    socketGem
                                                                                        .getName()}}
              ),
              StringListUtil.replaceArgs(MythicDropsPlugin.getInstance()
                                             .getSockettingSettings().getSocketGemLore(),
                                         new String[][]{{"%type%", socketGem.getPresentableType()}}
              )
        );
    }

}
