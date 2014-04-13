package net.nunnerycode.bukkit.mythicdrops.populating;

import net.nunnerycode.bukkit.mythicdrops.api.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.populating.PopulateWorld;
import net.nunnerycode.bukkit.mythicdrops.items.MythicDropBuilder;
import net.nunnerycode.bukkit.mythicdrops.utils.RandomRangeUtil;
import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkPopulateEvent;

public final class PopulatingListener implements Listener {

    private MythicDrops mythicDrops;

    public PopulatingListener(MythicDrops mythicDrops) {
        this.mythicDrops = mythicDrops;
    }

    @EventHandler
    public void onChunkPopulateEvent(ChunkPopulateEvent event) {
        World w = event.getWorld();
        String worldName = w.getName();
        PopulateWorld pw = mythicDrops.getPopulatingSettings().getWorld(worldName);
        if (pw == null) {
            return;
        }
        if (!pw.isEnabled()) {
            return;
        }
        for (int x = 0; x < 15; x++) {
            for (int y = 0; y < w.getMaxHeight(); y++) {
                for (int z = 0; z < 15; z++) {
                    Block b = event.getChunk().getBlock(x, y, z);
                    BlockState bs = b.getState();
                    if (!(bs instanceof Chest)) {
                        continue;
                    }
                    if (RandomUtils.nextDouble() > pw.getChance()) {
                        continue;
                    }
                    if (pw.getTiers().isEmpty()) {
                        continue;
                    }
                    Chest c = (Chest) bs;
                    if (pw.isOverwriteContents()) {
                        c.getInventory().clear();
                    }
                    int numOfItems = RandomRangeUtil.randomRange(pw.getMinimumItems(), pw.getMaximumItems());
                    String tier = pw.getTiers().get(RandomUtils.nextInt(pw.getTiers().size()));
                    for (int i = 0; i < numOfItems; i++) {
                        c.getInventory().addItem(new MythicDropBuilder(mythicDrops).withTier(tier).build());
                    }
                }
            }
        }
    }

}
