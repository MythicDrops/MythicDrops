package com.conventnunnery.plugins.mythicdrops.listeners;

import com.conventnunnery.plugins.mythicdrops.MythicDrops;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

public class TemporaryListener implements Listener {

    private final MythicDrops plugin;

    public TemporaryListener(MythicDrops plugin) {
        this.plugin = plugin;
    }

    public MythicDrops getPlugin() {
        return plugin;
    }

    @EventHandler
    public void onCraftItemEvent(CraftItemEvent event) {
        ItemStack itemStack = event.getRecipe().getResult();
        if (itemStack == null) {
            return;
        }
        if (itemStack.getType() != Material.DIAMOND_SWORD) {
            return;
        }
    }

    @EventHandler
    public void onBlockDamageEvent(BlockDamageEvent event) {
        if (event.getPlayer() == null) {
            return;
        }
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItemInHand();
    }

}
