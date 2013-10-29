package net.nunnerycode.bukkit.mythicdrops.api.commands;

import net.nunnerycode.bukkit.mythicdrops.MythicDropsPlugin;
import se.ranzdo.bukkit.methodcommand.CommandHandler;

public interface MythicCommand {

	CommandHandler getCommandHandler();

	MythicDropsPlugin getPlugin();

}
