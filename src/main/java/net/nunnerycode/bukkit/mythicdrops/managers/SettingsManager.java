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
    private double globalSpawnChance;
    private boolean socketGemsEnabled;
    private double socketGemsChanceToSpawn;
    private List<String> allowedSocketGemIds = new ArrayList<String>();
    private boolean itemsCanSpawnWithSockets;
    private double itemsSpawnWithSocketsChance;
    private boolean useAttackerItemInHandForEffects;
    private boolean useAttackerArmorForEffects;
    private boolean useDefenderItemInHandForEffects;
    private boolean useDefenderArmorForEffects;
    private boolean unidentifiedItemsEnabled;
    private double unidentifiedItemsChanceToSpawn;
    private boolean identityTomesEnabled;
    private double identityTomesChanceToSpawn;
    private boolean repairingEnabled;
    private boolean repairingPlaySound;
    private boolean multiworldSupport;
    private List<String> generateWorlds = new ArrayList<String>();
    private List<String> useWorlds = new ArrayList<String>();
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

    public boolean isUseDefenderArmorForEffects() {
        return useDefenderArmorForEffects;
    }

    public void setUseDefenderArmorForEffects(final boolean useDefenderArmorForEffects) {
        this.useDefenderArmorForEffects = useDefenderArmorForEffects;
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

    public double getGlobalSpawnChance() {
        return globalSpawnChance;
    }

    public void setGlobalSpawnChance(double globalSpawnChance) {
        this.globalSpawnChance = globalSpawnChance;
    }

    public boolean isSocketGemsEnabled() {
        return socketGemsEnabled;
    }

    public void setSocketGemsEnabled(boolean socketGemsEnabled) {
        this.socketGemsEnabled = socketGemsEnabled;
    }

    public double getSocketGemsChanceToSpawn() {
        return socketGemsChanceToSpawn;
    }

    public void setSocketGemsChanceToSpawn(double socketGemsChanceToSpawn) {
        this.socketGemsChanceToSpawn = socketGemsChanceToSpawn;
    }

    public List<String> getAllowedSocketGemIds() {
        return allowedSocketGemIds;
    }

    public void setAllowedSocketGemIds(List<String> allowedSocketGemIds) {
        this.allowedSocketGemIds = allowedSocketGemIds;
    }

    public boolean isItemsCanSpawnWithSockets() {
        return itemsCanSpawnWithSockets;
    }

    public void setItemsCanSpawnWithSockets(boolean itemsCanSpawnWithSockets) {
        this.itemsCanSpawnWithSockets = itemsCanSpawnWithSockets;
    }

    public double getItemsSpawnWithSocketsChance() {
        return itemsSpawnWithSocketsChance;
    }

    public void setItemsSpawnWithSocketsChance(double itemsSpawnWithSocketsChance) {
        this.itemsSpawnWithSocketsChance = itemsSpawnWithSocketsChance;
    }

    public boolean isUseAttackerItemInHandForEffects() {
        return useAttackerItemInHandForEffects;
    }

    public void setUseAttackerItemInHandForEffects(boolean useAttackerItemInHandForEffects) {
        this.useAttackerItemInHandForEffects = useAttackerItemInHandForEffects;
    }

    public boolean isUseAttackerArmorForEffects() {
        return useAttackerArmorForEffects;
    }

    public void setUseAttackerArmorForEffects(boolean useAttackerArmorForEffects) {
        this.useAttackerArmorForEffects = useAttackerArmorForEffects;
    }

    public boolean isUseDefenderItemInHandForEffects() {
        return useDefenderItemInHandForEffects;
    }

    public void setUseDefenderItemInHandForEffects(boolean useDefenderItemInHandForEffects) {
        this.useDefenderItemInHandForEffects = useDefenderItemInHandForEffects;
    }

    public boolean isUnidentifiedItemsEnabled() {
        return unidentifiedItemsEnabled;
    }

    public void setUnidentifiedItemsEnabled(boolean unidentifiedItemsEnabled) {
        this.unidentifiedItemsEnabled = unidentifiedItemsEnabled;
    }

    public double getUnidentifiedItemsChanceToSpawn() {
        return unidentifiedItemsChanceToSpawn;
    }

    public void setUnidentifiedItemsChanceToSpawn(double unidentifiedItemsChanceToSpawn) {
        this.unidentifiedItemsChanceToSpawn = unidentifiedItemsChanceToSpawn;
    }

    public boolean isIdentityTomesEnabled() {
        return identityTomesEnabled;
    }

    public void setIdentityTomesEnabled(boolean identityTomesEnabled) {
        this.identityTomesEnabled = identityTomesEnabled;
    }

    public double getIdentityTomesChanceToSpawn() {
        return identityTomesChanceToSpawn;
    }

    public void setIdentityTomesChanceToSpawn(double identityTomesChanceToSpawn) {
        this.identityTomesChanceToSpawn = identityTomesChanceToSpawn;
    }

    public boolean isRepairingEnabled() {
        return repairingEnabled;
    }

    public void setRepairingEnabled(boolean repairingEnabled) {
        this.repairingEnabled = repairingEnabled;
    }

    public boolean isRepairingPlaySound() {
        return repairingPlaySound;
    }

    public void setRepairingPlaySound(boolean repairingPlaySound) {
        this.repairingPlaySound = repairingPlaySound;
    }

    public boolean isMultiworldSupport() {
        return multiworldSupport;
    }

    public void setMultiworldSupport(boolean multiworldSupport) {
        this.multiworldSupport = multiworldSupport;
    }

    public List<String> getGenerateWorlds() {
        return generateWorlds;
    }

    public void setGenerateWorlds(List<String> generateWorlds) {
        this.generateWorlds = generateWorlds;
    }

    public List<String> getUseWorlds() {
        return useWorlds;
    }

    public void setUseWorlds(List<String> useWorlds) {
        this.useWorlds = useWorlds;
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