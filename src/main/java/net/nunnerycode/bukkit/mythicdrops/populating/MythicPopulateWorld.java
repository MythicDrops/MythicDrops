package net.nunnerycode.bukkit.mythicdrops.populating;

import net.nunnerycode.bukkit.mythicdrops.api.populating.PopulateWorld;

import java.util.ArrayList;
import java.util.List;

public final class MythicPopulateWorld implements PopulateWorld {

    private final String name;
    private boolean enabled;
    private double chance;
    private int minimumItems;
    private int maximumItems;
    private boolean overwriteContents;
    private List<String> tiers;

    public MythicPopulateWorld(String name) {
        this.name = name;
        tiers = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public double getChance() {
        return chance;
    }

    public void setChance(double chance) {
        this.chance = chance;
    }

    @Override
    public int getMinimumItems() {
        return minimumItems;
    }

    public void setMinimumItems(int minimumItems) {
        this.minimumItems = minimumItems;
    }

    @Override
    public int getMaximumItems() {
        return maximumItems;
    }

    public void setMaximumItems(int maximumItems) {
        this.maximumItems = maximumItems;
    }

    @Override
    public boolean isOverwriteContents() {
        return overwriteContents;
    }

    public void setOverwriteContents(boolean overwriteContents) {
        this.overwriteContents = overwriteContents;
    }

    @Override
    public List<String> getTiers() {
        return tiers;
    }

    public void setTiers(List<String> tiers) {
        this.tiers = tiers;
    }

}
