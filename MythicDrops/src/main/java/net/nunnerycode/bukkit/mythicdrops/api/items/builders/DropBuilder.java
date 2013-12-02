package net.nunnerycode.bukkit.mythicdrops.api.items.builders;

import net.nunnerycode.bukkit.mythicdrops.api.items.ItemGenerationReason;
import net.nunnerycode.bukkit.mythicdrops.api.items.MythicItemStack;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import org.bukkit.material.MaterialData;

public interface DropBuilder {

	DropBuilder withTier(Tier tier);

	DropBuilder withTier(String tierName);

	DropBuilder withMaterialData(MaterialData materialData);

	DropBuilder withMaterialData(String materialDataString);

	DropBuilder withItemGenerationReason(ItemGenerationReason reason);

	MythicItemStack build();

}
