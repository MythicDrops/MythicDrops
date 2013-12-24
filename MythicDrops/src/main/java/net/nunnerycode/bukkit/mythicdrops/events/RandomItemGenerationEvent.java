package net.nunnerycode.bukkit.mythicdrops.events;

import net.nunnerycode.bukkit.mythicdrops.api.events.MythicDropsCancellableEvent;
import net.nunnerycode.bukkit.mythicdrops.api.items.ItemGenerationReason;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import org.bukkit.inventory.ItemStack;

public class RandomItemGenerationEvent extends MythicDropsCancellableEvent {

	private Tier tier;
	private ItemStack itemStack;
	private ItemGenerationReason reason;

	public RandomItemGenerationEvent(Tier tier, ItemStack itemStack, ItemGenerationReason reason) {
		this.tier = tier;
		this.itemStack = itemStack;
		this.reason = reason;
	}

	public Tier getTier() {
		return tier;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	public void setItemStack(ItemStack itemStack) {
		this.itemStack = itemStack;
	}

	public ItemGenerationReason getReason() {
		return reason;
	}

}
