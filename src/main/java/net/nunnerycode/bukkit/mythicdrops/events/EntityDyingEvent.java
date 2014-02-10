package net.nunnerycode.bukkit.mythicdrops.events;

import net.nunnerycode.bukkit.mythicdrops.api.events.MythicDropsEvent;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class EntityDyingEvent extends MythicDropsEvent {

  private LivingEntity livingEntity;
  private ItemStack[] equipment;
  private List<ItemStack> equipmentDrops;

  public EntityDyingEvent(LivingEntity livingEntity, ItemStack[] equipment,
                          List<ItemStack> equipmentDrops) {
    this.livingEntity = livingEntity;
    this.equipment = equipment;
    this.equipmentDrops = equipmentDrops;
  }

  public LivingEntity getLivingEntity() {
    return livingEntity;
  }

  public ItemStack[] getEquipment() {
    return equipment;
  }

  public List<ItemStack> getEquipmentDrops() {
    return equipmentDrops;
  }

  public void addEquipmentDrop(ItemStack itemStack) {
    equipmentDrops.add(itemStack);
  }

}
