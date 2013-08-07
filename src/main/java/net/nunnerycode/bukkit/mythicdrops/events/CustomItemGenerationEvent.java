package net.nunnerycode.bukkit.mythicdrops.events;

import net.nunnerycode.bukkit.mythicdrops.api.events.MythicDropsEvent;
import net.nunnerycode.bukkit.mythicdrops.api.items.ItemGenerationReason;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import net.nunnerycode.bukkit.mythicdrops.tiers.DefaultTier;
import org.bukkit.inventory.ItemStack;

public class CustomItemGenerationEvent extends MythicDropsEvent implements ItemGenerationEvent {

	private final ItemStack itemStack;
	private final ItemGenerationReason reason;

	public CustomItemGenerationEvent(ItemGenerationReason reason, ItemStack itemStack) {
		this.reason = reason;
		this.itemStack = itemStack;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	@Override
	public ItemGenerationReason getReason() {
		return reason;
	}

	@Override
	public Tier getTier() {
		return DefaultTier.CUSTOM_ITEM;
	}


}