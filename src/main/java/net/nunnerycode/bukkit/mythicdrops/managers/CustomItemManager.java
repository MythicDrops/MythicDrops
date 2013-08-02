package net.nunnerycode.bukkit.mythicdrops.managers;

import net.nunnerycode.bukkit.libraries.utils.RandomRangeUtils;
import net.nunnerycode.bukkit.mythicdrops.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.items.CustomItem;
import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

public final class CustomItemManager {
    private final MythicDrops plugin;
    private final Set<CustomItem> customItems;

    public CustomItemManager(MythicDrops plugin) {
        this.plugin = plugin;
        customItems = new HashSet<CustomItem>();
    }

    public Set<CustomItem> getCustomItems() {
        return customItems;
    }

    public void debugCustomItems() {
        getPlugin().debug(Level.INFO, "Loaded custom items size: " + customItems.size());
    }

    public MythicDrops getPlugin() {
        return plugin;
    }

    public CustomItem getCustomItemFromItemStack(ItemStack itemStack) {
        for (CustomItem ci : customItems) {
            if (ci.toItemStack().isSimilar(itemStack)) {
                return ci;
            }
        }
        return null;
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

    public CustomItem getRandomCustomItem() {
        return getRandomCustomItemFromSet(customItems);
    }

    public CustomItem getRandomCustomItemFromSet(Set<CustomItem> items) {
        CustomItem[] array = items.toArray(new CustomItem[items.size()]);
        return array[RandomUtils.nextInt(array.length)];
    }

    public CustomItem getRandomCustomItemWithChance() {
        return getRandomCustomItemFromSetWithChance(customItems);
    }

    public CustomItem getRandomCustomItemFromSetWithChance(Set<CustomItem> tiers) {
        CustomItem customItem = null;
        CustomItem c;
        Set<CustomItem> zeroChanceCustomItems = new HashSet<CustomItem>();
        while (customItem == null && zeroChanceCustomItems.size() != tiers.size()) {
            c = getRandomCustomItemFromSet(tiers);
            if (c.getChanceToBeGivenToAMonster() <= 0.0) {
                zeroChanceCustomItems.add(c);
                continue;
            }
            if (RandomRangeUtils.randomRangeDoubleInclusive(0.0,
                    1.0) <= c.getChanceToBeGivenToAMonster()) {
                customItem = c;
            }
        }
        return customItem;
    }
}
