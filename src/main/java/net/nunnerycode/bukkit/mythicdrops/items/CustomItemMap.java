package net.nunnerycode.bukkit.mythicdrops.items;

import net.nunnerycode.bukkit.mythicdrops.MythicDropsPlugin;
import net.nunnerycode.bukkit.mythicdrops.api.items.CustomItem;
import org.apache.commons.lang.math.RandomUtils;

import java.util.concurrent.ConcurrentHashMap;

/**
 * An extension of {@link ConcurrentHashMap} designed to allow easy developer access to {@link CustomItem}s.
 */
public final class CustomItemMap extends ConcurrentHashMap<String, CustomItem> {

    private static final CustomItemMap _INSTANCE = new CustomItemMap();

    private CustomItemMap() {
        // do nothing
    }

    /**
     * Gets the instance of CustomItemMap running on the server.
     *
     * @return instance running on the server
     */
    public static CustomItemMap getInstance() {
        return _INSTANCE;
    }

    /**
     * Gets a random {@link CustomItem} out of the ones loaded on the server using chance.
     *
     * @return random CustomItem
     */
    public CustomItem getRandom() {
        CustomItem[] valueArray = values().toArray(new CustomItem[values().size()]);
        return valueArray[RandomUtils.nextInt(values().size())];
    }

    /**
     * Gets a random {@link CustomItem} out of the ones loaded on the server using chance. Returns null if none found.
     *
     * @return random CustomItem
     */
    public CustomItem getRandomWithChance() {
        double totalWeight = 0;
        for (CustomItem ci : values()) {
            totalWeight += ci.getChanceToBeGivenToAMonster();
        }

        double chosenWeight = MythicDropsPlugin.getInstance().getRandom().nextDouble() * totalWeight;

        double currentWeight = 0;
        for (CustomItem ci : values()) {
            currentWeight += ci.getChanceToBeGivenToAMonster();

            if (currentWeight >= chosenWeight) {
                return ci;
            }
        }

        return null;
    }

}
