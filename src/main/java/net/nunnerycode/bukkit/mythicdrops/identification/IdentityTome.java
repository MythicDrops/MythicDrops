package net.nunnerycode.bukkit.mythicdrops.identification;

import net.nunnerycode.bukkit.mythicdrops.MythicDropsPlugin;
import net.nunnerycode.bukkit.mythicdrops.api.items.MythicTome;
import org.bukkit.ChatColor;

public final class IdentityTome extends MythicTome {

    public IdentityTome() {
        super(MythicTome.TomeType.ENCHANTED_BOOK,
                MythicDropsPlugin.getInstance().getIdentifyingSettings().getIdentityTomeName(),
                ChatColor.MAGIC + "Herobrine",
                MythicDropsPlugin.getInstance().getIdentifyingSettings().getIdentityTomeLore(),
                new String[0]);
    }

}
