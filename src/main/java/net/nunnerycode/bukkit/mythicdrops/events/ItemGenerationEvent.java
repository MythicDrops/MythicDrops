package net.nunnerycode.bukkit.mythicdrops.events;


import net.nunnerycode.bukkit.mythicdrops.api.items.ItemGenerationReason;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;

public interface ItemGenerationEvent {

	/**
	 * Gets the {@link ItemGenerationReason} for this event.
	 * @return reason for this event
	 */
    ItemGenerationReason getReason();

	/**
	 * Gets the {@link Tier} involved in this event.
	 * @return Tier involved in this event.
	 */
    Tier getTier();

}