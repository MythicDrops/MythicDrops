package net.nunnerycode.bukkit.mythicdrops.populating;

import net.nunnerycode.bukkit.mythicdrops.api.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.items.MythicDropBuilder;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class PopulateTask extends BukkitRunnable {
    private MythicDrops mythicDrops;
    private Map<Location, Long> lastRun;

    public PopulateTask(MythicDrops mythicDrops) {
        this.mythicDrops = mythicDrops;
        lastRun = new HashMap<>();
    }

    @Override
    public void run() {
        for (World w : Bukkit.getWorlds()) {
            for (Chunk chunk : w.getLoadedChunks()) {
                for (int x = 0; x < 15; x++) {
                    for (int y = 0; y < w.getMaxHeight(); y++) {
                        for (int z = 0; z < 15; z++) {
                            Block b = chunk.getBlock(x, y, z);
                            BlockState bs = b.getState();
                            if (!(bs instanceof Chest)) {
                                continue;
                            }
                            Chest c = (Chest) bs;
                            Block down = b.getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN);
                            BlockState blockState = down.getState();
                            if (!(blockState instanceof Sign)) {
                                continue;
                            }
                            Sign s = (Sign) blockState;
                            if (!s.getLine(0).equals("[MythicDrops]")) {
                                continue;
                            }
                            long time = NumberUtils.toLong(s.getLine(1));
                            long cur = System.currentTimeMillis();
                            long last = lastRun.containsKey(s.getLocation()) ? lastRun.get(s.getLocation()) : 0;
                            if (cur - last < time) {
                                continue;
                            }
                            c.getInventory().clear();
                            String tier = s.getLine(2);
                            int amount = NumberUtils.toInt(s.getLine(3));
                            for (int i = 0; i < amount; i++) {
                                c.getInventory().addItem(new MythicDropBuilder(mythicDrops).withTier(tier).build());
                            }
                            lastRun.put(s.getLocation(), cur);
                        }
                    }
                }
            }
        }
    }

}
