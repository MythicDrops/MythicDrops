package net.nunnerycode.bukkit.mythicdrops.managers;

import com.conventnunnery.libraries.utils.CollectionUtils;
import com.conventnunnery.libraries.utils.NumberUtils;
import com.conventnunnery.libraries.utils.RandomUtils;
import net.nunnerycode.bukkit.mythicdrops.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ItemManager {
    private final MythicDrops plugin;

    public ItemManager(final MythicDrops plugin) {
        this.plugin = plugin;
    }

    public MaterialData getMatDataFromItemType(String itemType) {
        Map<String, List<String>> ids = getPlugin().getSettingsManager().getIds();
        if (!ids.containsKey(itemType.toLowerCase())) {
            return null;
        }
        List<String> idList = ids.get(itemType.toLowerCase());
        if (idList == null || idList.isEmpty()) {
            return null;
        }
        String s = idList.get((int) RandomUtils.randomRangeWholeExclusive(0, idList.size()));
        MaterialData matData;
        if (s == null) {
            return null;
        }
        if (s.contains(";")) {
            String[] split = s.split(";");
            int id = NumberUtils.getInt(split[0], 0);
            int data = NumberUtils.getInt(split[1], 0);
            if (id == 0) {
                return null;
            }
            matData = new MaterialData(id, (byte) data);
        } else {
            int id = NumberUtils.getInt(s, 0);
            if (id == 0) {
                return null;
            }
            matData = new MaterialData(id);
        }
        return matData;
    }

    public MythicDrops getPlugin() {
        return plugin;
    }

    public boolean isArmor(String itemType) {
        return CollectionUtils.containsIgnoreCase(getPlugin().getSettingsManager().getArmorIDTypes(), itemType);
    }

    public boolean isTool(String itemType) {
        return CollectionUtils.containsIgnoreCase(getPlugin().getSettingsManager().getToolIDTypes(), itemType);
    }

    public boolean isMatDataInTier(MaterialData materialData, Tier tier) {
        Set<MaterialData> materialDatas = getMaterialDataSetForTier(tier);
        for (MaterialData matData : materialDatas) {
            if (matData.getItemTypeId() == materialData.getItemTypeId() && matData.getData() == materialData.getData
                    ()) {
                return true;
            }
        }
        return false;
    }

    public Set<MaterialData> getMaterialDataSetForTier(Tier tier) {
        List<String> allowedItemIds = new ArrayList<String>(tier.getAllowedIds());
        List<String> disallowedItemIds = new ArrayList<String>(tier.getDisallowedIds());
        List<String> allowedItemGroups = new ArrayList<String>(tier.getAllowedGroups());
        List<String> disallowedItemGroups = new ArrayList<String>(tier.getDisallowedGroups());
        List<String> idList = new ArrayList<String>(allowedItemIds);
        idList.removeAll(disallowedItemIds);
        for (String itemType : allowedItemGroups) {
            idList.addAll(getMaterialIDsForItemType(itemType.toLowerCase()));
        }
        for (String itemType : disallowedItemGroups) {
            idList.removeAll(getMaterialIDsForItemType(itemType.toLowerCase()));
        }
        Set<MaterialData> materialDatas = new HashSet<MaterialData>();
        for (String s : idList) {
            String[] split = s.split(";");
            int id = NumberUtils.getInt(split[0], 0);
            if (id == 0) {
                continue;
            }
            byte data = 0;
            if (split.length > 1) {
                data = (byte) NumberUtils.getInt(split[1], 0);
            }
            materialDatas.add(new MaterialData(id, data));
        }
        return materialDatas;
    }

    public List<String> getMaterialIDsForItemType(String itemType) {
        List<String> materialIds = new ArrayList<String>();
        Map<String, List<String>> map = getPlugin().getSettingsManager()
                .getIds();
        if (map == null || map.isEmpty()) {
            return materialIds;
        }
        if (itemType == null) {
            return materialIds;
        }
        if (map.containsKey(itemType.toLowerCase())) {
            materialIds = getPlugin().getSettingsManager().getIds()
                    .get(itemType.toLowerCase());
        }
        return materialIds;
    }

    public String itemTypeFromMatData(MaterialData matData) {
        String comb =
                String.format("%s;%s", String.valueOf(matData.getItemTypeId()), String.valueOf(matData.getData()));
        String comb2;
        if (matData.getData() == (byte) 0) {
            comb2 = String.valueOf(matData.getItemTypeId());
        } else {
            comb2 = comb;
        }
        String comb3 = String.valueOf(matData.getItemTypeId());
        Map<String, List<String>> ids = getPlugin().getSettingsManager()
                .getIds();
        for (Map.Entry<String, List<String>> e : ids.entrySet()) {
            if (CollectionUtils.containsIgnoreCase(e.getValue(), comb)
                    || CollectionUtils.containsIgnoreCase(e.getValue(), comb2) || CollectionUtils.containsIgnoreCase
                    (e.getValue(), comb3)) {
                if (CollectionUtils
                        .containsIgnoreCase(getPlugin().getSettingsManager().getMaterialIDTypes(), e.getKey())) {
                    continue;
                }
                return e.getKey();
            }
        }
        return null;
    }

    public String materialTypeFromMatData(MaterialData matData) {
        String comb =
                String.format("%s;%s", String.valueOf(matData.getItemTypeId()), String.valueOf(matData.getData()));
        String comb2;
        if (matData.getData() == (byte) 0) {
            comb2 = String.valueOf(matData.getItemTypeId());
        } else {
            comb2 = comb;
        }
        String comb3 = String.valueOf(matData.getItemTypeId());
        Map<String, List<String>> ids = getPlugin().getSettingsManager()
                .getIds();
        for (Map.Entry<String, List<String>> e : ids.entrySet()) {
            if (CollectionUtils.containsIgnoreCase(e.getValue(), comb)
                    || CollectionUtils.containsIgnoreCase(e.getValue(), comb2) || CollectionUtils.containsIgnoreCase
                    (e.getValue(), comb3)) {
                if (!CollectionUtils
                        .containsIgnoreCase(getPlugin().getSettingsManager().getMaterialIDTypes(), e.getKey())) {
                    continue;
                }
                return e.getKey();
            }
        }
        return null;
    }

    public boolean isItemType(String itemType, MaterialData matData) {
        String comb =
                String.format("%s;%s", String.valueOf(matData.getItemTypeId()), String.valueOf(matData.getData()));
        String comb2;
        if (matData.getData() == (byte) 0) {
            comb2 = String.valueOf(matData.getItemTypeId());
        } else {
            comb2 = comb;
        }
        Map<String, List<String>> ids = getPlugin().getSettingsManager()
                .getIds();
        List<String> list = new ArrayList<String>();
        if (ids.keySet().contains(itemType.toLowerCase())) {
            list = ids.get(itemType.toLowerCase());
        }
        return CollectionUtils.containsIgnoreCase(list, comb) || CollectionUtils.containsIgnoreCase(list, comb2);
    }
}
