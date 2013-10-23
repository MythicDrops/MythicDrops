package net.nunnerycode.bukkit.mythicdrops.events;

import net.nunnerycode.bukkit.mythicdrops.api.events.MythicDropsCancellableEvent;
import net.nunnerycode.bukkit.mythicdrops.api.items.ItemGenerationReason;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import org.bukkit.material.MaterialData;

/**
 * An event that is called before a random item is generated.
 */
public class PreRandomItemGenerationEvent extends MythicDropsCancellableEvent implements ItemGenerationEvent {

	private MaterialData materialData;
	private Tier tier;
	private ItemGenerationReason reason;

	/**
	 * Instantiates a new PreRandomItemGenerationEvent.
	 * @param reason {@link ItemGenerationReason} for the generation
	 * @param tier {@link Tier} of the item
	 * @param materialData {@link MaterialData} of the item
	 */
	public PreRandomItemGenerationEvent(ItemGenerationReason reason, Tier tier, MaterialData materialData) {
		this.reason = reason;
		this.tier = tier;
		this.materialData = materialData;
	}

	/**
	 * Sets the {@link MaterialData} involved in the event.
	 * @param materialData MaterialData to be involved in the event
	 */
	public void setMaterialData(MaterialData materialData) {
		this.materialData = materialData;
	}

	/**
	 * Gets the {@link MaterialData} involved in the event.
	 * @return MaterialData involved in the event
	 */
	public MaterialData getMaterialData() {
		return materialData;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ItemGenerationReason getReason() {
		return reason;
	}

	/**
	 * Set the {@link ItemGenerationReason} involved in the event.
	 * @param reason ItemGenerationReason to be involved in the event
	 */
	public void setReason(ItemGenerationReason reason) {
		this.reason = reason;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Tier getTier() {
		return tier;
	}

	/**
	 * Set the {@link Tier} involved in the event.
	 * @param tier Tier to be involved in the event
	 */
	public void setTier(Tier tier) {
		this.tier = tier;
	}
}