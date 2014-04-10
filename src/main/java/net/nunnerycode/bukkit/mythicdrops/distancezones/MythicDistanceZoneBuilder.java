package net.nunnerycode.bukkit.mythicdrops.distancezones;

import net.nunnerycode.bukkit.mythicdrops.api.distancezones.DistanceZone;
import net.nunnerycode.bukkit.mythicdrops.api.distancezones.DistanceZoneBuilder;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;

import java.util.Map;

public class MythicDistanceZoneBuilder implements DistanceZoneBuilder {

    private MythicDistanceZone distanceZone;

    public MythicDistanceZoneBuilder(String name) {
        distanceZone = new MythicDistanceZone(name);
    }

    @Override
    public DistanceZoneBuilder withStartingDistance(double d) {
        distanceZone.setStartDistance(d);
        return this;
    }

    @Override
    public DistanceZoneBuilder withEndDistance(double d) {
        distanceZone.setEndDistance(d);
        return this;
    }

    @Override
    public DistanceZoneBuilder withTierMap(Map<Tier, Double> tierMap) {
        distanceZone.setTierMap(tierMap);
        return this;
    }

    @Override
    public DistanceZone build() {
        return distanceZone;
    }

}
