package net.nunnerycode.bukkit.mythicdrops.api.managers;

import java.util.List;
import java.util.Set;
import net.nunnerycode.bukkit.mythicdrops.api.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import org.bukkit.material.MaterialData;

public interface ItemManager {

	MaterialData getMatDataFromItemType(String itemType);

	List<Tier> getTiersForMaterialData(MaterialData materialData);

	Set<MaterialData> getMaterialDataSetForTier(Tier tier);

	List<String> getMaterialIDsForItemType(String itemType);

	boolean isArmor(String itemType);

	boolean isTool(String itemType);

	boolean isMatDataInTier(MaterialData materialData, Tier tier);

	String itemTypeFromMatData(MaterialData matData);

	String materialTypeFromMatData(MaterialData matData);

	boolean isItemType(String itemType, MaterialData matData);

	MythicDrops getPlugin();

}
