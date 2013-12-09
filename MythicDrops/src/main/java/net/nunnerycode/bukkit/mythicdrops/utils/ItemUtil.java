package net.nunnerycode.bukkit.mythicdrops.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.nunnerycode.bukkit.mythicdrops.MythicDropsPlugin;
import net.nunnerycode.bukkit.mythicdrops.api.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import net.nunnerycode.bukkit.mythicdrops.items.TierMap;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

public final class ItemUtil {

	private static MythicDrops plugin = MythicDropsPlugin.getInstance();

	private ItemUtil() {
		// do nothing
	}

	public static MaterialData getRandomMaterialDataFromCollection(Collection<MaterialData> collection) {
		MaterialData[] array = collection.toArray(new MaterialData[collection.size()]);
		return array[RandomUtils.nextInt(array.length)];
	}

	/**
	 * Gets a {@link Collection} of {@link Tier}s that the given {@link MaterialData} can be used by.
	 *
	 * @param materialData MaterialData to check
	 * @return All Tiers that can use the given MaterialData
	 */
	public static Collection<Tier> getTiersFromMaterialData(MaterialData materialData) {
		List<Tier> list = new ArrayList<Tier>();
		for (Tier t : TierMap.getInstance().values()) {
			Collection<MaterialData> materialDatas = getMaterialDatasFromTier(t);
			for (MaterialData md : materialDatas) {
				if (md.getItemType() == materialData.getItemType()) {
					list.add(t);
				}
			}
		}
		return list;
	}

	/**
	 * Gets a {@link Collection} of {@link MaterialData}s that the given {@link Tier} contains.
	 *
	 * @param tier Tier to check
	 * @return All MaterialDatas for the given Tier
	 */
	public static Collection<MaterialData> getMaterialDatasFromTier(Tier tier) {
		List<String> idList = new ArrayList<>(tier.getAllowedItemIds());
		for (String itemType : tier.getAllowedItemGroups()) {
			if (plugin.getConfigSettings().getItemTypesWithIds().containsKey(itemType.toLowerCase())) {
				idList.addAll(plugin.getConfigSettings().getItemTypesWithIds().get(itemType.toLowerCase()));
			}
			if (plugin.getConfigSettings().getMaterialTypesWithIds().containsKey(itemType.toLowerCase())) {
				idList.addAll(plugin.getConfigSettings().getMaterialTypesWithIds().get(itemType.toLowerCase()));
			}
		}
		for (String itemType : tier.getDisallowedItemGroups()) {
			if (plugin.getConfigSettings().getItemTypesWithIds().containsKey(itemType.toLowerCase())) {
				idList.removeAll(plugin.getConfigSettings().getItemTypesWithIds().get(itemType.toLowerCase()));
			}
			if (plugin.getConfigSettings().getMaterialTypesWithIds().containsKey(itemType.toLowerCase())) {
				idList.removeAll(plugin.getConfigSettings().getMaterialTypesWithIds().get(itemType.toLowerCase()));
			}
		}
		idList.removeAll(tier.getDisallowedItemIds());
		Set<MaterialData> materialDatas = new HashSet<>();
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

	/**
	 * Returns true if the given item type is a kind of armor.
	 *
	 * @param itemType item type to check
	 * @return if item type is a kind of armor
	 */
	public static boolean isArmor(String itemType) {
		return plugin.getConfigSettings().getArmorTypes().contains(itemType.toLowerCase());
	}

	/**
	 * Returns true if the given item type is a kind of tool.
	 *
	 * @param itemType item type to check
	 * @return if item type is a kind of tool
	 */
	public static boolean isTool(String itemType) {
		return plugin.getConfigSettings().getToolTypes().contains(itemType.toLowerCase());
	}

	/**
	 * Returns true if the given material type is a kind of material.
	 *
	 * @param materialType material type to check
	 * @return if item type is a kind of material
	 */
	public static boolean isMaterial(String materialType) {
		return plugin.getConfigSettings().getMaterialTypes().contains(materialType.toLowerCase());
	}

	/**
	 * Gets the item type from the given {@link MaterialData}.
	 *
	 * @param materialData MaterialData to check
	 * @return item type
	 */
	public static String getItemTypeFromMaterialData(MaterialData materialData) {
		String comb = String.format("%s;%s", String.valueOf(materialData.getItemTypeId()),
				String.valueOf(materialData.getData()));
		String comb2;
		if (materialData.getData() == (byte) 0) {
			comb2 = String.valueOf(materialData.getItemTypeId());
		} else {
			comb2 = comb;
		}
		String comb3 = String.valueOf(materialData.getItemTypeId());
		Map<String, List<String>> ids = plugin.getConfigSettings().getItemTypesWithIds();
		for (Map.Entry<String, List<String>> e : ids.entrySet()) {
			if (e.getValue().contains(comb) || e.getValue().contains(comb2) || e.getValue().contains(comb3)) {
				if (plugin.getConfigSettings().getMaterialTypes().contains(e.getKey())) {
					continue;
				}
				return e.getKey();
			}
		}
		return null;
	}

	/**
	 * Gets the material type from the given {@link MaterialData}.
	 *
	 * @param materialData MaterialData to check
	 * @return material type
	 */
	public static String getMaterialTypeFromMaterialData(MaterialData materialData) {
		String comb = String.format("%s;%s", String.valueOf(materialData.getItemTypeId()),
				String.valueOf(materialData.getData()));
		String comb2;
		if (materialData.getData() == (byte) 0) {
			comb2 = String.valueOf(materialData.getItemTypeId());
		} else {
			comb2 = comb;
		}
		String comb3 = String.valueOf(materialData.getItemTypeId());
		Map<String, List<String>> ids = plugin.getConfigSettings().getMaterialTypesWithIds();
		for (Map.Entry<String, List<String>> e : ids.entrySet()) {
			if (e.getValue().contains(comb) || e.getValue().contains(comb2) || e.getValue().contains(comb3)) {
				if (plugin.getConfigSettings().getArmorTypes().contains(e.getKey()) || plugin.getConfigSettings()
						.getToolTypes().contains(e.getKey())) {
					continue;
				}
				return e.getKey();
			}
		}
		return null;
	}

	/**
	 * Equivalent of using {@link String#equals(Object)} on
	 * {@link #getItemTypeFromMaterialData(org.bukkit.material.MaterialData)}.
	 *
	 * @param itemType     item type to check
	 * @param materialData MaterialData to check
	 * @return if item type matches item type of MaterialData
	 */
	public static boolean isItemType(String itemType, MaterialData materialData) {
		return getMaterialDatasFromItemType(itemType).contains(materialData);
	}

	/**
	 * Gets a {@link java.util.Collection} of {@link org.bukkit.material.MaterialData}s from an item type.
	 *
	 * @param itemType type of item
	 * @return All MaterialDatas associated with the item type
	 */
	public static Collection<MaterialData> getMaterialDatasFromItemType(String itemType) {
		List<MaterialData> list = new ArrayList<MaterialData>();
		List<String> ids = plugin.getConfigSettings().getItemTypesWithIds().get(itemType.toLowerCase());
		if (ids == null || ids.isEmpty()) {
			return list;
		}
		for (String id : ids) {
			if (id.contains(";")) {
				String[] split = id.split(";");
				try {
					Material material = Material.valueOf(split[0]);
					int data = NumberUtils.toInt(split[1], 0);
					MaterialData materialData = new MaterialData(material, (byte) data);
					list.add(materialData);
					continue;
				} catch (Exception e) {
					// do nothing
				}
				int mat = NumberUtils.toInt(split[0], 0);
				int data = NumberUtils.toInt(split[1], 0);
				MaterialData materialData = new MaterialData(mat, (byte) data);
				list.add(materialData);
			} else {
				try {
					Material material = Material.valueOf(id);
					MaterialData materialData = new MaterialData(material);
					list.add(materialData);
					continue;
				} catch (Exception e) {
					// do nothing
				}
				int mat = NumberUtils.toInt(id, 0);
				MaterialData materialData = new MaterialData(mat);
				list.add(materialData);
			}
		}
		return list;
	}

	/**
	 * Equivalent of using {@link String#equals(Object)} on
	 * {@link #getMaterialTypeFromMaterialData(org.bukkit.material.MaterialData)}.
	 *
	 * @param materialType item type to check
	 * @param materialData MaterialData to check
	 * @return if material type matches material type of MaterialData
	 */
	public static boolean isMaterialType(String materialType, MaterialData materialData) {
		return getMaterialDatasFromMaterialType(materialType).contains(materialData);
	}

	/**
	 * Gets a {@link Collection} of {@link MaterialData}s from a material type.
	 *
	 * @param materialType type of material
	 * @return All MaterialDatas associated with the material type
	 */
	public static Collection<MaterialData> getMaterialDatasFromMaterialType(String materialType) {
		List<MaterialData> list = new ArrayList<MaterialData>();
		List<String> ids = plugin.getConfigSettings().getMaterialTypesWithIds().get(materialType.toLowerCase());
		if (ids == null || ids.isEmpty()) {
			return list;
		}
		for (String id : ids) {
			if (id.contains(";")) {
				String[] split = id.split(";");
				try {
					Material material = Material.valueOf(split[0]);
					int data = NumberUtils.toInt(split[1], 0);
					MaterialData materialData = new MaterialData(material, (byte) data);
					list.add(materialData);
					continue;
				} catch (Exception e) {
					// do nothing
				}
				int mat = NumberUtils.toInt(split[0], 0);
				int data = NumberUtils.toInt(split[1], 0);
				MaterialData materialData = new MaterialData(mat, (byte) data);
				list.add(materialData);
			} else {
				try {
					Material material = Material.valueOf(id);
					MaterialData materialData = new MaterialData(material);
					list.add(materialData);
					continue;
				} catch (Exception e) {
					// do nothing
				}
				int mat = NumberUtils.toInt(id, 0);
				MaterialData materialData = new MaterialData(mat);
				list.add(materialData);
			}
		}
		return list;
	}

}
