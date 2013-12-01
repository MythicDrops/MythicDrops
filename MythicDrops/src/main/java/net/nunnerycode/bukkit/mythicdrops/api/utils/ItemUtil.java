package net.nunnerycode.bukkit.mythicdrops.api.utils;

import java.util.Collection;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import org.bukkit.material.MaterialData;

public final class ItemUtil {

	private ItemUtil() {
		// do nothing
	}

	/**
	 * Gets a {@link java.util.Collection} of {@link org.bukkit.material.MaterialData}s from an item type.
	 * @param itemType type of item
	 * @return All MaterialDatas associated with the item type
	 */
	Collection<MaterialData> getMaterialDatasFromItemType(String itemType);

	/**
	 * Gets a {@link Collection} of {@link MaterialData}s from a material type.
	 * @param materialType type of material
	 * @return All MaterialDatas associated with the material type
	 */
	Collection<MaterialData> getMaterialDatasFromMaterialType(String materialType);

	/**
	 * Gets a {@link Collection} of {@link net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier}s that the given {@link MaterialData} can be used by.
	 * @param materialData MaterialData to check
	 * @return All Tiers that can use the given MaterialData
	 */
	Collection<Tier> getTiersFromMaterialData(MaterialData materialData);

	/**
	 * Gets a {@link Collection} of {@link MaterialData}s that the given {@link Tier} contains.
	 * @param tier Tier to check
	 * @return All MaterialDatas for the given Tier
	 */
	Collection<MaterialData> getMaterialDatasFromTier(Tier tier);

	/**
	 * Returns true if the given item type is a kind of armor.
	 * @param itemType item type to check
	 * @return if item type is a kind of armor
	 */
	boolean isArmor(String itemType);

	/**
	 * Returns true if the given item type is a kind of tool.
	 * @param itemType item type to check
	 * @return if item type is a kind of tool
	 */
	boolean isTool(String itemType);

	/**
	 * Gets the item type from the given {@link MaterialData}.
	 * @param materialData MaterialData to check
	 * @return item type
	 */
	String getItemTypeFromMaterialData(MaterialData materialData);

	/**
	 * Gets the material type from the given {@link MaterialData}.
	 * @param materialData MaterialData to check
	 * @return material type
	 */
	String getMaterialTypeFromMaterialData(MaterialData materialData);

	/**
	 * Equivalent of using {@link String#equals(Object)} on
	 * {@link #getItemTypeFromMaterialData(org.bukkit.material.MaterialData)}.
	 * @param itemType item type to check
	 * @param materialData MaterialData to check
	 * @return if item type matches item type of MaterialData
	 */
	boolean isItemType(String itemType, MaterialData materialData);

	/**
	 * Equivalent of using {@link String#equals(Object)} on
	 * {@link #getMaterialTypeFromMaterialData(org.bukkit.material.MaterialData)}.
	 * @param materialType item type to check
	 * @param materialData MaterialData to check
	 * @return if material type matches material type of MaterialData
	 */
	boolean isMaterialType(String materialType, MaterialData materialData);

}
