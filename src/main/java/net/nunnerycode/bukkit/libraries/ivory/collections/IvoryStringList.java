package net.nunnerycode.bukkit.libraries.ivory.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class IvoryStringList extends ArrayList<String> {

    public IvoryStringList(int initialCapacity) {
        super(initialCapacity);
    }

    public IvoryStringList() {
        super();
    }

    public IvoryStringList(Collection<? extends String> c) {
        super(c);
    }

    public IvoryStringList replaceWithList(String key, List<String> list) {
        if (key == null || list == null) {
            return this;
        }
        for (int i = 0; i < size(); i++) {
            if (get(i).equals(key)) {
                remove(i);
                addAll(i, list);
            }
        }
        return this;
    }

}
