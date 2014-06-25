package net.nunnerycode.bukkit.mythicdrops.events;

import net.nunnerycode.bukkit.mythicdrops.api.events.MythicDropsCancellableEvent;
import org.bukkit.entity.LivingEntity;

public class EntityNameEvent extends MythicDropsCancellableEvent {

    private final LivingEntity livingEntity;
    private String name;

    public EntityNameEvent(LivingEntity livingEntity, String name) {
        this.livingEntity = livingEntity;
        this.name = name;
    }

    public LivingEntity getLivingEntity() {
        return livingEntity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
