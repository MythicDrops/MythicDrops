package net.nunnerycode.bukkit.mythicdrops.populating;

import net.nunnerycode.bukkit.mythicdrops.MythicDropsPlugin;
import net.nunnerycode.bukkit.mythicdrops.api.items.CustomItem;
import net.nunnerycode.bukkit.mythicdrops.api.items.ItemGenerationReason;
import net.nunnerycode.bukkit.mythicdrops.api.populating.PopulateWorld;
import net.nunnerycode.bukkit.mythicdrops.events.ChestGenerateEvent;
import net.nunnerycode.bukkit.mythicdrops.events.ChestPopulateEvent;
import net.nunnerycode.bukkit.mythicdrops.identification.IdentityTome;
import net.nunnerycode.bukkit.mythicdrops.identification.UnidentifiedItem;
import net.nunnerycode.bukkit.mythicdrops.items.CustomItemMap;
import net.nunnerycode.bukkit.mythicdrops.socketting.SocketGem;
import net.nunnerycode.bukkit.mythicdrops.socketting.SocketItem;
import net.nunnerycode.bukkit.mythicdrops.utils.RandomRangeUtil;
import net.nunnerycode.bukkit.mythicdrops.utils.SocketGemUtil;
import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

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
            final Chest chest = (Chest) bs;
            Bukkit.getScheduler().runTaskLater(mythicDrops, new Runnable() {
                @Override
                public void run() {
                    ChestGenerateEvent cge = new ChestGenerateEvent(chest);
                    Bukkit.getPluginManager().callEvent(cge);
                }
            }, 20L * 1);
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
                for (ItemStack is : cpe.getItemsToAdd()) {
                    event.getChest().getInventory().addItem(is);
                }
            }
        }, 20L * 1);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChestPopulateEvent(ChestPopulateEvent event) {
        if (event.isCancelled()) {
            return;
        }
        int num = RandomRangeUtil.randomRange(event.getPopulateWorld().getMinimumItems(),
                event.getPopulateWorld().getMaximumItems());
        List<String> tiers = event.getPopulateWorld().getTiers();
        if (tiers == null || tiers.isEmpty()) {
            return;
        }

        // Begin to check for socket gem, identity tome, and unidentified.
        double customItemChance = mythicDrops.getConfigSettings().getCustomItemChance();
        double socketGemChance = mythicDrops.getConfigSettings().getSocketGemChance();
        double unidentifiedItemChance = mythicDrops.getConfigSettings().getUnidentifiedItemChance();
        double identityTomeChance = mythicDrops.getConfigSettings().getIdentityTomeChance();
        boolean sockettingEnabled = mythicDrops.getConfigSettings().isSockettingEnabled();
        boolean identifyingEnabled = mythicDrops.getConfigSettings().isIdentifyingEnabled();

        for (int i = 0; i < num; i++) {
            ItemStack itemStack = MythicDropsPlugin.getNewDropBuilder().withItemGenerationReason(
                    ItemGenerationReason.POPULATING).useDurability(true).withTier(tiers.get(RandomUtils.nextInt
                    (tiers.size()))).build();

            if (itemStack == null) {
                continue;
            }

            if (RandomUtils.nextDouble() <= customItemChance) {
                CustomItem customItem = CustomItemMap.getInstance().getRandomWithChance();
                if (customItem != null) {
                    itemStack = customItem.toItemStack();
                }
            } else if (sockettingEnabled && RandomUtils.nextDouble() <= socketGemChance) {
                SocketGem socketGem = SocketGemUtil.getRandomSocketGemWithChance();
                Material material = SocketGemUtil.getRandomSocketGemMaterial();
                if (socketGem != null && material != null) {
                    itemStack = new SocketItem(material, socketGem);
                }
            } else if (identifyingEnabled && RandomUtils.nextDouble() <= unidentifiedItemChance) {
                Material material = itemStack.getType();
                itemStack = new UnidentifiedItem(material);
            } else if (identifyingEnabled && RandomUtils.nextDouble() <= identityTomeChance) {
                itemStack = new IdentityTome();
            }

            event.addItem(itemStack);
        }
    }

}
