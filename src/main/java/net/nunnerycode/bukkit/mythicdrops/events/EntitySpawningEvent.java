package net.nunnerycode.bukkit.mythicdrops.events;

import net.nunnerycode.bukkit.mythicdrops.api.events.MythicDropsCancellableEvent;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public class EntitySpawningEvent extends MythicDropsCancellableEvent {

  private LivingEntity livingEntity;
  private ItemStack[] equipment;

  public EntitySpawningEvent(LivingEntity livingEntity, ItemStack[] equipment) {
    this.livingEntity = livingEntity;
    this.equipment = equipment;
  }

  public LivingEntity getLivingEntity() {
    return livingEntity;
  }

  public ItemStack[] getEquipment() {
    return equipment;
  }

}
