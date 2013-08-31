package net.nunnerycode.bukkit.mythicdrops.managers;

import net.nunnerycode.bukkit.mythicdrops.MythicDrops;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingsManager {

    private final MythicDrops plugin;
    private boolean autoUpdate;
    private boolean debugMode;
    private boolean customItemsSpawn;
    private boolean onlyCustomItemsSpawn;
    private double customItemChanceToSpawn;
    private boolean preventSpawningFromSpawnEgg;
    private boolean preventSpawningFromMonsterSpawner;
    private boolean preventSpawningFromCustom;
    private String itemDisplayNameFormat;
    private boolean preventMultipleChangesFromSockets;
    private boolean randomLoreEnabled;
    private double randomLoreChance;
    private List<String> loreFormat = new ArrayList<String>();
    private Map<String, List<String>> ids = new HashMap<String, List<String>>();
    private List<String> armorIDTypes = new ArrayList<String>();
    private List<String> toolIDTypes = new ArrayList<String>();
    private List<String> materialIDTypes = new ArrayList<String>();

    public SettingsManager(MythicDrops plugin) {
        this.plugin = plugin;
    }

    public List<String> getArmorIDTypes() {
        return armorIDTypes;
    }

    public void setArmorIDTypes(final List<String> armorIDTypes) {
        this.armorIDTypes = armorIDTypes;
    }

    public List<String> getToolIDTypes() {
        return toolIDTypes;
    }

    public void setToolIDTypes(final List<String> toolIDTypes) {
        this.toolIDTypes = toolIDTypes;
    }

    public List<String> getMaterialIDTypes() {
        return materialIDTypes;
    }

    public void setMaterialIDTypes(final List<String> materialIDTypes) {
        this.materialIDTypes = materialIDTypes;
    }

    public boolean isCustomItemsSpawn() {
        return customItemsSpawn;
    }

    public void setCustomItemsSpawn(final boolean customItemsSpawn) {
        this.customItemsSpawn = customItemsSpawn;
    }

    public double getRandomLoreChance() {
        return randomLoreChance;
    }

    public void setRandomLoreChance(double randomLoreChance) {
        this.randomLoreChance = randomLoreChance;
    }

    public boolean isAutoUpdate() {
        return autoUpdate;
    }

    public void setAutoUpdate(boolean autoUpdate) {
        this.autoUpdate = autoUpdate;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public boolean isOnlyCustomItemsSpawn() {
        return onlyCustomItemsSpawn;
    }

    public void setOnlyCustomItemsSpawn(boolean onlyCustomItemsSpawn) {
        this.onlyCustomItemsSpawn = onlyCustomItemsSpawn;
    }

    public double getCustomItemChanceToSpawn() {
        return customItemChanceToSpawn;
    }

    public void setCustomItemChanceToSpawn(double customItemChanceToSpawn) {
        this.customItemChanceToSpawn = customItemChanceToSpawn;
    }

    public boolean isPreventSpawningFromSpawnEgg() {
        return preventSpawningFromSpawnEgg;
    }

    public void setPreventSpawningFromSpawnEgg(boolean preventSpawningFromSpawnEgg) {
        this.preventSpawningFromSpawnEgg = preventSpawningFromSpawnEgg;
    }

    public boolean isPreventSpawningFromMonsterSpawner() {
        return preventSpawningFromMonsterSpawner;
    }

    public void setPreventSpawningFromMonsterSpawner(boolean preventSpawningFromMonsterSpawner) {
        this.preventSpawningFromMonsterSpawner = preventSpawningFromMonsterSpawner;
    }

    public boolean isPreventSpawningFromCustom() {
        return preventSpawningFromCustom;
    }

    public void setPreventSpawningFromCustom(boolean preventSpawningFromCustom) {
        this.preventSpawningFromCustom = preventSpawningFromCustom;
    }

    public String getItemDisplayNameFormat() {
        return itemDisplayNameFormat;
    }

    public void setItemDisplayNameFormat(String itemDisplayNameFormat) {
        this.itemDisplayNameFormat = itemDisplayNameFormat;
    }

    public boolean isPreventMultipleChangesFromSockets() {
        return preventMultipleChangesFromSockets;
    }

    public void setPreventMultipleChangesFromSockets(boolean preventMultipleChangesFromSockets) {
        this.preventMultipleChangesFromSockets = preventMultipleChangesFromSockets;
    }

    public boolean isRandomLoreEnabled() {
        return randomLoreEnabled;
    }

    public void setRandomLoreEnabled(boolean randomLoreEnabled) {
        this.randomLoreEnabled = randomLoreEnabled;
    }

    public double isRandomLoreChance() {
        return randomLoreChance;
    }

    public List<String> getLoreFormat() {
        return loreFormat;
    }

    public void setLoreFormat(List<String> loreFormat) {
        this.loreFormat = loreFormat;
    }

    public MythicDrops getPlugin() {
        return plugin;
    }

    public Map<String, List<String>> getIds() {
        return ids;
    }

    public void setIds(final Map<String, List<String>> ids) {
        this.ids = ids;
    }
}