package net.nunnerycode.bukkit.mythicdrops.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.nunnerycode.bukkit.mythicdrops.MythicDropsPlugin;
import net.nunnerycode.bukkit.mythicdrops.api.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.commands.MythicCommand;
import net.nunnerycode.bukkit.mythicdrops.api.items.CustomItem;
import net.nunnerycode.bukkit.mythicdrops.api.items.ItemGenerationReason;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import net.nunnerycode.bukkit.mythicdrops.events.MythicDropsHelpCommandEvent;
import net.nunnerycode.bukkit.mythicdrops.items.MythicCustomItem;
import net.nunnerycode.bukkit.mythicdrops.utils.ChatColorUtils;
import net.nunnerycode.bukkit.mythicdrops.utils.ItemStackUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import se.ranzdo.bukkit.methodcommand.Arg;
import se.ranzdo.bukkit.methodcommand.Command;
import se.ranzdo.bukkit.methodcommand.CommandHandler;
import se.ranzdo.bukkit.methodcommand.FlagArg;
import se.ranzdo.bukkit.methodcommand.Flags;

import static java.util.Map.Entry;

public class MythicDropsCommand implements MythicCommand {

	private final MythicDropsPlugin plugin;
	private final CommandHandler commandHandler;

	public MythicDropsCommand(MythicDropsPlugin plugin) {
		this.plugin = plugin;
		commandHandler = new CommandHandler(this.plugin);
		commandHandler.registerCommands(this);
	}

	public CommandHandler getCommandHandler() {
		return commandHandler;
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

	@Command(identifier = "mythicdrops customcreate", description = "Creates a custom item from the item in the " +
			"user's hand", permissions = "mythicdrops.command.customcreate")
	public void customCreateSubcommand(CommandSender sender, @Arg(name = "chance to spawn") double chanceToSpawn,
									   @Arg(name = "chance to drop") double chanceToDrop) {
		if (!(sender instanceof Player)) {
			getPlugin().getLanguageManager().sendMessage(sender, "command.no-access");
			return;
		}
		Player p = (Player) sender;
		ItemStack itemInHand = p.getItemInHand();
		if (!itemInHand.hasItemMeta()) {
			getPlugin().getLanguageManager().sendMessage(p, "command.customcreate-failure");
			return;
		}
		ItemMeta im = itemInHand.getItemMeta();
		if (!im.hasDisplayName() || !im.hasLore()) {
			getPlugin().getLanguageManager().sendMessage(p, "command.customcreate-failure");
			return;
		}
		String displayName;
		if (im.hasDisplayName()) {
			displayName = im.getDisplayName().replace('\u00A7', '&');
		} else {
			displayName = "&" + ChatColorUtils.getRandomChatColor().getChar() + getPlugin().getNameManager()
					.randomGeneralPrefix() + " " + getPlugin().getNameManager().randomGeneralSuffix();
		}
		String name;
		if (im.hasDisplayName()) {
			name = ChatColor.stripColor(im.getDisplayName()).replaceAll("\\s+", "");
		} else {
			name = ChatColor.stripColor(displayName).replaceAll("\\s+", "");
		}
		List<String> lore = new ArrayList<String>();
		if (im.hasLore()) {
			lore = im.getLore();
		}
		Map<Enchantment, Integer> enchantments = new HashMap<Enchantment, Integer>();
		if (im.hasEnchants()) {
			enchantments = im.getEnchants();
		}
		CustomItem ci = new MythicCustomItem(name, displayName, lore, enchantments, itemInHand.getData(),
				chanceToSpawn, chanceToDrop);
		getPlugin().getCustomItemManager().getCustomItems().add(ci);
		getPlugin().getLanguageManager().sendMessage(p, "command.customcreate-success", new String[][]{{"%name%",
				name}});
		getPlugin().getCustomItemSaver().save();
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
			e.printStackTrace();
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
					itemStack = getPlugin().getDropManager().generateItemStack(ItemGenerationReason.COMMAND);
				} else {
					itemStack = getPlugin().getDropManager().generateItemStackFromTier(tier,
							ItemGenerationReason.COMMAND);
				}
				itemStack.setDurability(ItemStackUtils.getDurabilityForMaterial(itemStack.getType(), minDura,
						maxDura));
				player.getInventory().addItem(itemStack);
				amountGiven++;
			} catch (Exception ignored) {
				ignored.printStackTrace();
			}
		}
		getPlugin().getLanguageManager().sendMessage(player, "command.give-random-receiver",
				new String[][]{{"%amount%", String.valueOf(amountGiven)}});
		getPlugin().getLanguageManager().sendMessage(sender, "command.give-random-sender", new String[][]{{"%amount%",
				String.valueOf(amountGiven)}, {"%receiver%", player.getName()}});
	}

	@Command(identifier = "mythicdrops custom", description = "Gives custom MythicDrops items",
			permissions = "mythicdrops.command.custom")
	@Flags(identifier = {"a", "c", "mind", "maxd"}, description = {"Amount to spawn", "Custom Item to spawn",
			"Minimum durability", "Maximum durability"})
	public void customSubcommand(CommandSender sender, @Arg(name = "player", def = "self") String playerName,
								 @Arg(name = "amount", def = "1")
								 @FlagArg("a") int amount, @Arg(name = "item", def = "*") @FlagArg("c") String itemName,
								 @Arg(name = "mindurability", def = "1.0", verifiers = "min[0.0]|max[1.0]") @FlagArg
										 ("mind") double minDura,
								 @Arg(name = "maxdurability", def = "1.0", verifiers = "min[0.0]|max[1.0]") @FlagArg
										 ("maxd") double maxDura) {
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
				e.printStackTrace();
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
				ignored.printStackTrace();
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
			e.printStackTrace();
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
					itemStack = getPlugin().getDropManager().generateItemStack(ItemGenerationReason.COMMAND);
				} else {
					itemStack = getPlugin().getDropManager().generateItemStackFromTier(tier,
							ItemGenerationReason.COMMAND);
				}
				itemStack.setDurability(ItemStackUtils.getDurabilityForMaterial(itemStack.getType(), minDura,
						maxDura));
				player.getInventory().addItem(itemStack);
				amountGiven++;
			} catch (Exception ignored) {
				ignored.printStackTrace();
			}
		}
		getPlugin().getLanguageManager().sendMessage(player, "command.spawn-random",
				new String[][]{{"%amount%", String.valueOf(amountGiven)}});
	}

	@Command(identifier = "mythicdrops", description = "Basic MythicDrops command")
	public void baseCommand(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "<=-=-=-=-=-=-=-=-=-=-=-=-=-=-=>");
		Map<String, String> commands = new HashMap<String, String>();
		if (sender.hasPermission("mythicdrops.command.spawn")) {
			commands.put("mythicdrops spawn (-a [amount]) (-t [tier]) (-mind [mindurability]) (-maxd [maxdurability])"
					, "Spawns an amount of MythicDrops items (default 1) of a tier (* spawns random tiers) with " +
					"durability between mindurability (default 1.0) and maxdurability (default 1.0)");
		}
		if (sender.hasPermission("mythicdrops.command.give")) {
			commands.put("mythicdrops give [player] (-a [amount]) (-t [tier]) (-mind [mindurability]) (-maxd " +
					"[maxdurability])", "Gives an amount of MythicDrops items (default 1) of a tier (* spawns random " +
					"tiers) with durability between mindurability (default 1.0) and maxdurability (default 1.0) to a " +
					"player");
		}
		if (sender.hasPermission("mythicdrops.command.custom")) {
			commands.put("mythicdrops custom [player] (-a [amount]) (-c [item]) (-mind [mindurability]) (-maxd " +
					"[maxdurability])", "Gives an amount of MythicDropsitems (default 1) of a custom item (* spawns " +
					"random items) with durability between mindurability (default 1.0) and maxdurability (default 1" +
					".0) to a player (self is command sender)");
		}
		if (sender.hasPermission("mythicdrops.command.customcreate")) {
			commands.put("mythicdrops customcreate [chance to spawn] [chance to drop]",
					"Creates a custom item based on the item in the player's hand with " +
							"specified chance to spawn and drop");
		}
		if (sender.hasPermission("mythicdrops.command.save")) {
			commands.put("mythicdrops save", "Saves the configuration files");
		}
		if (sender.hasPermission("mythicdrops.command.load")) {
			commands.put("mythicdrops load", "Loads the configuration files");
		}

		MythicDropsHelpCommandEvent helpCommandEvent = new MythicDropsHelpCommandEvent(sender, commands);
		Bukkit.getPluginManager().callEvent(helpCommandEvent);

		for (Entry<String, String> entry : helpCommandEvent.getCommands().entrySet()) {
			getPlugin().getLanguageManager().sendMessage(sender, "command.command-help",
					new String[][]{{"%command%", entry.getKey()}, {"%help%", entry.getValue()}});
		}

		sender.sendMessage(ChatColor.GOLD + "<=-=-=-=-=-=-=-=-=-=-=-=-=-=-=>");
	}

}