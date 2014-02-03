package net.nunnerycode.bukkit.mythicdrops.events;

import net.nunnerycode.bukkit.mythicdrops.api.events.MythicDropsCancellableEvent;

import org.bukkit.entity.LivingEntity;

public class EntitySpawningEvent extends MythicDropsCancellableEvent {

  private LivingEntity livingEntity;

  public EntitySpawningEvent(LivingEntity livingEntity) {
    this.livingEntity = livingEntity;
  }

  public LivingEntity getLivingEntity() {
    return livingEntity;
  }

}
