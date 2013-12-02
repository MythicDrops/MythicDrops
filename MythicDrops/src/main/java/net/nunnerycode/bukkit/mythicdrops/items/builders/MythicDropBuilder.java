package net.nunnerycode.bukkit.mythicdrops.items.builders;

import net.nunnerycode.bukkit.mythicdrops.api.items.ItemGenerationReason;
import net.nunnerycode.bukkit.mythicdrops.api.items.MythicItemStack;
import net.nunnerycode.bukkit.mythicdrops.api.items.builders.DropBuilder;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import net.nunnerycode.bukkit.mythicdrops.items.TierMap;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

public final class MythicDropBuilder implements DropBuilder {

	private Tier tier;
	private MaterialData materialData;
	private ItemGenerationReason itemGenerationReason;

	public MythicDropBuilder() {
		tier = null;
		materialData = new MaterialData(Material.AIR);
		itemGenerationReason = ItemGenerationReason.DEFAULT;
	}

	@Override
	public DropBuilder withTier(Tier tier) {
		this.tier = tier;
		return this;
	}

	@Override
	public DropBuilder withTier(String tierName) {
		this.tier = TierMap.getInstance().get(tierName);
		return this;
	}

	@Override
	public DropBuilder withMaterialData(MaterialData materialData) {
		this.materialData = materialData;
		return this;
	}

	@Override
	public DropBuilder withMaterialData(String materialDataString) {
		MaterialData matData = null;
		if (materialDataString.contains(";")) {
		 	String[] split = materialDataString.split(";");
			matData = new MaterialData(NumberUtils.toInt(split[0], 0), (byte) NumberUtils.toInt(split[1], 0));
		} else {
			matData = new MaterialData(NumberUtils.toInt(materialDataString, 0));
		}
		this.materialData = matData;
		return this;
	}

	@Override
	public DropBuilder withItemGenerationReason(ItemGenerationReason reason) {
		this.itemGenerationReason = reason;
		return this;
	}

	@Override
	public MythicItemStack build() {
		// TODO: actually write a build method
		return null;
	}

}
