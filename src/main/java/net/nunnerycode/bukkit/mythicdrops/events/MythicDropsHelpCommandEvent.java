package net.nunnerycode.bukkit.mythicdrops.events;

import java.util.Collections;
import java.util.Map;
import net.nunnerycode.bukkit.mythicdrops.api.events.MythicDropsEvent;

public class MythicDropsHelpCommandEvent extends MythicDropsEvent {

	private Map<String, String> commands;

	public MythicDropsHelpCommandEvent(Map<String, String> commands) {
		this.commands = commands;
	}

	public Map<String, String> getCommands() {
		return Collections.unmodifiableMap(commands);
	}

	public void addCommand(String command, String help) {
		commands.put(command, help);
	}
}
