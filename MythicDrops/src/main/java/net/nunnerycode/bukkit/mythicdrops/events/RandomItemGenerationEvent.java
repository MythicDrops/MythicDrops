package net.nunnerycode.bukkit.mythicdrops.events;

import net.nunnerycode.bukkit.mythicdrops.api.events.MythicDropsCancellableEvent;
import net.nunnerycode.bukkit.mythicdrops.api.items.ItemGenerationReason;
import net.nunnerycode.bukkit.mythicdrops.api.items.MythicItemStack;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;

public class RandomItemGenerationEvent extends MythicDropsCancellableEvent {

	private Tier tier;
	private MythicItemStack itemStack;
	private ItemGenerationReason reason;
	private boolean modified;

	public RandomItemGenerationEvent(Tier tier, MythicItemStack itemStack, ItemGenerationReason reason) {
		this.tier = tier;
		this.itemStack = itemStack;
		this.reason = reason;
		modified = false;
	}

	public Tier getTier() {
		return tier;
	}

	public MythicItemStack getItemStack() {
		return itemStack;
	}

	public void setItemStack(MythicItemStack itemStack) {
		this.itemStack = itemStack;
		modified = true;
	}

	public ItemGenerationReason getReason() {
		return reason;
	}

	public boolean isModified() {
		return modified;
	}
}
