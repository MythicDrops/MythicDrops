package net.nunnerycode.bukkit.mythicdrops.socketting;

import net.nunnerycode.bukkit.mythicdrops.MythicDropsPlugin;
import net.nunnerycode.bukkit.mythicdrops.api.items.MythicItemStack;
import org.bukkit.ChatColor;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;

public final class SocketItem extends MythicItemStack {

	public SocketItem(MaterialData materialData, SocketGem socketGem) {
		super(materialData.getItemType(), 1, (short) 0, ChatColor.GOLD + replaceArgs
				(MythicDropsPlugin.getInstance().getSockettingSettings().getSocketGemName(), new String[][]{{"%socketgem%",
						socketGem.getName()}}) + ChatColor.GOLD, replaceArgs(MythicDropsPlugin.getInstance()
				.getSockettingSettings().getSocketGemLore(), new String[][]{{"%type%", socketGem.getPresentableType()}}));
	}

	private static String replaceArgs(String string, String[][] args) {
		String s = string;
		for (String[] arg : args) {
			s = s.replace(arg[0], arg[1]);
		}
		return s;
	}

	private static List<String> replaceArgs(List<String> strings, String[][] args) {
		List<String> list = new ArrayList<>();
		for (String s : strings) {
			list.add(replaceArgs(s, args));
		}
		return list;
	}

}
