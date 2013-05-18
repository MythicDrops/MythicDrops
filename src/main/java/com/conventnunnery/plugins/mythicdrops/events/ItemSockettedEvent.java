package com.conventnunnery.plugins.mythicdrops.events;

import com.conventnunnery.plugins.mythicdrops.events.parents.MythicDropsCancellableEvent;
import com.conventnunnery.plugins.mythicdrops.objects.SocketGem;
import org.bukkit.inventory.ItemStack;

public class ItemSockettedEvent extends MythicDropsCancellableEvent {
    private final ItemStack itemStack;
    private final SocketGem socketGem;

    public ItemSockettedEvent(ItemStack itemStack, SocketGem socketGem) {
        this.itemStack = itemStack;
        this.socketGem = socketGem;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public SocketGem getSocketGem() {
        return socketGem;
    }

}
