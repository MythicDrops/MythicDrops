package com.conventnunnery.plugins.mythicdrops.events;

import com.conventnunnery.plugins.mythicdrops.events.parents.MythicDropsCancellableEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemIdentifiedEvent extends MythicDropsCancellableEvent {
    public Player getPlayer() {
        return player;
    }

    private final Player player;
    private final ItemStack itemStack;

    public ItemIdentifiedEvent(final Player player, ItemStack itemStack) {
        this.player = player;
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

}
