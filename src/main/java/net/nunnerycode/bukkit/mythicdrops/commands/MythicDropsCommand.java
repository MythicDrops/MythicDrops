package net.nunnerycode.bukkit.mythicdrops.commands;

import net.nunnerycode.bukkit.mythicdrops.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.items.CustomItem;
import net.nunnerycode.bukkit.mythicdrops.api.items.ItemGenerationReason;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import net.nunnerycode.bukkit.mythicdrops.utils.ItemStackUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import se.ranzdo.bukkit.methodcommand.Arg;
import se.ranzdo.bukkit.methodcommand.Command;
import se.ranzdo.bukkit.methodcommand.CommandHandler;
import se.ranzdo.bukkit.methodcommand.FlagArg;
import se.ranzdo.bukkit.methodcommand.Flags;

public class MythicDropsCommand {

	private final MythicDrops plugin;
	private final CommandHandler commandHandler;

	public MythicDropsCommand(MythicDrops plugin) {
		this.plugin = plugin;
		commandHandler = new CommandHandler(this.plugin);
		commandHandler.registerCommands(this);
	}

	@Command(identifier = "mythicdrops save", description = "Saves the configuration files",
			permissions = "mythicdrops.command.save")
	public void saveSubcommand(CommandSender sender) {
		getPlugin().getSettingsSaver().save();
		getPlugin().getLanguageSaver().save();
		getPlugin().getCustomItemSaver().save();
		getPlugin().getTierSaver().save();
		getPlugin().getLanguageManager().sendMessage(sender, "command.save-config");
	}

	public MythicDrops getPlugin() {
		return plugin;
	}

	@Command(identifier = "mythicdrops load", description = "Reloads the configuration files",
			permissions = "mythicdrops.command.load")
	public void loadSubcommand(CommandSender sender) {
		getPlugin().getSettingsLoader().load();
		getPlugin().getLanguageLoader().load();
		getPlugin().getCustomItemLoader().load();
		getPlugin().getTierLoader().load();
		getPlugin().getLanguageManager().sendMessage(sender, "command.reload-config");
	}

	@Command(identifier = "mythicdrops reload", description = "Reloads the entire plugin",
			permissions = "mythicdrops.command.reload")
	public void reloadSubcommand(CommandSender sender) {
		getPlugin().reload();
		getPlugin().getLanguageManager().sendMessage(sender, "command.reload-plugin");
	}

	@Command(identifier = "mythicdrops give", description = "Gives MythicDrops items",
			permissions = "mythicdrops.command.give")
	@Flags(identifier = {"a", "t", "mind", "maxd"}, description = {"Amount to spawn", "Tier to spawn",
			"Minimum durability", "Maximum durability"})
	public void giveSubcommand(CommandSender sender, @Arg(name = "player") Player player, @Arg(name = "amount",
			def = "1")
	@FlagArg("a") int amount, @Arg(name = "tier", def = "*") @FlagArg("t") String tierName,
							   @Arg(name = "mindurability", def = "1.0", verifiers = "min[0.0]|max[1.0]") @FlagArg
									   ("mind") double minDura,
							   @Arg(name = "maxdurability", def = "1.0", verifiers = "min[0.0]|max[1.0]") @FlagArg
									   ("maxd") double maxDura) {
		if (tierName.equalsIgnoreCase("*") && !sender.hasPermission("mythicdrops.command.give.wildcard")) {
			getPlugin().getLanguageManager().sendMessage(player, "command.no-access");
			return;
		}
		Tier tier;
		try {
			if (tierName.equalsIgnoreCase("*")) {
				tier = null;
			} else {
				tier = plugin.getTierManager().getTierFromName(tierName);
				if (tier == null) {
					tier = plugin.getTierManager().getTierFromDisplayName(tierName);
				}
			}
		} catch (NullPointerException e) {
			getPlugin().getLanguageManager().sendMessage(player, "command.tier-does-not-exist");
			return;
		}
		if (tier == null) {
			if (!player.hasPermission("mythicdrops.command.give.wildcard")) {
				getPlugin().getLanguageManager().sendMessage(player, "command.no-access");
				return;
			}
		} else {
			if (!player.hasPermission("mythicdrops.command.give." + tier.getTierName().toLowerCase()) && !player
					.hasPermission("mythicdrops.command.give.wildcard")) {
				getPlugin().getLanguageManager().sendMessage(player, "command.no-access");
				return;
			}
		}
		int amountGiven = 0;
		for (int i = 0; i < amount; i++) {
			try {
				ItemStack itemStack;
				if (tier == null) {
					itemStack = getPlugin().getDropManager().constructItemStackFromTier(getPlugin().getTierManager()
							.getRandomTierWithChance(), ItemGenerationReason.COMMAND);
				} else {
					itemStack = getPlugin().getDropManager().constructItemStackFromTier(tier,
							ItemGenerationReason.COMMAND);
				}
				itemStack.setDurability(ItemStackUtils.getDurabilityForMaterial(itemStack.getType(), minDura,
						maxDura));
				player.getInventory().addItem(itemStack);
				amountGiven++;
			} catch (Exception ignored) {
			}
		}
		getPlugin().getLanguageManager().sendMessage(player, "command.give-random-receiver",
				new String[][]{{"%amount%", String.valueOf(amountGiven)}});
		getPlugin().getLanguageManager().sendMessage(sender, "command.give-random-sender", new String[][]{{"%amount%",
				String.valueOf(amountGiven)}, {"%receiver%", player.getName()}});
	}

	@Command(identifier = "mythicdrops give", description = "Gives custom MythicDrops items",
			permissions = "mythicdrops.command.give")
	@Flags(identifier = {"a", "c", "mind", "maxd"}, description = {"Amount to spawn", "Custom Item to spawn",
			"Minimum durability", "Maximum durability"})
	public void customSubcommand(CommandSender sender, @Arg(name = "player") String playerName, @Arg(name = "amount",
			def = "1")
	@FlagArg("a") int amount, @Arg(name = "tier", def = "*") @FlagArg("c") String itemName,
								 @Arg(name = "mindurability", def = "1.0", verifiers = "min[0.0]|max[1.0]") @FlagArg
										 ("mind") double minDura,
								 @Arg(name = "maxdurability", def = "1.0", verifiers = "min[0.0]|max[1.0]") @FlagArg
										 ("maxd") double maxDura) {
		if (!sender.hasPermission("mythicdrops.command.custom")) {
			getPlugin().getLanguageManager().sendMessage(sender, "command.no-access");
			return;
		}
		Player player;
		if (playerName.equalsIgnoreCase("self")) {
			if (sender instanceof Player) {
				player = (Player) sender;
			} else {
				getPlugin().getLanguageManager().sendMessage(sender, "command.no-access");
				return;
			}
		} else {
			player = Bukkit.getPlayer(playerName);
		}
		if (player == null) {
			getPlugin().getLanguageManager().sendMessage(sender, "command.player-does-not-exist");
			return;
		}
		CustomItem customItem = null;
		if (!itemName.equalsIgnoreCase("*")) {
			try {
				customItem = getPlugin().getCustomItemManager().getCustomItemFromName(itemName);
				if (customItem == null) {
					customItem = getPlugin().getCustomItemManager().getCustomItemFromDisplayName(itemName);
				}
			} catch (NullPointerException e) {
				getPlugin().getLanguageManager().sendMessage(sender, "command.custom-item-does-not-exist");
				return;
			}
		}
		int amountGiven = 0;
		for (int i = 0; i < amount; i++) {
			try {
				ItemStack itemStack;
				if (customItem == null) {
					itemStack = getPlugin().getDropManager().generateItemStackFromCustomItem(getPlugin()
							.getCustomItemManager().getRandomCustomItemWithChance(), ItemGenerationReason.COMMAND);
				} else {
					itemStack = getPlugin().getDropManager().generateItemStackFromCustomItem(customItem,
							ItemGenerationReason.COMMAND);
				}
				itemStack.setDurability(ItemStackUtils.getDurabilityForMaterial(itemStack.getType(), minDura,
						maxDura));
				player.getInventory().addItem(itemStack);
				amountGiven++;
			} catch (Exception ignored) {
			}
		}
		getPlugin().getLanguageManager().sendMessage(player, "command.give-custom-receiver",
				new String[][]{{"%amount%", String.valueOf(amountGiven)}});
		getPlugin().getLanguageManager().sendMessage(sender, "command.give-custom-sender", new String[][]{{"%amount%",
				String.valueOf(amountGiven)}, {"%receiver%", player.getName()}});
	}

	@Command(identifier = "mythicdrops spawn", description = "Spawns in MythicDrops items",
			permissions = "mythicdrops.command.spawn")
	@Flags(identifier = {"a", "t", "mind", "maxd"}, description = {"Amount to spawn", "Tier to spawn",
			"Minimum durability", "Maximum durability"})
	public void spawnSubcommand(CommandSender sender, @Arg(name = "amount", def = "1")
	@FlagArg("a") int amount, @Arg(name = "tier", def = "*") @FlagArg("t") String tierName,
								@Arg(name = "mindurability", def = "1.0", verifiers = "min[0.0]|max[1.0]") @FlagArg
										("mind") double minDura,
								@Arg(name = "maxdurability", def = "1.0", verifiers = "min[0.0]|max[1.0]") @FlagArg
										("maxd") double maxDura) {
		if (!(sender instanceof Player)) {
			getPlugin().getLanguageManager().sendMessage(sender, "command.no-access");
			return;
		}
		Player player = (Player) sender;
		if (tierName.equalsIgnoreCase("*") && !sender.hasPermission("mythicdrops.command.give.wildcard")) {
			getPlugin().getLanguageManager().sendMessage(player, "command.no-access");
			return;
		}
		Tier tier;
		try {
			if (tierName.equalsIgnoreCase("*")) {
				tier = null;
			} else {
				tier = plugin.getTierManager().getTierFromName(tierName);
				if (tier == null) {
					tier = plugin.getTierManager().getTierFromDisplayName(tierName);
				}
			}
		} catch (NullPointerException e) {
			getPlugin().getLanguageManager().sendMessage(player, "command.tier-does-not-exist");
			return;
		}
		if (tier == null) {
			if (!player.hasPermission("mythicdrops.command.spawn.wildcard")) {
				getPlugin().getLanguageManager().sendMessage(player, "command.no-access");
				return;
			}
		} else {
			if (!player.hasPermission("mythicdrops.command.spawn." + tier.getTierName().toLowerCase()) && !player
					.hasPermission("mythicdrops.command.spawn.wildcard")) {
				getPlugin().getLanguageManager().sendMessage(player, "command.no-access");
				return;
			}
		}
		int amountGiven = 0;
		for (int i = 0; i < amount; i++) {
			try {
				ItemStack itemStack;
				if (tier == null) {
					itemStack = getPlugin().getDropManager().constructItemStackFromTier(getPlugin().getTierManager()
							.getRandomTierWithChance(), ItemGenerationReason.COMMAND);
				} else {
					itemStack = getPlugin().getDropManager().constructItemStackFromTier(tier,
							ItemGenerationReason.COMMAND);
				}
				itemStack.setDurability(ItemStackUtils.getDurabilityForMaterial(itemStack.getType(), minDura,
						maxDura));
				player.getInventory().addItem(itemStack);
				amountGiven++;
			} catch (Exception ignored) {
			}
		}
		getPlugin().getLanguageManager().sendMessage(player, "command.spawn-random",
				new String[][]{{"%amount%", String.valueOf(amountGiven)}});
	}

}