package com.conventnunnery.plugins.mythicdrops.objects.parents;

import com.conventnunnery.plugins.conventlib.utils.CollectionUtils;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

import java.util.List;

public class MythicScroll extends MythicRegularItem {

    public MythicScroll(String displayName, List<String> lore) {
        super(new MaterialData(Material.EMPTY_MAP, (byte) 0), displayName, CollectionUtils.toStringArray(lore));
    }

}
