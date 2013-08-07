package net.nunnerycode.bukkit.mythicdrops.events;

import net.nunnerycode.bukkit.mythicdrops.api.events.MythicDropsCancellableEvent;
import net.nunnerycode.bukkit.mythicdrops.api.items.CustomItem;
import net.nunnerycode.bukkit.mythicdrops.api.items.ItemGenerationReason;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import net.nunnerycode.bukkit.mythicdrops.tiers.DefaultTier;

public class PreCustomItemGenerationEvent extends MythicDropsCancellableEvent implements ItemGenerationEvent {

    private CustomItem customItem;
    private ItemGenerationReason reason;

    public PreCustomItemGenerationEvent(ItemGenerationReason reason, CustomItem customItem) {
        this.reason = reason;
        this.customItem = customItem;
    }

    public CustomItem getCustomItem() {
        return customItem;
    }

	public void setCustomItem(CustomItem customItem) {
		this.customItem = customItem;
	}

	public void setReason(ItemGenerationReason reason) {
		this.reason = reason;
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