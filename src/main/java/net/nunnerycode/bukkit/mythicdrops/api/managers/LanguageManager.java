package net.nunnerycode.bukkit.mythicdrops.api.managers;

import java.util.List;
import java.util.Map;
import net.nunnerycode.bukkit.mythicdrops.api.MythicDrops;
import org.bukkit.command.CommandSender;

public interface LanguageManager {

	Map<String, String> getMessages();

	void sendMessage(CommandSender reciever, String path);

	String getMessage(String path);

	void sendMessage(CommandSender reciever, String path, String[][] arguments);

	String getMessage(String path, String[][] arguments);

	List<String> getStringList(String path);

	List<String> getStringList(String path, String[][] arguments);

	MythicDrops getPlugin();

}
