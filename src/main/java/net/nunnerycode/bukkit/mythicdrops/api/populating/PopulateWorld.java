package net.nunnerycode.bukkit.mythicdrops.api.populating;

import java.util.List;

public interface PopulateWorld {

    String getName();

    boolean isEnabled();

    double getChance();

    int getMinimumItems();

    int getMaximumItems();

    boolean isOverwriteContents();

    List<String> getTiers();

}
