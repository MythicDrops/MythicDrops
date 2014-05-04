package net.nunnerycode.bukkit.mythicdrops.events;

import net.nunnerycode.bukkit.mythicdrops.api.events.MythicDropsCancellableEvent;
import net.nunnerycode.bukkit.mythicdrops.api.populating.PopulateWorld;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ChestPopulateEvent extends MythicDropsCancellableEvent {

    private final Chest chest;
    private final List<ItemStack> itemsToAdd;
    private final PopulateWorld populateWorld;

    public ChestPopulateEvent(Chest chest, List<ItemStack> itemsToAdd, PopulateWorld populateWorld) {
        this.chest = chest;
        this.itemsToAdd = itemsToAdd != null ? itemsToAdd : new ArrayList<ItemStack>();
        this.populateWorld = populateWorld;
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

    public PopulateWorld getPopulateWorld() {
        return populateWorld;
    }

}
