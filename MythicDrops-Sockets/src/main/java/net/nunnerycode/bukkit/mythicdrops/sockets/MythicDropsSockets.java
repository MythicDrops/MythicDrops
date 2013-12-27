package net.nunnerycode.bukkit.mythicdrops.sockets;

import com.conventnunnery.libraries.config.ConventYamlConfiguration;
import net.nunnerycode.bukkit.mythicdrops.api.items.MythicItemStack;
import net.nunnerycode.bukkit.mythicdrops.events.RandomItemGenerationEvent;
import net.nunnerycode.bukkit.mythicdrops.utils.ItemUtil;
import net.nunnerycode.bukkit.mythicdrops.utils.RandomRangeUtil;
import net.nunnerycode.java.libraries.cannonball.DebugPrinter;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import se.ranzdo.bukkit.methodcommand.Arg;
import se.ranzdo.bukkit.methodcommand.Command;
import se.ranzdo.bukkit.methodcommand.CommandHandler;
import se.ranzdo.bukkit.methodcommand.FlagArg;
import se.ranzdo.bukkit.methodcommand.Flags;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

public class MythicDropsSockets extends JavaPlugin implements Listener {

	private static MythicDropsSockets _INSTANCE;
	private final Map<String, HeldItem> heldSocket = new HashMap<>();
	private DebugPrinter debugPrinter;
	private Map<String, String> language;
	private String socketGemName;
	private List<String> socketGemLore;
	private String sockettedItemSocket;
	private List<String> sockettedItemLore;
	private ConventYamlConfiguration configYAML;
	private ConventYamlConfiguration socketGemsYAML;
	private boolean useAttackerItemInHand;
	private boolean useAttackerArmorEquipped;
	private boolean useDefenderItemInHand;
	private boolean useDefenderArmorEquipped;
	private double socketGemChanceToSpawn;
	private List<MaterialData> socketGemMaterialIds;
	private Map<String, SocketGem> socketGemMap;
	private List<String> socketGemPrefixes;
	private boolean preventMultipleChangesFromSockets;
	private List<String> socketGemSuffixes;

	public static MythicDropsSockets getInstance() {
		return _INSTANCE;
	}

	public Map<String, SocketGem> getSocketGemMap() {
		return Collections.unmodifiableMap(socketGemMap);
	}

	public List<String> getSocketGemPrefixes() {
		return Collections.unmodifiableList(socketGemPrefixes);
	}

	public List<String> getSocketGemSuffixes() {
		return Collections.unmodifiableList(socketGemSuffixes);
	}

	public String getSockettedItemSocket() {
		return sockettedItemSocket;
	}

	public List<String> getSockettedItemLore() {
		return sockettedItemLore;
	}

	public ConventYamlConfiguration getConfigYAML() {
		return configYAML;
	}

	public ConventYamlConfiguration getSocketGemsYAML() {
		return socketGemsYAML;
	}

	@Override
	public void onDisable() {
		debugPrinter.debug(Level.INFO, "v" + getDescription().getVersion() + " disabled");
	}

	@Override
	public void onEnable() {
		_INSTANCE = this;
		debugPrinter = new DebugPrinter(getDataFolder().getPath(), "debug.log");

		language = new HashMap<>();
		socketGemMaterialIds = new ArrayList<>();
		socketGemMap = new HashMap<>();
		socketGemPrefixes = new ArrayList<>();
		socketGemSuffixes = new ArrayList<>();

		unpackConfigurationFiles(new String[]{"config.yml", "socketGems.yml"}, false);

		configYAML = new ConventYamlConfiguration(new File(getDataFolder(), "config.yml"),
				YamlConfiguration.loadConfiguration(getResource("config.yml")).getString("version"));
		configYAML.options().backupOnUpdate(true);
		configYAML.options().updateOnLoad(true);
		configYAML.load();

		socketGemsYAML = new ConventYamlConfiguration(new File(getDataFolder(), "socketGems.yml"),
				YamlConfiguration.loadConfiguration(getResource("socketGems.yml")).getString("version"));
		socketGemsYAML.options().backupOnUpdate(true);
		socketGemsYAML.options().updateOnLoad(true);
		socketGemsYAML.load();

		loadSettings();
		loadGems();

		getServer().getPluginManager().registerEvents(this, this);

		CommandHandler commandHandler = new CommandHandler(this);
		commandHandler.registerCommands(this);

		debugPrinter.debug(Level.INFO, "v" + getDescription().getVersion() + " enabled");
	}

	private void loadGems() {
		socketGemMap.clear();
		List<String> loadedSocketGems = new ArrayList<>();
		if (!socketGemsYAML.isConfigurationSection("socket-gems")) {
			return;
		}
		ConfigurationSection cs = socketGemsYAML.getConfigurationSection("socket-gems");
		for (String key : cs.getKeys(false)) {
			if (!cs.isConfigurationSection(key)) {
				continue;
			}
			ConfigurationSection gemCS = cs.getConfigurationSection(key);
			GemType gemType = GemType.getFromName(gemCS.getString("type"));
			if (gemType == null) {
				gemType = GemType.ANY;
			}
			List<SocketPotionEffect> socketPotionEffects = buildSocketPotionEffects(gemCS);
			List<SocketParticleEffect> socketParticleEffects = buildSocketParticleEffects(gemCS);
			double chance = gemCS.getDouble("chance");
			String prefix = gemCS.getString("prefix");
			if (prefix != null && !prefix.equalsIgnoreCase("")) {
				socketGemPrefixes.add(prefix);
			}
			String suffix = gemCS.getString("suffix");
			if (suffix != null && !suffix.equalsIgnoreCase("")) {
				socketGemSuffixes.add(suffix);
			}
			List<String> lore = gemCS.getStringList("lore");
			Map<Enchantment, Integer> enchantments = new HashMap<>();
			if (gemCS.isConfigurationSection("enchantments")) {
				ConfigurationSection enchCS = gemCS.getConfigurationSection("enchantments");
				for (String key1 : enchCS.getKeys(false)) {
					Enchantment ench = null;
					for (Enchantment ec : Enchantment.values()) {
						if (ec.getName().equalsIgnoreCase(key1)) {
							ench = ec;
							break;
						}
					}
					if (ench == null) {
						continue;
					}
					int level = enchCS.getInt(key1);
					enchantments.put(ench, level);
				}
			}
			List<String> commands = gemCS.getStringList("commands");
			List<SocketCommand> socketCommands = new ArrayList<>();
			for (String s : commands) {
				SocketCommand sc = new SocketCommand(s);
				socketCommands.add(sc);
			}
			SocketGem sg = new SocketGem(key, gemType, socketPotionEffects, socketParticleEffects, chance, prefix,
					suffix, lore, enchantments, socketCommands);
			socketGemMap.put(key, sg);
			loadedSocketGems.add(key);
		}
		debugPrinter.debug(Level.INFO, "Loaded socket gems: " + loadedSocketGems.toString());
	}

	private List<SocketPotionEffect> buildSocketPotionEffects(ConfigurationSection cs) {
		List<SocketPotionEffect> socketPotionEffectList = new ArrayList<>();
		if (!cs.isConfigurationSection("potion-effects")) {
			return socketPotionEffectList;
		}
		ConfigurationSection cs1 = cs.getConfigurationSection("potion-effects");
		for (String key : cs1.getKeys(false)) {
			PotionEffectType pet = PotionEffectType.getByName(key);
			if (pet == null) {
				continue;
			}
			int duration = cs1.getInt(key + ".duration");
			int intensity = cs1.getInt(key + ".intensity");
			int radius = cs1.getInt(key + ".radius");
			String target = cs1.getString(key + ".target");
			EffectTarget et = EffectTarget.getFromName(target);
			if (et == null) {
				et = EffectTarget.NONE;
			}
			boolean affectsWielder = cs1.getBoolean(key + ".affectsWielder");
			boolean affectsTarget = cs1.getBoolean(key + ".affectsTarget");
			socketPotionEffectList.add(new SocketPotionEffect(pet, intensity, duration, radius, et, affectsWielder,
					affectsTarget));
		}
		return socketPotionEffectList;
	}

	private List<SocketParticleEffect> buildSocketParticleEffects(ConfigurationSection cs) {
		List<SocketParticleEffect> socketParticleEffectList = new ArrayList<>();
		if (!cs.isConfigurationSection("particle-effects")) {
			return socketParticleEffectList;
		}
		ConfigurationSection cs1 = cs.getConfigurationSection("particle-effects");
		for (String key : cs1.getKeys(false)) {
			Effect pet;
			try {
				pet = Effect.valueOf(key);
			} catch (Exception e) {
				continue;
			}
			if (pet == null) {
				continue;
			}
			int duration = cs1.getInt(key + ".duration");
			int intensity = cs1.getInt(key + ".intensity");
			int radius = cs1.getInt(key + ".radius");
			String target = cs1.getString(key + ".target");
			EffectTarget et = EffectTarget.getFromName(target);
			if (et == null) {
				et = EffectTarget.NONE;
			}
			boolean affectsWielder = cs1.getBoolean(key + ".affectsWielder");
			boolean affectsTarget = cs1.getBoolean(key + ".affectsTarget");
			socketParticleEffectList.add(new SocketParticleEffect(pet, intensity, duration, radius, et,
					affectsWielder, affectsTarget));
		}
		return socketParticleEffectList;
	}

	private void loadSettings() {
		useAttackerItemInHand = configYAML.getBoolean("options.use-attacker-item-in-hand", true);
		useAttackerArmorEquipped = configYAML.getBoolean("options.use-attacker-armor-equipped", false);
		useDefenderItemInHand = configYAML.getBoolean("options.use-defender-item-in-hand", false);
		useDefenderArmorEquipped = configYAML.getBoolean("options.use-defender-armor-equipped", true);
		socketGemChanceToSpawn = configYAML.getDouble("options.socket-gem-chance-to-spawn", 0.25);
		preventMultipleChangesFromSockets = configYAML.getBoolean("options.prevent-multiple-changes-from-sockets",
				true);

		List<String> socketGemMats = configYAML.getStringList("options.socket-gem-material-ids");
		for (String s : socketGemMats) {
			int id;
			byte data;
			if (s.contains(";")) {
				String[] split = s.split(";");
				id = NumberUtils.toInt(split[0], 0);
				data = (byte) NumberUtils.toInt(split[1], 0);
			} else {
				id = NumberUtils.toInt(s, 0);
				data = 0;
			}
			if (id == 0) {
				continue;
			}
			socketGemMaterialIds.add(new MaterialData(id, data));
		}

		socketGemName = configYAML.getString("items.socket-name", "&6Socket Gem - %socketgem%");
		socketGemLore = configYAML.getStringList("items.socket-lore");
		sockettedItemSocket = configYAML.getString("items.socketted-item-socket", "&6(Socket)");
		sockettedItemLore = configYAML.getStringList("items.socketted-item-lore");

		language.clear();
		for (String key : configYAML.getConfigurationSection("language").getKeys(true)) {
			if (configYAML.getConfigurationSection("language").isConfigurationSection(key)) {
				continue;
			}
			language.put(key, configYAML.getConfigurationSection("language").getString(key, key));
		}
	}

	private void unpackConfigurationFiles(String[] configurationFiles, boolean overwrite) {
		for (String s : configurationFiles) {
			YamlConfiguration yc = YamlConfiguration.loadConfiguration(getResource(s));
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

	@EventHandler
	public void onRandomItemGeneration(RandomItemGenerationEvent event) {
		if (event.isModified()) {
			return;
		}

		if (RandomUtils.nextDouble() < getSocketGemChanceToSpawn()) {
			event.setItemStack(new SocketItem(getRandomSocketGemMaterial(), getRandomSocketGemWithChance()));
			return;
		}

		int minimumSockets = event.getTier().getMinimumSockets();
		int maximumSockets = event.getTier().getMaximumSockets();

		int totalSockets = (int) RandomRangeUtil.randomRangeLongInclusive(minimumSockets, maximumSockets);

		MythicItemStack mis = event.getItemStack();

		for (int i = 0; i < totalSockets; i++) {
			mis.getItemMeta().getLore().add(getSockettedItemSocket());
		}

		event.setItemStack(mis);
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
		if (getSocketGemMaterialIds().contains(itemInHand.getData())) {
			event.setUseItemInHand(Event.Result.DENY);
			player.updateInventory();
		}
		if (itemType != null && ItemUtil.isArmor(itemType) && itemInHand.hasItemMeta()) {
			event.setUseItemInHand(Event.Result.DENY);
			player.updateInventory();
		}
		if (heldSocket.containsKey(player.getName())) {
			socketItem(event, player, itemInHand, itemType);
			heldSocket.remove(player.getName());
		} else {
			addHeldSocket(event, player, itemInHand);
		}
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

	public List<String> replaceArgs(List<String> strings, String[][] args) {
		List<String> list = new ArrayList<>();
		for (String s : strings) {
			list.add(replaceArgs(s, args));
		}
		return list;
	}

	public String replaceArgs(String string, String[][] args) {
		String s = string;
		for (String[] arg : args) {
			s = s.replace(arg[0], arg[1]);
		}
		return s;
	}

	public String getSocketGemName() {
		return socketGemName;
	}

	public List<String> getSocketGemLore() {
		return socketGemLore;
	}

	public double getSocketGemChanceToSpawn() {
		return socketGemChanceToSpawn;
	}

	public boolean socketGemTypeMatchesItemStack(SocketGem socketGem, ItemStack itemStack) {
		String itemType = ItemUtil.getItemTypeFromMaterialData(itemStack.getData());
		if (itemType == null) {
			return false;
		}
		switch (socketGem.getGemType()) {
			case TOOL:
				return ItemUtil.isTool(itemType);
			case ARMOR:
				return ItemUtil.isArmor(itemType);
			case ANY:
				return true;
			default:
				return false;
		}
	}

	private void addHeldSocket(PlayerInteractEvent event, final Player player, ItemStack itemInHand) {
		if (!getSocketGemMaterialIds().contains(itemInHand.getData())) {
			return;
		}
		if (!itemInHand.hasItemMeta()) {
			return;
		}
		ItemMeta im = itemInHand.getItemMeta();
		if (!im.hasDisplayName()) {
			return;
		}
		String replacedArgs = ChatColor.stripColor(replaceArgs(socketGemName, new String[][]{{"%socketgem%", ""}}).replace('&', '\u00A7').replace("\u00A7\u00A7", "&"));
		String type = ChatColor.stripColor(im.getDisplayName().replace(replacedArgs, ""));
		if (type == null) {
			return;
		}
		SocketGem socketGem = socketGemMap.get(type);
		if (socketGem == null) {
			socketGem = getSocketGemFromName(type);
			if (socketGem == null) {
				return;
			}
		}
		sendMessage(player, "messages.instructions", new String[][]{});
		HeldItem hg = new HeldItem(socketGem.getName(), itemInHand);
		heldSocket.put(player.getName(), hg);
		Bukkit.getScheduler().runTaskLaterAsynchronously(this, new Runnable() {
			@Override
			public void run() {
				heldSocket.remove(player.getName());
			}
		}, 30 * 20L);
		event.setCancelled(true);
		event.setUseInteractedBlock(Event.Result.DENY);
		event.setUseItemInHand(Event.Result.DENY);
		player.updateInventory();
	}

	public boolean isPreventMultipleChangesFromSockets() {
		return preventMultipleChangesFromSockets;
	}

	private void socketItem(PlayerInteractEvent event, Player player, ItemStack itemInHand, String itemType) {
		if (ItemUtil.isArmor(itemType) || ItemUtil.isTool(itemType)) {
			if (!itemInHand.hasItemMeta()) {
				sendMessage(player, "messages.cannot-use", new String[][]{});
				event.setCancelled(true);
				event.setUseInteractedBlock(Event.Result.DENY);
				event.setUseItemInHand(Event.Result.DENY);
				heldSocket.remove(player.getName());
				player.updateInventory();
				return;
			}
			ItemMeta im = itemInHand.getItemMeta();
			if (!im.hasLore()) {
				sendMessage(player, "messages.cannot-use", new String[][]{});
				event.setCancelled(true);
				event.setUseInteractedBlock(Event.Result.DENY);
				event.setUseItemInHand(Event.Result.DENY);
				heldSocket.remove(player.getName());
				player.updateInventory();
				return;
			}
			List<String> lore = new ArrayList<>(im.getLore());
			String socketString = getSockettedItemSocket().replace('&', '\u00A7').replace("\u00A7\u00A7", "&");
			int index = indexOfStripColor(lore, socketString);
			if (index < 0) {
				sendMessage(player, "messages.cannot-use", new String[][]{});
				event.setCancelled(true);
				event.setUseInteractedBlock(Event.Result.DENY);
				event.setUseItemInHand(Event.Result.DENY);
				heldSocket.remove(player.getName());
				player.updateInventory();
				return;
			}
			HeldItem heldSocket1 = heldSocket.get(player.getName());
			String socketGemType = ChatColor.stripColor(heldSocket1
					.getName());
			SocketGem socketGem = getSocketGemFromName(socketGemType);
			if (socketGem == null ||
					!socketGemTypeMatchesItemStack(socketGem, itemInHand)) {
				sendMessage(player, "messages.cannot-use", new String[][]{});
				event.setCancelled(true);
				event.setUseInteractedBlock(Event.Result.DENY);
				event.setUseItemInHand(Event.Result.DENY);
				heldSocket.remove(player.getName());
				player.updateInventory();
				return;
			}
			lore.set(index, ChatColor.GOLD + socketGem.getName());
			lore.removeAll(sockettedItemLore);
			im.setLore(lore);
			itemInHand.setItemMeta(im);
			prefixItemStack(itemInHand, socketGem);
			suffixItemStack(itemInHand, socketGem);
			loreItemStack(itemInHand, socketGem);
			enchantmentItemStack(itemInHand, socketGem);
			if (player.getInventory().contains(heldSocket1.getItemStack())) {
				int indexOfItem = player.getInventory().first(heldSocket1.getItemStack());
				ItemStack inInventory = player.getInventory().getItem(indexOfItem);
				inInventory.setAmount(inInventory.getAmount() - 1);
				player.getInventory().setItem(indexOfItem, inInventory);
				player.updateInventory();
			} else {
				sendMessage(player, "messages.do-not-have", new String[][]{});
				event.setCancelled(true);
				event.setUseInteractedBlock(Event.Result.DENY);
				event.setUseItemInHand(Event.Result.DENY);
				heldSocket.remove(player.getName());
				player.updateInventory();
				return;
			}
			player.setItemInHand(itemInHand);
			sendMessage(player, "messages.success", new String[][]{});
			event.setUseInteractedBlock(Event.Result.DENY);
			event.setUseItemInHand(Event.Result.DENY);
			heldSocket.remove(player.getName());
			player.updateInventory();
		} else {
			sendMessage(player, "messages.cannot-use", new String[][]{});
			event.setCancelled(true);
			event.setUseInteractedBlock(Event.Result.DENY);
			event.setUseItemInHand(Event.Result.DENY);
			heldSocket.remove(player.getName());
			player.updateInventory();
		}
	}

	public int indexOfStripColor(List<String> list, String string) {
		String[] array = list.toArray(new String[list.size()]);
		for (int i = 0; i < array.length; i++) {
			if (ChatColor.stripColor(array[i]).equalsIgnoreCase(ChatColor.stripColor(string))) {
				return i;
			}
		}
		return -1;
	}

	public int indexOfStripColor(String[] array, String string) {
		for (int i = 0; i < array.length; i++) {
			if (ChatColor.stripColor(array[i]).equalsIgnoreCase(ChatColor.stripColor(string))) {
				return i;
			}
		}
		return -1;
	}

	public ItemStack loreItemStack(ItemStack itemStack, SocketGem socketGem) {
		ItemMeta im;
		if (itemStack.hasItemMeta()) {
			im = itemStack.getItemMeta();
		} else {
			im = Bukkit.getItemFactory().getItemMeta(itemStack.getType());
		}
		if (!im.hasLore()) {
			im.setLore(new ArrayList<String>());
		}
		List<String> lore = new ArrayList<String>(im.getLore());
		if (lore.containsAll(socketGem.getLore())) {
			return itemStack;
		}
		for (String s : socketGem.getLore()) {
			lore.add(s.replace('&', '\u00A7').replace("\u00A7\u00A7", "&"));
		}
		im.setLore(lore);
		itemStack.setItemMeta(im);
		return itemStack;
	}

	public ItemStack enchantmentItemStack(ItemStack itemStack, SocketGem socketGem) {
		if (itemStack == null || socketGem == null) {
			return itemStack;
		}
		Map<Enchantment, Integer> itemStackEnchantments =
				new HashMap<Enchantment, Integer>(itemStack.getEnchantments());
		for (Map.Entry<Enchantment, Integer> entry : socketGem.getEnchantments().entrySet()) {
			if (itemStackEnchantments.containsKey(entry.getKey())) {
				itemStack.removeEnchantment(entry.getKey());
				int level = Math.abs(itemStackEnchantments.get(entry.getKey()) + entry.getValue());
				if (level <= 0) {
					continue;
				}
				itemStack.addUnsafeEnchantment(entry.getKey(), level);
			} else {
				itemStack.addUnsafeEnchantment(entry.getKey(),
						entry.getValue() <= 0 ? Math.abs(entry.getValue()) == 0 ? 1 : Math.abs(entry.getValue()) :
								entry.getValue());
			}
		}
		return itemStack;
	}

	public ItemStack suffixItemStack(ItemStack itemStack, SocketGem socketGem) {
		ItemMeta im;
		if (!itemStack.hasItemMeta()) {
			im = Bukkit.getItemFactory().getItemMeta(itemStack.getType());
		} else {
			im = itemStack.getItemMeta();
		}
		String name = im.getDisplayName();
		if (name == null) {
			return itemStack;
		}
		ChatColor beginColor = findColor(name);
		String lastColors = ChatColor.getLastColors(name);
		if (beginColor == null) {
			beginColor = ChatColor.WHITE;
		}
		String suffix = socketGem.getSuffix();
		if (suffix == null || suffix.equalsIgnoreCase("")) {
			return itemStack;
		}
		if (isPreventMultipleChangesFromSockets() &&
				ChatColor.stripColor(name).contains(suffix) ||
				containsAnyFromList(ChatColor.stripColor(name), socketGemSuffixes)) {
			return itemStack;
		}
		im.setDisplayName(name + " " + beginColor + suffix + lastColors);
		itemStack.setItemMeta(im);
		return itemStack;
	}

	public ItemStack prefixItemStack(ItemStack itemStack, SocketGem socketGem) {
		ItemMeta im;
		if (itemStack.hasItemMeta()) {
			im = itemStack.getItemMeta();
		} else {
			im = Bukkit.getItemFactory().getItemMeta(itemStack.getType());
		}
		String name = im.getDisplayName();
		if (name == null) {
			return itemStack;
		}
		ChatColor beginColor = findColor(name);
		if (beginColor == null) {
			beginColor = ChatColor.WHITE;
		}
		String prefix = socketGem.getPrefix();
		if (prefix == null || prefix.equalsIgnoreCase("")) {
			return itemStack;
		}
		if (isPreventMultipleChangesFromSockets() &&
				ChatColor.stripColor(name).contains(prefix) ||
				containsAnyFromList(ChatColor.stripColor(name), socketGemPrefixes)) {
			return itemStack;
		}
		im.setDisplayName(beginColor + prefix + " " + name);
		itemStack.setItemMeta(im);
		return itemStack;
	}

	public ChatColor findColor(final String s) {
		char[] c = s.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == (char) 167 && i + 1 < c.length) {
				return ChatColor.getByChar(c[i + 1]);
			}
		}
		return null;
	}

	public boolean containsAnyFromList(String string, List<String> list) {
		for (String s : list) {
			if (string.toUpperCase().contains(s.toUpperCase())) {
				return true;
			}
		}
		return false;
	}

	public void applyEffects(LivingEntity attacker, LivingEntity defender) {
		if (attacker == null || defender == null) {
			return;
		}
		// handle attacker
		if (isUseAttackerArmorEquipped()) {
			for (ItemStack attackersItem : attacker.getEquipment().getArmorContents()) {
				if (attackersItem == null) {
					continue;
				}
				List<SocketGem> attackerSocketGems = getSocketGems(attackersItem);
				if (attackerSocketGems != null && !attackerSocketGems.isEmpty()) {
					for (SocketGem sg : attackerSocketGems) {
						if (sg == null) {
							continue;
						}
						if (sg.getGemType() != GemType.TOOL && sg.getGemType() != GemType.ANY) {
							continue;
						}
						for (SocketPotionEffect se : sg.getSocketPotionEffects()) {
							if (se == null) {
								continue;
							}
							switch (se.getEffectTarget()) {
								case SELF:
									se.apply(attacker);
									break;
								case OTHER:
									se.apply(defender);
									break;
								case AREA:
									for (Entity e : attacker.getNearbyEntities(se.getRadius(), se.getRadius(),
											se.getRadius())) {
										if (!(e instanceof LivingEntity)) {
											continue;
										}
										if (!se.isAffectsTarget() && e.equals(defender)) {
											continue;
										}
										se.apply((LivingEntity) e);
									}
									if (se.isAffectsWielder()) {
										se.apply(attacker);
									}
									break;
								default:
									break;
							}
						}
						for (SocketParticleEffect se : sg.getSocketParticleEffects()) {
							if (se == null) {
								continue;
							}
							switch (se.getEffectTarget()) {
								case SELF:
									se.apply(attacker);
									break;
								case OTHER:
									se.apply(defender);
									break;
								case AREA:
									for (Entity e : attacker.getNearbyEntities(se.getRadius(), se.getRadius(),
											se.getRadius())) {
										if (!(e instanceof LivingEntity)) {
											continue;
										}
										if (!se.isAffectsTarget() && e.equals(defender)) {
											continue;
										}
										se.apply((LivingEntity) e);
									}
									if (se.isAffectsWielder()) {
										se.apply(attacker);
									}
									break;
								default:
									break;
							}
						}
					}
				}
			}
		}
		if (isUseAttackerItemInHand() && attacker.getEquipment().getItemInHand() != null) {
			List<SocketGem> attackerSocketGems = getSocketGems(attacker.getEquipment().getItemInHand());
			if (attackerSocketGems != null && !attackerSocketGems.isEmpty()) {
				for (SocketGem sg : attackerSocketGems) {
					if (sg == null) {
						continue;
					}
					if (sg.getGemType() != GemType.TOOL && sg.getGemType() != GemType.ANY) {
						continue;
					}
					for (SocketPotionEffect se : sg.getSocketPotionEffects()) {
						if (se == null) {
							continue;
						}
						switch (se.getEffectTarget()) {
							case SELF:
								se.apply(attacker);
								break;
							case OTHER:
								se.apply(defender);
								break;
							case AREA:
								for (Entity e : attacker.getNearbyEntities(se.getRadius(), se.getRadius(),
										se.getRadius())) {
									if (!(e instanceof LivingEntity)) {
										continue;
									}
									if (!se.isAffectsTarget() && e.equals(defender)) {
										continue;
									}
									se.apply((LivingEntity) e);
								}
								if (se.isAffectsWielder()) {
									se.apply(attacker);
								}
								break;
							default:
								break;
						}
					}
					for (SocketParticleEffect se : sg.getSocketParticleEffects()) {
						if (se == null) {
							continue;
						}
						switch (se.getEffectTarget()) {
							case SELF:
								se.apply(attacker);
								break;
							case OTHER:
								se.apply(defender);
								break;
							case AREA:
								for (Entity e : attacker.getNearbyEntities(se.getRadius(), se.getRadius(),
										se.getRadius())) {
									if (!(e instanceof LivingEntity)) {
										continue;
									}
									if (!se.isAffectsTarget() && e.equals(defender)) {
										continue;
									}
									se.apply((LivingEntity) e);
								}
								if (se.isAffectsWielder()) {
									se.apply(attacker);
								}
								break;
							default:
								break;
						}
					}
				}
			}
		}
		// handle defender
		if (isUseDefenderArmorEquipped()) {
			for (ItemStack defenderItem : defender.getEquipment().getArmorContents()) {
				if (defenderItem == null) {
					continue;
				}
				List<SocketGem> defenderSocketGems = getSocketGems(defenderItem);
				for (SocketGem sg : defenderSocketGems) {
					if (sg.getGemType() != GemType.ARMOR && sg.getGemType() != GemType.ANY) {
						continue;
					}
					for (SocketPotionEffect se : sg.getSocketPotionEffects()) {
						if (se == null) {
							continue;
						}
						switch (se.getEffectTarget()) {
							case SELF:
								se.apply(defender);
								break;
							case OTHER:
								se.apply(attacker);
								break;
							case AREA:
								for (Entity e : defender.getNearbyEntities(se.getRadius(), se.getRadius(),
										se.getRadius())) {
									if (!(e instanceof LivingEntity)) {
										continue;
									}
									if (!se.isAffectsTarget() && e.equals(attacker)) {
										continue;
									}
									se.apply((LivingEntity) e);
								}
								if (se.isAffectsWielder()) {
									se.apply(defender);
								}
								break;
							default:
								break;
						}
					}
					for (SocketParticleEffect se : sg.getSocketParticleEffects()) {
						if (se == null) {
							continue;
						}
						switch (se.getEffectTarget()) {
							case SELF:
								se.apply(defender);
								break;
							case OTHER:
								se.apply(attacker);
								break;
							case AREA:
								for (Entity e : defender.getNearbyEntities(se.getRadius(), se.getRadius(),
										se.getRadius())) {
									if (!(e instanceof LivingEntity)) {
										continue;
									}
									if (!se.isAffectsTarget() && e.equals(attacker)) {
										continue;
									}
									se.apply((LivingEntity) e);
								}
								if (se.isAffectsWielder()) {
									se.apply(defender);
								}
								break;
							default:
								break;
						}
					}
				}
			}
		}
		if (isUseDefenderItemInHand() && defender.getEquipment().getItemInHand() != null) {
			List<SocketGem> defenderSocketGems = getSocketGems(defender.getEquipment().getItemInHand());
			if (defenderSocketGems != null && !defenderSocketGems.isEmpty()) {
				for (SocketGem sg : defenderSocketGems) {
					if (sg.getGemType() != GemType.ARMOR && sg.getGemType() != GemType.ANY) {
						continue;
					}
					for (SocketPotionEffect se : sg.getSocketPotionEffects()) {
						if (se == null) {
							continue;
						}
						switch (se.getEffectTarget()) {
							case SELF:
								se.apply(defender);
								break;
							case OTHER:
								se.apply(attacker);
								break;
							case AREA:
								for (Entity e : defender.getNearbyEntities(se.getRadius(), se.getRadius(),
										se.getRadius())) {
									if (!(e instanceof LivingEntity)) {
										continue;
									}
									if (!se.isAffectsTarget() && e.equals(attacker)) {
										continue;
									}
									se.apply((LivingEntity) e);
								}
								if (se.isAffectsWielder()) {
									se.apply(defender);
								}
								break;
							default:
								break;
						}
					}
					for (SocketParticleEffect se : sg.getSocketParticleEffects()) {
						if (se == null) {
							continue;
						}
						switch (se.getEffectTarget()) {
							case SELF:
								se.apply(defender);
								break;
							case OTHER:
								se.apply(attacker);
								break;
							case AREA:
								for (Entity e : defender.getNearbyEntities(se.getRadius(), se.getRadius(),
										se.getRadius())) {
									if (!(e instanceof LivingEntity)) {
										continue;
									}
									if (!se.isAffectsTarget() && e.equals(attacker)) {
										continue;
									}
									se.apply((LivingEntity) e);
								}
								if (se.isAffectsWielder()) {
									se.apply(defender);
								}
								break;
							default:
								break;
						}
					}
				}
			}
		}
	}

	public boolean isUseAttackerItemInHand() {
		return useAttackerItemInHand;
	}

	public boolean isUseAttackerArmorEquipped() {
		return useAttackerArmorEquipped;
	}

	public boolean isUseDefenderItemInHand() {
		return useDefenderItemInHand;
	}

	public boolean isUseDefenderArmorEquipped() {
		return useDefenderArmorEquipped;
	}

	public List<SocketGem> getSocketGems(ItemStack itemStack) {
		List<SocketGem> socketGemList = new ArrayList<SocketGem>();
		ItemMeta im;
		if (itemStack.hasItemMeta()) {
			im = itemStack.getItemMeta();
		} else {
			return socketGemList;
		}
		List<String> lore = im.getLore();
		if (lore == null) {
			return socketGemList;
		}
		for (String s : lore) {
			SocketGem sg = getSocketGemFromName(ChatColor.stripColor(s));
			if (sg == null) {
				continue;
			}
			socketGemList.add(sg);
		}
		return socketGemList;
	}

	public void runCommands(LivingEntity attacker, LivingEntity defender) {
		if (attacker == null || defender == null) {
			return;
		}
		if (attacker instanceof Player) {
			if (isUseAttackerArmorEquipped()) {
				for (ItemStack attackersItem : attacker.getEquipment().getArmorContents()) {
					if (attackersItem == null) {
						continue;
					}
					List<SocketGem> attackerSocketGems = getSocketGems(attackersItem);
					if (attackerSocketGems != null && !attackerSocketGems.isEmpty()) {
						for (SocketGem sg : attackerSocketGems) {
							if (sg == null) {
								continue;
							}
							for (SocketCommand sc : sg.getCommands()) {
								if (sc.getRunner() == SocketCommandRunner.CONSOLE) {
									String command = sc.getCommand();
									if (command.contains("%wielder%") || command.contains("%target%")) {
										if (command.contains("%wielder%")) {
											command = command.replace("%wielder%", ((Player) attacker).getName());
										}
										if (command.contains("%target%")) {
											if (defender instanceof Player) {
												command = command.replace("%target%", ((Player) defender).getName());
											} else {
												continue;
											}
										}
									}
									Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
								} else {
									String command = sc.getCommand();
									if (command.contains("%wielder%") || command.contains("%target%")) {
										if (command.contains("%wielder%")) {
											command = command.replace("%wielder%", ((Player) attacker).getName());
										}
										if (command.contains("%target%")) {
											if (defender instanceof Player) {
												command = command.replace("%target%", ((Player) defender).getName());
											} else {
												continue;
											}
										}
									}
									((Player) attacker).chat("/" + command);
								}
							}
						}
					}
				}
			}
			if (isUseAttackerItemInHand() && attacker.getEquipment().getItemInHand() != null) {
				List<SocketGem> attackerSocketGems = getSocketGems(attacker.getEquipment().getItemInHand());
				if (attackerSocketGems != null && !attackerSocketGems.isEmpty()) {
					for (SocketGem sg : attackerSocketGems) {
						if (sg == null) {
							continue;
						}
						for (SocketCommand sc : sg.getCommands()) {
							if (sc.getRunner() == SocketCommandRunner.CONSOLE) {
								String command = sc.getCommand();
								if (command.contains("%wielder%") || command.contains("%target%")) {
									if (command.contains("%wielder%")) {
										command = command.replace("%wielder%", ((Player) attacker).getName());
									}
									if (command.contains("%target%")) {
										if (defender instanceof Player) {
											command = command.replace("%target%", ((Player) defender).getName());
										} else {
											continue;
										}
									}
								}
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
							} else {
								String command = sc.getCommand();
								if (command.contains("%wielder%") || command.contains("%target%")) {
									if (command.contains("%wielder%")) {
										command = command.replace("%wielder%", ((Player) attacker).getName());
									}
									if (command.contains("%target%")) {
										if (defender instanceof Player) {
											command = command.replace("%target%", ((Player) defender).getName());
										} else {
											continue;
										}
									}
								}
								((Player) attacker).chat("/" + command);
							}
						}
					}
				}
			}
		}
		if (defender instanceof Player) {
			if (isUseDefenderArmorEquipped()) {
				for (ItemStack defendersItem : defender.getEquipment().getArmorContents()) {
					if (defendersItem == null) {
						continue;
					}
					List<SocketGem> defenderSocketGems = getSocketGems(defendersItem);
					if (defenderSocketGems != null && !defenderSocketGems.isEmpty()) {
						for (SocketGem sg : defenderSocketGems) {
							if (sg == null) {
								continue;
							}
							for (SocketCommand sc : sg.getCommands()) {
								if (sc.getRunner() == SocketCommandRunner.CONSOLE) {
									String command = sc.getCommand();
									if (command.contains("%wielder%") || command.contains("%target%")) {
										if (command.contains("%wielder%")) {
											command = command.replace("%wielder%", ((Player) defender).getName());
										}
										if (command.contains("%target%")) {
											if (attacker instanceof Player) {
												command = command.replace("%target%", ((Player) attacker).getName());
											} else {
												continue;
											}
										}
									}
									Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
								} else {
									String command = sc.getCommand();
									if (command.contains("%wielder%") || command.contains("%target%")) {
										if (command.contains("%wielder%")) {
											command = command.replace("%wielder%", ((Player) defender).getName());
										}
										if (command.contains("%target%")) {
											if (attacker instanceof Player) {
												command = command.replace("%target%", ((Player) attacker).getName());
											} else {
												continue;
											}
										}
									}
									((Player) defender).chat("/" + command);
								}
							}
						}
					}
				}
			}
			if (isUseDefenderItemInHand() && defender.getEquipment().getItemInHand() != null) {
				List<SocketGem> defenderSocketGems = getSocketGems(defender.getEquipment().getItemInHand());
				if (defenderSocketGems != null && !defenderSocketGems.isEmpty()) {
					for (SocketGem sg : defenderSocketGems) {
						if (sg == null) {
							continue;
						}
						for (SocketCommand sc : sg.getCommands()) {
							if (sc.getRunner() == SocketCommandRunner.CONSOLE) {
								String command = sc.getCommand();
								if (command.contains("%wielder%") || command.contains("%target%")) {
									if (command.contains("%wielder%")) {
										command = command.replace("%wielder%", ((Player) defender).getName());
									}
									if (command.contains("%target%")) {
										if (attacker instanceof Player) {
											command = command.replace("%target%", ((Player) attacker).getName());
										} else {
											continue;
										}
									}
								}
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
							} else {
								String command = sc.getCommand();
								if (command.contains("%wielder%") || command.contains("%target%")) {
									if (command.contains("%wielder%")) {
										command = command.replace("%wielder%", ((Player) defender).getName());
									}
									if (command.contains("%target%")) {
										if (attacker instanceof Player) {
											command = command.replace("%target%", ((Player) attacker).getName());
										} else {
											continue;
										}
									}
								}
								((Player) defender).chat("/" + command);
							}
						}
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
		if (event.isCancelled()) {
			return;
		}
		Entity e = event.getEntity();
		Entity d = event.getDamager();
		if (!(e instanceof LivingEntity)) {
			return;
		}
		LivingEntity lee = (LivingEntity) e;
		LivingEntity led;
		if (d instanceof LivingEntity) {
			led = (LivingEntity) d;
		} else if (d instanceof Projectile) {
			led = ((Projectile) d).getShooter();
		} else {
			return;
		}
		applyEffects(led, lee);
		runCommands(led, lee);
	}

	@Command(identifier = "mythicdropssockets gem", description = "Gives MythicDrops gems",
			permissions = "mythicdrops.command.gem")
	@Flags(identifier = {"a", "g"}, description = {"Amount to spawn", "Socket Gem to spawn"})
	public void customSubcommand(CommandSender sender, @Arg(name = "player", def = "self") String playerName,
								 @Arg(name = "amount", def = "1")
								 @FlagArg("a") int amount, @Arg(name = "item", def = "*") @FlagArg("g") String
										 itemName) {
		Player player;
		if (playerName.equalsIgnoreCase("self")) {
			if (sender instanceof Player) {
				player = (Player) sender;
			} else {
				sendMessage(sender, "command.no-access", new String[][]{});
				return;
			}
		} else {
			player = Bukkit.getPlayer(playerName);
		}
		if (player == null) {
			sendMessage(sender, "command.player-does-not-exist", new String[][]{});
			return;
		}
		SocketGem socketGem = null;
		if (!itemName.equalsIgnoreCase("*")) {
			try {
				socketGem = getSocketGemFromName(itemName);
			} catch (NullPointerException e) {
				e.printStackTrace();
				sendMessage(sender, "command.socket-gem-does-not-exist", new String[][]{});
				return;
			}
		}
		int amountGiven = 0;
		for (int i = 0; i < amount; i++) {
			try {
				ItemStack itemStack;
				if (socketGem == null) {
					itemStack = new SocketItem(getRandomSocketGemMaterial(), getRandomSocketGemWithChance());
				} else {
					itemStack = new SocketItem(getRandomSocketGemMaterial(), socketGem);
				}
				itemStack.setDurability((short) 0);
				player.getInventory().addItem(itemStack);
				amountGiven++;
			} catch (Exception ignored) {
				ignored.printStackTrace();
			}
		}
		sendMessage(player, "command.give-gem-receiver",
				new String[][]{{"%amount%", String.valueOf(amountGiven)}});
		sendMessage(sender, "command.give-gem-sender", new String[][]{{"%amount%",
				String.valueOf(amountGiven)}, {"%receiver%", player.getName()}});
	}

	public void sendMessage(CommandSender reciever, String path, String[][] arguments) {
		String message = getFormattedLanguageString(path, arguments);
		if (message == null) {
			return;
		}
		reciever.sendMessage(message);
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

	public SocketGem getRandomSocketGemWithChance() {
		if (socketGemMap == null || socketGemMap.isEmpty()) {
			return null;
		}
		Set<SocketGem> zeroChanceSocketGems = new HashSet<>();
		while (zeroChanceSocketGems.size() != socketGemMap.size()) {
			for (SocketGem socket : socketGemMap.values()) {
				if (socket.getChance() <= 0.0D) {
					zeroChanceSocketGems.add(socket);
					continue;
				}
				if (RandomUtils.nextDouble() < socket.getChance()) {
					return socket;
				}
			}
		}
		return null;
	}

	public MaterialData getRandomSocketGemMaterial() {
		if (getSocketGemMaterialIds() == null || getSocketGemMaterialIds().isEmpty()) {
			return null;
		}
		return getSocketGemMaterialIds().get(RandomUtils.nextInt(getSocketGemMaterialIds().size()));
	}

	public List<MaterialData> getSocketGemMaterialIds() {
		return socketGemMaterialIds;
	}

	public SocketGem getSocketGemFromName(String name) {
		for (SocketGem sg : socketGemMap.values()) {
			if (sg.getName().equalsIgnoreCase(name)) {
				return sg;
			}
		}
		return null;
	}

	private class HeldItem {

		private final String name;
		private final ItemStack itemStack;

		public HeldItem(String name, ItemStack itemStack) {
			this.name = name;
			this.itemStack = itemStack;
		}

		public String getName() {
			return name;
		}

		public ItemStack getItemStack() {
			return itemStack;
		}

	}

}
