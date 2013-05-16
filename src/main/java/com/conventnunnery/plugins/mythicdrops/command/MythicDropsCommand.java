/*
 * Copyright (c) 2013. ToppleTheNun
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.conventnunnery.plugins.mythicdrops.command;

import com.conventnunnery.plugins.conventlib.containers.DecimalRangeContainer;
import com.conventnunnery.plugins.conventlib.utils.NumberUtils;
import com.conventnunnery.plugins.conventlib.utils.RandomUtils;
import com.conventnunnery.plugins.mythicdrops.MythicDrops;
import com.conventnunnery.plugins.mythicdrops.managers.DropManager;
import com.conventnunnery.plugins.mythicdrops.objects.CustomItem;
import com.conventnunnery.plugins.mythicdrops.objects.SocketGem;
import com.conventnunnery.plugins.mythicdrops.objects.SocketItem;
import com.conventnunnery.plugins.mythicdrops.objects.Tier;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class MythicDropsCommand implements CommandExecutor {

	private final MythicDrops plugin;

	public MythicDropsCommand(MythicDrops plugin) {
		this.plugin = plugin;
	}

	/**
	 * @return the plugin
	 */
	public MythicDrops getPlugin() {
		return plugin;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command,
	                         String commandLabel, String[] args) {
		switch (args.length) {
			case 1:
				if (args[0].equalsIgnoreCase("spawn")) {
					if (sender.hasPermission("Mythicdrops.command.spawn")) {
						if (!(sender instanceof Player)) {
							getPlugin().getLanguageManager().sendMessage(sender, "command.only-players");
							break;
						}
						Player player = (Player) sender;
						ItemStack is = getPlugin().getDropManager().constructItemStack(
								DropManager.GenerationReason.COMMAND);
						player.getInventory().addItem(is);
						getPlugin().getLanguageManager().sendMessage(sender, "command.spawn-random",
								new String[][]{{"%amount%", "1"}});
						player.updateInventory();
						break;
					} else {
						getPlugin().getLanguageManager().sendMessage(sender, "command.no-access");
						break;
					}
				} else if (args[0].equalsIgnoreCase("custom")) {
					if (sender.hasPermission("Mythicdrops.command.spawn")) {
						if (!(sender instanceof Player)) {
							getPlugin().getLanguageManager().sendMessage(sender, "command.only-players");
							break;
						}
						Player player = (Player) sender;
						CustomItem ci = getPlugin().getDropManager().randomCustomItemWithChance();
						if (ci == null) {
							getPlugin().getLanguageManager().sendMessage(player, "spawn-custom-failure",
									new String[][]{{"%amount%", "1"}});
							break;
						}
						ItemStack is = ci.toItemStack();
						if (is != null) {
							player.getInventory().addItem(is);
							player.updateInventory();
							getPlugin().getLanguageManager().sendMessage(sender, "command.spawn-custom",
									new String[][]{{"%amount%", "1"}});
						} else {
							getPlugin().getLanguageManager().sendMessage(player, "spawn-custom-failure",
									new String[][]{{"%amount%", "1"}});
						}
						break;
					} else {
						getPlugin().getLanguageManager().sendMessage(sender, "command.no-access");
						break;
					}
				} else if (args[0].equalsIgnoreCase("reload")) {
					if (sender.hasPermission("Mythicdrops.command.reload")) {
						getPlugin().getConfigurationManager().loadConfig();
						getPlugin().getPluginSettings().loadPluginSettings();
						getPlugin().getTierBuilder().build();
						getPlugin().getCustomBuilder().build();
						getPlugin().getSocketGemBuilder().build();
						getPlugin().getPluginSettings().debugSettings();
						getPlugin().getNameManager().debugPrefixesAndSuffixes();
						getPlugin().getTierManager().debugTiers();
						getPlugin().getDropManager().debugCustomItems();
						getPlugin().getSocketGemManager().debugSocketGems();
						getPlugin().getLanguageManager().sendMessage(sender, "command.reload-config");
						break;
					} else {
						getPlugin().getLanguageManager().sendMessage(sender, "command.no-access");
						break;
					}
				} else {
					showHelp(sender);
					break;
				}
			case 2:
				if (args[0].equalsIgnoreCase("spawn")) {
					if (sender.hasPermission("Mythicdrops.command.spawn")) {
						if (!(sender instanceof Player)) {
							getPlugin().getLanguageManager().sendMessage(sender, "command.only-players");
							break;
						}
						Player player = (Player) sender;
						if (args[1].equalsIgnoreCase("*")) {
							player.getInventory().addItem(
									getPlugin().getDropManager()
											.constructItemStack(DropManager.GenerationReason.COMMAND));
							player.updateInventory();
							getPlugin().getLanguageManager().sendMessage(sender, "command.spawn-random",
									new String[][]{{"%amount%", "1"}});
							break;
						} else if (args[1].equalsIgnoreCase("gem")) {
							SocketGem socketGem = getPlugin().getSocketGemManager().getRandomSocketGemWithChance();
							MaterialData materialData = getPlugin().getSocketGemManager().getRandomSocketGemMaterial();
							if (socketGem == null || materialData == null) {
								getPlugin().getLanguageManager().sendMessage(sender, "command.spawn-gem-failure",
										new String[][]{{"%amount%", "1"}});
							} else {
								player.getInventory().addItem(
										new SocketItem(materialData, socketGem));
								player.updateInventory();
								getPlugin().getLanguageManager().sendMessage(sender, "command.spawn-gem",
										new String[][]{{"%amount%", "1"}});
							}
							break;
						} else {
							Tier t = getPlugin().getTierManager().getTierFromName(
									args[1]);
							if (t == null) {
								getPlugin().getLanguageManager().sendMessage(sender, "command.tier-does-not-exist");
								break;
							}
							player.getInventory().addItem(
									getPlugin().getDropManager()
											.constructItemStack(t, DropManager.GenerationReason.COMMAND));
							player.updateInventory();
							getPlugin().getLanguageManager().sendMessage(sender, "command.spawn-random",
									new String[][]{{"%amount%", "1"}});
							break;
						}
					} else {
						getPlugin().getLanguageManager().sendMessage(sender, "command.no-access");
						break;
					}
				} else if (args[0].equalsIgnoreCase("custom")) {
					if (sender.hasPermission("Mythicdrops.command.custom")) {
						Player player;
						if (args[1].equalsIgnoreCase("self")) {
							if (!(sender instanceof Player)) {
								getPlugin().getLanguageManager().sendMessage(sender, "command.only-players");
								break;
							}
							player = (Player) sender;
						} else {
							player = Bukkit.getPlayer(args[1]);
						}
						if (player == null) {
							getPlugin().getLanguageManager().sendMessage(sender, "command.player-does-not-exist");
							break;
						}
						CustomItem ci = getPlugin().getDropManager().randomCustomItemWithChance();
						if (ci == null) {
							getPlugin().getLanguageManager().sendMessage(sender, "command.spawn-custom-sender-failure",
									new String[][]{{"%receiver%", player.getName()}, {"%amount%", "1"}});
							break;
						}
						ItemStack is = ci.toItemStack();
						if (is != null) {
							player.getInventory().addItem(is);
							player.updateInventory();
							getPlugin().getLanguageManager().sendMessage(sender, "command.spawn-custom-receiver",
									new String[][]{{"%amount%", "1"}});
							getPlugin().getLanguageManager().sendMessage(sender, "command.spawn-custom-sender",
									new String[][]{{"%receiver%", player.getName()}, {"%amount%", "1"}});
							break;
						}
						getPlugin().getLanguageManager().sendMessage(sender, "command.spawn-custom-sender-failure",
								new String[][]{{"%receiver%", player.getName()}, {"%amount%", "1"}});
						break;
					} else {
						getPlugin().getLanguageManager().sendMessage(sender, "command.no-access");
						break;
					}
				} else if (args[0].equalsIgnoreCase("give")) {
					if (sender.hasPermission("Mythicdrops.command.give")) {
						Player player = Bukkit.getPlayer(args[1]);
						if (player == null || !player.isOnline()) {
							getPlugin().getLanguageManager().sendMessage(sender, "command.player-does-not-exist");
							break;
						}
						player.getInventory().addItem(
								getPlugin().getDropManager().constructItemStack(
										DropManager.GenerationReason.COMMAND));
						player.updateInventory();
						getPlugin().getLanguageManager().sendMessage(player, "command.give-random-receiver",
								new String[][]{{"%amount%", "1"}});
						getPlugin().getLanguageManager().sendMessage(sender, "command.give-random-sender",
								new String[][]{{"%receiver%", player.getName()}, {"%amount%", "1"}});
						break;
					} else {
						getPlugin().getLanguageManager().sendMessage(sender, "command.no-access");
						break;
					}
				} else {
					showHelp(sender);
					break;
				}
			case 3:
				if (args[0].equalsIgnoreCase("spawn")) {
					if (sender.hasPermission("Mythicdrops.command.spawn")) {
						if (!(sender instanceof Player)) {
							getPlugin().getLanguageManager().sendMessage(sender, "command.only-players");
							break;
						}
						Player player = (Player) sender;
						if (args[1].equalsIgnoreCase("*")) {
							int amt = NumberUtils.getInt(args[2], 1);
							for (int i = 0; i < amt; i++) {
								player.getInventory().addItem(
										getPlugin().getDropManager()
												.constructItemStack(DropManager.GenerationReason.COMMAND));
							}
							getPlugin().getLanguageManager().sendMessage(sender, "command.spawn-random",
									new String[][]{{"%amount%", String.valueOf(amt)}});
							player.updateInventory();
							break;
						} else if (args[1].equalsIgnoreCase("gem")) {
							int amt = NumberUtils.getInt(args[2], 1);
							for (int i = 0; i < amt; i++) {
								SocketGem socketGem = getPlugin().getSocketGemManager().getRandomSocketGemWithChance();
								MaterialData materialData =
										getPlugin().getSocketGemManager().getRandomSocketGemMaterial();
								if (socketGem != null && materialData != null) {
									player.getInventory().addItem(
											new SocketItem(materialData, socketGem));
								}
							}
							player.updateInventory();
							getPlugin().getLanguageManager()
									.sendMessage(sender, "command.spawn-gem",
											new String[][]{{"%amount%", String.valueOf(amt)}});
							break;
						} else {
							Tier t = getPlugin().getTierManager().getTierFromName(
									args[1]);
							if (t == null) {
								getPlugin().getLanguageManager().sendMessage(sender, "command.tier-does-not-exist");
								break;
							}
							int amt = NumberUtils.getInt(args[2], 1);
							for (int i = 0; i < amt; i++) {
								player.getInventory().addItem(
										getPlugin().getDropManager()
												.constructItemStack(t, DropManager.GenerationReason.COMMAND));
							}
							player.updateInventory();
							getPlugin().getLanguageManager()
									.sendMessage(sender, "command.spawn-random",
											new String[][]{{"%amount%", String.valueOf(amt)}});
							break;
						}
					} else {
						getPlugin().getLanguageManager().sendMessage(sender, "command.no-access");
						break;
					}
				} else if (args[0].equalsIgnoreCase("give")) {
					if (sender.hasPermission("Mythicdrops.command.give")) {
						Player player = Bukkit.getPlayer(args[1]);
						if (player == null || !player.isOnline()) {
							getPlugin().getLanguageManager().sendMessage(sender, "command.player-does-not-exist");
							break;
						}
						if (args[2].equalsIgnoreCase("*")) {
							player.getInventory().addItem(
									getPlugin().getDropManager()
											.constructItemStack(DropManager.GenerationReason.COMMAND));
							player.updateInventory();
							getPlugin().getLanguageManager()
									.sendMessage(player, "command.give-random-receiver",
											new String[][]{{"%amount%", "1"}});
							getPlugin().getLanguageManager().sendMessage(sender, "command.give-random-sender",
									new String[][]{{"%receiver%", player.getName()}, {"%amount%", "1"}});
							break;
						} else if (args[2].equalsIgnoreCase("gem")) {
							SocketGem socketGem = getPlugin().getSocketGemManager().getRandomSocketGemWithChance();
							MaterialData materialData = getPlugin().getSocketGemManager().getRandomSocketGemMaterial();
							if (socketGem != null && materialData != null) {
								player.getInventory().addItem(
										new SocketItem(materialData, socketGem));
							}
							player.updateInventory();
							getPlugin().getLanguageManager()
									.sendMessage(player, "command.give-gem-receiver",
											new String[][]{{"%amount%", "1"}});
							getPlugin().getLanguageManager().sendMessage(sender, "command.give-gem-sender",
									new String[][]{{"%receiver%", player.getName()}, {"%amount%", "1"}});
							break;
						} else {
							Tier t = getPlugin().getTierManager().getTierFromName(
									args[2]);
							if (t == null) {
								getPlugin().getLanguageManager().sendMessage(sender, "command.tier-does-not-exist");
								break;
							}
							player.getInventory().addItem(
									getPlugin().getDropManager()
											.constructItemStack(t, DropManager.GenerationReason.COMMAND));
							player.updateInventory();
							getPlugin().getLanguageManager()
									.sendMessage(player, "command.give-random-receiver",
											new String[][]{{"%amount%", "1"}});
							getPlugin().getLanguageManager().sendMessage(sender, "command.give-random-sender",
									new String[][]{{"%receiver%", player.getName()}, {"%amount%", "1"}});
							break;
						}
					} else {
						getPlugin().getLanguageManager().sendMessage(sender, "command.no-access");
						break;
					}
				} else if (args[0].equalsIgnoreCase("custom")) {
					if (sender.hasPermission("Mythicdrops.command.spawn")) {
						Player player;
						if (args[1].equalsIgnoreCase("self")) {
							if (!(sender instanceof Player)) {
								getPlugin().getLanguageManager().sendMessage(sender, "command.only-players");
								break;
							}
							player = (Player) sender;
						} else {
							player = Bukkit.getPlayer(args[1]);
						}
						if (player == null || !player.isOnline()) {
							getPlugin().getLanguageManager().sendMessage(sender, "command.player-does-not-exist");
							break;
						}
						CustomItem ci = getPlugin().getDropManager().getCustomItemByName(args[2]);
						if (ci == null) {
							getPlugin().getLanguageManager().sendMessage(sender, "command.give-custom-sender-failure",
									new String[][]{{"%receiver%", player.getName()}, {"%amount%", "1"}});
							break;
						}
						ItemStack is = ci.toItemStack();
						if (is != null) {
							player.getInventory().addItem(is);
							player.updateInventory();
							getPlugin().getLanguageManager()
									.sendMessage(player, "command.give-custom-receiver",
											new String[][]{{"%amount%", "1"}});
							getPlugin().getLanguageManager().sendMessage(sender, "command.give-custom-sender",
									new String[][]{{"%receiver%", player.getName()}, {"%amount%", "1"}});
							break;
						}
						getPlugin().getLanguageManager().sendMessage(sender, "command.give-custom-sender-failure",
								new String[][]{{"%receiver%", player.getName()}, {"%amount%", "1"}});
						break;
					} else {
						getPlugin().getLanguageManager().sendMessage(sender, "command.no-access");
						break;
					}
				} else {
					showHelp(sender);
					break;
				}
			case 4:
				if (args[0].equalsIgnoreCase("give")) {
					if (sender.hasPermission("Mythicdrops.command.give")) {
						Player player = Bukkit.getPlayer(args[1]);
						if (player == null || !player.isOnline()) {
							getPlugin().getLanguageManager().sendMessage(sender, "command.player-does-not-exist");
							break;
						}
						if (args[2].equalsIgnoreCase("*")) {
							int amt = NumberUtils.getInt(args[3], 1);
							for (int i = 0; i < amt; i++) {
								player.getInventory().addItem(
										getPlugin().getDropManager()
												.constructItemStack(DropManager.GenerationReason.COMMAND));
							}
							player.updateInventory();
							getPlugin().getLanguageManager()
									.sendMessage(player, "command.give-random-receiver",
											new String[][]{{"%amount%", String.valueOf(amt)}});
							break;
						} else if (args[2].equalsIgnoreCase("gem")) {
							int amt = NumberUtils.getInt(args[3], 1);
							for (int i = 0; i < amt; i++) {
								SocketGem socketGem = getPlugin().getSocketGemManager().getRandomSocketGemWithChance();
								MaterialData materialData =
										getPlugin().getSocketGemManager().getRandomSocketGemMaterial();
								if (socketGem != null && materialData != null) {
									player.getInventory().addItem(
											new SocketItem(materialData, socketGem));
								}
							}
							player.updateInventory();
							getPlugin().getLanguageManager()
									.sendMessage(player, "command.give-gem-receiver",
											new String[][]{{"%amount%", String.valueOf(amt)}});
							break;
						} else {
							Tier t = getPlugin().getTierManager().getTierFromName(
									args[2]);
							if (t == null) {
								getPlugin().getLanguageManager().sendMessage(sender, "command.tier-does-not-exist");
								break;
							}
							int amt = NumberUtils.getInt(args[3], 1);
							for (int i = 0; i < amt; i++) {
								player.getInventory().addItem(
										getPlugin().getDropManager()
												.constructItemStack(t, DropManager.GenerationReason.COMMAND));
							}
							player.updateInventory();
							getPlugin().getLanguageManager()
									.sendMessage(player, "command.give-random-receiver",
											new String[][]{{"%amount%", String.valueOf(amt)}});
							getPlugin().getLanguageManager().sendMessage(sender, "command.give-random-sender",
									new String[][]{{"%receiver%", player.getName()}, {"%amount%", "1"}});
							break;
						}
					} else {
						getPlugin().getLanguageManager().sendMessage(sender, "command.no-access");
						break;
					}
				} else if (args[0].equalsIgnoreCase("custom")) {
					if (sender.hasPermission("Mythicdrops.command.spawn")) {
						Player player;
						if (args[1].equalsIgnoreCase("self")) {
							if (!(sender instanceof Player)) {
								getPlugin().getLanguageManager().sendMessage(sender, "command.only-players");
								break;
							}
							player = (Player) sender;
						} else {
							player = Bukkit.getPlayer(args[1]);
						}
						if (player == null) {
							getPlugin().getLanguageManager().sendMessage(sender, "command.player-does-not-exist");
							break;
						}
						int amt = NumberUtils.getInt(args[3], 1);
						CustomItem ci = getPlugin().getDropManager().getCustomItemByName(args[2]);
						if (ci == null) {
							getPlugin().getLanguageManager().sendMessage(sender, "command.give-custom-sender-failure",
									new String[][]{{"%receiver%", player.getName()}, {"%amount%", "1"}});
							break;
						}
						ItemStack is = ci.toItemStack();
						if (is != null) {
							for (int i = 0; i < amt; i++) {
								player.getInventory().addItem(is);
							}
							player.updateInventory();
							getPlugin().getLanguageManager()
									.sendMessage(player, "command.give-custom-receiver",
											new String[][]{{"%amount%", String.valueOf(amt)}});
							getPlugin().getLanguageManager().sendMessage(sender, "command.give-custom-sender",
									new String[][]{{"%receiver%", player.getName()}, {"%amount%", "1"}});
							break;
						}
						getPlugin().getLanguageManager().sendMessage(sender, "command.give-custom-sender-failure",
								new String[][]{{"%receiver%", player.getName()}, {"%amount%", "1"}});
						break;
					} else {
						getPlugin().getLanguageManager().sendMessage(sender, "command.no-access");
						break;
					}
				} else {
					showHelp(sender);
					break;
				}
			case 5:
				if (args[0].equalsIgnoreCase("spawn")) {
					if (sender.hasPermission("Mythicdrops.command.spawn")) {
						if (!(sender instanceof Player)) {
							getPlugin().getLanguageManager().sendMessage(sender, "command.only-players");
							break;
						}
						Player player = (Player) sender;
						if (args[1].equalsIgnoreCase("*")) {
							int amt = NumberUtils.getInt(args[2], 1);
							double min = NumberUtils.getDouble(args[3], 1.0);
							double max = NumberUtils.getDouble(args[4], 1.0);
							for (int i = 0; i < amt; i++) {
								ItemStack itemstack = getPlugin().getDropManager()
										.constructItemStack(DropManager.GenerationReason.COMMAND);
                                DecimalRangeContainer tierDurabilityContainer = new DecimalRangeContainer(min, max);
                                double minDamagePerc = tierDurabilityContainer.getLower() * itemstack.getType().getMaxDurability();
                                double maxDamagePerc = tierDurabilityContainer.getHigher() * itemstack.getType().getMaxDurability();
                                DecimalRangeContainer decimalRangeContainer = new DecimalRangeContainer(minDamagePerc, maxDamagePerc);
                                double perc = RandomUtils.randomRangeDecimalContainerInclusive(decimalRangeContainer);
                                short durability = (short) (itemstack.getType().getMaxDurability() * perc);
                                itemstack.setDurability((short) (itemstack.getType().getMaxDurability() - durability));
								player.getInventory().addItem(itemstack);
							}
							player.updateInventory();
							getPlugin().getLanguageManager()
									.sendMessage(sender, "command.spawn-random",
											new String[][]{{"%amount%", String.valueOf(amt)}});
							break;
						} else if (args[1].equalsIgnoreCase("gem")) {
							int amt = NumberUtils.getInt(args[3], 1);
							for (int i = 0; i < amt; i++) {
								SocketGem socketGem = getPlugin().getSocketGemManager().getRandomSocketGemWithChance();
								MaterialData materialData =
										getPlugin().getSocketGemManager().getRandomSocketGemMaterial();
								if (socketGem != null && materialData != null) {
									player.getInventory().addItem(
											new SocketItem(materialData, socketGem));
								}
							}
							player.updateInventory();
							getPlugin().getLanguageManager()
									.sendMessage(sender, "command.spawn-gem",
											new String[][]{{"%amount%", String.valueOf(amt)}});
							break;
						} else {
							Tier t = getPlugin().getTierManager().getTierFromName(
									args[1]);
							if (t == null) {
								getPlugin().getLanguageManager().sendMessage(sender, "command.tier-does-not-exist");
								break;
							}
							int amt = NumberUtils.getInt(args[2], 1);
							double min = NumberUtils.getDouble(args[3], 1.0);
							double max = NumberUtils.getDouble(args[4], 1.0);
							for (int i = 0; i < amt; i++) {
								ItemStack itemstack = getPlugin().getDropManager()
										.constructItemStack(t, DropManager.GenerationReason.COMMAND);
                                DecimalRangeContainer tierDurabilityContainer = new DecimalRangeContainer(min, max);
                                double minDamagePerc = tierDurabilityContainer.getLower() * itemstack.getType().getMaxDurability();
                                double maxDamagePerc = tierDurabilityContainer.getHigher() * itemstack.getType().getMaxDurability();
                                DecimalRangeContainer decimalRangeContainer = new DecimalRangeContainer(minDamagePerc, maxDamagePerc);
                                double perc = RandomUtils.randomRangeDecimalContainerInclusive(decimalRangeContainer);
                                short durability = (short) (itemstack.getType().getMaxDurability() * perc);
                                itemstack.setDurability((short) (itemstack.getType().getMaxDurability() - durability));
								player.getInventory().addItem(itemstack);
							}
							player.updateInventory();
							getPlugin().getLanguageManager()
									.sendMessage(sender, "command.spawn-random",
											new String[][]{{"%amount%", String.valueOf(amt)}});
							break;
						}
					} else {
						getPlugin().getLanguageManager().sendMessage(sender, "command.no-access");
						break;
					}
				} else {
					showHelp(sender);
					break;
				}
			case 6:
				if (args[0].equalsIgnoreCase("give")) {
					if (sender.hasPermission("Mythicdrops.command.give")) {
						Player player = Bukkit.getPlayer(args[1]);
						if (player == null || !player.isOnline()) {
							getPlugin().getLanguageManager().sendMessage(sender, "command.player-does-not-exist");
							break;
						}
						if (args[2].equalsIgnoreCase("*")) {
							int amt = NumberUtils.getInt(args[3], 1);
							double min = NumberUtils.getDouble(args[4], 1.0);
							double max = NumberUtils.getDouble(args[5], 1.0);
							for (int i = 0; i < amt; i++) {
								ItemStack itemstack = getPlugin().getDropManager()
										.constructItemStack(DropManager.GenerationReason.COMMAND);
                                DecimalRangeContainer tierDurabilityContainer = new DecimalRangeContainer(min, max);
                                double minDamagePerc = tierDurabilityContainer.getLower() * itemstack.getType().getMaxDurability();
                                double maxDamagePerc = tierDurabilityContainer.getHigher() * itemstack.getType().getMaxDurability();
                                DecimalRangeContainer decimalRangeContainer = new DecimalRangeContainer(minDamagePerc, maxDamagePerc);
                                double perc = RandomUtils.randomRangeDecimalContainerInclusive(decimalRangeContainer);
                                short durability = (short) (itemstack.getType().getMaxDurability() * perc);
                                itemstack.setDurability((short) (itemstack.getType().getMaxDurability() - durability));
								player.getInventory().addItem(itemstack);
							}
							player.updateInventory();
							getPlugin().getLanguageManager()
									.sendMessage(player, "command.give-random-receiver",
											new String[][]{{"%amount%", String.valueOf(amt)}});
							getPlugin().getLanguageManager().sendMessage(sender, "command.give-random-sender",
									new String[][]{{"%receiver%", player.getName()}, {"%amount%", "1"}});
							break;
						} else if (args[2].equalsIgnoreCase("gem")) {
							int amt = NumberUtils.getInt(args[3], 1);
							for (int i = 0; i < amt; i++) {
								SocketGem socketGem = getPlugin().getSocketGemManager().getRandomSocketGemWithChance();
								MaterialData materialData =
										getPlugin().getSocketGemManager().getRandomSocketGemMaterial();
								if (socketGem != null && materialData != null) {
									player.getInventory().addItem(
											new SocketItem(materialData, socketGem));
								}
							}
							player.updateInventory();
							getPlugin().getLanguageManager()
									.sendMessage(player, "command.give-gem-receiver",
											new String[][]{{"%amount%", String.valueOf(amt)}});
							getPlugin().getLanguageManager().sendMessage(sender, "command.give-gem-sender",
									new String[][]{{"%receiver%", player.getName()}, {"%amount%", "1"}});
							break;
						} else {
							Tier t = getPlugin().getTierManager().getTierFromName(
									args[2]);
							if (t == null) {
								getPlugin().getLanguageManager().sendMessage(sender, "command.tier-does-not-exist");
								break;
							}
							int amt = NumberUtils.getInt(args[3], 1);
							double min = NumberUtils.getDouble(args[4], 1.0);
							double max = NumberUtils.getDouble(args[5], 1.0);
							for (int i = 0; i < amt; i++) {
								ItemStack itemstack = getPlugin().getDropManager()
										.constructItemStack(t, DropManager.GenerationReason.COMMAND);
                                DecimalRangeContainer tierDurabilityContainer = new DecimalRangeContainer(min, max);
                                double minDamagePerc = tierDurabilityContainer.getLower() * itemstack.getType().getMaxDurability();
                                double maxDamagePerc = tierDurabilityContainer.getHigher() * itemstack.getType().getMaxDurability();
                                DecimalRangeContainer decimalRangeContainer = new DecimalRangeContainer(minDamagePerc, maxDamagePerc);
                                double perc = RandomUtils.randomRangeDecimalContainerInclusive(decimalRangeContainer);
                                short durability = (short) (itemstack.getType().getMaxDurability() * perc);
                                itemstack.setDurability((short) (itemstack.getType().getMaxDurability() - durability));
								player.getInventory().addItem(itemstack);
							}
							player.updateInventory();
							getPlugin().getLanguageManager()
									.sendMessage(player, "command.give-random-receiver",
											new String[][]{{"%amount%", String.valueOf(amt)}});
							getPlugin().getLanguageManager().sendMessage(sender, "command.give-random-sender",
									new String[][]{{"%receiver%", player.getName()}, {"%amount%", "1"}});
							break;
						}
					} else {
						getPlugin().getLanguageManager().sendMessage(sender, "command.no-access");
						break;
					}
				} else {
					showHelp(sender);
					break;
				}
			default:
				showHelp(sender);
				break;
		}
		return true;
	}

	private void showHelp(CommandSender sender) {
		sender.sendMessage(ChatColor.DARK_PURPLE
				+ "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
		sender.sendMessage(ChatColor.BLUE + "Mythicdrops v"
				+ getPlugin().getDescription().getVersion() + " Help");
		sender.sendMessage(ChatColor.BLUE + "Coded by ToppleTheNun");
		getPlugin().getLanguageManager().sendMessage(sender, "command.command-help",
				new String[][]{{"%command%", "md"}, {"%help%", "Shows plugin help"}});
		if (sender.hasPermission("Mythicdrops.command.spawn")) {
			getPlugin().getLanguageManager().sendMessage(sender, "command.command-help",
					new String[][]{{"%command%", "md spawn [tier|*|gem] [amount] [minimum] [maximum]"}, {"%help%",
							"Gives the sender [amount] Mythicdrops of [tier] " +
									"with durability percentage between [minimum] and [maximum]."}});
		}
		if (sender.hasPermission("Mythicdrops.command.custom")) {
			getPlugin().getLanguageManager().sendMessage(sender, "command.command-help",
					new String[][]{{"%command%", "md custom [player|self] [name] [amount]"}, {"%help%",
							"Gives the [player|sender] an [amount] of custom items with name [name]."}});
		}
		if (sender.hasPermission("Mythicdrops.command.give")) {
			getPlugin().getLanguageManager().sendMessage(sender, "command.command-help",
					new String[][]{{"%command%", "md give <player> [tier|*|gem] [amount] [minimum] [maximum]"},
							{"%help%",
									"Gives the <player> [amount] Mythicdrops of [tier] " +
											"with durability percentage between [minimum] and [maximum]."}});
		}
		if (sender.hasPermission("Mythicdrops.command.reload")) {
			getPlugin().getLanguageManager().sendMessage(sender, "command.command-help",
					new String[][]{{"%command%", "md reload"}, {"%help%",
							"Reloads the plugin's configuration files."}});
		}
		sender.sendMessage(ChatColor.DARK_PURPLE
				+ "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
	}
}
