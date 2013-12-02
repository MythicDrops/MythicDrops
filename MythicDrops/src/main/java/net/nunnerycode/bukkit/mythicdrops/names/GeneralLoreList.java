package net.nunnerycode.bukkit.mythicdrops.names;

import java.util.ArrayList;
import org.apache.commons.lang.math.RandomUtils;

public final class GeneralLoreList extends ArrayList<String> {

	private static final GeneralLoreList _INSTANCE = new GeneralLoreList();

	private GeneralLoreList() {
		// do nothing
	}

	public static GeneralLoreList getInstance() {
		return _INSTANCE;
	}

	public String getRandom() {
		return get(RandomUtils.nextInt(size()));
	}

}
