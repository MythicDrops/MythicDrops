package com.conventnunnery.plugins.mythicdrops.events;

import com.conventnunnery.plugins.mythicdrops.events.parents.MythicDropsCancellableEvent;
import com.conventnunnery.plugins.mythicdrops.objects.SocketGem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemSockettedEvent extends MythicDropsCancellableEvent {
    private final Player player;
    private final ItemStack itemStack;
    private final SocketGem socketGem;

    public ItemSockettedEvent(final Player player, ItemStack itemStack, SocketGem socketGem) {
        this.player = player;
        this.itemStack = itemStack;
        this.socketGem = socketGem;
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public SocketGem getSocketGem() {
        return socketGem;
    }

}
