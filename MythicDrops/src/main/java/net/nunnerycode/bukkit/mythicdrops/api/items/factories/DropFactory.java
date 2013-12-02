package net.nunnerycode.bukkit.mythicdrops.api.items.factories;

import net.nunnerycode.bukkit.mythicdrops.api.items.ItemGenerationReason;
import net.nunnerycode.bukkit.mythicdrops.api.items.MythicItemStack;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import org.bukkit.material.MaterialData;

public interface DropFactory {

	DropFactory withTier(Tier tier);

	DropFactory withTier(String tierName);

	DropFactory withMaterialData(MaterialData materialData);

	DropFactory withMaterialData(String materialDataString);

	DropFactory withItemGenerationReason(ItemGenerationReason reason);

	MythicItemStack build();

}
