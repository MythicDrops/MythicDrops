/*
 * Originally created by deathmarine
 * Modified by Nunnery on March 10, 2013
 */

package com.modcrafting.diablodrops.builders;

import com.conventnunnery.plugins.mythicdrops.MythicDrops;
import com.conventnunnery.plugins.mythicdrops.managers.ConfigurationManager;
import com.conventnunnery.plugins.mythicdrops.objects.CustomItem;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.material.MaterialData;

import java.util.HashMap;
import java.util.Map;

public class CustomBuilder {
	private final MythicDrops plugin;

	public CustomBuilder(MythicDrops plugin) {
		this.plugin = plugin;
	}

	public void build() {
		getPlugin().getDropManager().getCustomItems().clear();
		FileConfiguration fc = getPlugin().getConfigurationManager()
				.getConfiguration(ConfigurationManager.ConfigurationFile.CUSTOMITEM);
		for (String s : fc.getKeys(false)) {
			if (!fc.isConfigurationSection(s)) {
                continue;
            }
			ConfigurationSection cs = fc.getConfigurationSection(s);
			String displayName = cs.getString("displayName");
			if (displayName == null) {
				displayName = s;
			}
			Map<Enchantment, Integer> map = new HashMap<Enchantment, Integer>();
			if (cs.isConfigurationSection("enchantments")) {
				ConfigurationSection enchCS = cs
						.getConfigurationSection("enchantments");
				for (String s2 : enchCS.getKeys(false)) {
					Enchantment ench = null;
					for (Enchantment ec : Enchantment.values()) {
						if (ec.getName().equalsIgnoreCase(s2)) {
							ench = ec;
							break;
						}
					}
					if (ench == null) {
                        continue;
                    }
					int level = enchCS.getInt(s2);
					map.put(ench, level);
				}
			}
			CustomItem ci = new CustomItem(s, displayName, cs.getStringList("lore"), map,
					new MaterialData(cs.getInt("materialID"), (byte) cs.getInt("materialData")),
					cs.getDouble("chance"));
			getPlugin()
					.getDropManager()
					.getCustomItems()
					.add(ci);
		}
	}

	/**
	 * @return the plugin
	 */
	public MythicDrops getPlugin() {
		return plugin;
	}
}
