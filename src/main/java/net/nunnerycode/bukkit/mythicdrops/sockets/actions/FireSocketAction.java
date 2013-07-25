package net.nunnerycode.bukkit.mythicdrops.sockets.actions;

import net.nunnerycode.bukkit.mythicdrops.api.sockets.SocketAction;
import net.nunnerycode.bukkit.mythicdrops.api.sockets.SocketActionType;
import org.bukkit.entity.LivingEntity;

public final class FireSocketAction implements SocketAction {
    private FireSocketAction() {
    }

    @Override
    public SocketActionType getType() {
        return SocketActionType.FIRE;
    }

    @Override
    public boolean apply(final LivingEntity livingEntity, final int duration, final int intensity) {
        if (livingEntity == null) {
            return false;
        }
        livingEntity.setFireTicks(Math.min(livingEntity.getMaxFireTicks(), Math.max(livingEntity.getFireTicks() +
                duration, 0)));
        return true;
    }
}
