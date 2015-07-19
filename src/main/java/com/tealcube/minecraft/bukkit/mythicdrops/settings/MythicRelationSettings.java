package com.tealcube.minecraft.bukkit.mythicdrops.settings;

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
