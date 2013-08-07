package net.nunnerycode.bukkit.mythicdrops.events;


import net.nunnerycode.bukkit.mythicdrops.api.items.ItemGenerationReason;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;

public interface ItemGenerationEvent {

    ItemGenerationReason getReason();

    Tier getTier();

}