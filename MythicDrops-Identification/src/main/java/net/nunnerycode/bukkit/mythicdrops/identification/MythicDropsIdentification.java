package net.nunnerycode.bukkit.mythicdrops.identification;

import com.conventnunnery.libraries.config.CommentedConventYamlConfiguration;
import com.conventnunnery.libraries.config.ConventYamlConfiguration;
import net.nunnerycode.bukkit.mythicdrops.MythicDropsPlugin;
import net.nunnerycode.bukkit.mythicdrops.api.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.events.MythicDropsCancellableEvent;
import net.nunnerycode.bukkit.mythicdrops.api.items.ItemGenerationReason;
import net.nunnerycode.bukkit.mythicdrops.api.items.MythicTome;
import net.nunnerycode.bukkit.mythicdrops.api.items.NonrepairableItemStack;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import net.nunnerycode.bukkit.mythicdrops.items.MythicDropBuilder;
import net.nunnerycode.bukkit.mythicdrops.tiers.TierMap;
import net.nunnerycode.bukkit.mythicdrops.utils.ItemUtil;
import net.nunnerycode.java.libraries.cannonball.DebugPrinter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import se.ranzdo.bukkit.methodcommand.Arg;
import se.ranzdo.bukkit.methodcommand.Command;
import se.ranzdo.bukkit.methodcommand.CommandHandler;
import se.ranzdo.bukkit.methodcommand.FlagArg;
import se.ranzdo.bukkit.methodcommand.Flags;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

public class MythicDropsIdentification extends JavaPlugin {

	private static MythicDropsIdentification _INSTANCE;
	private DebugPrinter debugPrinter;
	private MythicDrops mythicDrops;
	private ConventYamlConfiguration configYAML;
	private Map<String, String> language;
	private String identityTomeName;
	private List<String> identityTomeLore;
	private String unidentifiedItemName;
	private List<String> unidentifiedItemLore;
	private double unidentifiedChanceToSpawn;
	private double identityTomeChanceToSpawn;
	private Set<Tier> allowedUnidentifiedTiers;
	private CommandHandler commandHandler;

	public static MythicDropsIdentification getInstance() {
		return _INSTANCE;
	}

	public CommandHandler getCommandHandler() {
		return commandHandler;
	}

	public Set<Tier> getAllowedUnidentifiedTiers() {
		return allowedUnidentifiedTiers;
	}

	public MythicDrops getMythicDrops() {
		return mythicDrops;
	}

	@Override
	public void onDisable() {
		debugPrinter.debug(Level.INFO, "disabled", "", "", "");
	}

	@Override
	public void onEnable() {
		_INSTANCE = this;
		mythicDrops = MythicDropsPlugin.getInstance();
		debugPrinter = new DebugPrinter(getDataFolder().getPath(), "debug.log");

		for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
			if (plugin instanceof MythicDrops) {
				mythicDrops = (MythicDrops) plugin;
			}
		}

		if (mythicDrops == null) {
			debugPrinter.debug(Level.INFO, "Could not find MythicDrops to hook into, disabling");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}

		language = new HashMap<>();

		unpackConfigurationFiles(new String[]{"config.yml"}, false);

		configYAML = new CommentedConventYamlConfiguration(new File(getDataFolder(), "config.yml"),
				YamlConfiguration.loadConfiguration(getResource("config.yml")).getString("version"));
		configYAML.options().backupOnUpdate(true);
		configYAML.options().updateOnLoad(true);
		configYAML.load();

		unidentifiedChanceToSpawn = configYAML.getDouble("options.unidentified-chance-to-spawn", 0.5);
		identityTomeChanceToSpawn = configYAML.getDouble("options.identity-tome-chance-to-spawn", 0.1);
		ConfigurationSection cs = configYAML.getConfigurationSection("messages");
		if (cs != null) {
			for (String key : cs.getKeys(true)) {
				if (cs.isConfigurationSection(key)) {
					continue;
				}
				language.put("messages." + key, cs.getString(key, key));
			}
		}
		unidentifiedItemName = configYAML.getString("items.unidentified.name");
		unidentifiedItemLore = configYAML.getStringList("items.unidentified.lore");
		identityTomeName = configYAML.getString("items.identity-tome.name");
		identityTomeLore = configYAML.getStringList("items.identity-tome.lore");
		allowedUnidentifiedTiers = getTiersFromStrings(configYAML.getStringList("tiers.can-be-unidentified"));

		commandHandler = new CommandHandler(this);
		commandHandler.registerCommands(this);

		getServer().getPluginManager().registerEvents(new IdentificationListener(this), this);

		debugPrinter.debug(Level.INFO, "enabled");
	}

	private Set<Tier> getTiersFromStrings(List<String> strings) {
		Set<Tier> set = new HashSet<>();
		for (String s : strings) {
			Tier t = TierMap.getInstance().get(s);
			if (t == null) {
				continue;
			}
			set.add(t);
		}
		return set;
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

	public ConventYamlConfiguration getConfigYAML() {
		return configYAML;
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

	public String getIdentityTomeName() {
		return identityTomeName;
	}

	public List<String> getIdentityTomeLore() {
		return identityTomeLore;
	}

	public String getUnidentifiedItemName() {
		return unidentifiedItemName;
	}

	public List<String> getUnidentifiedItemLore() {
		return unidentifiedItemLore;
	}

	@Command(identifier = "mythicdropsidentification unidentified", description = "Gives Unidentified Item",
			permissions = "mythicdrops.command.unidentified")
	@Flags(identifier = {"a"}, description = {"Amount to spawn"})
	public void unidentifiedSubcommand(CommandSender sender, @Arg(name = "player", def = "self") String playerName,
									   @Arg(name = "amount", def = "1") @FlagArg("a") int amount) {
		if (!sender.hasPermission("mythicdrops.command.custom")) {
			sender.sendMessage(getFormattedLanguageString("command.no-access"));
			return;
		}
		Player player;
		if (playerName.equalsIgnoreCase("self")) {
			if (sender instanceof Player) {
				player = (Player) sender;
			} else {
				sender.sendMessage(getFormattedLanguageString("command.no-access"));
				return;
			}
		} else {
			player = Bukkit.getPlayer(playerName);
		}
		if (player == null) {
			sender.sendMessage(getFormattedLanguageString("command.player-does-not-exist"));
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
		player.sendMessage(getFormattedLanguageString("messages.commands.give-unidentified-receiver",
				new String[][]{{"%amount%", String.valueOf(amountGiven)}}));
		if (player != sender) {
			sender.sendMessage(getFormattedLanguageString("messages.commands.give-unidentified-sender",
					new String[][]{{"%amount%", String.valueOf(amountGiven)}, {"%receiver%", player.getName()}}));
		}
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

	public String getFormattedLanguageString(String key) {
		return getLanguageString(key).replace('&', '\u00A7').replace("\u00A7\u00A7", "&");
	}

	public String getFormattedLanguageString(String key, String[][] args) {
		String s = getFormattedLanguageString(key);
		for (String[] arg : args) {
			s = s.replace(arg[0], arg[1]);
		}
		return s;
	}

	@Command(identifier = "mythicdropsidentification tome", description = "Gives Identity Tome",
			permissions = "mythicdrops.command.tome")
	@Flags(identifier = {"a"}, description = {"Amount to spawn"})
	public void tomeSubcommand(CommandSender sender, @Arg(name = "player", def = "self") String playerName,
							   @Arg(name = "amount", def = "1") @FlagArg("a") int amount) {
		if (!sender.hasPermission("mythicdrops.command.tome")) {
			sender.sendMessage(getFormattedLanguageString("command.no-access"));
			return;
		}
		Player player;
		if (playerName.equalsIgnoreCase("self")) {
			if (sender instanceof Player) {
				player = (Player) sender;
			} else {
				sender.sendMessage(mythicDrops.getConfigSettings().getFormattedLanguageString("command.no-access"));
				return;
			}
		} else {
			player = Bukkit.getPlayer(playerName);
		}
		if (player == null) {
			sender.sendMessage(mythicDrops.getConfigSettings().getFormattedLanguageString("command" +
					".player-does-not-exist"));
			return;
		}
		int amountGiven = 0;
		for (int i = 0; i < amount; i++) {
			player.getInventory().addItem(new IdentityTome());
			amountGiven++;
		}
		player.sendMessage(getFormattedLanguageString("messages.commands.give-tome-receiver",
				new String[][]{{"%amount%", String.valueOf(amountGiven)}}));
		if (player != sender) {
			sender.sendMessage(getFormattedLanguageString("messages.commands.give-tome-sender",
					new String[][]{{"%amount%",
							String.valueOf(amountGiven)}, {"%receiver%", player.getName()}}));
		}
	}

	public double getUnidentifiedChanceToSpawn() {
		return unidentifiedChanceToSpawn;
	}

	public double getIdentityTomeChanceToSpawn() {
		return identityTomeChanceToSpawn;
	}

	public static class IdentityTome extends MythicTome {
		public IdentityTome() {
			super(MythicTome.TomeType.ENCHANTED_BOOK, MythicDropsIdentification.getInstance().getIdentityTomeName(),
					ChatColor.MAGIC + "Herobrine", MythicDropsIdentification.getInstance().getIdentityTomeLore(),
					new String[0]);
		}
	}

	public static class UnidentifiedItem extends NonrepairableItemStack {
		public UnidentifiedItem(Material material) {
			super(material, 1, (short) 0, ChatColor.WHITE + MythicDropsIdentification.getInstance()
					.getUnidentifiedItemName() + ChatColor.WHITE, MythicDropsIdentification.getInstance()
					.getUnidentifiedItemLore());
		}
	}

	public static class IdentificationEvent extends MythicDropsCancellableEvent {

		private final Player identifier;
		private ItemStack result;

		public IdentificationEvent(ItemStack result, Player identifier) {
			this.result = result;
			this.identifier = identifier;
		}

		public ItemStack getResult() {
			return result;
		}

		public void setResult(ItemStack result) {
			this.result = result;
		}

		public Player getIdentifier() {
			return identifier;
		}
	}

	public static class IdentificationListener implements Listener {
		private Map<String, ItemStack> heldIdentify;
		private MythicDropsIdentification ident;

		public IdentificationListener(MythicDropsIdentification plugin) {
			ident = plugin;
			heldIdentify = new HashMap<>();
		}

		@EventHandler
		public void onPlayerDeath(PlayerDeathEvent event) {
			Player player = event.getEntity();
			if (heldIdentify.containsKey(player.getName())) {
				heldIdentify.remove(player.getName());
			}
		}

		@EventHandler(priority = EventPriority.NORMAL)
		public void onRightClick(PlayerInteractEvent event) {
			if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
				return;
			}
			if (event.getItem() == null) {
				return;
			}
			Player player = event.getPlayer();
			ItemStack itemInHand = event.getItem();
			String itemType = ItemUtil.getItemTypeFromMaterialData(itemInHand.getData());
			if (itemType != null && ItemUtil.isArmor(itemType) && itemInHand.hasItemMeta()) {
				event.setUseItemInHand(Event.Result.DENY);
				player.updateInventory();
			}
			if (heldIdentify.containsKey(player.getName())) {
				identifyItem(event, player, itemInHand, itemType);
			} else {
				addHeldIdentify(event, player, itemInHand);
			}
		}

		private void identifyItem(PlayerInteractEvent event, Player player, ItemStack itemInHand, String itemType) {
			if (ItemUtil.isArmor(itemType) || ItemUtil.isTool(itemType)) {
				if (!itemInHand.hasItemMeta()) {
					cannotUse(event, player);
					return;
				}
				if (!player.getInventory().contains(heldIdentify.get(player.getName()))) {
					player.sendMessage(ident.getFormattedLanguageString("messages.do-not-have"));
					cancelResults(event);
					heldIdentify.remove(player.getName());
					player.updateInventory();
					return;
				}
				UnidentifiedItem uid = new UnidentifiedItem(itemInHand.getData().getItemType());
				boolean b = itemInHand.getItemMeta().getDisplayName().equals(uid.getItemMeta().getDisplayName());
				if (!b) {
					cannotUse(event, player);
					return;
				}
				List<Tier> iihTiers = new ArrayList<>(ItemUtil.getTiersFromMaterialData(itemInHand.getData()));
				Collections.shuffle(iihTiers);
				Tier iihTier = null;
				for (Tier t : iihTiers) {
					if (ident.getAllowedUnidentifiedTiers().contains(t)) {
						iihTier = t;
						break;
					}
				}
				if (iihTier == null) {
					cannotUse(event, player);
					return;
				}
				ItemStack iih = new MythicDropBuilder().withItemGenerationReason(ItemGenerationReason.EXTERNAL)
						.withMaterialData(itemInHand.getData()).withTier(iihTier).useDurability(false).build();
				iih.setDurability(itemInHand.getDurability());

				ItemMeta itemMeta = iih.getItemMeta();
				List<String> lore = new ArrayList<String>();
				if (itemMeta.hasLore()) {
					lore = itemMeta.getLore();
				}

				itemMeta.setLore(lore);
				iih.setItemMeta(itemMeta);

				IdentificationEvent identificationEvent = new IdentificationEvent(iih, player);
				Bukkit.getPluginManager().callEvent(identificationEvent);

				if (identificationEvent.isCancelled()) {
					cannotUse(event, player);
					return;
				}

				int indexOfItem = player.getInventory().first(heldIdentify.get(player.getName()));
				ItemStack inInventory = player.getInventory().getItem(indexOfItem);
				inInventory.setAmount(inInventory.getAmount() - 1);
				player.getInventory().setItem(indexOfItem, inInventory);
				player.setItemInHand(identificationEvent.getResult());
				player.sendMessage(ident.getFormattedLanguageString("messages.success"));
				cancelResults(event);
				heldIdentify.remove(player.getName());
				player.updateInventory();
			} else {
				cannotUse(event, player);
			}
		}

		private void cannotUse(PlayerInteractEvent event, Player player) {
			player.sendMessage(ident.getFormattedLanguageString("messages.cannot-use"));
			cancelResults(event);
			heldIdentify.remove(player.getName());
			player.updateInventory();
		}

		private void cancelResults(PlayerInteractEvent event) {
			event.setCancelled(true);
			event.setUseInteractedBlock(Event.Result.DENY);
			event.setUseItemInHand(Event.Result.DENY);
		}

		private void addHeldIdentify(PlayerInteractEvent event, final Player player, ItemStack itemInHand) {
			if (!itemInHand.hasItemMeta()) {
				return;
			}
			ItemMeta im = itemInHand.getItemMeta();
			ItemStack identityTome = new IdentityTome();
			if (!im.hasDisplayName() || !identityTome.getItemMeta().hasDisplayName() || !im.getDisplayName().equals
					(identityTome.getItemMeta().getDisplayName())) {
				return;
			}
			player.sendMessage(ident.getFormattedLanguageString("messages.instructions"));
			heldIdentify.put(player.getName(), itemInHand);
			Bukkit.getScheduler().runTaskLaterAsynchronously(ident, new Runnable() {
				@Override
				public void run() {
					heldIdentify.remove(player.getName());
				}
			}, 20L * 30);
			cancelResults(event);
			player.updateInventory();
		}
	}

}
