package net.nunnerycode.bukkit.mythicdrops.events;

import net.nunnerycode.bukkit.mythicdrops.api.events.MythicDropsCancellableEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

/**
 * An event called whenever a {@link LivingEntity} spawns with a MythicDrops item.
 */
public class CreatureEquippedWithItemStackEvent extends MythicDropsCancellableEvent {
    private final LivingEntity entity;
    private ItemStack itemStack;

	/**
	 * Instantiates a new CreatureEquippedWithItemStackEvent.
	 * @param entity LivingEntity spawning with item
	 * @param itemStack ItemStack being given to a LivingEntity
	 */
    public CreatureEquippedWithItemStackEvent(LivingEntity entity, ItemStack itemStack) {
        this.entity = entity;
        this.itemStack = itemStack;
    }

	/**
	 * Gets the {@link ItemStack} involved in this event.
	 * @return ItemStack involved in the event
	 */
    public ItemStack getItemStack() {
        return itemStack;
    }

	/**
	 * Sets the {@link ItemStack} involved in the event.
	 * @param itemStack new ItemStack to be equipped
	 */
    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

	/**
	 * Gets the {@link LivingEntity} involved in the event.
	 * @return LivingEntity involved in the event.
	 */
    public LivingEntity getEntity() {
        return entity;
    }
}