package com.conventnunnery.plugins.mythicdrops.listeners;

import com.conventnunnery.plugins.mythicdrops.MythicDrops;
import com.conventnunnery.plugins.mythicdrops.objects.RepairCost;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("deprecation")
public class RepairListener implements Listener {
    private final MythicDrops plugin;
    private final Map<String, ItemStack> repairing;

    public RepairListener(MythicDrops plugin) {
        this.plugin = plugin;
        repairing = new HashMap<String, ItemStack>();
    }

    public MythicDrops getPlugin() {
        return plugin;
    }

    @EventHandler
    public void onBlockDamageEvent(BlockDamageEvent event) {
        if (event.getPlayer() == null) {
            return;
        }
        if (event.getBlock().getType() != Material.ANVIL) {
            return;
        }
        Player player = event.getPlayer();
        if (repairing.containsKey(player.getName())) {
            ItemStack oldInHand = repairing.get(player.getName());
            ItemStack currentInHand = player.getItemInHand();
            if (oldInHand.getType() != currentInHand.getType()) {
                getPlugin().getLanguageManager().sendMessage(player, "repair.cannot-use");
                repairing.remove(player.getName());
                return;
            }
            RepairCost repairCost = getPlugin().getRepairManager().getRepairCost(currentInHand.getData());
            if (repairCost == null) {
                getPlugin().getLanguageManager().sendMessage(player, "repair.cannot-use");
                repairing.remove(player.getName());
                return;
            }
            if (!player.getInventory().containsAtLeast(repairCost.getRepairItem().toItemStack(1),
                    repairCost.getAmountRequired())) {
                getPlugin().getLanguageManager().sendMessage(player, "repair.do-not-have");
                repairing.remove(player.getName());
                return;
            }
            player.setItemInHand(getPlugin().getRepairManager().repairItemStack(oldInHand, player.getInventory()));
            getPlugin().getLanguageManager().sendMessage(player, "repair.success");
            player.updateInventory();
        } else {
            if (player.getItemInHand().getType() == Material.AIR) {
                return;
            }
            repairing.put(player.getName(), player.getItemInHand());
            getPlugin().getLanguageManager().sendMessage(player, "repair.instructions");
        }
    }


}
