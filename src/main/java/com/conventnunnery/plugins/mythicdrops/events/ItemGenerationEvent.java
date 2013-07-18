package com.conventnunnery.plugins.mythicdrops.events;


import com.conventnunnery.plugins.mythicdrops.api.items.ItemGenerationReason;
import com.conventnunnery.plugins.mythicdrops.api.tiers.Tier;
import org.bukkit.inventory.ItemStack;

public interface ItemGenerationEvent {

    ItemGenerationReason getReason();

    ItemStack getResultingItemStack();

    Tier getBeginningTier();

    Tier getEndingTier();

}
