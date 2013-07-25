package net.nunnerycode.bukkit.mythicdrops.api.sockets;

import org.bukkit.entity.LivingEntity;

public interface SocketAction {

    SocketActionType getType();

    boolean apply(LivingEntity livingEntity, int duration, int intensity);

}
