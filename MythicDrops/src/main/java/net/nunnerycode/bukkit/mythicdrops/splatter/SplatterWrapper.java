package net.nunnerycode.bukkit.mythicdrops.splatter;

import net.nunnerycode.bukkit.libraries.splatter.SplatterTracker;
import net.nunnerycode.bukkit.libraries.splatter.SplatterTrackerGitHub;
import org.apache.commons.lang3.Validate;

public final class SplatterWrapper {

	private SplatterTracker splatterTracker;

	public SplatterWrapper(String pluginName, String user, String pass) {
		Validate.notNull(user, "user cannot be null");
		Validate.notNull(pass, "user cannot be null");

		splatterTracker = new SplatterTrackerGitHub(pluginName, user, pass);
	}

	public SplatterTracker getSplatterTracker() {
		return splatterTracker;
	}

}
