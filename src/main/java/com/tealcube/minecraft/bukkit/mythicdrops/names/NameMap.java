package com.tealcube.minecraft.bukkit.mythicdrops.names;

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


import com.tealcube.minecraft.bukkit.mythicdrops.api.names.NameType;

import org.apache.commons.lang.math.RandomUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public final class NameMap extends ConcurrentHashMap<String, List<String>> {

    private static final NameMap _INSTANCE = new NameMap();

    private NameMap() {
        // do nothing
    }

    public static NameMap getInstance() {
        return _INSTANCE;
    }

    public String getRandomKey(NameType nameType) {
        List<String> matchingKeys = getMatchingKeys(nameType);
        String key = matchingKeys.get(RandomUtils.nextInt(matchingKeys.size()));
        return key.replace(nameType.getFormat(), "");
    }

    public List<String> getMatchingKeys(NameType nameType) {
        List<String> matchingKeys = new ArrayList<>();
        for (String key : keySet()) {
            if (key.startsWith(nameType.getFormat())) {
                matchingKeys.add(key);
            }
        }
        return matchingKeys;
    }

    public String getRandom(NameType nameType, String key) {
        List<String> list = new ArrayList<>();
        if (containsKey(nameType.getFormat() + key)) {
            list = get(nameType.getFormat() + key);
        }
        if (list == null || list.isEmpty()) {
            return "";
        }
        return list.get(RandomUtils.nextInt(list.size()));
    }

    @Override
    public List<String> put(String string, List<String> list) {
        if (string == null) {
            return list;
        }
        if (list == null) {
            return new ArrayList<>();
        }
        return super.put(string, list);
    }

    public List<String> get(String key) {
        if (key == null) {
            return new ArrayList<>();
        }
        if (!containsKey(key)) {
            return new ArrayList<>();
        }
        return super.get(key);
    }

}
