/*
 * Copyright (c) 2013. ToppleTheNun
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.conventnunnery.plugins.mythicdrops.managers;

import com.conventnunnery.plugins.mythicdrops.MythicDrops;
import com.conventnunnery.plugins.mythicdrops.objects.Tier;
import com.conventnunnery.plugins.mythicdrops.utils.NumberUtils;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * The type ItemManager.
 */
public class ItemManager {
	private final MythicDrops plugin;

	/**
	 * Instantiates a new ItemManager.
	 *
	 * @param plugin the plugin
	 */
	public ItemManager(MythicDrops plugin) {
		this.plugin = plugin;
	}

	/**
	 * Checks if a collection contains a string in any case.
	 *
	 * @param collection the collection
	 * @param string     the string
	 * @return if it contains it
	 */
	public boolean containsIgnoreCase(Collection<String> collection,
	                                  String string) {
		for (String s : collection) {
			if (s.equalsIgnoreCase(string)) {
                return true;
            }
		}
		return false;
	}

	/**
	 * Gets MaterialData from item type.
	 *
	 * @param itemType the item type
	 * @return the mat data from item type
	 */
	public MaterialData getMatDataFromItemType(String itemType) {
		Map<String, List<String>> ids = getPlugin().getPluginSettings()
				.getIDs();
		if (!ids.containsKey(itemType.toLowerCase())) {
			return null;
		}
		List<String> idList = ids.get(itemType.toLowerCase());
		if (idList == null || idList.isEmpty()) {
            return null;
        }
		String s = idList.get(getPlugin().getRandom().nextInt(idList.size()));
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

	public boolean isArmor(String itemType) {
		return containsIgnoreCase(getPlugin().getPluginSettings().getArmorIDTypes(), itemType);
	}

	public boolean isTool(String itemType) {
		return containsIgnoreCase(getPlugin().getPluginSettings().getToolIDTypes(), itemType);
	}

	/**
	 * Gets MaterialData from tier.
	 *
	 * @param tier the tier
	 * @return the mat data from tier
	 */
	public MaterialData getMatDataFromTier(Tier tier) {
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
		if (idList.isEmpty()) {
			return new MaterialData(0, (byte) 0);
		}
		String s = idList.get(getPlugin().getRandom().nextInt(idList.size()));
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

	/**
	 * Gets material IDs for item type.
	 *
	 * @param itemType the item type
	 * @return the material IDs for item type
	 */
	public List<String> getMaterialIDsForItemType(String itemType) {
		List<String> materialIds = new ArrayList<String>();
		Map<String, List<String>> map = getPlugin().getPluginSettings()
				.getIDs();
		if (map == null || map.isEmpty()) {
            return materialIds;
        }
		if (itemType == null) {
            return materialIds;
        }
		if (map.containsKey(itemType.toLowerCase())) {
			materialIds = getPlugin().getPluginSettings().getIDs()
					.get(itemType.toLowerCase());
		}
		return materialIds;
	}

	/**
	 * Gets plugin.
	 *
	 * @return the plugin
	 */
	public MythicDrops getPlugin() {
		return plugin;
	}

	/**
	 * Is MaterialData part of an item type?
	 *
	 * @param itemType the item type
	 * @param matData  the mat data
	 * @return if it does
	 */
	public boolean isItemType(String itemType, MaterialData matData) {
		String comb =
				String.format("%s;%s", String.valueOf(matData.getItemTypeId()), String.valueOf(matData.getData()));
		String comb2;
		if (matData.getData() == (byte) 0) {
			comb2 = String.valueOf(matData.getItemTypeId());
		} else {
			comb2 = comb;
		}
		Map<String, List<String>> ids = getPlugin().getPluginSettings()
				.getIDs();
		List<String> list = new ArrayList<String>();
		if (ids.keySet().contains(itemType.toLowerCase())) {
			list = ids.get(itemType.toLowerCase());
		}
		return containsIgnoreCase(list, comb) || containsIgnoreCase(list, comb2);
	}

	/**
	 * Item type from MaterialData.
	 *
	 * @param matData the mat data
	 * @return the string
	 */
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
		Map<String, List<String>> ids = getPlugin().getPluginSettings()
				.getIDs();
		for (Entry<String, List<String>> e : ids.entrySet()) {
			if (containsIgnoreCase(e.getValue(), comb)
					|| containsIgnoreCase(e.getValue(), comb2) || containsIgnoreCase(e.getValue(), comb3)) {
				return e.getKey();
			}
		}
		return null;
	}
}
