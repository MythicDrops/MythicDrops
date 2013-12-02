package net.nunnerycode.bukkit.mythicdrops.names;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Material;

public final class MaterialLoreMap extends ConcurrentHashMap<Material, List<String>> {

	private static final MaterialLoreMap _INSTANCE = new MaterialLoreMap();

	private MaterialLoreMap() {
		// do nothing
	}

	public static MaterialLoreMap getInstance() {
		return _INSTANCE;
	}

	public String getRandom(Material material) {
		if (!containsKey(material)) {
			return null;
		}
		List<String> materialLore = get(material);
		return materialLore.get(RandomUtils.nextInt(materialLore.size()));
	}

}
