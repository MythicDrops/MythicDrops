package net.nunnerycode.bukkit.mythicdrops.names;

import java.util.ArrayList;
import org.apache.commons.lang.math.RandomUtils;

public class GeneralSuffixList extends ArrayList<String> {

	private static final GeneralSuffixList _INSTANCE = new GeneralSuffixList();

	private GeneralSuffixList() {
		// do nothing
	}

	public static GeneralSuffixList getInstance() {
		return _INSTANCE;
	}

	public String getRandom() {
		return get(RandomUtils.nextInt(size()));
	}

}
