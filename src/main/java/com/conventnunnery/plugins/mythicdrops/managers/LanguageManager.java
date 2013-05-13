package com.conventnunnery.plugins.mythicdrops.managers;

import com.conventnunnery.plugins.mythicdrops.MythicDrops;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class LanguageManager {

	private final MythicDrops plugin;

	public LanguageManager(MythicDrops plugin) {
		this.plugin = plugin;
	}

	public MythicDrops getPlugin() {
		return plugin;
	}

	@SuppressWarnings("unused")
	public void sendMessage(CommandSender reciever, String path) {
		String message = getMessage(path);
		if (message == null)
			return;
		reciever.sendMessage(message);
	}

	@SuppressWarnings("unused")
	public void sendMessage(CommandSender reciever, String path,
	                        String[][] arguments) {
		String message = getMessage(path, arguments);
		if (message == null)
			return;
		reciever.sendMessage(message);
	}

	public List<String> getStringList(String path) {
		List<String> message =
				getPlugin().getConfigurationManager().getConfiguration(ConfigurationManager.ConfigurationFile.LANGUAGE)
						.getStringList(
								path);
		List<String> strings = new ArrayList<String>();
		for (String s : message) {
			strings.add(ChatColor.translateAlternateColorCodes('&', s));
		}
		return strings;
	}

	public List<String> getStringList(String path, String[][] arguments) {
		List<String> message =
				getPlugin().getConfigurationManager().getConfiguration(ConfigurationManager.ConfigurationFile.LANGUAGE)
						.getStringList(
								path);
		List<String> strings = new ArrayList<String>();
		for (String s : message) {
			for (String[] argument : arguments) {
				strings.add(s.replace(argument[0], argument[1]));
			}
		}
		return strings;
	}

	public String getMessage(String path) {
		String message =
				getPlugin().getConfigurationManager().getConfiguration(ConfigurationManager.ConfigurationFile.LANGUAGE)
						.getString(
								path);
		if (message == null)
			return null;
		message = ChatColor.translateAlternateColorCodes('&', message);
		return message;
	}

	public String getMessage(String path, String[][] arguments) {
		String message =
				getPlugin().getConfigurationManager().getConfiguration(ConfigurationManager.ConfigurationFile.LANGUAGE)
						.getString(
								path);
		if (message == null)
			return null;
		message = ChatColor.translateAlternateColorCodes('&', message);
		for (String[] argument : arguments) {
			message = message.replaceAll(argument[0], argument[1]);
		}
		return message;
	}
}
