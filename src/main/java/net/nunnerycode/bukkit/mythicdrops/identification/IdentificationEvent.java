package net.nunnerycode.bukkit.mythicdrops.identification;

import net.nunnerycode.bukkit.mythicdrops.api.events.MythicDropsCancellableEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class IdentificationEvent extends MythicDropsCancellableEvent {

    private final Player identifier;
    private ItemStack result;

    public IdentificationEvent(ItemStack result, Player identifier) {
        this.result = result;
        this.identifier = identifier;
    }

    public ItemStack getResult() {
        return result;
    }

    public void setResult(ItemStack result) {
        this.result = result;
    }

    public Player getIdentifier() {
        return identifier;
    }

}
