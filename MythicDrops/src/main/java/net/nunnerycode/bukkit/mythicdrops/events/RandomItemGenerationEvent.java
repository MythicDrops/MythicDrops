package net.nunnerycode.bukkit.mythicdrops.events;

import net.nunnerycode.bukkit.mythicdrops.api.events.MythicDropsCancellableEvent;
import net.nunnerycode.bukkit.mythicdrops.api.items.ItemGenerationReason;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import org.bukkit.inventory.ItemStack;

public class RandomItemGenerationEvent extends MythicDropsCancellableEvent {

	private Tier tier;
	private ItemStack itemStack;
	private ItemGenerationReason reason;
	private boolean modified;

	public RandomItemGenerationEvent(Tier tier, ItemStack itemStack, ItemGenerationReason reason) {
		this.tier = tier;
		this.itemStack = itemStack;
		this.reason = reason;
		modified = false;
	}

	public Tier getTier() {
		return tier;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	public void setItemStack(ItemStack itemStack) {
		setItemStack(itemStack, true);
	}

	public void setItemStack(ItemStack itemStack, boolean modified) {
		this.itemStack = itemStack;
		if (modified) {
			this.modified = true;
		}
	}

	public ItemGenerationReason getReason() {
		return reason;
	}

	public boolean isModified() {
		return modified;
	}
}
