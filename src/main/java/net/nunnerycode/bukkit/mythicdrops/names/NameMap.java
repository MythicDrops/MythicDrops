package net.nunnerycode.bukkit.mythicdrops.names;

import net.nunnerycode.bukkit.mythicdrops.api.names.NameType;
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
