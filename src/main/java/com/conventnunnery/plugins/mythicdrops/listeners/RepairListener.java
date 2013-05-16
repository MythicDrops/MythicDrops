package com.conventnunnery.plugins.mythicdrops.listeners;

import com.conventnunnery.plugins.mythicdrops.MythicDrops;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;

public class RepairListener implements Listener {
    private final MythicDrops plugin;

    public RepairListener(MythicDrops plugin) {
        this.plugin = plugin;
    }

    public MythicDrops getPlugin() {
        return plugin;
    }

    @EventHandler
    public void onBlockDamageEvent(BlockDamageEvent event) {
        if (event.getPlayer() == null) {
            return;
        }
        Bukkit.getLogger().info(event.getBlock().getLocation().toString());
    }

}
