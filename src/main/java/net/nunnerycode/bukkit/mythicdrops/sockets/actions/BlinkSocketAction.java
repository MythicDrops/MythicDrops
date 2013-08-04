package net.nunnerycode.bukkit.mythicdrops.sockets.actions;

import net.nunnerycode.bukkit.libraries.utils.RandomRangeUtils;
import net.nunnerycode.bukkit.mythicdrops.api.sockets.SocketAction;
import net.nunnerycode.bukkit.mythicdrops.api.sockets.SocketActionType;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerTeleportEvent;

public final class BlinkSocketAction implements SocketAction {

    private BlinkSocketAction() {
    }

    @Override
    public SocketActionType getType() {
        return SocketActionType.BLINK;
    }

    @Override
    public boolean apply(final LivingEntity livingEntity, final int duration, final int intensity) {
        if (livingEntity == null) {
            return false;
        }
        Location location = getAcceptibleLocationNearby(livingEntity.getLocation(), intensity);
        if (location.equals(livingEntity.getLocation())) {
            return false;
        }
        livingEntity.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
        return true;
    }

    private Location getAcceptibleLocationNearby(Location location, int radius) {
        int x1 = location.getBlockX() - radius;
        int x2 = location.getBlockX() + radius;
        int y1 = location.getBlockY() - radius;
        int y2 = location.getBlockY() + radius;
        int z1 = location.getBlockZ() - radius;
        int z2 = location.getBlockZ() + radius;
        int xdist = x2 - x1;
        int ydist = y2 - y1;
        int zdist = z2 - z1;
        int dist = xdist + ydist + zdist;
        int x3, y3, z3;
        Location testLoc;
        Location testLocAbove;
        int attempts = 0;
        while (attempts < dist) {
            x3 = (int) RandomRangeUtils.randomRangeLongInclusive(x1, x2);
            y3 = (int) RandomRangeUtils.randomRangeLongInclusive(y1, y2);
            z3 = (int) RandomRangeUtils.randomRangeLongInclusive(z1, z2);
            testLoc = new Location(location.getWorld(), x3, y3, z3);
            testLocAbove = new Location(location.getWorld(), x3, y3 - 1, z3);
            if ((testLoc.getBlock().isEmpty() || testLoc.getBlock().isLiquid()) && (testLocAbove.getBlock().isEmpty()
                    || testLocAbove.getBlock().isLiquid())) {
                return testLoc;
            }
        }
        return location;
    }
}