package net.nunnerycode.bukkit.mythicdrops.commands;

import net.nunnerycode.bukkit.mythicdrops.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.items.ItemGenerationReason;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import net.nunnerycode.bukkit.mythicdrops.utils.ItemStackUtils;
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

	@Command(identifier = "mythicdrops spawn", description = "Spawns in MythicDrops items", 
			permissions = "mythicdrops.command.spawn")
	@Flags(identifier = {"a", "t", "mind", "maxd"}, description = {"Amount to spawn", "Tier to spawn",
			"Minimum durability", "Maximum durability"})
	public void spawnSubcommand(CommandSender sender, @Arg(name = "amount", def = "1", verifiers = "min[1]|max[27]") 
			@FlagArg("a") int amount, @Arg(name = "tier", def = "*") @FlagArg("t") String tierName,
			@Arg(name = "mindurability", def = "1.0", verifiers = "min[0.0]|max[1.0]") @FlagArg("mind") double minDura,
			@Arg(name = "maxdurability", def = "1.0", verifiers = "min[0.0]|max[1.0]") @FlagArg("maxd") double
					maxDura) {
		if (!(sender instanceof Player)) {
			getPlugin().getLanguageManager().sendMessage(sender, "command.no-access");
			return;	
		}
		Player player = (Player) sender;
		if (tierName.equalsIgnoreCase("*") && !player.hasPermission("mythicdrops.command.spawn.wildcard")) {
			getPlugin().getLanguageManager().sendMessage(player, "command.no-access");
			return;
		}
		Tier tier;
		try {
			if (tierName.equalsIgnoreCase("*")) {
				tier = plugin.getTierManager().getRandomTierWithChance();
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
		if (!player.hasPermission("mythicdrops.command.spawn." + tier.getTierName().toLowerCase()) && !player
				.hasPermission("mythicdrops.command.spawn.wildcard")) {
			getPlugin().getLanguageManager().sendMessage(player, "command.no-access");
			return;
		}
		int amountGiven = 0;
		for (int i = 0; i < amount; i++) {
			try {
				ItemStack itemStack = getPlugin().getDropManager().constructItemStackFromTier(tier,
						ItemGenerationReason.COMMAND);
				itemStack.setDurability(ItemStackUtils.getDurabilityForMaterial(itemStack.getType(), minDura,
						maxDura));
				player.getInventory().addItem(itemStack);
				amountGiven++;
			} catch (Exception ignored) {
			}
		}
		getPlugin().getLanguageManager().sendMessage(player, "command.spawn-random", new String[][]{{"%amount%",
				String.valueOf(amountGiven)}});
	}

	public MythicDrops getPlugin() {
		return plugin;
	}

}
