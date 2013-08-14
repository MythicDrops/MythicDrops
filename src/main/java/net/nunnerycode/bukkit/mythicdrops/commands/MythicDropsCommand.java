package net.nunnerycode.bukkit.mythicdrops.commands;

import net.nunnerycode.bukkit.mythicdrops.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.items.ItemGenerationReason;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import org.bukkit.entity.Player;
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

	@Command(identifier = "md spawn", description = "Allows players to spawn in MythicDrops items",
			onlyPlayers = true, permissions = "mythicdrops.command.spawn")
	@Flags(identifier = {"a", "t"}, description = {"Amount to spawn", "Tier to spawn"})
	public void spawnSubcommand(Player sender, @Arg(name = "amount", def = "1") @FlagArg("a") int amount,
								@Arg(name = "tier", def = "*") @FlagArg("t") String tierName) {
		if (tierName.equalsIgnoreCase("*") && !sender.hasPermission("mythicdrops.command.spawn.wildcard")) {
			getPlugin().getLanguageManager().sendMessage(sender, "command.no-access");
			return;
		}
		Tier tier;
		try {
			if (tierName.equalsIgnoreCase("*")) {
			 	tier = plugin.getTierManager().getFilteredRandomTierWithChance();
			} else {
				tier = plugin.getTierManager().getTierFromName(tierName);
				if (tier == null) {
					tier = plugin.getTierManager().getTierFromDisplayName(tierName);
				}
			}
		} catch (NullPointerException e) {
			getPlugin().getLanguageManager().sendMessage(sender, "command.tier-does-not-exist");
			return;
		}
		if (!sender.hasPermission("mythicdrops.command.spawn." + tier.getTierName().toLowerCase())) {
			getPlugin().getLanguageManager().sendMessage(sender, "command.no-access");
			return;
		}
		int amountGiven = 0;
		for (int i = 0; i < amount; i++) {
			try {
				sender.getInventory().addItem(getPlugin().getDropManager().constructItemStackFromTier(tier,
						ItemGenerationReason.COMMAND));
				amountGiven++;
			} catch (IllegalArgumentException ignored) {
			} catch (NullPointerException ignored) {
			}
		}
		getPlugin().getLanguageManager().sendMessage(sender, "command.spawn-random", new String[][]{{"%amount%",
				String.valueOf(amountGiven)}});
	}

	public MythicDrops getPlugin() {
		return plugin;
	}

}
