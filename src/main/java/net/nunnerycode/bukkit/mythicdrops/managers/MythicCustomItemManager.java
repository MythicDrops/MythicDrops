package net.nunnerycode.bukkit.mythicdrops.managers;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import net.nunnerycode.bukkit.mythicdrops.api.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.items.CustomItem;
import net.nunnerycode.bukkit.mythicdrops.api.managers.CustomItemManager;
import net.nunnerycode.bukkit.mythicdrops.utils.RandomRangeUtils;
import org.bukkit.inventory.ItemStack;

public class MythicCustomItemManager implements CustomItemManager {
    private final MythicDrops plugin;
    private final Set<CustomItem> customItems;

    public MythicCustomItemManager(MythicDrops plugin) {
        this.plugin = plugin;
        customItems = new LinkedHashSet<CustomItem>();
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

	@Override
	public CustomItem createCustomItemFromItemStack(ItemStack itemStack) {
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
        return getRandomCustomItem(customItems);
    }

    public CustomItem getRandomCustomItemWithChance() {
        return getRandomCustomItemWithChance(customItems);
    }

	@Override
	public CustomItem getRandomCustomItem(Set<CustomItem> set) {
		CustomItem[] array = set.toArray(new CustomItem[set.size()]);
		return array[(int) RandomRangeUtils.randomRangeLongExclusive(0, array.length)];
	}

	@Override
	public CustomItem getRandomCustomItemWithChance(Set<CustomItem> set) {
		CustomItem customItem = null;
		CustomItem c;
		Set<CustomItem> zeroChanceCustomItems = new HashSet<CustomItem>();
		while (customItem == null && zeroChanceCustomItems.size() != set.size()) {
			c = getRandomCustomItem(set);
			if (c.getChanceToBeGivenToAMonster() <= 0.0) {
				zeroChanceCustomItems.add(c);
				continue;
			}
			if (RandomRangeUtils.randomRangeDoubleInclusive(0.0, 1.0) <= c.getChanceToBeGivenToAMonster()) {
				customItem = c;
			}
		}
		return customItem;
	}

}