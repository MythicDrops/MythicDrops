package net.nunnerycode.bukkit.mythicdrops.events;

import net.nunnerycode.bukkit.mythicdrops.api.events.MythicDropsCancellableEvent;
import net.nunnerycode.bukkit.mythicdrops.api.items.CustomItem;
import net.nunnerycode.bukkit.mythicdrops.api.items.ItemGenerationReason;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import net.nunnerycode.bukkit.mythicdrops.tiers.DefaultTier;

/**
 * An event that is called before a {@link CustomItem} is generated.
 */
public class PreCustomItemGenerationEvent extends MythicDropsCancellableEvent implements ItemGenerationEvent {

    private CustomItem customItem;
    private ItemGenerationReason reason;

	/**
	 * Instantiates a new PreCustomItemGenerationEvent.
	 * @param reason {@link ItemGenerationReason} for generation
	 * @param customItem {@link CustomItem} to be generated
	 */
    public PreCustomItemGenerationEvent(ItemGenerationReason reason, CustomItem customItem) {
        this.reason = reason;
        this.customItem = customItem;
    }

	/**
	 * Gets the {@link CustomItem} involved in the event.
	 * @return CustomItem involved in the event
	 */
    public CustomItem getCustomItem() {
        return customItem;
    }

	/**
	 * Sets the {@link CustomItem} involved in the event.
	 * @param customItem CustomItem to be involved in the event.
	 */
	public void setCustomItem(CustomItem customItem) {
		this.customItem = customItem;
	}

	/**
	 * Sets the {@link ItemGenerationReason} for the generation.
	 * @param reason ItemGenerationReason for the item being generated
	 */
	public void setReason(ItemGenerationReason reason) {
		this.reason = reason;
	}

	/**
	 * {@inheritDoc}
	 */
    @Override
    public ItemGenerationReason getReason() {
        return reason;
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
    public Tier getTier() {
        return DefaultTier.CUSTOM_ITEM;
    }


}