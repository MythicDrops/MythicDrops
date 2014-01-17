package net.nunnerycode.bukkit.mythicdrops.commands;

import com.pastebinclick.PasteBinClick;
import net.nunnerycode.bukkit.mythicdrops.api.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.items.CustomItem;
import net.nunnerycode.bukkit.mythicdrops.api.items.ItemGenerationReason;
import net.nunnerycode.bukkit.mythicdrops.api.items.MythicItemStack;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import net.nunnerycode.bukkit.mythicdrops.identification.IdentityTome;
import net.nunnerycode.bukkit.mythicdrops.identification.UnidentifiedItem;
import net.nunnerycode.bukkit.mythicdrops.items.CustomItemBuilder;
import net.nunnerycode.bukkit.mythicdrops.items.CustomItemMap;
import net.nunnerycode.bukkit.mythicdrops.items.MythicDropBuilder;
import net.nunnerycode.bukkit.mythicdrops.socketting.SocketGem;
import net.nunnerycode.bukkit.mythicdrops.socketting.SocketItem;
import net.nunnerycode.bukkit.mythicdrops.tiers.TierMap;
import net.nunnerycode.bukkit.mythicdrops.utils.ItemStackUtil;
import net.nunnerycode.bukkit.mythicdrops.utils.ItemUtil;
import net.nunnerycode.bukkit.mythicdrops.utils.SocketGemUtil;
import net.nunnerycode.bukkit.mythicdrops.utils.TierUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import se.ranzdo.bukkit.methodcommand.Arg;
import se.ranzdo.bukkit.methodcommand.Command;
import se.ranzdo.bukkit.methodcommand.FlagArg;
import se.ranzdo.bukkit.methodcommand.Flags;
import se.ranzdo.bukkit.methodcommand.Wildcard;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

		Tier tier = TierUtil.getTier(tierName);

		if (!player.hasPermission("mythicdrops.command.spawn.wildcard")) {
			if (tier == null) {
				player.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.tier-does-not-exist"));
				return;
			} else if (!player.hasPermission("mythicdrops.command.spawn." + tier.getName())) {
				player.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.no-access"));
				return;
			}
		}

		if (tier == null) {
			tier = TierUtil.randomTierWithChance(TierMap.getInstance().values(), player.getWorld().getName());
		}

		int amountGiven = 0;
		while (amountGiven < amount) {
			MythicItemStack mis = new MythicDropBuilder().inWorld(player.getWorld()).useDurability(false)
					.withItemGenerationReason(ItemGenerationReason.COMMAND).withTier(tier).build();
			if (mis != null) {
				mis.setDurability(ItemStackUtil.getDurabilityForMaterial(mis.getType(), minDura, maxDura));
				player.getInventory().addItem(mis);
				amountGiven++;
			}
		}

		player.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.spawn-random",
				new String[][]{{"%amount%", String.valueOf(amountGiven)}}));
	}

	@Command(identifier = "mythicdrops drop", description = "Drops in MythicDrops items",
			permissions = "mythicdrops.command.drop")
	@Flags(identifier = {"a", "t", "w", "mind", "maxd"}, description = {"Amount to drop", "Tier to drop", "World",
			"Minimum durability", "Maximum durability"})
	public void dropSubcommand(CommandSender sender, @Arg(name = "amount", def = "1")
	@FlagArg("a") int amount, @Arg(name = "tier", def = "*") @FlagArg("t") String tierName,
							   @Arg(name = "world", def = "") @FlagArg("w") String worldName,
							   @Arg(name = "x") double x, @Arg(name = "y") double y, @Arg(name = "z") double z,
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

		Tier tier = TierUtil.getTier(tierName);

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

		if (tier == null) {
			tier = TierUtil.randomTierWithChance(TierMap.getInstance().values(), worldN);
		}

		World w = Bukkit.getWorld(worldN);
		if (w == null) {
			sender.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.world-does-not-exist"));
			return;
		}
		Location l = new Location(w, x, y, z);

		int amountGiven = 0;
		while (amountGiven < amount) {
			MythicItemStack mis = new MythicDropBuilder().inWorld(worldN).useDurability(false)
					.withItemGenerationReason(ItemGenerationReason.COMMAND).withTier(tier).build();
			if (mis != null) {
				mis.setDurability(ItemStackUtil.getDurabilityForMaterial(mis.getType(), minDura, maxDura));
				if (l.getBlock().getState() instanceof InventoryHolder) {
					((InventoryHolder) l.getBlock().getState()).getInventory().addItem(mis);
				} else {
					w.dropItem(l, mis);
				}
				amountGiven++;
			}
		}

		sender.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.drop-random",
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

		Tier tier = TierUtil.getTier(tierName);

		if (!sender.hasPermission("mythicdrops.command.give.wildcard")) {
			if (tier == null) {
				sender.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command" +
						".tier-does-not-exist"));
				return;
			} else if (!sender.hasPermission("mythicdrops.command.give." + tier.getName())) {
				sender.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.no-access"));
				return;
			}
		}

		if (tier == null) {
			tier = TierUtil.randomTierWithChance(TierMap.getInstance().values(), player.getWorld().getName());
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

		player.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.give-random-receiver",
				new String[][]{{"%amount%", String.valueOf(amountGiven)}}));
		sender.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.give-random-sender",
				new String[][]{{"%amount%", String.valueOf(amountGiven)}, {"%receiver%", player.getName()}}));
	}

	@Command(identifier = "mythicdrops load", description = "Reloads the configuration files",
			permissions = "mythicdrops.command.load")
	public void loadSubcommand(CommandSender sender) {
		plugin.reloadSettings();
		plugin.reloadCustomItems();
		plugin.reloadTiers();
		plugin.reloadNames();
		sender.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.reload-config"));
	}

	@Command(identifier = "mythicdrops reload", description = "An alias for \"/mythicdrops load\"",
			permissions = "mythicdrops.command.load")
	public void reloadSubcommand(CommandSender sender) {
		loadSubcommand(sender);
	}

	@Command(identifier = "mythicdrops customcreate", description = "Creates a custom item from the item in the " +
			"user's hand", permissions = "mythicdrops.command.customcreate")
	public void customCreateSubcommand(CommandSender sender, @Arg(name = "chance to spawn") double chanceToSpawn,
									   @Arg(name = "chance to drop") double chanceToDrop) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.no-access"));
			return;
		}
		Player p = (Player) sender;
		ItemStack itemInHand = p.getItemInHand();
		if (!itemInHand.hasItemMeta()) {
			sender.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.customcreate-failure"));
			return;
		}
		ItemMeta im = itemInHand.getItemMeta();
		if (!im.hasDisplayName() || !im.hasLore()) {
			sender.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.customcreate-failure"));
			return;
		}
		String displayName;
		String name;
		if (im.hasDisplayName()) {
			displayName = im.getDisplayName().replace('\u00A7', '&');
			name = ChatColor.stripColor(im.getDisplayName()).replaceAll("\\s+", "");
		} else {
			sender.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.customcreate-failure"));
			return;
		}
		List<String> itemLore = new ArrayList<>();
		if (im.hasLore()) {
			itemLore = im.getLore();
		}

		List<String> lore = new ArrayList<>();
		for (String s : itemLore) {
			lore.add(s.replace('\u00A7', '&'));
		}

		Map<Enchantment, Integer> enchantments = new HashMap<>();
		if (im.hasEnchants()) {
			enchantments = im.getEnchants();
		}
		CustomItem ci = new CustomItemBuilder(name).withDisplayName(displayName).withLore(lore).withEnchantments
				(enchantments).withMaterialData(itemInHand.getData()).withChanceToBeGivenToMonster(chanceToSpawn)
				.withChanceToDropOnDeath(chanceToDrop).build();
		CustomItemMap.getInstance().put(name, ci);
		sender.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.customcreate-success",
				new String[][]{{"%name%", name}}));

		plugin.getCustomItemYAML().set(name + ".displayName", ci.getDisplayName());
		plugin.getCustomItemYAML().set(name + ".lore", ci.getLore());
		plugin.getCustomItemYAML().set(name + ".chanceToBeGivenToAMonster", ci.getChanceToBeGivenToAMonster());
		plugin.getCustomItemYAML().set(name + ".chanceToDropOnMonsterDeath", ci.getChanceToDropOnDeath());
		plugin.getCustomItemYAML().set(name + ".materialID", ci.getMaterialData().getItemTypeId());
		plugin.getCustomItemYAML().set(name + ".materialData", ci.getMaterialData().getData());
		for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
			plugin.getCustomItemYAML().set(name + ".enchantments." + entry.getKey().getName(), entry.getValue());
		}
		plugin.getCustomItemYAML().save();
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
				sender.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.no-access"));
				return;
			}
		} else {
			player = Bukkit.getPlayer(playerName);
		}
		if (player == null) {
			sender.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.player-does-not-exist"));
			return;
		}
		CustomItem customItem = null;
		if (!itemName.equalsIgnoreCase("*")) {
			try {
				customItem = CustomItemMap.getInstance().get(itemName);
			} catch (NullPointerException e) {
				sender.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command" +
						".custom-item-does-not-exist"));
				return;
			}
		}
		int amountGiven = 0;
		for (int i = 0; i < amount; i++) {
			try {
				ItemStack itemStack;
				if (customItem == null) {
					itemStack = CustomItemMap.getInstance().getRandomWithChance().toItemStack();
				} else {
					itemStack = customItem.toItemStack();
				}
				if (itemStack == null) {
					continue;
				}
				itemStack.setDurability(ItemStackUtil.getDurabilityForMaterial(itemStack.getType(), minDura,
						maxDura));
				player.getInventory().addItem(itemStack);
				amountGiven++;
			} catch (Exception ignored) {
				ignored.printStackTrace();
			}
		}
		player.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.give-custom-receiver",
				new String[][]{{"%amount%", String.valueOf(amountGiven)}}));
		if (!player.equals(sender)) {
			sender.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.give-custom-sender", new String[][]{{"%amount%",
					String.valueOf(amountGiven)}, {"%receiver%", player.getName()}}));
		}
	}

	@Command(identifier = "mythicdrops gem", description = "Gives MythicDrops gems",
			permissions = "mythicdrops.command.gem")
	@Flags(identifier = {"a", "g"}, description = {"Amount to spawn", "Socket Gem to spawn"})
	public void gemSubcommand(CommandSender sender, @Arg(name = "player", def = "self") String playerName,
							  @Arg(name = "amount", def = "1")
							  @FlagArg("a") int amount, @Arg(name = "item", def = "*") @FlagArg("g") String
									  itemName) {
		Player player;
		if (playerName.equalsIgnoreCase("self")) {
			if (sender instanceof Player) {
				player = (Player) sender;
			} else {
				sender.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.no-access",
						new String[][]{}));
				return;
			}
		} else {
			player = Bukkit.getPlayer(playerName);
		}
		if (player == null) {
			sender.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.player-does-not-exist",
					new String[][]{}));
			return;
		}
		SocketGem socketGem = null;
		if (!itemName.equalsIgnoreCase("*")) {
			try {
				socketGem = SocketGemUtil.getSocketGemFromName(itemName);
			} catch (NullPointerException e) {
				sender.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.socket-gem-does-not-exist",
						new String[][]{}));
				return;
			}
		}
		int amountGiven = 0;
		for (int i = 0; i < amount; i++) {
			try {
				ItemStack itemStack;
				if (socketGem == null) {
					MaterialData materialData = SocketGemUtil.getRandomSocketGemMaterial();
					socketGem = SocketGemUtil.getRandomSocketGemWithChance();
					itemStack = new SocketItem(materialData, socketGem);
				} else {
					itemStack = new SocketItem(SocketGemUtil.getRandomSocketGemMaterial(), socketGem);
				}
				itemStack.setDurability((short) 0);
				player.getInventory().addItem(itemStack);
				amountGiven++;
			} catch (Exception ignored) {
				ignored.printStackTrace();
			}
		}
		player.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.give-gem-receiver",
				new String[][]{{"%amount%", String.valueOf(amountGiven)}}));
		if (!sender.equals(player)) {
			sender.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.give-gem-sender", new String[][]{{"%amount%",
					String.valueOf(amountGiven)}, {"%receiver%", player.getName()}}));
		}
	}

	@Command(identifier = "mythicdrops unidentified", description = "Gives Unidentified Item",
			permissions = "mythicdrops.command.unidentified")
	@Flags(identifier = {"a"}, description = {"Amount to spawn"})
	public void unidentifiedSubcommand(CommandSender sender, @Arg(name = "player", def = "self") String playerName,
									   @Arg(name = "amount", def = "1") @FlagArg("a") int amount) {
		Player player;
		if (playerName.equalsIgnoreCase("self")) {
			if (sender instanceof Player) {
				player = (Player) sender;
			} else {
				sender.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.no-access"));
				return;
			}
		} else {
			player = Bukkit.getPlayer(playerName);
		}
		if (player == null) {
			sender.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.player-does-not-exist"));
			return;
		}
		int amountGiven = 0;
		for (int i = 0; i < amount; i++) {
			Tier t = getTier("*", player.getWorld().getName());
			if (t == null) {
				continue;
			}
			Collection<MaterialData> materialDatas = ItemUtil.getMaterialDatasFromTier(t);
			MaterialData materialData = ItemUtil.getRandomMaterialDataFromCollection(materialDatas);
			player.getInventory().addItem(new UnidentifiedItem(materialData.getItemType()));
			amountGiven++;
		}
		player.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.give-unidentified-receiver",
				new String[][]{{"%amount%", String.valueOf(amountGiven)}}));
		if (!player.equals(sender)) {
			sender.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.give-unidentified-sender",
					new String[][]{{"%amount%", String.valueOf(amountGiven)}, {"%receiver%", player.getName()}}));
		}
	}

	@Command(identifier = "mythicdrops tome", description = "Gives Identity Tome",
			permissions = "mythicdrops.command.tome")
	@Flags(identifier = {"a"}, description = {"Amount to spawn"})
	public void tomeSubcommand(CommandSender sender, @Arg(name = "player", def = "self") String playerName,
							   @Arg(name = "amount", def = "1") @FlagArg("a") int amount) {
		Player player;
		if (playerName.equalsIgnoreCase("self")) {
			if (sender instanceof Player) {
				player = (Player) sender;
			} else {
				sender.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.no-access"));
				return;
			}
		} else {
			player = Bukkit.getPlayer(playerName);
		}
		if (player == null) {
			sender.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command" +
					".player-does-not-exist"));
			return;
		}
		int amountGiven = 0;
		for (int i = 0; i < amount; i++) {
			player.getInventory().addItem(new IdentityTome());
			amountGiven++;
		}
		player.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command" +
				".give-tome-receiver", new String[][]{{"%amount%", String.valueOf(amountGiven)}}));
		if (player != sender) {
			sender.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command" +
					".give-tome-sender", new String[][]{{"%amount%", String.valueOf(amountGiven)}, {"%receiver%",
					player.getName()}}));
		}
	}

	@Command(identifier = "mythicdrops bug", description = "Creates an issue on GitHub",
			permissions = "mythicdrops.command.bug")
	public void bugSubcommand(CommandSender sender, @Wildcard @Arg(name = "bug", def = "") String issue) {
		if (!plugin.getConfigSettings().isReportingEnabled() || plugin.getSplatterWrapper() == null) {
			sender.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.no-access"));
			return;
		}
		int i = plugin.getSplatterWrapper().getSplatterTracker().createIssue("nunnery", "mythicdrops",
				"Command Reported Issue", issue);
		if (i == -1) {
			sender.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.bug-failure"));
			return;
		}

		YamlConfiguration yc = new YamlConfiguration();
		for (String key : plugin.getConfigYAML().getKeys(true)) {
			if (plugin.getConfigYAML().isConfigurationSection(key)) {
				continue;
			}
			if (key.equals("options.reporting.github-name") || key.equals("options.reporting.github-password")) {
				continue;
			}
			yc.set(key, plugin.getConfigYAML().get(key));
		}

		List<String> configLinks = new ArrayList<>();
		try {
			configLinks.add(new PasteBinClick("8ce41c4a293d5108913d1477223fc1e3").makePaste(yc.saveToString(),
					"config.yml", "yaml"));
			configLinks.add(new PasteBinClick("8ce41c4a293d5108913d1477223fc1e3").makePaste(plugin
					.getCreatureSpawningYAML().saveToString(), "creatureSpawning.yml", "yaml"));
			configLinks.add(new PasteBinClick("8ce41c4a293d5108913d1477223fc1e3").makePaste(plugin.getCustomItemYAML
					().saveToString(), "customItems.yml", "yaml"));
			configLinks.add(new PasteBinClick("8ce41c4a293d5108913d1477223fc1e3").makePaste(plugin.getIdentifyingYAML
					().saveToString(), "identifying.yml", "yaml"));
			configLinks.add(new PasteBinClick("8ce41c4a293d5108913d1477223fc1e3").makePaste(plugin.getLanguageYAML().saveToString(),
					"language.yml", "yaml"));
			configLinks.add(new PasteBinClick("8ce41c4a293d5108913d1477223fc1e3").makePaste(plugin.getRepairingYAML()
					.saveToString(), "repairing.yml", "yaml"));
			configLinks.add(new PasteBinClick("8ce41c4a293d5108913d1477223fc1e3").makePaste(plugin.getSocketGemsYAML()
					.saveToString(), "socketGems.yml", "yaml"));
			configLinks.add(new PasteBinClick("8ce41c4a293d5108913d1477223fc1e3").makePaste(plugin.getSockettingYAML()
					.saveToString(), "socketting.yml", "yaml"));
			configLinks.add(new PasteBinClick("8ce41c4a293d5108913d1477223fc1e3").makePaste(plugin.getTierYAML()
					.saveToString(), "tier.yml", "yaml"));
		} catch (UnsupportedEncodingException e) {
			// ignored
		}

		for (String s : configLinks) {
			plugin.getSplatterWrapper().getSplatterTracker().commentIssue("nunnery", "mythicdrops", i, s);
		}
		sender.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.bug-success",
				new String[][]{{"%id%", String.valueOf(i)}}));
	}

	@Command(identifier = "mythicdrops tiers", description = "Lists all Tiers",
			permissions = "mythicdrops.command.tiers")
	public void tiersCommand(CommandSender sender) {
		List<String> loadedTierNames = new ArrayList<>();
		for (Tier t : TierMap.getInstance().values()) {
			loadedTierNames.add(t.getName());
		}
		sender.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.tier-list",
				new String[][]{{"%tiers%", loadedTierNames.toString().replace("[", "").replace("]", "")}}));
	}

}
