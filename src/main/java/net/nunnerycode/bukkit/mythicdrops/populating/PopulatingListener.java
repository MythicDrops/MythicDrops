package net.nunnerycode.bukkit.mythicdrops.populating;

import net.nunnerycode.bukkit.mythicdrops.MythicDropsPlugin;
import net.nunnerycode.bukkit.mythicdrops.api.populating.PopulateWorld;
import net.nunnerycode.bukkit.mythicdrops.events.ChestGenerateEvent;
import net.nunnerycode.bukkit.mythicdrops.events.ChestPopulateEvent;
import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public final class PopulatingListener implements Listener {

    private MythicDropsPlugin mythicDrops;

    public PopulatingListener(MythicDropsPlugin mythicDrops) {
        this.mythicDrops = mythicDrops;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChunkPopulateEvent(ChunkPopulateEvent event) {
        Chunk c = event.getChunk();
        for (BlockState bs : c.getTileEntities()) {
            if (!(bs instanceof Chest)) {
                continue;
            }
            Chest chest = (Chest) bs;
            ChestGenerateEvent cge = new ChestGenerateEvent(chest);
            Bukkit.getPluginManager().callEvent(cge);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChestGenerateEvent(final ChestGenerateEvent event) {
        World w = event.getChest().getWorld();
        String wName = w.getName();
        final PopulateWorld pw = mythicDrops.getPopulatingSettings().getWorld(wName);
        if (pw == null || !pw.isEnabled() || RandomUtils.nextDouble() > pw.getChance()) {
            return;
        }
        final ChestPopulateEvent cpe = new ChestPopulateEvent(event.getChest(), new ArrayList<ItemStack>(), pw);
        Bukkit.getPluginManager().callEvent(cpe);
        if (cpe.isCancelled()) {
            return;
        }
        Bukkit.getScheduler().runTaskLater(mythicDrops, new Runnable() {
            @Override
            public void run() {
                if (pw.isOverwriteContents()) {
                    event.getChest().getInventory().clear();
                }
                ItemStack[] array = cpe.getItemsToAdd().toArray(new ItemStack[cpe.getItemsToAdd().size()]);
                event.getChest().getInventory().addItem(array);
            }
        }, 20L * 1);
    }

}
