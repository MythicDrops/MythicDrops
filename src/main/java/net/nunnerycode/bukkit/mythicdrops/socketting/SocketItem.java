package net.nunnerycode.bukkit.mythicdrops.socketting;

import net.nunnerycode.bukkit.libraries.ivory.utils.StringListUtils;
import net.nunnerycode.bukkit.libraries.ivory.utils.StringUtils;
import net.nunnerycode.bukkit.mythicdrops.MythicDropsPlugin;
import net.nunnerycode.bukkit.mythicdrops.api.items.MythicItemStack;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

public final class SocketItem extends MythicItemStack {

    public SocketItem(Material material, SocketGem socketGem) {
        super(material, 1, (short) 0, StringUtils.replaceArgs(MythicDropsPlugin
                                .getInstance()
                                .getSockettingSettings()
                                .getSocketGemName(),
                        new String[][]{
                                {"%socketgem%",
                                        socketGem
                                                .getName()}}
                ),
                StringListUtils.replaceArgs(MythicDropsPlugin.getInstance()
                                .getSockettingSettings().getSocketGemLore(),
                        new String[][]{{"%type%", socketGem.getPresentableType()}}
                )
        );
    }

    @Deprecated
    public SocketItem(MaterialData materialData, SocketGem socketGem) {
        super(materialData.getItemType(), 1, (short) 0, StringUtils.replaceArgs(MythicDropsPlugin
                                .getInstance()
                                .getSockettingSettings()
                                .getSocketGemName(),
                        new String[][]{
                                {"%socketgem%",
                                        socketGem
                                                .getName()}}
                ),
                StringListUtils.replaceArgs(MythicDropsPlugin.getInstance()
                                .getSockettingSettings().getSocketGemLore(),
                        new String[][]{{"%type%", socketGem.getPresentableType()}}
                )
        );
    }

}
