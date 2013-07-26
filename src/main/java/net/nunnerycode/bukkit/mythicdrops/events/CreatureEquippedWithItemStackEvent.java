package net.nunnerycode.bukkit.mythicdrops.events;

import net.nunnerycode.bukkit.mythicdrops.api.events.MythicDropsCancellableEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public class CreatureEquippedWithItemStackEvent extends MythicDropsCancellableEvent {
    private final LivingEntity entity;
    private ItemStack itemStack;

    public CreatureEquippedWithItemStackEvent(LivingEntity entity, ItemStack itemStack) {
        this.entity = entity;
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public LivingEntity getEntity() {
        return entity;
    }
}
