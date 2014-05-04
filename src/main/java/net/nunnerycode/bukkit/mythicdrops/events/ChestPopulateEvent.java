package net.nunnerycode.bukkit.mythicdrops.events;

import net.nunnerycode.bukkit.mythicdrops.api.events.MythicDropsCancellableEvent;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ChestPopulateEvent extends MythicDropsCancellableEvent {

    private final Chest chest;
    private final List<ItemStack> itemsToAdd;

    public ChestPopulateEvent(Chest chest, List<ItemStack> itemsToAdd) {
        this.chest = chest;
        this.itemsToAdd = itemsToAdd != null ? itemsToAdd : new ArrayList<ItemStack>();
    }

    public Chest getChest() {
        return chest;
    }

    public List<ItemStack> getItemsToAdd() {
        return new ArrayList<>(itemsToAdd);
    }

    public void addItem(ItemStack itemStack) {
        itemsToAdd.add(itemStack);
    }

    public void removeItem(ItemStack itemStack) {
        itemsToAdd.remove(itemStack);
    }

    public void clearItems() {
        itemsToAdd.clear();
    }

}
