package net.nunnerycode.bukkit.mythicdrops.identification;

import com.conventnunnery.libraries.config.CommentedConventYamlConfiguration;
import com.conventnunnery.libraries.config.ConventYamlConfiguration;
import net.nunnerycode.bukkit.mythicdrops.api.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.events.MythicDropsCancellableEvent;
import net.nunnerycode.bukkit.mythicdrops.api.items.MythicTome;
import net.nunnerycode.bukkit.mythicdrops.api.items.NonrepairableItemStack;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import net.nunnerycode.bukkit.mythicdrops.items.TierMap;
import net.nunnerycode.bukkit.mythicdrops.utils.ItemUtil;
import net.nunnerycode.java.libraries.cannonball.DebugPrinter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import se.ranzdo.bukkit.methodcommand.Arg;
import se.ranzdo.bukkit.methodcommand.Command;
import se.ranzdo.bukkit.methodcommand.FlagArg;
import se.ranzdo.bukkit.methodcommand.Flags;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
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

	public static MythicDropsIdentification getInstance() {
		return _INSTANCE;
	}

	@Override
	public void onDisable() {
		debugPrinter.debug(Level.INFO, "disabled", "", "", "");
	}

	@Override
	public void onEnable() {
		_INSTANCE = this;
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

		unpackConfigurationFiles(new String[]{"config.yml"}, false);

		configYAML = new CommentedConventYamlConfiguration(new File(getDataFolder(), "config.yml"),
				YamlConfiguration.loadConfiguration(getResource("config.yml")).getString("version"));
		configYAML.options().backupOnUpdate(true);
		configYAML.options().updateOnLoad(true);
		configYAML.load();



		mythicDrops.getCommandHandler().registerCommands(this);

		debugPrinter.debug(Level.INFO, "enabled");
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

	@Command(identifier = "mythicdrops unidentified", description = "Gives Unidentified Item",
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
			Tier t = TierMap.getInstance().getRandomWithChance(player.getWorld().getName());
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

	@Command(identifier = "mythicdrops tome", description = "Gives Identity Tome",
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

}
