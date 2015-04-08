package com.tealcube.minecraft.bukkit.mythicdrops.settings;

/*
 * #%L
 * MythicDrops
 * %%
 * Copyright (C) 2013 - 2015 TealCube
 * %%
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
 * provided that the above copyright notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
 * INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF
 * THIS SOFTWARE.
 * #L%
 */


import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SockettingSettings;
import com.tealcube.minecraft.bukkit.mythicdrops.socketting.SocketGem;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MythicSockettingSettings implements SockettingSettings {

    private String socketGemName;
    private List<String> socketGemLore;
    private String sockettedItemString;
    private List<String> sockettedItemLore;
    private boolean useAttackerItemInHand;
    private boolean useAttackerArmorEquipped;
    private boolean useDefenderItemInHand;
    private boolean useDefenderArmorEquipped;
    private List<Material> socketGemMaterialDatas;
    private Map<String, SocketGem> socketGemMap;
    private List<String> socketGemPrefixes;
    private boolean preventMultipleChangesFromSockets;
    private List<String> socketGemSuffixes;
    private boolean canDropSocketGemsOnItems;

    public MythicSockettingSettings() {
        socketGemLore = new ArrayList<>();
        sockettedItemLore = new ArrayList<>();
        socketGemMaterialDatas = new ArrayList<>();
        socketGemMap = new HashMap<>();
        socketGemPrefixes = new ArrayList<>();
        socketGemSuffixes = new ArrayList<>();
    }

    @Override
    @Deprecated
    public boolean isEnabled() {
        return true;
    }

    @Deprecated
    public void setEnabled(boolean enabled) {
        // do nothing
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
        return socketGemMaterialDatas;
    }

    public void setSocketGemMaterials(List<Material> socketGemMaterialDatas) {
        this.socketGemMaterialDatas = socketGemMaterialDatas;
    }

    @Override
    public Map<String, SocketGem> getSocketGemMap() {
        return socketGemMap;
    }

    public void setSocketGemMap(Map<String, SocketGem> socketGemMap) {
        this.socketGemMap = socketGemMap;
    }

    @Override
    public List<String> getSocketGemPrefixes() {
        return socketGemPrefixes;
    }

    @Override
    public List<String> getSocketGemSuffixes() {
        return socketGemSuffixes;
    }

    @Override
    public boolean isCanDropSocketGemsOnItems() {
        return canDropSocketGemsOnItems;
    }

    public void setCanDropSocketGemsOnItems(boolean canDropSocketGemsOnItems) {
        this.canDropSocketGemsOnItems = canDropSocketGemsOnItems;
    }
}
