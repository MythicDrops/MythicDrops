package net.nunnerycode.bukkit.mythicdrops.commands;

import net.nunnerycode.bukkit.mythicdrops.api.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.items.ItemGenerationReason;
import net.nunnerycode.bukkit.mythicdrops.api.items.MythicItemStack;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import net.nunnerycode.bukkit.mythicdrops.items.MythicDropBuilder;
import net.nunnerycode.bukkit.mythicdrops.items.TierMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import se.ranzdo.bukkit.methodcommand.Arg;
import se.ranzdo.bukkit.methodcommand.Command;
import se.ranzdo.bukkit.methodcommand.FlagArg;
import se.ranzdo.bukkit.methodcommand.Flags;

public final class MythicDropsCommand {

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
			sender.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.only-players"));
			return;
		}

		Player player = (Player) sender;
		if (tierName.equalsIgnoreCase("*") && !player.hasPermission("mythicdrops.command.spawn.wildcard")) {
			player.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.no-access"));
			return;
		}

		Tier tier = getTier(tierName, player.getWorld().getName());

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
					.withItemGenerationReason(ItemGenerationReason.COMMAND).withTier(getTier(tierName,
							player.getWorld().getName())).build();
			if (mis != null) {
				player.getInventory().addItem(mis);
				amountGiven++;
			}
		}

		player.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.spawn-random",
				new String[][]{{"%amount%", String.valueOf(amountGiven)}}));
	}

	private Tier getTier(String tierName, String worldName) {
		Tier tier;
		if (tierName.equals("*")) {
			tier = TierMap.getInstance().getRandomWithChance(worldName);
			if (tier == null) {
				tier = TierMap.getInstance().getRandomWithChance("default");
			}
		} else {
			tier = TierMap.getInstance().get(tierName.toLowerCase());
			if (tier == null) {
				tier = TierMap.getInstance().get(tierName);
			}
		}
		return tier;
	}

	@Command(identifier = "mythicdrops drop", description = "Drops in MythicDrops items",
			permissions = "mythicdrops.command.drop")
	@Flags(identifier = {"a", "t", "w", "mind", "maxd"}, description = {"Amount to drop", "Tier to drop", "World",
			"Minimum durability", "Maximum durability"})
	public void dropSubcommand(CommandSender sender, @Arg(name = "amount", def = "1")
	@FlagArg("a") int amount, @Arg(name = "tier", def = "*") @FlagArg("t") String tierName,
							   @Arg(name = "world", def = "") @FlagArg("w") String worldName,
							   @Arg(name = "x") int x, @Arg(name = "y") int y, @Arg(name = "z") int z,
							   @Arg(name = "mindurability", def = "1.0", verifiers = "min[0.0]|max[1.0]") @FlagArg
									   ("mind") double minDura,
							   @Arg(name = "maxdurability", def = "1.0", verifiers = "min[0.0]|max[1.0]") @FlagArg
									   ("maxd") double maxDura) {
		if (!(sender instanceof Player) && "".equals(worldName)) {
			sender.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.only-players"));
			return;
		}

		if (tierName.equalsIgnoreCase("*") && !sender.hasPermission("mythicdrops.command.spawn.wildcard")) {
			sender.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.no-access"));
			return;
		}

		String worldN = sender instanceof Player ? ((Player) sender).getWorld().getName() : worldName;

		Tier tier = getTier(tierName, worldN);

		if (!sender.hasPermission("mythicdrops.command.spawn.wildcard")) {
			if (tier == null) {
				sender.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command" +
						".tier-does-not-exist"));
				return;
			} else if (!sender.hasPermission("mythicdrops.command.spawn." + tier.getName())) {
				sender.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.no-access"));
				return;
			}
		}

		World w = Bukkit.getWorld(worldN);
		if (w == null) {
			sender.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.world-does-not-exist"));
			return;
		}

		int amountGiven = 0;
		while (amountGiven < amount) {
			MythicItemStack mis = new MythicDropBuilder().inWorld(worldN).useDurability(false)
					.withItemGenerationReason(ItemGenerationReason.COMMAND).withTier(getTier(tierName, worldN)).build();
			if (mis != null) {
				w.dropItemNaturally(new Location(w, x, y, z), mis);
				amountGiven++;
			}
		}

		sender.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.drop-random",
				new String[][]{{"%amount%", String.valueOf(amountGiven)}}));
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
			sender.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.no-access"));
			return;
		}

		Tier tier = getTier(tierName, player.getWorld().getName());

		if (!sender.hasPermission("mythicdrops.command.give.wildcard")) {
			if (tier == null) {
				sender.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command" +
						".tier-does-not-exist"));
				return;
			} else if (!player.hasPermission("mythicdrops.command.give." + tier.getName())) {
				sender.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.no-access"));
				return;
			}
		}

		int amountGiven = 0;
		while (amountGiven < amount) {
			MythicItemStack mis = new MythicDropBuilder().inWorld(player.getWorld()).useDurability(false)
					.withItemGenerationReason(ItemGenerationReason.COMMAND).withTier(getTier(tierName,
							player.getWorld().getName())).build();
			if (mis != null) {
				player.getInventory().addItem(mis);
				amountGiven++;
			}
		}

		player.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.give-random-receiver",
				new String[][]{{"%amount%", String.valueOf(amountGiven)}}));
		sender.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.give-random-sender",
				new String[][]{{"%amount%", String.valueOf(amountGiven)}}));
	}

}
