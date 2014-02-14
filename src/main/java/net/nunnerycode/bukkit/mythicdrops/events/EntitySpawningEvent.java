package net.nunnerycode.bukkit.mythicdrops.events;

import net.nunnerycode.bukkit.mythicdrops.api.events.MythicDropsEvent;

import org.bukkit.entity.LivingEntity;

public class EntitySpawningEvent extends MythicDropsEvent {

  private LivingEntity livingEntity;

  public EntitySpawningEvent(LivingEntity livingEntity) {
    this.livingEntity = livingEntity;
  }

  public LivingEntity getLivingEntity() {
    return livingEntity;
  }

}
