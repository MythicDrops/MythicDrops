package net.nunnerycode.bukkit.mythicdrops.names;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Material;

public final class MaterialSuffixMap extends ConcurrentHashMap<Material, List<String>> {

	private static final MaterialSuffixMap _INSTANCE = new MaterialSuffixMap();

	private MaterialSuffixMap() {
		// do nothing
	}

	public static MaterialSuffixMap getInstance() {
		return _INSTANCE;
	}

	public String getRandom(Material material) {
		if (!containsKey(material)) {
			return null;
		}
		List<String> materialSuffixes = get(material);
		return materialSuffixes.get(RandomUtils.nextInt(materialSuffixes.size()));
	}

}
