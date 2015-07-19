package com.tealcube.minecraft.bukkit.mythicdrops.api.settings;

/*
 * #%L
 * MythicDrops
 * %%
 * Copyright (C) 2013 - 2015 TealCube
 * %%
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby
 * granted,
 * provided that the above copyright notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
 * INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER
 * IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
 * PERFORMANCE OF
 * THIS SOFTWARE.
 * #L%
 */


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

}
