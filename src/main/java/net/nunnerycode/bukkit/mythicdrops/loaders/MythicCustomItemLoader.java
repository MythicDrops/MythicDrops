package net.nunnerycode.bukkit.mythicdrops.loaders;

import com.conventnunnery.libraries.config.ConventConfiguration;
import net.nunnerycode.bukkit.mythicdrops.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.loaders.ConfigLoader;
import net.nunnerycode.bukkit.mythicdrops.items.MythicCustomItem;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.material.MaterialData;

import java.util.HashMap;
import java.util.Map;

public class MythicCustomItemLoader implements ConfigLoader {
    private final MythicDrops plugin;

    public MythicCustomItemLoader(final MythicDrops plugin) {
        this.plugin = plugin;
    }

    @Override
    public void load() {
        getPlugin().getCustomItemManager().getCustomItems().clear();
		ConventConfiguration c = getPlugin().getCustomItemsYAML();
		if (c == null) {
			return;
		}
        FileConfiguration configuration = c.getFileConfiguration();
        for (String key : configuration.getKeys(false)) {
            if (!configuration.isConfigurationSection(key)) {
                continue;
            }
            ConfigurationSection cs = configuration.getConfigurationSection(key);
            MythicCustomItem mythicCustomItem = new MythicCustomItem();
            mythicCustomItem.setName(key);
            mythicCustomItem.setDisplayName(cs.getString("displayName", mythicCustomItem.getName()));
            mythicCustomItem.setLore(cs.getStringList("lore"));
            mythicCustomItem.setMaterialData(new MaterialData(cs.getInt("materialID", 0),
                    (byte) cs.getInt("materialData", 0)));
            if (mythicCustomItem.getMaterialData().getItemTypeId() == 0) {
                continue;
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
            mythicCustomItem.setEnchantments(map);
            mythicCustomItem.setChanceToBeGivenToAMonster(cs.getDouble("chanceToBeGivenToAMonster", 1.0));
            mythicCustomItem.setChanceToDropOnMonsterDeath(cs.getDouble("chanceToDropOnMonsterDeath", 1.0));
            getPlugin().getCustomItemManager().getCustomItems().add(mythicCustomItem);
        }
    }

    public MythicDrops getPlugin() {
        return plugin;
    }
}