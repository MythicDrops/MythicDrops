package net.nunnerycode.bukkit.mythicdrops.distancezones;

import net.nunnerycode.bukkit.mythicdrops.api.distancezones.DistanceZone;

import java.util.HashSet;

public final class DistanceZoneSet extends HashSet<DistanceZone> {

    private static final DistanceZoneSet INSTANCE = new DistanceZoneSet();

    private DistanceZoneSet() {
        // do nothing
    }

    public static DistanceZoneSet getInstance() {
        return INSTANCE;
    }

}
