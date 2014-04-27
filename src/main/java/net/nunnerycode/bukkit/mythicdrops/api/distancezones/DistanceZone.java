package net.nunnerycode.bukkit.mythicdrops.api.distancezones;

import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;

import java.util.Map;

public interface DistanceZone {

    /**
     * Gets and returns the identifier key of the DistanceZone.
     *
     * @return identifier key
     */
    String getKey();

    /**
     * Gets and returns the starting distance of the DistanceZone.
     *
     * @return starting distance
     */
    double getStartDistance();

    /**
     * Gets and returns the end distance of the DistanceZone.
     *
     * @return end distance
     */
    double getEndDistance();

    /**
     * Gets and returns a copy of the {@link Tier}s and their chances that can be dropped in this DistanceZone.
     *
     * @return copy of Tiers and chances
     */
    Map<Tier, Double> getTierMap();

}
