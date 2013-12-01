package net.nunnerycode.bukkit.mythicdrops.api.items;

import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import org.bukkit.material.MaterialData;

public interface DropFactory {

	DropFactory withTier(Tier tier);

	DropFactory withMaterialData(MaterialData materialData);

	MythicItemStack build();

}
