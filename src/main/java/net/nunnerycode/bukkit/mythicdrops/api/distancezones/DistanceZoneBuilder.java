package net.nunnerycode.bukkit.mythicdrops.api.distancezones;

import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;

import java.util.Map;

public interface DistanceZoneBuilder {
    DistanceZoneBuilder withStartingDistance(double d);

    DistanceZoneBuilder withEndDistance(double d);

    DistanceZoneBuilder withTierMap(Map<Tier, Double> tierMap);

    DistanceZone build();
}
