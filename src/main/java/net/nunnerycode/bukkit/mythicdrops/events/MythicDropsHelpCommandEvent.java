package net.nunnerycode.bukkit.mythicdrops.events;

import java.util.Collections;
import java.util.Map;
import net.nunnerycode.bukkit.mythicdrops.api.events.MythicDropsEvent;
import org.bukkit.command.CommandSender;

/**
 * An event called whenever the command "/md help" is issued.
 */
public class MythicDropsHelpCommandEvent extends MythicDropsEvent {

	private CommandSender commandSender;
	private Map<String, String> commands;

	/**
	 * Instantiates a new MythicDropsHelpCommandEvent.
	 * @param commandSender whoever sent the command
	 * @param commands Map of commands and their descriptions
	 */
	public MythicDropsHelpCommandEvent(CommandSender commandSender, Map<String, String> commands) {
		this.commandSender = commandSender;
		this.commands = commands;
	}

	/**
	 * Gets a {@link Map} of commands and their descriptions involved in the event.
	 * @return Unmodifiable Map of commands and their descriptions
	 */
	public Map<String, String> getCommands() {
		return Collections.unmodifiableMap(commands);
	}

	/**
	 * Safe way to add a command to the Map returned by {@link #getCommands()}.
	 * @param command Command to add
	 * @param help Description of the command
	 */
	public void addCommand(String command, String help) {
		commands.put(command, help);
	}

	/**
	 * Gets the sender of the command.
	 * @return sender of the command
	 */
	public CommandSender getCommandSender() {
		return commandSender;
	}
}
