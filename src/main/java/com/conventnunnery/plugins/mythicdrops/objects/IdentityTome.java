package com.conventnunnery.plugins.mythicdrops.objects;

import com.conventnunnery.plugins.mythicdrops.MythicDrops;
import com.conventnunnery.plugins.mythicdrops.objects.parents.MythicTome;
import com.conventnunnery.plugins.mythicdrops.utils.ContainerUtils;
import org.bukkit.ChatColor;

public class IdentityTome extends MythicTome {
	public IdentityTome() {
		super(TomeType.ENCHANTED_BOOK,
				MythicDrops.getInstance().getLanguageManager().getMessage("items.identity-tome.name"),
				ChatColor.MAGIC + "Herobrine",
				ContainerUtils.toStringArray(
						MythicDrops.getInstance().getLanguageManager().getStringList("items.identity-tome.lore")),
				new String[0]);
	}
}
