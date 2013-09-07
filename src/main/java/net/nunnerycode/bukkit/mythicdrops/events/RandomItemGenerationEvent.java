package net.nunnerycode.bukkit.mythicdrops.events;

import net.nunnerycode.bukkit.mythicdrops.api.events.MythicDropsEvent;
import net.nunnerycode.bukkit.mythicdrops.api.items.ItemGenerationReason;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import org.bukkit.inventory.ItemStack;

public class RandomItemGenerationEvent extends MythicDropsEvent implements ItemGenerationEvent {
	private ItemStack itemStack;
	private Tier tier;
	private ItemGenerationReason reason;

	public RandomItemGenerationEvent(ItemGenerationReason reason, Tier tier, ItemStack itemStack) {
		this.reason = reason;
		this.tier = tier;
		this.itemStack = itemStack;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	public void setItemStack(ItemStack itemStack) {
		this.itemStack = itemStack;
	}

	@Override
	public ItemGenerationReason getReason() {
		return reason;
	}

	@Override
	public Tier getTier() {
		return tier;
	}

	public void setTier(Tier tier) {
		this.tier = tier;
	}

	public void setReason(ItemGenerationReason reason) {
		this.reason = reason;
	}
}