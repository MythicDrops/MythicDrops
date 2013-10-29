package net.nunnerycode.bukkit.mythicdrops.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.nunnerycode.bukkit.mythicdrops.api.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.managers.ItemManager;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import net.nunnerycode.bukkit.mythicdrops.utils.RandomRangeUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.material.MaterialData;

public class MythicItemManager implements ItemManager {
	private final MythicDrops plugin;

	public MythicItemManager(final MythicDrops plugin) {
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
		String s = idList.get((int) RandomRangeUtils.randomRangeLongExclusive(0, idList.size()));
		MaterialData matData;
		if (s == null) {
			return null;
		}
		if (s.contains(";")) {
			String[] split = s.split(";");
			int id = NumberUtils.toInt(split[0], 0);
			int data = NumberUtils.toInt(split[1], 0);
			if (id == 0) {
				return null;
			}
			matData = new MaterialData(id, (byte) data);
		} else {
			int id = NumberUtils.toInt(s, 0);
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

	public List<Tier> getTiersForMaterialData(MaterialData materialData) {
		List<Tier> tiers = new ArrayList<Tier>();
		for (Tier t : getPlugin().getTierManager().getTiers()) {
			Set<MaterialData> materialDatas = getMaterialDataSetForTier(t);
			for (MaterialData md : materialDatas) {
				if (md.getItemTypeId() == materialData.getItemTypeId()) {
					tiers.add(t);
				}
			}
		}
		return tiers;
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
			int id;
			byte data = 0;
			if (s.contains(";")) {
				String[] split = s.split(";");
				id = NumberUtils.toInt(split[0], 0);
				data = NumberUtils.toByte(split[1], (byte) 0);
			} else {
				id = NumberUtils.toInt(s, 0);
			}
			if (id == 0) {
				continue;
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

	public boolean isArmor(String itemType) {
		return containsIgnoreCase(getPlugin().getSettingsManager().getArmorIDTypes(), itemType);
	}

	private boolean containsIgnoreCase(Collection<String> stringCollection, String s) {
		for (String s1 : stringCollection) {
			if (s1.equalsIgnoreCase(s)) {
				return true;
			}
		}
		return false;
	}

	public boolean isTool(String itemType) {
		return containsIgnoreCase(getPlugin().getSettingsManager().getToolIDTypes(), itemType);
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
			if (containsIgnoreCase(e.getValue(), comb)
					|| containsIgnoreCase(e.getValue(), comb2) || containsIgnoreCase
					(e.getValue(), comb3)) {
				if (containsIgnoreCase(getPlugin().getSettingsManager().getMaterialIDTypes(), e.getKey())) {
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
			if (containsIgnoreCase(e.getValue(), comb)
					|| containsIgnoreCase(e.getValue(), comb2) || containsIgnoreCase
					(e.getValue(), comb3)) {
				if (!containsIgnoreCase(getPlugin().getSettingsManager().getMaterialIDTypes(), e.getKey())) {
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
		return containsIgnoreCase(list, comb) || containsIgnoreCase(list, comb2);
	}
}