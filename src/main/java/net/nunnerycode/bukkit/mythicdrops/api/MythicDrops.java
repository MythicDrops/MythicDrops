package net.nunnerycode.bukkit.mythicdrops.api;

import com.conventnunnery.libraries.config.CommentedConventYamlConfiguration;
import java.io.File;
import java.util.logging.Level;
import net.nunnerycode.bukkit.mythicdrops.api.commands.MythicCommand;
import net.nunnerycode.bukkit.mythicdrops.api.loaders.ConfigLoader;
import net.nunnerycode.bukkit.mythicdrops.api.managers.CustomItemManager;
import net.nunnerycode.bukkit.mythicdrops.api.managers.DropManager;
import net.nunnerycode.bukkit.mythicdrops.api.managers.EntityManager;
import net.nunnerycode.bukkit.mythicdrops.api.managers.ItemManager;
import net.nunnerycode.bukkit.mythicdrops.api.managers.LanguageManager;
import net.nunnerycode.bukkit.mythicdrops.api.managers.NameManager;
import net.nunnerycode.bukkit.mythicdrops.api.managers.SettingsManager;
import net.nunnerycode.bukkit.mythicdrops.api.managers.TierManager;
import net.nunnerycode.bukkit.mythicdrops.api.savers.ConfigSaver;

public interface MythicDrops {

	CommentedConventYamlConfiguration getConfigYAML();

	CommentedConventYamlConfiguration getCustomItemsYAML();

	CommentedConventYamlConfiguration getItemGroupsYAML();

	CommentedConventYamlConfiguration getLanguageYAML();

	CommentedConventYamlConfiguration getTierYAML();

	EntityManager getEntityManager();

	DropManager getDropManager();

	ConfigLoader getLanguageLoader();

	ConfigLoader getSettingsLoader();

	ConfigLoader getCustomItemLoader();

	NameManager getNameManager();

	void reload();

	SettingsManager getSettingsManager();

	ConfigLoader getTierLoader();

	TierManager getTierManager();

	LanguageManager getLanguageManager();

	CustomItemManager getCustomItemManager();

	ItemManager getItemManager();

	File getJar();

	ConfigSaver getCustomItemSaver();

	ConfigSaver getLanguageSaver();

	ConfigSaver getTierSaver();

	ConfigSaver getSettingsSaver();

	MythicCommand getCommand();

	void debug(Level level, String... messages);

}
