package net.nunnerycode.bukkit.mythicdrops.events;

import net.nunnerycode.bukkit.mythicdrops.api.items.ItemGenerationReason;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import org.bukkit.inventory.ItemStack;

public class PreRandomItemGenerationEvent implements ItemGenerationEvent {
    public ItemStack getItemStack() {
        return itemStack;
    }

    private final ItemStack itemStack;
    private final Tier tier;
    private final ItemGenerationReason reason;

    public PreRandomItemGenerationEvent(ItemGenerationReason reason, Tier tier, ItemStack itemStack) {
        this.reason = reason;
        this.tier = tier;
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
}