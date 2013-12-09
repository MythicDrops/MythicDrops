package net.nunnerycode.bukkit.mythicdrops.commands;

import net.nunnerycode.bukkit.mythicdrops.api.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.items.ItemGenerationReason;
import net.nunnerycode.bukkit.mythicdrops.api.items.MythicItemStack;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import net.nunnerycode.bukkit.mythicdrops.items.MythicDropBuilder;
import net.nunnerycode.bukkit.mythicdrops.items.TierMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import se.ranzdo.bukkit.methodcommand.Arg;
import se.ranzdo.bukkit.methodcommand.Command;
import se.ranzdo.bukkit.methodcommand.FlagArg;
import se.ranzdo.bukkit.methodcommand.Flags;

public class MythicDropsCommand {

	private MythicDrops plugin;

	public MythicDropsCommand(MythicDrops plugin) {
		this.plugin = plugin;
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
			sender.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.no-access"));
			return;
		}

		Player player = (Player) sender;
		if (tierName.equalsIgnoreCase("*") && !player.hasPermission("mythicdrops.command.spawn.wildcard")) {
			player.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.no-access"));
			return;
		}

		Tier tier;
		if (tierName.equals("*")) {
			tier = TierMap.getInstance().getRandomWithChance(player.getWorld().getName());
			if (tier == null) {
				tier = TierMap.getInstance().getRandomWithChance("default");
			}
		} else {
			tier = TierMap.getInstance().get(tierName.toLowerCase());
			if (tier == null) {
				tier = TierMap.getInstance().get(tierName);
			}
		}

		System.out.println(tier);

		if (!player.hasPermission("mythicdrops.command.spawn.wildcard")) {
			if (tier == null) {
				player.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.tier-does-not-exist"));
				return;
			} else if (!player.hasPermission("mythicdrops.command.spawn." + tier.getName())) {
				player.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.no-access"));
				return;
			}
		}

		int amountGiven = 0;
		while (amountGiven < amount) {
			MythicItemStack mis = new MythicDropBuilder().inWorld(player.getWorld()).useDurability(false)
					.withItemGenerationReason(ItemGenerationReason.COMMAND).withTier(tier).build();
			if (mis != null) {
				player.getInventory().addItem(mis);
				amountGiven++;
			}
		}

		player.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.spawn-random",
				new String[][]{{"%amount%", String.valueOf(amountGiven)}}));
	}


}
