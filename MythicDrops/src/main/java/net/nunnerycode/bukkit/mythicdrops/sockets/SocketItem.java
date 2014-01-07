package net.nunnerycode.bukkit.mythicdrops.sockets;

import net.nunnerycode.bukkit.mythicdrops.api.items.MythicItemStack;
import org.bukkit.ChatColor;
import org.bukkit.material.MaterialData;

public class SocketItem extends MythicItemStack {

	public SocketItem(MaterialData materialData, SocketGem socketGem) {
		super(materialData.getItemType(), 1, (short) 0, ChatColor.GOLD + MythicDropsSockets.getInstance().replaceArgs
				(MythicDropsSockets.getInstance().getSocketGemName(), new String[][]{{"%socketgem%",
						socketGem.getName()}}) + ChatColor.GOLD, MythicDropsSockets.getInstance().replaceArgs
				(MythicDropsSockets.getInstance().getSocketGemLore(), new String[][]{{"%type%",
						socketGem.getPresentableType()}}));
	}

}
