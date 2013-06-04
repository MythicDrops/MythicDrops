package com.conventnunnery.plugins.mythicdrops.builders;

import com.conventnunnery.plugins.conventlib.utils.NumberUtils;
import com.conventnunnery.plugins.mythicdrops.MythicDrops;
import com.conventnunnery.plugins.mythicdrops.configuration.MythicConfigurationFile;
import com.conventnunnery.plugins.mythicdrops.objects.RepairCost;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.material.MaterialData;

public class RepairCostBuilder {
    private final MythicDrops plugin;

    public RepairCostBuilder(MythicDrops plugin) {
        this.plugin = plugin;
    }

    public MythicDrops getPlugin() {
        return plugin;
    }

    public void build() {
        getPlugin().getRepairManager().getRepairCostSet().clear();
        FileConfiguration fc = getPlugin().getConfigurationManager().getConfiguration(MythicConfigurationFile.REPAIRCOSTS);
        for (String key : fc.getKeys(false)) {
            if (!fc.isConfigurationSection(key)) {
                continue;
            }
            ConfigurationSection cs = fc.getConfigurationSection(key);
            String[] keySplit = key.split(";");
            int id = NumberUtils.getInt(keySplit[0], 0);
            byte data = 0;
            if (keySplit.length > 1) {
                data = (byte) NumberUtils.getInt(keySplit[1], 0);
            }
            if (id == 0) {
                continue;
            }
            MaterialData materialData = new MaterialData(id, data);
            String required = cs.getString("id");
            String[] requiredSplit = required.split(";");
            int reqId = NumberUtils.getInt(requiredSplit[0], 0);
            byte reqData = 0;
            if (requiredSplit.length > 1) {
                reqData = (byte) NumberUtils.getInt(requiredSplit[1], 0);
            }
            if (reqId == 0) {
                continue;
            }
            MaterialData requiredData = new MaterialData(reqId, reqData);
            int cost = NumberUtils.getInt(cs.getString("cost"), 1);
            double repairPerCost = NumberUtils.getDouble(cs.getString("repairPerCost"), 0.1);
            RepairCost repairCost = new RepairCost(materialData, requiredData, cost, repairPerCost);
            getPlugin().getRepairManager().getRepairCostSet().add(repairCost);
        }
    }
}
