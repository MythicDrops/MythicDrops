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

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SockettingSettings;
import java.util.ArrayList;
import java.util.List;
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

  public MythicSockettingSettings() {
    socketGemLore = new ArrayList<>();
    sockettedItemLore = new ArrayList<>();
    socketGemMaterials = new ArrayList<>();
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

  @Override
  public boolean isCanDropSocketGemsOnItems() {
    return canDropSocketGemsOnItems;
  }

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
}
