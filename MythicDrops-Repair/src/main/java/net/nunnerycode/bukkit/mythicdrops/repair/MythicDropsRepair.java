package net.nunnerycode.bukkit.mythicdrops.repair;

import com.comphenix.xp.rewards.xp.ExperienceManager;
import com.conventnunnery.libraries.config.CommentedConventYamlConfiguration;
import com.conventnunnery.libraries.config.ConventYamlConfiguration;
import net.nunnerycode.java.libraries.cannonball.DebugPrinter;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class MythicDropsRepair extends JavaPlugin {

	private DebugPrinter debugPrinter;
	private Map<String, RepairItem> repairItemMap;
	private Map<String, String> language;
	private ConventYamlConfiguration configYAML;
	private boolean playSounds;

	@Override
	public void onDisable() {
		debugPrinter.debug(Level.INFO, "v" + getDescription().getVersion() + " disabled");
	}

	@Override
	public void onEnable() {
		debugPrinter = new DebugPrinter(getDataFolder().getPath(), "debug.log");

		repairItemMap = new HashMap<>();
		language = new HashMap<>();

		unpackConfigurationFiles(new String[]{"config.yml"}, false);

		configYAML = new ConventYamlConfiguration(new File(getDataFolder(), "config.yml"),
				YamlConfiguration.loadConfiguration(getResource("config.yml")).getString("version"));
		configYAML.options().updateOnLoad(true);
		configYAML.options().backupOnUpdate(true);
		configYAML.load();

		ConfigurationSection messages = configYAML.getConfigurationSection("messages");
		if (messages != null) {
			for (String key : messages.getKeys(true)) {
				if (messages.isConfigurationSection(key)) {
					continue;
				}
				language.put("messages." + key, messages.getString(key, key));
			}
		}

		playSounds = configYAML.getBoolean("play-sounds", true);

		if (!configYAML.isConfigurationSection("repair-costs")) {
			defaultRepairCosts();
		}
		ConfigurationSection costs = configYAML.getConfigurationSection("repair-costs");
		for (String key : costs.getKeys(false)) {
			if (!costs.isConfigurationSection(key)) {
				continue;
			}
			ConfigurationSection cs = costs.getConfigurationSection(key);
			MaterialData matData = parseMaterialData(cs);
			String itemName = cs.getString("item-name");
			List<String> itemLore = cs.getStringList("item-lore");
			List<RepairCost> costList = new ArrayList<>();
			ConfigurationSection costsSection = cs.getConfigurationSection("costs");
			for (String costKey : costsSection.getKeys(false)) {
				if (!costsSection.isConfigurationSection(costKey)) {
					continue;
				}
				ConfigurationSection costSection = costsSection.getConfigurationSection(costKey);
				MaterialData itemCost = parseMaterialData(costSection);
				int experienceCost = costSection.getInt("experience-cost", 0);
				int priority = costSection.getInt("priority", 0);
				int amount = costSection.getInt("amount", 1);
				double repairPerCost = costSection.getDouble("repair-per-cost", 0.1);
				String costName = costSection.getString("item-name");
				List<String> costLore = costSection.getStringList("item-lore");

				RepairCost rc = new RepairCost(costKey, priority, experienceCost, repairPerCost, amount, itemCost,
						costName, costLore);
				costList.add(rc);
			}

			RepairItem ri = new RepairItem(key, matData, itemName, itemLore);
			ri.addRepairCosts(costList.toArray(new RepairCost[costList.size()]));

			repairItemMap.put(ri.getName(), ri);
		}

		debugPrinter.debug(Level.INFO, "Loaded repair items: " + repairItemMap.keySet().size());

		Bukkit.getPluginManager().registerEvents(new RepairListener(this), this);

		debugPrinter.debug(Level.INFO, "v" + getDescription().getVersion() + " enabled");
	}

	private void unpackConfigurationFiles(String[] configurationFiles, boolean overwrite) {
		for (String s : configurationFiles) {
			YamlConfiguration yc = CommentedConventYamlConfiguration.loadConfiguration(getResource(s));
			try {
				File f = new File(getDataFolder(), s);
				if (!f.exists()) {
					yc.save(f);
					continue;
				}
				if (overwrite) {
					yc.save(f);
				}
			} catch (IOException e) {
				getLogger().warning("Could not unpack " + s);
			}
		}
	}

	private MaterialData parseMaterialData(ConfigurationSection cs) {
		String materialDat = cs.getString("material-data", "");
		String materialName = cs.getString("material-name", "");
		if (materialDat.isEmpty()) {
			return new MaterialData(Material.getMaterial(materialName));
		}
		int id = 0;
		byte data = 0;
		String[] split = materialDat.split(";");
		switch (split.length) {
			case 0:
				break;
			case 1:
				id = NumberUtils.toInt(split[0], 0);
				break;
			default:
				id = NumberUtils.toInt(split[0], 0);
				data = NumberUtils.toByte(split[1], (byte) 0);
				break;
		}
		return new MaterialData(id, data);
	}

	private void defaultRepairCosts() {
		Material[] wood = {Material.WOOD_AXE, Material.WOOD_HOE, Material.WOOD_PICKAXE, Material.WOOD_SPADE,
				Material.WOOD_SWORD, Material.BOW, Material.FISHING_ROD};
		Material[] stone = {Material.STONE_AXE, Material.STONE_PICKAXE, Material.STONE_HOE, Material.STONE_SWORD,
				Material.STONE_SPADE};
		Material[] leather = {Material.LEATHER_BOOTS, Material.LEATHER_CHESTPLATE, Material.LEATHER_HELMET, 
				Material.LEATHER_LEGGINGS};
		Material[] chain = {Material.CHAINMAIL_BOOTS, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_HELMET,
				Material.CHAINMAIL_LEGGINGS};
		Material[] iron = {Material.IRON_AXE, Material.IRON_BOOTS, Material.IRON_CHESTPLATE, Material.IRON_HELMET,
				Material.IRON_LEGGINGS, Material.IRON_PICKAXE, Material.IRON_HOE, Material.IRON_SPADE,
				Material.IRON_SWORD};
		Material[] diamond = {Material.DIAMOND_AXE, Material.DIAMOND_BOOTS, Material.DIAMOND_CHESTPLATE,
				Material.DIAMOND_HELMET, Material.DIAMOND_HOE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_PICKAXE,
				Material.DIAMOND_SPADE, Material.DIAMOND_SWORD};
		Material[] gold = {Material.GOLD_AXE, Material.GOLD_BOOTS, Material.GOLD_CHESTPLATE, Material.GOLD_HELMET,
				Material.GOLD_LEGGINGS, Material.GOLD_PICKAXE, Material.GOLD_HOE, Material.GOLD_SPADE,
				Material.GOLD_SWORD};
		for (Material m : wood) {
			configYAML.set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".material-name", m.name());
			configYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
					"-") + ".costs.default.material-name", Material.WOOD.name());
			configYAML.set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".costs.default.priority", 0);
			configYAML.set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".costs.default.amount", 1);
			configYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
					"-") + ".costs.default.experience-cost", 0);
			configYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
					"-") + ".costs.default.repair-per-cost", 0.1);
		}
		for (Material m : stone) {
			configYAML.set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".material-name", m.name());
			configYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
					"-") + ".costs.default.material-name", Material.STONE.name());
			configYAML.set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".costs.default.priority", 0);
			configYAML.set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".costs.default.amount", 1);
			configYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
					"-") + ".costs.default.experience-cost", 0);
			configYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
					"-") + ".costs.default.repair-per-cost", 0.1);
		}
		for (Material m : gold) {
			configYAML.set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".material-name", m.name());
			configYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
					"-") + ".costs.default.material-name", Material.GOLD_INGOT.name());
			configYAML.set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".costs.default.priority", 0);
			configYAML.set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".costs.default.amount", 1);
			configYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
					"-") + ".costs.default.experience-cost", 0);
			configYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
					"-") + ".costs.default.repair-per-cost", 0.1);
		}
		for (Material m : iron) {
			configYAML.set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".material-name", m.name());
			configYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
					"-") + ".costs.default.material-name", Material.IRON_INGOT.name());
			configYAML.set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".costs.default.priority", 0);
			configYAML.set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".costs.default.amount", 1);
			configYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
					"-") + ".costs.default.experience-cost", 0);
			configYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
					"-") + ".costs.default.repair-per-cost", 0.1);
		}
		for (Material m : diamond) {
			configYAML.set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".material-name", m.name());
			configYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
					"-") + ".costs.default.material-name", Material.DIAMOND.name());
			configYAML.set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".costs.default.priority", 0);
			configYAML.set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".costs.default.amount", 1);
			configYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
					"-") + ".costs.default.experience-cost", 0);
			configYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
					"-") + ".costs.default.repair-per-cost", 0.1);
		}
		for (Material m : leather) {
			configYAML.set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".material-name", m.name());
			configYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
					"-") + ".costs.default.material-name", Material.LEATHER.name());
			configYAML.set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".costs.default.priority", 0);
			configYAML.set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".costs.default.amount", 1);
			configYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
					"-") + ".costs.default.experience-cost", 0);
			configYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
					"-") + ".costs.default.repair-per-cost", 0.1);
		}
		for (Material m : chain) {
			configYAML.set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".material-name", m.name());
			configYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
					"-") + ".costs.default.material-name", Material.IRON_FENCE.name());
			configYAML.set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".costs.default.priority", 0);
			configYAML.set("repair-costs." + m.name().toLowerCase().replace("_", "-") + ".costs.default.amount", 1);
			configYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
					"-") + ".costs.default.experience-cost", 0);
			configYAML.set("repair-costs." + m.name().toLowerCase().replace("_",
					"-") + ".costs.default.repair-per-cost", 0.1);
		}
		configYAML.save();
	}

	public String getLanguageString(String key, String[][] args) {
		String s = getLanguageString(key);
		for (String[] arg : args) {
			s = s.replace(arg[0], arg[1]);
		}
		return s;
	}

	public String getLanguageString(String key) {
		return language.containsKey(key) ? language.get(key) : key;
	}

	public String getFormattedLanguageString(String key, String[][] args) {
		String s = getFormattedLanguageString(key);
		for (String[] arg : args) {
			s = s.replace(arg[0], arg[1]);
		}
		return s;
	}

	public String getFormattedLanguageString(String key) {
		return getLanguageString(key).replace('&', '\u00A7').replace("\u00A7\u00A7", "&");
	}

	public RepairItem getRepairItem(ItemStack itemStack) {
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
		for (RepairItem repItem : repairItemMap.values()) {
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

	private RepairCost getRepairCost(RepairItem repairItem, List<RepairCost> repairCostsList, Inventory inventory) {
		RepairCost repCost = null;
		for (RepairCost repairCost : repairCostsList) {
			ItemStack itemStack = repairCost.toItemStack(1);
			if (inventory.containsAtLeast(itemStack, repairCost.getAmount())) {
				if (repCost == null) {
					repCost = repairCost;
					continue;
				}
				if (repCost.getPriority() > repairCost.getPriority()) {
					repCost = repairCost;
				}
			}
		}
		return repCost;
	}

	protected ItemStack repairItemStack(ItemStack itemStack, Inventory inventory) {
		if (itemStack == null) {
			return null;
		}
		ItemStack repaired = itemStack.clone();
		RepairItem repairItem = getRepairItem(itemStack);
		if (repairItem == null) {
			return repaired;
		}
		List<RepairCost> repairCostList = repairItem.getRepairCosts();
		if (repairCostList == null) {
			return repaired;
		}
		RepairCost repairCost = getRepairCost(repairItem, repairCostList, inventory);
		if (repairCost == null) {
			return repaired;
		}
		if (!inventory.containsAtLeast(repairCost.toItemStack(1), repairCost.getAmount())) {
			return repaired;
		}

		inventory.removeItem(repairCost.toItemStack(repairCost.getAmount()));

		short currentDurability = repaired.getDurability();
		short newDurability = (short) (currentDurability - repaired.getType().getMaxDurability()
				* repairCost.getRepairPercentagePerCost());
		repaired.setDurability((short) Math.max(newDurability, 0));
		for (HumanEntity humanEntity : inventory.getViewers()) {
			if (humanEntity instanceof Player) {
				((Player) humanEntity).updateInventory();
			}
		}
		return repaired;
	}

	public static class RepairListener implements Listener {
		private MythicDropsRepair repair;
		private Map<String, ItemStack> repairing;

		public RepairListener(MythicDropsRepair repair) {
			this.repair = repair;
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
					player.sendMessage(repair.getFormattedLanguageString("messages.cannot-use"));
					repairing.remove(player.getName());
					return;
				}
				if (oldInHand.getDurability() == 0 || currentInHand.getDurability() == 0) {
					player.sendMessage(repair.getFormattedLanguageString("messages.cannot-use"));
					repairing.remove(player.getName());
					return;
				}
				RepairItem repairItem = repair.getRepairItem(currentInHand);
				if (repairItem == null) {
					player.sendMessage(repair.getFormattedLanguageString("messages.cannot-use"));
					repairing.remove(player.getName());
					return;
				}
				List<RepairCost> repairCostList = repairItem.getRepairCosts();
				if (repairCostList == null) {
					player.sendMessage(repair.getFormattedLanguageString("messages.cannot-use"));
					repairing.remove(player.getName());
					return;
				}
				RepairCost repairCost = repair.getRepairCost(repairItem, repairCostList, player.getInventory());
				if (repairCost == null) {
					player.sendMessage(repair.getFormattedLanguageString("messages.cannot-use"));
					repairing.remove(player.getName());
					return;
				}
				if (!player.getInventory().containsAtLeast(repairCost.toItemStack(1),
						repairCost.getAmount())) {
					player.sendMessage(repair.getFormattedLanguageString("messages.do-not-have", new String[][]{{"%material%",
							repairItem.toItemStack(1).getType().name()}}));
					repairing.remove(player.getName());
					return;
				}
				ExperienceManager experienceManager = new ExperienceManager(player);
				if (!experienceManager.hasExp(repairCost.getExperienceCost())) {
					player.sendMessage(repair.getFormattedLanguageString("messages.do-not-have", new String[][]{{"%material%",
							"experience"}}));
					repairing.remove(player.getName());
					return;
				}
				experienceManager.changeExp(-repairCost.getExperienceCost());
				player.setItemInHand(repair.repairItemStack(oldInHand, player.getInventory()));
				player.sendMessage(repair.getFormattedLanguageString("messages.success"));
				repairing.remove(player.getName());
				player.updateInventory();
				if (repair.playSounds) {
					player.playSound(event.getBlock().getLocation(), Sound.ANVIL_USE, 1.0F, 1.0F);
				}
			} else {
				if (player.getItemInHand().getType() == Material.AIR) {
					return;
				}
				repairing.put(player.getName(), player.getItemInHand());
				player.sendMessage(repair.getFormattedLanguageString("messages.instructions"));
			}
		}

	}

}
