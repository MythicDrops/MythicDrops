package net.nunnerycode.bukkit.mythicdrops.distancezones;

import net.nunnerycode.bukkit.mythicdrops.api.distancezones.DistanceZone;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;

import java.util.HashMap;
import java.util.Map;

public final class MythicDistanceZone implements DistanceZone {

    private final String key;
    private double startDistance;
    private double endDistance;
    private Map<Tier, Double> tierMap;

    public MythicDistanceZone(String key) {
        this(key, 0, 0, new HashMap<Tier, Double>());
    }

    public MythicDistanceZone(String key, double startDistance, double endDistance, Map<Tier, Double> tierMap) {
        this.key = key;
        this.startDistance = startDistance;
        this.endDistance = endDistance;
        this.tierMap = tierMap != null ? tierMap : new HashMap<Tier, Double>();
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public double getStartDistance() {
        return startDistance;
    }

    public void setStartDistance(double startDistance) {
        this.startDistance = startDistance;
    }

    @Override
    public double getEndDistance() {
        return endDistance;
    }

    public void setEndDistance(double endDistance) {
        this.endDistance = endDistance;
    }

    @Override
    public Map<Tier, Double> getTierMap() {
        return new HashMap<>(tierMap);
    }

    public void setTierMap(Map<Tier, Double> tierMap) {
        this.tierMap = tierMap;
    }

}
