package net.nunnerycode.bukkit.mythicdrops.events;

import java.util.Collections;
import java.util.Map;
import net.nunnerycode.bukkit.mythicdrops.api.events.MythicDropsEvent;
import org.bukkit.command.CommandSender;

public class MythicDropsHelpCommandEvent extends MythicDropsEvent {

	private CommandSender commandSender;
	private Map<String, String> commands;

	public MythicDropsHelpCommandEvent(CommandSender commandSender, Map<String, String> commands) {
		this.commandSender = commandSender;
		this.commands = commands;
	}

	public Map<String, String> getCommands() {
		return Collections.unmodifiableMap(commands);
	}

	public void addCommand(String command, String help) {
		commands.put(command, help);
	}

	public CommandSender getCommandSender() {
		return commandSender;
	}
}
