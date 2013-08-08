package net.nunnerycode.bukkit.mythicdrops.savers;

import com.conventnunnery.libraries.config.ConventConfiguration;
import net.nunnerycode.bukkit.mythicdrops.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.items.CustomItem;
import net.nunnerycode.bukkit.mythicdrops.api.utils.MythicSaver;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;

import java.util.Map;

public class MythicCustomItemSaver implements MythicSaver {
	private MythicDrops plugin;

	public MythicCustomItemSaver(MythicDrops plugin) {
		this.plugin = plugin;
	}

	@Override
	public void save() {
		ConventConfiguration c = getPlugin().getConventConfigurationGroup().getConventConfiguration("customItems.yml");
		if (c == null) {
			return;
		}
		FileConfiguration fc = c.getFileConfiguration();
		for (CustomItem ci : plugin.getCustomItemManager().getCustomItems()) {
			fc.set(ci.getName() + ".displayName", ci.getDisplayName());
			fc.set(ci.getName() + ".lore", ci.getLore());
			fc.set(ci.getName() + ".materialID", ci.getMaterialData().getItemTypeId());
			fc.set(ci.getName() + ".materialData", ci.getMaterialData().getData());
			fc.set(ci.getName() + ".chanceToBeGivenToAMonster", ci.getChanceToBeGivenToAMonster());
			fc.set(ci.getName() + ".chanceToDropOnMonsterDeath", ci.getChanceToDropOnDeath());
			for (Map.Entry<Enchantment, Integer> entry : ci.getEnchantments().entrySet()) {
				fc.set(ci.getName() + ".enchantments." + entry.getKey().getName(), entry.getValue());
			}
		}
		save();
	}

	public MythicDrops getPlugin() {
		return plugin;
	}
}
