package net.nunnerycode.bukkit.mythicdrops.events;

import net.nunnerycode.bukkit.mythicdrops.api.events.MythicDropsCancellableEvent;
import net.nunnerycode.bukkit.mythicdrops.api.items.ItemGenerationReason;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import org.bukkit.material.MaterialData;

public class PreRandomItemGenerationEvent extends MythicDropsCancellableEvent implements ItemGenerationEvent {

	private MaterialData materialData;
	private Tier tier;
	private ItemGenerationReason reason;

	public PreRandomItemGenerationEvent(ItemGenerationReason reason, Tier tier, MaterialData materialData) {
		this.reason = reason;
		this.tier = tier;
		this.materialData = materialData;
	}

	public void setMaterialData(MaterialData materialData) {
		this.materialData = materialData;
	}

	public MaterialData getMaterialData() {
		return materialData;
	}

	@Override
	public ItemGenerationReason getReason() {
		return reason;
	}

	public void setReason(ItemGenerationReason reason) {
		this.reason = reason;
	}

	@Override
	public Tier getTier() {
		return tier;
	}

	public void setTier(Tier tier) {
		this.tier = tier;
	}
}