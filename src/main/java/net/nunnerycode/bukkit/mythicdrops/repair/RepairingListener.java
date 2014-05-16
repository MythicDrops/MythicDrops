package net.nunnerycode.bukkit.mythicdrops.repair;

import com.comphenix.xp.rewards.xp.ExperienceManager;
import net.nunnerycode.bukkit.mythicdrops.api.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.repair.RepairCost;
import net.nunnerycode.bukkit.mythicdrops.api.repair.RepairItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class RepairingListener implements Listener {

    private MythicDrops mythicDrops;
    private Map<String, ItemStack> repairing;

    public RepairingListener(MythicDrops mythicDrops) {
        this.mythicDrops = mythicDrops;
        repairing = new HashMap<>();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockDamageEvent(BlockDamageEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getPlayer() == null) {
            return;
        }
        if (!event.getPlayer().hasPermission("mythicdrops.repair")
            || event.getBlock().getType() != Material.ANVIL) {
            return;
        }
        Player player = event.getPlayer();
        if (repairing.containsKey(player.getName())) {
            ItemStack oldInHand = repairing.get(player.getName());
            ItemStack currentInHand = player.getItemInHand();
            if (oldInHand.getType() != currentInHand.getType()) {
                player.sendMessage(mythicDrops.getConfigSettings().getFormattedLanguageString("command" +
                                                                                              ".repair-cannot-use"));
                repairing.remove(player.getName());
                return;
            }
            if (oldInHand.getDurability() == 0 || currentInHand.getDurability() == 0) {
                player.sendMessage(mythicDrops.getConfigSettings().getFormattedLanguageString("command" +
                                                                                              ".repair-cannot-use"));
                repairing.remove(player.getName());
                return;
            }
            if (!oldInHand.isSimilar(currentInHand)) {
                player.sendMessage(mythicDrops.getConfigSettings().getFormattedLanguageString("command" +
                                                                                              ".repair-cannot-use"));
                repairing.remove(player.getName());
                return;
            }
            RepairItem mythicRepairItem = getRepairItem(currentInHand);
            if (mythicRepairItem == null) {
                player.sendMessage(mythicDrops.getConfigSettings().getFormattedLanguageString("command" +
                                                                                              ".repair-cannot-use"));
                repairing.remove(player.getName());
                return;
            }
            List<RepairCost> mythicRepairCostList = mythicRepairItem.getRepairCosts();
            if (mythicRepairCostList == null) {
                player.sendMessage(mythicDrops.getConfigSettings().getFormattedLanguageString("command" +
                                                                                              ".repair-cannot-use"));
                repairing.remove(player.getName());
                return;
            }
            RepairCost mythicRepairCost =
                    getRepairCost(mythicRepairItem, mythicRepairCostList, player.getInventory());
            if (mythicRepairCost == null) {
                player.sendMessage(mythicDrops.getConfigSettings().getFormattedLanguageString("command" +
                                                                                              ".repair-cannot-use"));
                repairing.remove(player.getName());
                return;
            }
            if (!player.getInventory().containsAtLeast(mythicRepairCost.toItemStack(1),
                    mythicRepairCost.getAmount())) {
                player.sendMessage(mythicDrops.getConfigSettings().getFormattedLanguageString("command" +
                                                                                              ".repair-do-not-have",
                        new String[][]{
                                {"%material%",
                                 mythicRepairItem.toItemStack(1).getType().name()}}
                ));
                repairing.remove(player.getName());
                return;
            }
            ExperienceManager experienceManager = new ExperienceManager(player);
            if (!experienceManager.hasExp(mythicRepairCost.getExperienceCost())) {
                player.sendMessage(mythicDrops.getConfigSettings().getFormattedLanguageString("command" +
                                                                                              ".repair.do-not-have",
                        new String[][]{
                                {"%material%",
                                 "experience"}}
                ));
                repairing.remove(player.getName());
                return;
            }
            experienceManager.changeExp(-mythicRepairCost.getExperienceCost());
            player.setItemInHand(repairItemStack(currentInHand, player.getInventory()));
            player.sendMessage(
                    mythicDrops.getConfigSettings().getFormattedLanguageString("command.repair-success"));
            repairing.remove(player.getName());
            player.updateInventory();
            if (mythicDrops.getRepairingSettings().isPlaySounds()) {
                player.playSound(event.getBlock().getLocation(), Sound.ANVIL_USE, 1.0F, 1.0F);
            }
        } else {
            if (player.getItemInHand().getType() == Material.AIR) {
                return;
            }
            if (player.getItemInHand().getDurability() == 0) {
                return;
            }
            if (getRepairItem(player.getItemInHand()) == null) {
                return;
            }
            if (player.getItemInHand().hasItemMeta()) {
                ItemMeta itemMeta = player.getItemInHand().getItemMeta();
                if (itemMeta.hasLore()) {
                    List<String> lore = new ArrayList<>(itemMeta.getLore());
                    lore.add(ChatColor.BLACK + "Repairing");
                    itemMeta.setLore(lore);
                } else {
                    itemMeta.setLore(Arrays.asList(ChatColor.BLACK + "Repairing"));
                }
                player.getItemInHand().setItemMeta(itemMeta);
            } else {
                ItemMeta itemMeta = Bukkit.getItemFactory().getItemMeta(player.getItemInHand().getType());
                itemMeta.setLore(Arrays.asList(ChatColor.BLACK + "Repairing"));
                player.getInventory().getItemInHand().setItemMeta(itemMeta);
            }
            repairing.put(player.getName(), player.getItemInHand());
            player.sendMessage(mythicDrops.getConfigSettings().getFormattedLanguageString("command" +
                                                                                          ".repair-instructions"));
        }
    }

    private RepairItem getRepairItem(ItemStack itemStack) {
        String displayName = null;
        List<String> lore = new ArrayList<>();
        Material material = itemStack.getType();
        if (itemStack.hasItemMeta()) {
            if (itemStack.getItemMeta().hasDisplayName()) {
                displayName = itemStack.getItemMeta().getDisplayName();
            }
            if (itemStack.getItemMeta().hasLore()) {
                lore = itemStack.getItemMeta().getLore();
            }
        }
        for (RepairItem repItem : MythicRepairItemMap.getInstance().values()) {
            if (repItem.getMaterial() != material) {
                continue;
            }
            if (repItem.getItemName() != null && (displayName == null || !ChatColor
                    .translateAlternateColorCodes('&',
                            repItem.getName()).equals(displayName))) {
                continue;
            }
            if (repItem.getItemLore() != null && !repItem.getItemLore().isEmpty()) {
                if (lore == null) {
                    continue;
                }
                List<String> coloredLore = new ArrayList<>();
                for (String s : repItem.getItemLore()) {
                    coloredLore.add(ChatColor.translateAlternateColorCodes('&', s));
                }
                if (!coloredLore.equals(lore)) {
                    continue;
                }
            }
            return repItem;
        }
        return null;
    }

    private RepairCost getRepairCost(RepairItem mythicRepairItem,
                                     List<RepairCost> mythicRepairCostsList, Inventory inventory) {
        RepairCost repCost = null;
        for (RepairCost mythicRepairCost : mythicRepairCostsList) {
            ItemStack itemStack = mythicRepairCost.toItemStack(1);
            if (inventory.contains(itemStack)) {
                if (repCost == null) {
                    repCost = mythicRepairCost;
                    continue;
                }
                if (repCost.getPriority() > mythicRepairCost.getPriority()) {
                    repCost = mythicRepairCost;
                }
            }
        }
        return repCost;
    }

    private ItemStack repairItemStack(ItemStack itemStack, Inventory inventory) {
        if (itemStack == null) {
            return null;
        }
        ItemStack repaired = itemStack.clone();
        RepairItem mythicRepairItem = getRepairItem(itemStack);
        if (mythicRepairItem == null) {
            return repaired;
        }
        List<RepairCost> mythicRepairCostList = mythicRepairItem.getRepairCosts();
        if (mythicRepairCostList == null) {
            return repaired;
        }
        RepairCost mythicRepairCost = getRepairCost(mythicRepairItem, mythicRepairCostList, inventory);
        if (mythicRepairCost == null) {
            return repaired;
        }
        if (!inventory.containsAtLeast(mythicRepairCost.toItemStack(1), mythicRepairCost.getAmount())) {
            return repaired;
        }

        inventory.removeItem(mythicRepairCost.toItemStack(mythicRepairCost.getAmount()));

        short currentDurability = repaired.getDurability();
        short newDurability = (short) (currentDurability - repaired.getType().getMaxDurability()
                                                           * mythicRepairCost
                .getRepairPercentagePerCost());
        repaired.setDurability((short) Math.max(newDurability, 0));
        if (repaired.hasItemMeta()) {
            ItemMeta itemMeta = repaired.getItemMeta();
            if (itemMeta.hasLore()) {
                List<String> lore = itemMeta.getLore();
                lore.remove(ChatColor.BLACK + "Repairing");
                itemMeta.setLore(lore);
            }
            repaired.setItemMeta(itemMeta);
        }
        for (HumanEntity humanEntity : inventory.getViewers()) {
            if (humanEntity instanceof Player) {
                ((Player) humanEntity).updateInventory();
            }
        }
        return repaired;
    }

}
