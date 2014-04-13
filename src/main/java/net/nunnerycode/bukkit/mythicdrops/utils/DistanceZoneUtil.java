package net.nunnerycode.bukkit.mythicdrops.utils;

import net.nunnerycode.bukkit.mythicdrops.api.distancezones.DistanceZone;
import net.nunnerycode.bukkit.mythicdrops.distancezones.DistanceZoneSet;
import org.apache.commons.lang3.Validate;
import org.bukkit.Location;

public final class DistanceZoneUtil {

    private DistanceZoneUtil() {
        // do nothing
    }

    public static DistanceZone getDistanceZone(Location location) {
        Validate.notNull(location, "Location cannot be null");
        Location worldSpawn = location.getWorld().getSpawnLocation();
        double distance = location.distance(worldSpawn);
        for (DistanceZone dz : DistanceZoneSet.getInstance()) {
            if (distance >= dz.getStartDistance() && distance <= dz.getEndDistance()) {
                return dz;
            }
        }
        return null;
    }

}
