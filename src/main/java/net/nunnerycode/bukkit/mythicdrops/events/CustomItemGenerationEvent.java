package net.nunnerycode.bukkit.mythicdrops.events;

import net.nunnerycode.bukkit.mythicdrops.api.events.MythicDropsEvent;
import net.nunnerycode.bukkit.mythicdrops.api.items.ItemGenerationReason;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import net.nunnerycode.bukkit.mythicdrops.tiers.DefaultTier;
import org.bukkit.inventory.ItemStack;

/**
 * An event called whenever a {@link net.nunnerycode.bukkit.mythicdrops.api.items.CustomItem} is generated.
 */
public class CustomItemGenerationEvent extends MythicDropsEvent implements ItemGenerationEvent {

	private final ItemStack itemStack;
	private final ItemGenerationReason reason;

	/**
	 * Instantiates a new CustomItemGenerationEvent.
	 * @param reason reason for item to be generated
	 * @param itemStack {@link ItemStack} result
	 */
	public CustomItemGenerationEvent(ItemGenerationReason reason, ItemStack itemStack) {
		this.reason = reason;
		this.itemStack = itemStack;
	}

	/**
	 * Get the resulting {@link ItemStack}.
	 * @return resulting ItemStack
	 */
	public ItemStack getItemStack() {
		return itemStack;
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