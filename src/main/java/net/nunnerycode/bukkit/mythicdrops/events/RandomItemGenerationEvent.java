package net.nunnerycode.bukkit.mythicdrops.events;

import net.nunnerycode.bukkit.mythicdrops.api.events.MythicDropsEvent;
import net.nunnerycode.bukkit.mythicdrops.api.items.ItemGenerationReason;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import org.bukkit.inventory.ItemStack;

/**
 * An event that is called whenever a random item is generated.
 */
public class RandomItemGenerationEvent extends MythicDropsEvent implements ItemGenerationEvent {
	private ItemStack itemStack;
	private Tier tier;
	private ItemGenerationReason reason;

	/**
	 * Instantiates a new RandomItemGenerationEvent.
	 * @param reason {@link ItemGenerationReason} for the generation
	 * @param tier {@link Tier} of the resulting item
	 * @param itemStack {@link ItemStack} resulting ItemStack
	 */
	public RandomItemGenerationEvent(ItemGenerationReason reason, Tier tier, ItemStack itemStack) {
		this.reason = reason;
		this.tier = tier;
		this.itemStack = itemStack;
	}

	/**
	 * Gets the {@link ItemStack} involved in the event.
	 * @return ItemStack involved in the event
	 */
	public ItemStack getItemStack() {
		return itemStack;
	}

	/**
	 * Set the {@link ItemStack} that be involved in the event.
	 * @param itemStack ItemStack to be involved in the event
	 */
	public void setItemStack(ItemStack itemStack) {
		this.itemStack = itemStack;
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
		return tier;
	}

	/**
	 * Set the {@link Tier} that be involved in the event.
	 * @param tier Tier to be involved in the event
	 */
	public void setTier(Tier tier) {
		this.tier = tier;
	}

	/**
	 * Set the {@link ItemGenerationReason} that be involved in the event.
	 * @param reason ItemStack to be involved in the event
	 */
	public void setReason(ItemGenerationReason reason) {
		this.reason = reason;
	}
}