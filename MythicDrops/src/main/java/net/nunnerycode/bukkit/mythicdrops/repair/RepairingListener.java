package net.nunnerycode.bukkit.mythicdrops.repair;

import com.comphenix.xp.rewards.xp.ExperienceManager;
import net.nunnerycode.bukkit.mythicdrops.api.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.repair.RepairCost;
import net.nunnerycode.bukkit.mythicdrops.api.repair.RepairItem;
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
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
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
		if (event.getBlock().getType() != Material.ANVIL) {
			return;
		}
		Player player = event.getPlayer();
		if (repairing.containsKey(player.getName())) {
			ItemStack oldInHand = repairing.get(player.getName());
			ItemStack currentInHand = player.getItemInHand();
			if (oldInHand.getType() != currentInHand.getType()) {
				player.sendMessage(mythicDrops.getConfigSettings().getFormattedLanguageString("messages.cannot-use"));
				repairing.remove(player.getName());
				return;
			}
			if (oldInHand.getDurability() == 0 || currentInHand.getDurability() == 0) {
				player.sendMessage(mythicDrops.getConfigSettings().getFormattedLanguageString("messages.cannot-use"));
				repairing.remove(player.getName());
				return;
			}
			RepairItem mythicRepairItem = getRepairItem(currentInHand);
			if (mythicRepairItem == null) {
				player.sendMessage(mythicDrops.getConfigSettings().getFormattedLanguageString("messages.cannot-use"));
				repairing.remove(player.getName());
				return;
			}
			List<RepairCost> mythicRepairCostList = mythicRepairItem.getRepairCosts();
			if (mythicRepairCostList == null) {
				player.sendMessage(mythicDrops.getConfigSettings().getFormattedLanguageString("messages.cannot-use"));
				repairing.remove(player.getName());
				return;
			}
			RepairCost mythicRepairCost = getRepairCost(mythicRepairItem, mythicRepairCostList, player.getInventory());
			if (mythicRepairCost == null) {
				player.sendMessage(mythicDrops.getConfigSettings().getFormattedLanguageString("messages.cannot-use"));
				repairing.remove(player.getName());
				return;
			}
			if (!player.getInventory().containsAtLeast(mythicRepairCost.toItemStack(1),
					mythicRepairCost.getAmount())) {
				player.sendMessage(mythicDrops.getConfigSettings().getFormattedLanguageString("messages.do-not-have", new String[][]{{"%material%",
						mythicRepairItem.toItemStack(1).getType().name()}}));
				repairing.remove(player.getName());
				return;
			}
			ExperienceManager experienceManager = new ExperienceManager(player);
			if (!experienceManager.hasExp(mythicRepairCost.getExperienceCost())) {
				player.sendMessage(mythicDrops.getConfigSettings().getFormattedLanguageString("messages.do-not-have", new String[][]{{"%material%",
						"experience"}}));
				repairing.remove(player.getName());
				return;
			}
			experienceManager.changeExp(-mythicRepairCost.getExperienceCost());
			player.setItemInHand(repairItemStack(oldInHand, player.getInventory()));
			player.sendMessage(mythicDrops.getConfigSettings().getFormattedLanguageString("messages.success"));
			repairing.remove(player.getName());
			player.updateInventory();
			if (mythicDrops.getRepairingSettings().isPlaySounds()) {
				player.playSound(event.getBlock().getLocation(), Sound.ANVIL_USE, 1.0F, 1.0F);
			}
		} else {
			if (player.getItemInHand().getType() == Material.AIR) {
				return;
			}
			repairing.put(player.getName(), player.getItemInHand());
			player.sendMessage(mythicDrops.getConfigSettings().getFormattedLanguageString("messages.instructions"));
		}
	}

	private RepairItem getRepairItem(ItemStack itemStack) {
		String displayName = null;
		List<String> lore = new ArrayList<>();
		MaterialData materialData = itemStack.getData();
		MaterialData baseData = new MaterialData(itemStack.getType().getId(), (byte) 0);
		if (itemStack.hasItemMeta()) {
			if (itemStack.getItemMeta().hasDisplayName()) {
				displayName = itemStack.getItemMeta().getDisplayName();
			}
			if (itemStack.getItemMeta().hasLore()) {
				lore = itemStack.getItemMeta().getLore();
			}
		}
		for (RepairItem repItem : mythicDrops.getRepairingSettings().getRepairItemMap().values()) {
			if (!repItem.getMaterialData().equals(materialData) && !repItem.getMaterialData().equals(baseData)) {
				continue;
			}
			if (repItem.getItemName() != null && (displayName == null || !ChatColor.translateAlternateColorCodes('&',
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

	private RepairCost getRepairCost(RepairItem mythicRepairItem, List<RepairCost> mythicRepairCostsList, Inventory inventory) {
		RepairCost repCost = null;
		for (RepairCost mythicRepairCost : mythicRepairCostsList) {
			ItemStack itemStack = mythicRepairCost.toItemStack(1);
			if (inventory.containsAtLeast(itemStack, mythicRepairCost.getAmount())) {
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
				* mythicRepairCost.getRepairPercentagePerCost());
		repaired.setDurability((short) Math.max(newDurability, 0));
		for (HumanEntity humanEntity : inventory.getViewers()) {
			if (humanEntity instanceof Player) {
				((Player) humanEntity).updateInventory();
			}
		}
		return repaired;
	}

}
