package net.nunnerycode.bukkit.mythicdrops.socketting;

import net.nunnerycode.bukkit.mythicdrops.MythicDropsPlugin;
import net.nunnerycode.bukkit.mythicdrops.api.items.MythicItemStack;
import net.nunnerycode.bukkit.mythicdrops.utils.StringUtil;

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
                                                                  .getName()}}),
          StringUtil.replaceArgs(MythicDropsPlugin.getInstance()
                                     .getSockettingSettings().getSocketGemLore(),
                                 new String[][]{{"%type%", socketGem.getPresentableType()}}));
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
                                                                                    .getName()}}),
          StringUtil.replaceArgs(MythicDropsPlugin.getInstance()
                                     .getSockettingSettings().getSocketGemLore(),
                                 new String[][]{{"%type%", socketGem.getPresentableType()}}));
  }

}
