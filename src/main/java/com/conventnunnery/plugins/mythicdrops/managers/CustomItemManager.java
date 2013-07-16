package com.conventnunnery.plugins.mythicdrops.managers;

import com.conventnunnery.libraries.utils.RandomUtils;
import com.conventnunnery.plugins.mythicdrops.MythicDrops;
import com.conventnunnery.plugins.mythicdrops.api.items.CustomItem;
import com.conventnunnery.plugins.mythicdrops.api.tiers.Tier;

import java.util.HashSet;
import java.util.Set;

public class CustomItemManager {
    private final MythicDrops plugin;
    private final Set<CustomItem> customItems;

    public CustomItemManager(MythicDrops plugin) {
        this.plugin = plugin;
        customItems = new HashSet<CustomItem>();
    }

    public MythicDrops getPlugin() {
        return plugin;
    }

    public Set<CustomItem> getCustomItems() {
        return customItems;
    }

    public CustomItem getCustomItemFromDisplayName(String name) {
        for (CustomItem ci : customItems) {
            if (ci.getDisplayName().equalsIgnoreCase(name)) {
                return ci;
            }
        }
        throw new NullPointerException(name + " is not the display name of a loaded Custom Item");
    }

    public CustomItem getCustomItemFromName(String name) {
        for (CustomItem ci : customItems) {
            if (ci.getName().equalsIgnoreCase(name)) {
                return ci;
            }
        }
        throw new NullPointerException(name + " is not the name of a loaded Custom Item");
    }

    public CustomItem getRandomCustomItemFromSetWithChance(Set<CustomItem> tiers) {
        CustomItem customItem = null;
        CustomItem c = null;
        Set<CustomItem> zeroChanceCustomItems = new HashSet<CustomItem>();
        while (customItem == null && zeroChanceCustomItems.size() != tiers.size()) {
            c = getRandomCustomItemFromSet(tiers);
            if (c.getChanceToBeGivenToAMonster() <= 0.0) {
                zeroChanceCustomItems.add(c);
                continue;
            }
            if (RandomUtils.randomRangeDecimalInclusive(0.0, 1.0) <= c.getChanceToBeGivenToAMonster()) {
                customItem = c;
            }
        }
        return customItem;
    }

    public CustomItem getRandomCustomItem() {
        return getRandomCustomItemFromSet(customItems);
    }

    public CustomItem getRandomCustomItemFromSet(Set<CustomItem> items) {
        CustomItem[] array = items.toArray(new CustomItem[items.size()]);
        return array[(int) RandomUtils.randomRangeWholeExclusive(0, array.length)];
    }
}
