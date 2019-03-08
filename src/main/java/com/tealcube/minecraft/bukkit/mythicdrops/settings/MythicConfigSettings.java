/*
 * The MIT License
 * Copyright © 2013 Richard Harrah
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.tealcube.minecraft.bukkit.mythicdrops.settings;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.tealcube.minecraft.bukkit.mythicdrops.StringExtensionsKt;
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.ConfigSettings;
import com.tealcube.minecraft.bukkit.mythicdrops.gson.annotations.Exclude;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kotlin.Pair;

public final class MythicConfigSettings implements ConfigSettings {

  private final List<String> armorTypes;
  private final List<String> toolTypes;
  private final List<String> materialTypes;
  private final Map<String, List<String>> itemTypesWithIds;
  private final Map<String, List<String>> materialTypesWithIds;
  private final List<String> tooltipFormat;
  @Exclude
  private final Map<String, String> language;
  private boolean debugMode;
  private String itemDisplayNameFormat;
  private List<String> enabledWorlds;
  private boolean giveMobsNames;
  private boolean giveAllMobsNames;
  private boolean displayMobEquipment;
  private boolean mobsPickupEquipment;
  private boolean blankMobSpawnEnabled;
  private boolean skeletonsSpawnWithoutBows;
  private boolean allowRepairingUsingAnvil;
  private boolean allowEquippingItemsViaRightClick;
  private double randomItemChance;
  private double tieredItemChance;
  private double customItemChance;
  private double socketGemChance;
  private double identityTomeChance;
  private double unidentifiedItemChance;
  private boolean creatureSpawningEnabled;
  private boolean repairingEnabled;
  private boolean identifyingEnabled;
  private boolean sockettingEnabled;
  private boolean populatingEnabled;
  private boolean randomizeLeatherColors;

  public MythicConfigSettings() {
    armorTypes = new ArrayList<>();
    toolTypes = new ArrayList<>();
    materialTypes = new ArrayList<>();
    itemTypesWithIds = new HashMap<>();
    materialTypesWithIds = new HashMap<>();
    tooltipFormat = new ArrayList<>();
    language = new HashMap<>();
    enabledWorlds = new ArrayList<>();
  }

  public Map<String, String> getLanguageMap() {
    return language;
  }

  @Override
  public List<String> getArmorTypes() {
    return armorTypes;
  }

  @Override
  public List<String> getToolTypes() {
    return toolTypes;
  }

  @Override
  public List<String> getMaterialTypes() {
    return materialTypes;
  }

  @Override
  public Map<String, List<String>> getItemTypesWithIds() {
    return itemTypesWithIds;
  }

  @Override
  public Map<String, List<String>> getMaterialTypesWithIds() {
    return materialTypesWithIds;
  }

  @Override
  public boolean isDebugMode() {
    return debugMode;
  }

  public void setDebugMode(boolean debugMode) {
    this.debugMode = debugMode;
  }

  @Override
  public String getItemDisplayNameFormat() {
    return itemDisplayNameFormat;
  }

  public void setItemDisplayNameFormat(String itemDisplayNameFormat) {
    this.itemDisplayNameFormat = itemDisplayNameFormat;
  }

  @Override
  public List<String> getTooltipFormat() {
    return tooltipFormat;
  }

  @Override
  public List<String> getEnabledWorlds() {
    return enabledWorlds;
  }

  public void setEnabledWorlds(List<String> enabledWorlds) {
    this.enabledWorlds = enabledWorlds;
  }

  @Override
  public String getLanguageString(String key) {
    return language.getOrDefault(key, key);
  }

  @Override
  public String getLanguageString(String key, String[][] args) {
    String s = getLanguageString(key);
    for (String[] arg : args) {
      s = s.replace(arg[0], arg[1]);
    }
    return s;
  }

  @Override
  public String getFormattedLanguageString(String key) {
    return getLanguageString(key).replace('&', '\u00A7').replace("\u00A7\u00A7", "&");
  }

  @Override
  public String getFormattedLanguageString(String key, String[][] args) {
    return getFormattedLanguageString(
        key,
        Arrays.stream(args)
            .map(strings -> new Pair<>(strings[0], strings[1]))
            .collect(Collectors.toList())
    );
  }

  @Override
  public String getFormattedLanguageString(String key, Collection<Pair<String, String>> args) {
    return StringExtensionsKt.replaceArgs(getFormattedLanguageString(key), args);
  }

  @Override
  public boolean isGiveMobsNames() {
    return giveMobsNames;
  }

  public void setGiveMobsNames(boolean giveMobsNames) {
    this.giveMobsNames = giveMobsNames;
  }

  @Override
  public boolean isGiveAllMobsNames() {
    return giveAllMobsNames;
  }

  public void setGiveAllMobsNames(boolean giveAllMobsNames) {
    this.giveAllMobsNames = giveAllMobsNames;
  }

  @Override
  public boolean isDisplayMobEquipment() {
    return displayMobEquipment;
  }

  public void setDisplayMobEquipment(boolean displayMobEquipment) {
    this.displayMobEquipment = displayMobEquipment;
  }

  @Override
  public boolean isMobsPickupEquipment() {
    return mobsPickupEquipment;
  }

  public void setMobsPickupEquipment(boolean mobsPickupEquipment) {
    this.mobsPickupEquipment = mobsPickupEquipment;
  }

  @Override
  public boolean isBlankMobSpawnEnabled() {
    return blankMobSpawnEnabled;
  }

  public void setBlankMobSpawnEnabled(boolean blankMobSpawnEnabled) {
    this.blankMobSpawnEnabled = blankMobSpawnEnabled;
  }

  @Override
  public boolean isSkeletonsSpawnWithoutBows() {
    return skeletonsSpawnWithoutBows;
  }

  public void setSkeletonsSpawnWithoutBows(boolean skeletonsSpawnWithoutBow) {
    this.skeletonsSpawnWithoutBows = skeletonsSpawnWithoutBow;
  }

  @Override
  public double getItemChance() {
    return randomItemChance;
  }

  public void setItemChance(double randomItemChance) {
    this.randomItemChance = randomItemChance;
  }

  @Override
  public double getTieredItemChance() {
    return tieredItemChance;
  }

  public void setTieredItemChance(double tieredItemChance) {
    this.tieredItemChance = tieredItemChance;
  }

  @Deprecated
  @Override
  public double getChainItemChance() {
    return 0D;
  }

  @Deprecated
  public void setChainItemChance(double chainItemChance) {
    // do nothing
  }

  @Override
  public double getSocketGemChance() {
    return socketGemChance;
  }

  public void setSocketGemChance(double socketGemChance) {
    this.socketGemChance = socketGemChance;
  }

  @Override
  public double getIdentityTomeChance() {
    return identityTomeChance;
  }

  public void setIdentityTomeChance(double identityTomeChance) {
    this.identityTomeChance = identityTomeChance;
  }

  @Override
  public double getUnidentifiedItemChance() {
    return unidentifiedItemChance;
  }

  public void setUnidentifiedItemChance(double unidentifiedItemChance) {
    this.unidentifiedItemChance = unidentifiedItemChance;
  }

  @Override
  public boolean isCreatureSpawningEnabled() {
    return creatureSpawningEnabled;
  }

  public void setCreatureSpawningEnabled(boolean creatureSpawningEnabled) {
    this.creatureSpawningEnabled = creatureSpawningEnabled;
  }

  @Override
  public boolean isRepairingEnabled() {
    return repairingEnabled;
  }

  public void setRepairingEnabled(boolean repairingEnabled) {
    this.repairingEnabled = repairingEnabled;
  }

  @Override
  public boolean isIdentifyingEnabled() {
    return identifyingEnabled;
  }

  public void setIdentifyingEnabled(boolean identifyingEnabled) {
    this.identifyingEnabled = identifyingEnabled;
  }

  @Override
  public boolean isSockettingEnabled() {
    return sockettingEnabled;
  }

  public void setSockettingEnabled(boolean sockettingEnabled) {
    this.sockettingEnabled = sockettingEnabled;
  }

  @Override
  public boolean isPopulatingEnabled() {
    return populatingEnabled;
  }

  public void setPopulatingEnabled(boolean populatingEnabled) {
    this.populatingEnabled = populatingEnabled;
  }

  @Override
  public double getCustomItemChance() {
    return customItemChance;
  }

  public void setCustomItemChance(double customItemChance) {
    this.customItemChance = customItemChance;
  }

  @Override
  public boolean isAllowRepairingUsingAnvil() {
    return allowRepairingUsingAnvil;
  }

  public void setAllowRepairingUsingAnvil(boolean allowRepairingUsingAnvil) {
    this.allowRepairingUsingAnvil = allowRepairingUsingAnvil;
  }

  @Override
  public boolean isAllowEquippingItemsViaRightClick() {
    return allowEquippingItemsViaRightClick;
  }

  public void setAllowEquippingItemsViaRightClick(boolean allowEquippingItemsViaRightClick) {
    this.allowEquippingItemsViaRightClick = allowEquippingItemsViaRightClick;
  }

  public boolean isRandomizeLeatherColors() {
    return randomizeLeatherColors;
  }

  public void setRandomizeLeatherColors(boolean randomizedLeatherColors) {
    this.randomizeLeatherColors = randomizedLeatherColors;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("armorTypes", armorTypes)
        .add("toolTypes", toolTypes)
        .add("materialTypes", materialTypes)
        .add("itemTypesWithIds", itemTypesWithIds)
        .add("materialTypesWithIds", materialTypesWithIds)
        .add("tooltipFormat", tooltipFormat)
        .add("language", language)
        .add("debugMode", debugMode)
        .add("itemDisplayNameFormat", itemDisplayNameFormat)
        .add("enabledWorlds", enabledWorlds)
        .add("giveMobsNames", giveMobsNames)
        .add("giveAllMobsNames", giveAllMobsNames)
        .add("displayMobEquipment", displayMobEquipment)
        .add("mobsPickupEquipment", mobsPickupEquipment)
        .add("blankMobSpawnEnabled", blankMobSpawnEnabled)
        .add("skeletonsSpawnWithoutBows", skeletonsSpawnWithoutBows)
        .add("allowRepairingUsingAnvil", allowRepairingUsingAnvil)
        .add("allowEquippingItemsViaRightClick", allowEquippingItemsViaRightClick)
        .add("randomItemChance", randomItemChance)
        .add("tieredItemChance", tieredItemChance)
        .add("customItemChance", customItemChance)
        .add("socketGemChance", socketGemChance)
        .add("identityTomeChance", identityTomeChance)
        .add("unidentifiedItemChance", unidentifiedItemChance)
        .add("creatureSpawningEnabled", creatureSpawningEnabled)
        .add("repairingEnabled", repairingEnabled)
        .add("identifyingEnabled", identifyingEnabled)
        .add("sockettingEnabled", sockettingEnabled)
        .add("populatingEnabled", populatingEnabled)
        .add("randomizeLeatherColors", randomizeLeatherColors)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MythicConfigSettings that = (MythicConfigSettings) o;
    return debugMode == that.debugMode &&
        giveMobsNames == that.giveMobsNames &&
        giveAllMobsNames == that.giveAllMobsNames &&
        displayMobEquipment == that.displayMobEquipment &&
        mobsPickupEquipment == that.mobsPickupEquipment &&
        blankMobSpawnEnabled == that.blankMobSpawnEnabled &&
        skeletonsSpawnWithoutBows == that.skeletonsSpawnWithoutBows &&
        allowRepairingUsingAnvil == that.allowRepairingUsingAnvil &&
        allowEquippingItemsViaRightClick == that.allowEquippingItemsViaRightClick &&
        Double.compare(that.randomItemChance, randomItemChance) == 0 &&
        Double.compare(that.tieredItemChance, tieredItemChance) == 0 &&
        Double.compare(that.customItemChance, customItemChance) == 0 &&
        Double.compare(that.socketGemChance, socketGemChance) == 0 &&
        Double.compare(that.identityTomeChance, identityTomeChance) == 0 &&
        Double.compare(that.unidentifiedItemChance, unidentifiedItemChance) == 0 &&
        creatureSpawningEnabled == that.creatureSpawningEnabled &&
        repairingEnabled == that.repairingEnabled &&
        identifyingEnabled == that.identifyingEnabled &&
        sockettingEnabled == that.sockettingEnabled &&
        populatingEnabled == that.populatingEnabled &&
        randomizeLeatherColors == that.randomizeLeatherColors &&
        Objects.equal(armorTypes, that.armorTypes) &&
        Objects.equal(toolTypes, that.toolTypes) &&
        Objects.equal(materialTypes, that.materialTypes) &&
        Objects.equal(itemTypesWithIds, that.itemTypesWithIds) &&
        Objects.equal(materialTypesWithIds, that.materialTypesWithIds) &&
        Objects.equal(tooltipFormat, that.tooltipFormat) &&
        Objects.equal(language, that.language) &&
        Objects.equal(itemDisplayNameFormat, that.itemDisplayNameFormat) &&
        Objects.equal(enabledWorlds, that.enabledWorlds);
  }

  @Override
  public int hashCode() {
    return Objects
        .hashCode(armorTypes, toolTypes, materialTypes, itemTypesWithIds, materialTypesWithIds, tooltipFormat, language,
            debugMode, itemDisplayNameFormat, enabledWorlds, giveMobsNames, giveAllMobsNames, displayMobEquipment,
            mobsPickupEquipment, blankMobSpawnEnabled, skeletonsSpawnWithoutBows, allowRepairingUsingAnvil,
            allowEquippingItemsViaRightClick, randomItemChance, tieredItemChance, customItemChance, socketGemChance,
            identityTomeChance, unidentifiedItemChance, creatureSpawningEnabled, repairingEnabled, identifyingEnabled,
            sockettingEnabled, populatingEnabled, randomizeLeatherColors);
  }

}
