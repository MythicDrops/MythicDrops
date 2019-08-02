/*
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2019 Richard Harrah
 *
 * Permission is hereby granted, free of charge,
 * to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.tealcube.minecraft.bukkit.mythicdrops.settings;

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SockettingSettings;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public final class MythicSockettingSettings implements SockettingSettings {

  private String socketGemName;
  private List<String> socketGemLore;
  private String sockettedItemString;
  private List<String> sockettedItemLore;
  private boolean useAttackerItemInHand;
  private boolean useAttackerArmorEquipped;
  private boolean useDefenderItemInHand;
  private boolean useDefenderArmorEquipped;
  private List<Material> socketGemMaterials;
  private boolean preventMultipleChangesFromSockets;
  private boolean canDropSocketGemsOnItems;
  private boolean preventCraftingWithGems;
  private boolean useTierColorForSocketName;
  private ChatColor defaultSocketNameColorOnItems;
  private String socketGemCombinerName;
  private String socketGemCombinerBufferName;
  private Material socketGemCombinerBufferMaterial;
  private String socketGemCombinerClickToCombineName;
  private Material socketGemCombinerClickToCombineMaterial;
  private Material socketGemCombinerIneligibleToCombineMaterial;
  private String socketGemCombinerIneligibleToCombineName;
  private boolean socketGemCombinerRequireSameFamily;
  private boolean socketGemCombinerRequireSameLevel;
  private List<String> socketFamilyLore;
  private List<String> socketTypeLore;
  private List<String> socketGemCombinerSameFamilyLore;
  private List<String> socketGemCombinerSameLevelLore;
  private List<String> socketGemCombinerSameFamilyAndLevelLore;

  public MythicSockettingSettings() {
    socketGemLore = new ArrayList<>();
    sockettedItemLore = new ArrayList<>();
    socketGemMaterials = new ArrayList<>();
    socketFamilyLore = new ArrayList<>();
    socketTypeLore = new ArrayList<>();
    socketGemCombinerSameFamilyLore = new ArrayList<>();
    socketGemCombinerSameLevelLore = new ArrayList<>();
    socketGemCombinerSameFamilyAndLevelLore = new ArrayList<>();
  }

  @Override
  public String getSocketGemName() {
    return socketGemName;
  }

  public void setSocketGemName(String socketGemName) {
    this.socketGemName = socketGemName;
  }

  @Override
  public List<String> getSocketGemLore() {
    return socketGemLore;
  }

  public void setSocketGemLore(List<String> socketGemLore) {
    this.socketGemLore = socketGemLore;
  }

  @Override
  public String getSockettedItemString() {
    return sockettedItemString;
  }

  public void setSockettedItemString(String sockettedItemString) {
    this.sockettedItemString = sockettedItemString;
  }

  @Override
  public List<String> getSockettedItemLore() {
    return sockettedItemLore;
  }

  public void setSockettedItemLore(List<String> sockettedItemLore) {
    this.sockettedItemLore = sockettedItemLore;
  }

  @Override
  public boolean isUseAttackerItemInHand() {
    return useAttackerItemInHand;
  }

  public void setUseAttackerItemInHand(boolean useAttackerItemInHand) {
    this.useAttackerItemInHand = useAttackerItemInHand;
  }

  @Override
  public boolean isUseAttackerArmorEquipped() {
    return useAttackerArmorEquipped;
  }

  public void setUseAttackerArmorEquipped(boolean useAttackerArmorEquipped) {
    this.useAttackerArmorEquipped = useAttackerArmorEquipped;
  }

  @Override
  public boolean isUseDefenderItemInHand() {
    return useDefenderItemInHand;
  }

  public void setUseDefenderItemInHand(boolean useDefenderItemInHand) {
    this.useDefenderItemInHand = useDefenderItemInHand;
  }

  @Override
  public boolean isUseDefenderArmorEquipped() {
    return useDefenderArmorEquipped;
  }

  public void setUseDefenderArmorEquipped(boolean useDefenderArmorEquipped) {
    this.useDefenderArmorEquipped = useDefenderArmorEquipped;
  }

  @Override
  public boolean isPreventMultipleChangesFromSockets() {
    return preventMultipleChangesFromSockets;
  }

  public void setPreventMultipleChangesFromSockets(boolean preventMultipleChangesFromSockets) {
    this.preventMultipleChangesFromSockets = preventMultipleChangesFromSockets;
  }

  @Override
  public List<Material> getSocketGemMaterials() {
    return socketGemMaterials;
  }

  public void setSocketGemMaterials(List<Material> socketGemMaterials) {
    this.socketGemMaterials = socketGemMaterials;
  }

  @Deprecated
  @Override
  public boolean isCanDropSocketGemsOnItems() {
    return canDropSocketGemsOnItems;
  }

  @Deprecated
  public void setCanDropSocketGemsOnItems(boolean canDropSocketGemsOnItems) {
    this.canDropSocketGemsOnItems = canDropSocketGemsOnItems;
  }

  @Override
  public boolean isPreventCraftingWithGems() {
    return preventCraftingWithGems;
  }

  public void setPreventCraftingWithGems(boolean preventCraftingWithGems) {
    this.preventCraftingWithGems = preventCraftingWithGems;
  }

  @Override
  public boolean isUseTierColorForSocketName() {
    return useTierColorForSocketName;
  }

  public void setUseTierColorForSocketName(boolean useTierColorForSocketName) {
    this.useTierColorForSocketName = useTierColorForSocketName;
  }

  @Override
  public ChatColor getDefaultSocketNameColorOnItems() {
    return defaultSocketNameColorOnItems;
  }

  public void setDefaultSocketNameColorOnItems(ChatColor defaultSocketNameColorOnItems) {
    this.defaultSocketNameColorOnItems = defaultSocketNameColorOnItems;
  }

  @Override
  public String getSocketGemCombinerName() {
    return socketGemCombinerName;
  }

  public void setSocketGemCombinerName(String socketGemCombinerName) {
    this.socketGemCombinerName = socketGemCombinerName;
  }

  @Override
  public String getSocketGemCombinerBufferName() {
    return socketGemCombinerBufferName;
  }

  public void setSocketGemCombinerBufferName(String socketGemCombinerBufferName) {
    this.socketGemCombinerBufferName = socketGemCombinerBufferName;
  }

  @Override
  public Material getSocketGemCombinerBufferMaterial() {
    return socketGemCombinerBufferMaterial;
  }

  public void setSocketGemCombinerBufferMaterial(Material socketGemCombinerBufferMaterial) {
    this.socketGemCombinerBufferMaterial = socketGemCombinerBufferMaterial;
  }

  @Override
  public String getSocketGemCombinerClickToCombineName() {
    return socketGemCombinerClickToCombineName;
  }

  public void setSocketGemCombinerClickToCombineName(String socketGemCombinerClickToCombineName) {
    this.socketGemCombinerClickToCombineName = socketGemCombinerClickToCombineName;
  }

  @Override
  public boolean isSocketGemCombinerRequireSameFamily() {
    return socketGemCombinerRequireSameFamily;
  }

  public void setSocketGemCombinerRequireSameFamily(boolean socketGemCombinerRequireSameFamily) {
    this.socketGemCombinerRequireSameFamily = socketGemCombinerRequireSameFamily;
  }

  @Override
  public List<String> getSocketFamilyLore() {
    return socketFamilyLore;
  }

  public void setSocketFamilyLore(List<String> socketFamilyLore) {
    this.socketFamilyLore = socketFamilyLore;
  }

  @Override
  public List<String> getSocketTypeLore() {
    return socketTypeLore;
  }

  public void setSocketTypeLore(List<String> socketTypeLore) {
    this.socketTypeLore = socketTypeLore;
  }

  @Override
  public Material getSocketGemCombinerClickToCombineMaterial() {
    return socketGemCombinerClickToCombineMaterial;
  }

  public void setSocketGemCombinerClickToCombineMaterial(
      Material socketGemCombinerClickToCombineMaterial) {
    this.socketGemCombinerClickToCombineMaterial = socketGemCombinerClickToCombineMaterial;
  }

  @Override
  public Material getSocketGemCombinerIneligibleToCombineMaterial() {
    return socketGemCombinerIneligibleToCombineMaterial;
  }

  public void setSocketGemCombinerIneligibleToCombineMaterial(
      Material socketGemCombinerIneligibleToCombineMaterial) {
    this.socketGemCombinerIneligibleToCombineMaterial =
        socketGemCombinerIneligibleToCombineMaterial;
  }

  @Override
  public String getSocketGemCombinerIneligibleToCombineName() {
    return socketGemCombinerIneligibleToCombineName;
  }

  public void setSocketGemCombinerIneligibleToCombineName(
      String socketGemCombinerIneligibleToCombineName) {
    this.socketGemCombinerIneligibleToCombineName = socketGemCombinerIneligibleToCombineName;
  }

  @Override
  public List<String> getSocketGemCombinerSameFamilyAndLevelLore() {
    return socketGemCombinerSameFamilyAndLevelLore;
  }

  public void setSocketGemCombinerSameFamilyAndLevelLore(
      List<String> socketGemCombinerSameFamilyAndLevelLore) {
    this.socketGemCombinerSameFamilyAndLevelLore = socketGemCombinerSameFamilyAndLevelLore;
  }

  @Override
  public boolean isSocketGemCombinerRequireSameLevel() {
    return socketGemCombinerRequireSameLevel;
  }

  public void setSocketGemCombinerRequireSameLevel(boolean socketGemCombinerRequireSameLevel) {
    this.socketGemCombinerRequireSameLevel = socketGemCombinerRequireSameLevel;
  }

  @Override
  public List<String> getSocketGemCombinerSameFamilyLore() {
    return socketGemCombinerSameFamilyLore;
  }

  public void setSocketGemCombinerSameFamilyLore(List<String> socketGemCombinerSameFamilyLore) {
    this.socketGemCombinerSameFamilyLore = socketGemCombinerSameFamilyLore;
  }

  @Override
  public List<String> getSocketGemCombinerSameLevelLore() {
    return socketGemCombinerSameLevelLore;
  }

  public void setSocketGemCombinerSameLevelLore(List<String> socketGemCombinerSameLevelLore) {
    this.socketGemCombinerSameLevelLore = socketGemCombinerSameLevelLore;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("socketGemName", socketGemName)
        .append("socketGemLore", socketGemLore)
        .append("sockettedItemString", sockettedItemString)
        .append("sockettedItemLore", sockettedItemLore)
        .append("useAttackerItemInHand", useAttackerItemInHand)
        .append("useAttackerArmorEquipped", useAttackerArmorEquipped)
        .append("useDefenderItemInHand", useDefenderItemInHand)
        .append("useDefenderArmorEquipped", useDefenderArmorEquipped)
        .append("socketGemMaterials", socketGemMaterials)
        .append("preventMultipleChangesFromSockets", preventMultipleChangesFromSockets)
        .append("canDropSocketGemsOnItems", canDropSocketGemsOnItems)
        .append("preventCraftingWithGems", preventCraftingWithGems)
        .append("useTierColorForSocketName", useTierColorForSocketName)
        .append("defaultSocketNameColorOnItems", defaultSocketNameColorOnItems)
        .append("socketGemCombinerName", socketGemCombinerName)
        .append("socketGemCombinerBufferName", socketGemCombinerBufferName)
        .append("socketGemCombinerBufferMaterial", socketGemCombinerBufferMaterial)
        .append("socketGemCombinerClickToCombineName", socketGemCombinerClickToCombineName)
        .append("socketGemCombinerClickToCombineMaterial", socketGemCombinerClickToCombineMaterial)
        .append(
            "socketGemCombinerIneligibleToCombineMaterial",
            socketGemCombinerIneligibleToCombineMaterial)
        .append(
            "socketGemCombinerIneligibleToCombineName", socketGemCombinerIneligibleToCombineName)
        .append("socketGemCombinerRequireSameFamily", socketGemCombinerRequireSameFamily)
        .append("socketGemCombinerRequireSameLevel", socketGemCombinerRequireSameLevel)
        .append("socketFamilyLore", socketFamilyLore)
        .append("socketTypeLore", socketTypeLore)
        .append("socketGemCombinerSameFamilyLore", socketGemCombinerSameFamilyLore)
        .append("socketGemCombinerSameLevelLore", socketGemCombinerSameLevelLore)
        .append("socketGemCombinerSameFamilyAndLevelLore", socketGemCombinerSameFamilyAndLevelLore)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MythicSockettingSettings that = (MythicSockettingSettings) o;
    return useAttackerItemInHand == that.useAttackerItemInHand
        && useAttackerArmorEquipped == that.useAttackerArmorEquipped
        && useDefenderItemInHand == that.useDefenderItemInHand
        && useDefenderArmorEquipped == that.useDefenderArmorEquipped
        && preventMultipleChangesFromSockets == that.preventMultipleChangesFromSockets
        && canDropSocketGemsOnItems == that.canDropSocketGemsOnItems
        && preventCraftingWithGems == that.preventCraftingWithGems
        && useTierColorForSocketName == that.useTierColorForSocketName
        && socketGemCombinerRequireSameFamily == that.socketGemCombinerRequireSameFamily
        && socketGemCombinerRequireSameLevel == that.socketGemCombinerRequireSameLevel
        && Objects.equals(socketGemName, that.socketGemName)
        && Objects.equals(socketGemLore, that.socketGemLore)
        && Objects.equals(sockettedItemString, that.sockettedItemString)
        && Objects.equals(sockettedItemLore, that.sockettedItemLore)
        && Objects.equals(socketGemMaterials, that.socketGemMaterials)
        && defaultSocketNameColorOnItems == that.defaultSocketNameColorOnItems
        && Objects.equals(socketGemCombinerName, that.socketGemCombinerName)
        && Objects.equals(socketGemCombinerBufferName, that.socketGemCombinerBufferName)
        && socketGemCombinerBufferMaterial == that.socketGemCombinerBufferMaterial
        && Objects.equals(
            socketGemCombinerClickToCombineName, that.socketGemCombinerClickToCombineName)
        && socketGemCombinerClickToCombineMaterial == that.socketGemCombinerClickToCombineMaterial
        && socketGemCombinerIneligibleToCombineMaterial
            == that.socketGemCombinerIneligibleToCombineMaterial
        && Objects.equals(
            socketGemCombinerIneligibleToCombineName, that.socketGemCombinerIneligibleToCombineName)
        && Objects.equals(socketFamilyLore, that.socketFamilyLore)
        && Objects.equals(socketTypeLore, that.socketTypeLore)
        && Objects.equals(socketGemCombinerSameFamilyLore, that.socketGemCombinerSameFamilyLore)
        && Objects.equals(socketGemCombinerSameLevelLore, that.socketGemCombinerSameLevelLore)
        && Objects.equals(
            socketGemCombinerSameFamilyAndLevelLore, that.socketGemCombinerSameFamilyAndLevelLore);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        socketGemName,
        socketGemLore,
        sockettedItemString,
        sockettedItemLore,
        useAttackerItemInHand,
        useAttackerArmorEquipped,
        useDefenderItemInHand,
        useDefenderArmorEquipped,
        socketGemMaterials,
        preventMultipleChangesFromSockets,
        canDropSocketGemsOnItems,
        preventCraftingWithGems,
        useTierColorForSocketName,
        defaultSocketNameColorOnItems,
        socketGemCombinerName,
        socketGemCombinerBufferName,
        socketGemCombinerBufferMaterial,
        socketGemCombinerClickToCombineName,
        socketGemCombinerClickToCombineMaterial,
        socketGemCombinerIneligibleToCombineMaterial,
        socketGemCombinerIneligibleToCombineName,
        socketGemCombinerRequireSameFamily,
        socketGemCombinerRequireSameLevel,
        socketFamilyLore,
        socketTypeLore,
        socketGemCombinerSameFamilyLore,
        socketGemCombinerSameLevelLore,
        socketGemCombinerSameFamilyAndLevelLore);
  }
}
