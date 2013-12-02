package net.nunnerycode.bukkit.mythicdrops.names;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Material;

public final class MaterialPrefixMap extends ConcurrentHashMap<Material, List<String>> {

	private static final MaterialPrefixMap _INSTANCE = new MaterialPrefixMap();

	private MaterialPrefixMap() {
		// do nothing
	}

	public static MaterialPrefixMap getInstance() {
		return _INSTANCE;
	}

	public String getRandom(Material material) {
		if (!containsKey(material)) {
			return null;
		}
		List<String> materialPrefixes = get(material);
		return materialPrefixes.get(RandomUtils.nextInt(materialPrefixes.size()));
	}

}
