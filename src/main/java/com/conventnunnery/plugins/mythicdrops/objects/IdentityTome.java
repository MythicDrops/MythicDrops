package com.conventnunnery.plugins.mythicdrops.objects;

import com.conventnunnery.plugins.conventlib.utils.CollectionUtils;
import com.conventnunnery.plugins.mythicdrops.MythicDrops;
import com.conventnunnery.plugins.mythicdrops.objects.parents.MythicTome;
import org.bukkit.ChatColor;

public class IdentityTome extends MythicTome {
    public IdentityTome() {
        super(TomeType.ENCHANTED_BOOK,
                MythicDrops.getInstance().getLanguageManager().getMessage("items.identity-tome.name"),
                ChatColor.MAGIC + "Herobrine",
                CollectionUtils.toStringArray(
                        MythicDrops.getInstance().getLanguageManager().getStringList("items.identity-tome.lore")),
                new String[0]);
    }
}
