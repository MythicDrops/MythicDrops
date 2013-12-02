package net.nunnerycode.bukkit.mythicdrops.names;

import java.util.ArrayList;
import org.apache.commons.lang.math.RandomUtils;

public class GeneralPrefixList extends ArrayList<String> {

	private static final GeneralPrefixList _INSTANCE = new GeneralPrefixList();

	private GeneralPrefixList() {
		// do nothing
	}

	public static GeneralPrefixList getInstance() {
		return _INSTANCE;
	}

	public String getRandom() {
		return get(RandomUtils.nextInt(size()));
	}

}
