package net.nunnerycode.bukkit.mythicdrops.items.factories;

import net.nunnerycode.bukkit.mythicdrops.api.items.ItemGenerationReason;
import net.nunnerycode.bukkit.mythicdrops.api.items.MythicItemStack;
import net.nunnerycode.bukkit.mythicdrops.api.items.factories.DropFactory;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
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
	public DropFactory withMaterialData(MaterialData materialData) {
		this.materialData = materialData;
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
