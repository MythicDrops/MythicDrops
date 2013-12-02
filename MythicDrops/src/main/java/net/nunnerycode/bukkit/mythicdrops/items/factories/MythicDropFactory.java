package net.nunnerycode.bukkit.mythicdrops.items.factories;

import net.nunnerycode.bukkit.mythicdrops.api.items.ItemGenerationReason;
import net.nunnerycode.bukkit.mythicdrops.api.items.MythicItemStack;
import net.nunnerycode.bukkit.mythicdrops.api.items.factories.DropFactory;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import net.nunnerycode.bukkit.mythicdrops.items.TierMap;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

public final class MythicDropFactory implements DropFactory {

	private Tier tier;
	private MaterialData materialData;
	private ItemGenerationReason itemGenerationReason;

	public MythicDropFactory() {
		tier = null;
		materialData = new MaterialData(Material.AIR);
		itemGenerationReason = ItemGenerationReason.DEFAULT;
	}

	@Override
	public DropFactory withTier(Tier tier) {
		this.tier = tier;
		return this;
	}

	@Override
	public DropFactory withTier(String tierName) {
		this.tier = TierMap.getInstance().get(tierName);
		return this;
	}

	@Override
	public DropFactory withMaterialData(MaterialData materialData) {
		this.materialData = materialData;
		return this;
	}

	@Override
	public DropFactory withMaterialData(String materialDataString) {
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
	public DropFactory withItemGenerationReason(ItemGenerationReason reason) {
		this.itemGenerationReason = reason;
		return this;
	}

	@Override
	public MythicItemStack build() {
		// TODO: actually write a build method
		return null;
	}

}
