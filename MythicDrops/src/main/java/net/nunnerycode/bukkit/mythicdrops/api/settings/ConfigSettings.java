package net.nunnerycode.bukkit.mythicdrops.api.settings;

import java.util.List;
import java.util.Map;

public interface ConfigSettings {

	// general
	boolean isModified();

	// itemGroups.yml
	List<String> getArmorTypes();

	List<String> getToolTypes();

	List<String> getMaterialTypes();

	Map<String, List<String>> getItemTypesWithIds();

	Map<String, List<String>> getMaterialTypesWithIds();

	// config.yml
	boolean isAutoUpdate();

	boolean isDebugMode();

	String getItemDisplayNameFormat();

	boolean isRandomLoreEnabled();

	double getRandomLoreChance();

	List<String> getTooltipFormat();

	// language.yml
	String getLanguageString(String key);

	String getLanguageString(String key, String[][] args);

	String getFormattedLanguageString(String key);

	String getFormattedLanguageString(String key, String[][] args);

}
