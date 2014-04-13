package net.nunnerycode.bukkit.mythicdrops.api.settings;

import java.util.List;
import java.util.Map;

public interface ConfigSettings {

    List<String> getArmorTypes();

    List<String> getToolTypes();

    List<String> getMaterialTypes();

    Map<String, List<String>> getItemTypesWithIds();

    Map<String, List<String>> getMaterialTypesWithIds();

    boolean isDebugMode();

    String getItemDisplayNameFormat();

    List<String> getTooltipFormat();

    List<String> getEnabledWorlds();

    String getLanguageString(String key);

    String getLanguageString(String key, String[][] args);

    String getFormattedLanguageString(String key);

    String getFormattedLanguageString(String key, String[][] args);

    boolean isReportingEnabled();

    boolean isHookMcMMO();

    boolean isGiveMobsNames();

    boolean isGiveAllMobsNames();

    boolean isDisplayMobEquipment();

    boolean isMobsPickupEquipment();

    boolean isBlankMobSpawnEnabled();

    boolean isSkeletonsSpawnWithoutBows();

    double getItemChance();

    double getSocketGemChance();

    double getIdentityTomeChance();

    double getUnidentifiedItemChance();

    boolean isCreatureSpawningEnabled();

    boolean isRepairingEnabled();

    boolean isIdentifyingEnabled();

    boolean isSockettingEnabled();

    boolean isPopulatingEnabled();

    double getCustomItemChance();

    boolean isDistanceZonesEnabled();
}
