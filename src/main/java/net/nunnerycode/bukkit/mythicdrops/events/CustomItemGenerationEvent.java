package net.nunnerycode.bukkit.mythicdrops.events;

import net.nunnerycode.bukkit.mythicdrops.api.events.MythicDropsCancellableEvent;
import net.nunnerycode.bukkit.mythicdrops.api.items.CustomItem;
import net.nunnerycode.bukkit.mythicdrops.api.items.ItemGenerationReason;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;

public class CustomItemGenerationEvent extends MythicDropsCancellableEvent implements ItemGenerationEvent {

    private final CustomItem customItem;
    private final Tier tier;
    private final ItemGenerationReason reason;

    public CustomItemGenerationEvent(ItemGenerationReason reason, Tier tier, CustomItem customItem) {
        this.reason = reason;
        this.tier = tier;
        this.customItem = customItem;
    }

    public CustomItem getCustomItem() {
        return customItem;
    }

    @Override
    public ItemGenerationReason getReason() {
        return reason;
    }

    @Override
    public Tier getTier() {
        return tier;
    }


}