package net.nunnerycode.bukkit.mythicdrops.repair;

import net.nunnerycode.bukkit.mythicdrops.api.repair.RepairItem;

import java.util.concurrent.ConcurrentHashMap;

public class MythicRepairItemMap extends ConcurrentHashMap<String, RepairItem> {

    private static final MythicRepairItemMap INSTANCE = new MythicRepairItemMap();

    public static MythicRepairItemMap getInstance() {
        return INSTANCE;
    }

}
