/**
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2013 Teal Cube Games
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

import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.RelationSettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MythicRelationSettings implements RelationSettings {

    private Map<String, List<String>> nameRelationMap;

    public MythicRelationSettings() {
        this.nameRelationMap = new HashMap<>();
    }

    @Override
    public List<String> getLoreFromName(String name) {
        return nameRelationMap.containsKey(name) ? nameRelationMap.get(name) : new ArrayList<String>();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setLoreFromName(String name, List<String> lore) {
        nameRelationMap.put(name, lore);
    }

}
