package net.nunnerycode.bukkit.mythicdrops.socketting;

import net.nunnerycode.bukkit.mythicdrops.MythicDropsPlugin;
import net.nunnerycode.bukkit.mythicdrops.api.items.MythicItemStack;
import net.nunnerycode.bukkit.mythicdrops.utils.StringUtil;
import org.bukkit.ChatColor;
import org.bukkit.material.MaterialData;

public final class SocketItem extends MythicItemStack {

	public SocketItem(MaterialData materialData, SocketGem socketGem) {
		super(materialData.getItemType(), 1, (short) 0, ChatColor.GOLD + StringUtil.replaceArgs(MythicDropsPlugin
				.getInstance().getSockettingSettings().getSocketGemName(), new String[][]{{"%socketgem%",
				socketGem.getName()}}) + ChatColor.GOLD, StringUtil.replaceArgs(MythicDropsPlugin.getInstance()
				.getSockettingSettings().getSocketGemLore(), new String[][]{{"%type%", socketGem.getPresentableType()}}));
	}

}
