/*
 * The MIT License
 * Copyright © 2013 Richard Harrah
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.tealcube.minecraft.bukkit.mythicdrops.commands;

import com.tealcube.minecraft.bukkit.mythicdrops.MythicDropsPlugin;
import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDrops;
import com.tealcube.minecraft.bukkit.mythicdrops.api.enchantments.MythicEnchantment;
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.CustomItem;
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGenerationReason;
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier;
import com.tealcube.minecraft.bukkit.mythicdrops.identification.IdentityTome;
import com.tealcube.minecraft.bukkit.mythicdrops.identification.UnidentifiedItem;
import com.tealcube.minecraft.bukkit.mythicdrops.items.CustomItemBuilder;
import com.tealcube.minecraft.bukkit.mythicdrops.items.CustomItemMap;
import com.tealcube.minecraft.bukkit.mythicdrops.logging.MythicLoggerFactory;
import com.tealcube.minecraft.bukkit.mythicdrops.socketting.SocketGem;
import com.tealcube.minecraft.bukkit.mythicdrops.socketting.SocketItem;
import com.tealcube.minecraft.bukkit.mythicdrops.tiers.TierMap;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.EntityUtil;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.GsonUtil;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.ItemStackUtil;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.ItemUtil;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.SocketGemUtil;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.StringListUtil;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.TierUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import se.ranzdo.bukkit.methodcommand.Arg;
import se.ranzdo.bukkit.methodcommand.Command;
import se.ranzdo.bukkit.methodcommand.FlagArg;
import se.ranzdo.bukkit.methodcommand.Flags;
import se.ranzdo.bukkit.methodcommand.Wildcard;

public final class MythicDropsCommand {

  private static final Logger LOGGER = MythicLoggerFactory.getLogger(MythicDropsCommand.class);
  private MythicDrops plugin;

  public MythicDropsCommand(MythicDropsPlugin plugin) {
    this.plugin = plugin;
  }

  @Command(identifier = "mythicdrops debug", description = "Prints a bunch of debug messages",
      permissions = "mythicdrops.command.debug")
  public void debugCommand(CommandSender sender) {
    LOGGER.info("server package: " + Bukkit.getServer().getClass().getPackage().toString());
    LOGGER.info("number of tiers: " + TierMap.INSTANCE.size());
    LOGGER.info("number of custom items: " + CustomItemMap.getInstance().size());
    LOGGER.info("config settings: " + GsonUtil.INSTANCE.toJson(this.plugin.getConfigSettings()));
    LOGGER.info("creature spawning settings: " + GsonUtil.INSTANCE.toJson(this.plugin.getCreatureSpawningSettings()));
    sender.sendMessage(
        plugin.getConfigSettings().getFormattedLanguageString("command.debug"));
  }

  @Command(identifier = "mythicdrops reload", description = "Reloads the configuration files",
      permissions = "mythicdrops.command.reload")
  public void reloadCommand(CommandSender sender) {
    LOGGER.info("Reloading the configuration files");
    plugin.reloadConfigurationFiles();
    // Lord help us all
    plugin.reloadTiers();
    plugin.reloadNames();
    plugin.reloadCustomItems();
    plugin.reloadRepairCosts();
    plugin.reloadSettings();
    LOGGER.info("Done reloading the configuration files");
    sender.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.reload"));
  }

  @Command(identifier = "mythicdrops spawn", description = "Spawns in MythicDrops items",
      permissions = "mythicdrops.command.spawn")
  @Flags(identifier = {"a", "t", "mind", "maxd"}, description = {"Amount to spawn", "Tier to spawn",
      "Minimum durability",
      "Maximum durability"})
  public void spawnSubcommand(CommandSender sender, @Arg(name = "amount", def = "1")
  @FlagArg("a") int amount, @Arg(name = "tier", def = "*") @FlagArg("t") String tierName,
      @Arg(name = "mindurability", def = "1.0",
          verifiers = "min[0.0]|max[1.0]") @FlagArg
          ("mind") double minDura,
      @Arg(name = "maxdurability", def = "1.0",
          verifiers = "min[0.0]|max[1.0]") @FlagArg
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
    if (TierMap.INSTANCE.size() <= 0) {
      sender.sendMessage(plugin.getConfigSettings().getFormattedLanguageString(
          "command.spawn-random-failure", new String[][]{
              {"%amount%", String.valueOf(amount)}
          })
      );
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

    int amountGiven = 0;
    while (amountGiven < amount) {
      ItemStack mis = MythicDropsPlugin.getNewDropBuilder().useDurability(false)
          .withItemGenerationReason(ItemGenerationReason.COMMAND).withTier(tier)
          .build();
      if (mis != null) {
        ItemStackUtil.setDurabilityForItemStack(mis, minDura, maxDura);
        player.getInventory().addItem(mis);
        amountGiven++;
      }
    }

    player.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.spawn-random",
        new String[][]{
            {"%amount%", String
                .valueOf(
                amountGiven)}}
    ));
  }

  @Command(identifier = "mythicdrops drop", description = "Drops in MythicDrops items",
      permissions = "mythicdrops.command.drop")
  @Flags(identifier = {"a", "t", "w", "mind", "maxd"},
      description = {"Amount to drop", "Tier to drop", "World",
          "Minimum durability", "Maximum durability"})
  public void dropSubcommand(CommandSender sender, @Arg(name = "amount", def = "1")
  @FlagArg("a") int amount, @Arg(name = "tier", def = "*") @FlagArg("t") String tierName,
      @Arg(name = "world", def = "") @FlagArg("w") String worldName,
      @Arg(name = "x") double x, @Arg(name = "y") double y,
      @Arg(name = "z") double z,
      @Arg(name = "mindurability", def = "1.0",
          verifiers = "min[0.0]|max[1.0]") @FlagArg
          ("mind") double minDura,
      @Arg(name = "maxdurability", def = "1.0",
          verifiers = "min[0.0]|max[1.0]") @FlagArg
          ("maxd") double maxDura) {
    if (!(sender instanceof Player) && "".equals(worldName)) {
      sender.sendMessage(
          plugin.getConfigSettings().getFormattedLanguageString("command.only-players"));
      return;
    }

    if (tierName.equalsIgnoreCase("*") && !sender
        .hasPermission("mythicdrops.command.spawn.wildcard")) {
      sender
          .sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.no-access"));
      return;
    }

    String worldN = sender instanceof Player ? ((Player) sender).getWorld().getName() : worldName;

    if (TierMap.INSTANCE.size() <= 0) {
      sender.sendMessage(plugin.getConfigSettings().getFormattedLanguageString(
          "command.drop-random-failure", new String[][]{
              {"%amount%", String.valueOf(amount)}
          })
      );
      return;
    }

    Tier tier = TierUtil.getTier(tierName);

    if (!sender.hasPermission("mythicdrops.command.spawn.wildcard")) {
      if (tier == null) {
        sender.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command" +
            ".tier-does-not-exist"));
        return;
      } else if (!sender.hasPermission("mythicdrops.command.spawn." + tier.getName())) {
        sender.sendMessage(
            plugin.getConfigSettings().getFormattedLanguageString("command.no-access"));
        return;
      }
    }

    World w = Bukkit.getWorld(worldN);
    if (w == null) {
      sender.sendMessage(
          plugin.getConfigSettings().getFormattedLanguageString("command.world-does-not-exist"));
      return;
    }
    Location l = new Location(w, x, y, z);
    Entity e = EntityUtil.getEntityAtLocation(l);

    int amountGiven = 0;
    while (amountGiven < amount) {
      ItemStack mis = MythicDropsPlugin.getNewDropBuilder().useDurability(false)
          .withItemGenerationReason(ItemGenerationReason.COMMAND).withTier(tier)
          .build();
      if (mis != null) {
        ItemStackUtil.setDurabilityForItemStack(mis, minDura, maxDura);
        if (e instanceof InventoryHolder) {
          ((InventoryHolder) e).getInventory().addItem(mis);
        } else if (l.getBlock().getState() instanceof InventoryHolder) {
          ((InventoryHolder) l.getBlock().getState()).getInventory().addItem(mis);
        } else {
          w.dropItem(l, mis);
        }
        amountGiven++;
      }
    }

    sender.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.drop-random",
        new String[][]{
            {"%amount%", String
                .valueOf(
                amountGiven)}}
    ));
  }

  @Command(identifier = "mythicdrops give", description = "Gives MythicDrops items",
      permissions = "mythicdrops.command.give")
  @Flags(identifier = {"a", "t", "mind", "maxd"}, description = {"Amount to spawn", "Tier to spawn",
      "Minimum durability",
      "Maximum durability"})
  public void giveSubcommand(CommandSender sender, @Arg(name = "player") Player player,
      @Arg(name = "amount",
          def = "1")
      @FlagArg("a") int amount,
      @Arg(name = "tier", def = "*") @FlagArg("t") String tierName,
      @Arg(name = "mindurability", def = "1.0",
          verifiers = "min[0.0]|max[1.0]") @FlagArg
          ("mind") double minDura,
      @Arg(name = "maxdurability", def = "1.0",
          verifiers = "min[0.0]|max[1.0]") @FlagArg
          ("maxd") double maxDura) {
    if (tierName.equalsIgnoreCase("*") && !sender
        .hasPermission("mythicdrops.command.give.wildcard")) {
      sender
          .sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.no-access"));
      return;
    }
    if (TierMap.INSTANCE.size() <= 0) {
      sender.sendMessage(plugin.getConfigSettings().getFormattedLanguageString(
          "command.give-random-sender-failure", new String[][]{
              {"%amount%", String.valueOf(amount)},
              {"%receiver%", player.getName()}
          })
      );
      return;
    }

    Tier tier = TierUtil.getTier(tierName);

    if (!sender.hasPermission("mythicdrops.command.give.wildcard")) {
      if (tier == null) {
        sender.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command" +
            ".tier-does-not-exist"));
        return;
      } else if (!sender.hasPermission("mythicdrops.command.give." + tier.getName())) {
        sender.sendMessage(
            plugin.getConfigSettings().getFormattedLanguageString("command.no-access"));
        return;
      }
    }

    int amountGiven = 0;
    while (amountGiven < amount) {
      ItemStack mis = MythicDropsPlugin.getNewDropBuilder().useDurability(true)
          .withItemGenerationReason(ItemGenerationReason.COMMAND).withTier(tier)
          .build();
      if (mis != null) {
        ItemStackUtil.setDurabilityForItemStack(mis, minDura, maxDura);
        player.getInventory().addItem(mis);
        amountGiven++;
      }
    }

    player.sendMessage(
        plugin.getConfigSettings().getFormattedLanguageString("command.give-random-receiver",
            new String[][]{{"%amount%", String
                .valueOf(amountGiven)}}
        )
    );
    sender.sendMessage(
        plugin.getConfigSettings().getFormattedLanguageString("command.give-random-sender",
            new String[][]{{"%amount%", String
                .valueOf(amountGiven)},
                {"%receiver%",
                    player.getName()}}
        )
    );
  }

  @Command(identifier = "mythicdrops customcreate",
      description = "Creates a custom item from the item in the " +
          "user's hand", permissions = "mythicdrops.command.customcreate")
  public void customCreateSubcommand(CommandSender sender,
      @Arg(name = "chance to spawn") double chanceToSpawn,
      @Arg(name = "chance to drop") double chanceToDrop) {
    if (!(sender instanceof Player)) {
      sender
          .sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.no-access"));
      return;
    }
    Player p = (Player) sender;
    ItemStack itemInHand = p.getEquipment().getItemInMainHand();
    if (!itemInHand.hasItemMeta()) {
      sender.sendMessage(
          plugin.getConfigSettings().getFormattedLanguageString("command.customcreate-failure"));
      return;
    }
    ItemMeta im = itemInHand.getItemMeta();
    if (!im.hasDisplayName() || !im.hasLore()) {
      sender.sendMessage(
          plugin.getConfigSettings().getFormattedLanguageString("command.customcreate-failure"));
      return;
    }
    String displayName;
    String name;
    if (im.hasDisplayName()) {
      displayName = im.getDisplayName().replace('\u00A7', '&');
      name = ChatColor.stripColor(im.getDisplayName()).replaceAll("\\s+", "");
    } else {
      sender.sendMessage(
          plugin.getConfigSettings().getFormattedLanguageString("command.customcreate-failure"));
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
    CustomItem ci = new CustomItemBuilder(name)
        .withDisplayName(displayName)
        .withLore(lore)
        .withEnchantments(enchantments.entrySet().stream().map(
            enchantmentIntegerEntry -> new MythicEnchantment(
                enchantmentIntegerEntry.getKey(), enchantmentIntegerEntry.getValue(),
                enchantmentIntegerEntry.getValue()
            )).collect(Collectors.toList()))
        .withMaterial(itemInHand.getType())
        .withChanceToBeGivenToMonster(chanceToSpawn)
        .withChanceToDropOnDeath(chanceToDrop)
        .withDurability(itemInHand.getDurability())
        .build();
    CustomItemMap.getInstance().put(name, ci);
    sender.sendMessage(
        plugin.getConfigSettings().getFormattedLanguageString("command.customcreate-success",
            new String[][]{{"%name%", name}})
    );

    plugin.getCustomItemYAML().set(name + ".displayName", ci.getDisplayName());
    plugin.getCustomItemYAML().set(name + ".lore", ci.getLore());
    plugin.getCustomItemYAML()
        .set(name + ".spawnOnMonsterWeight", ci.getChanceToBeGivenToAMonster());
    plugin.getCustomItemYAML()
        .set(name + ".chanceToDropOnMonsterDeath", ci.getChanceToDropOnDeath());
    plugin.getCustomItemYAML().set(name + ".materialName", ci.getMaterial().name());
    for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
      plugin.getCustomItemYAML()
          .set(name + ".enchantments." + entry.getKey().getName(), entry.getValue());
    }
    plugin.getCustomItemYAML().save();
  }

  @Command(identifier = "mythicdrops custom", description = "Gives custom MythicDrops items",
      permissions = "mythicdrops.command.custom")
  @Flags(identifier = {"a", "c", "mind", "maxd"},
      description = {"Amount to spawn", "Custom Item to spawn",
          "Minimum durability", "Maximum durability"})
  public void customSubcommand(CommandSender sender,
      @Arg(name = "player", def = "self") String playerName,
      @Arg(name = "amount", def = "1")
      @FlagArg("a") int amount,
      @Arg(name = "item", def = "*") @FlagArg("c") String itemName,
      @Arg(name = "mindurability", def = "1.0",
          verifiers = "min[0.0]|max[1.0]") @FlagArg
          ("mind") double minDura,
      @Arg(name = "maxdurability", def = "1.0",
          verifiers = "min[0.0]|max[1.0]") @FlagArg
          ("maxd") double maxDura) {
    Player player;
    if (playerName.equalsIgnoreCase("self")) {
      if (sender instanceof Player) {
        player = (Player) sender;
      } else {
        sender.sendMessage(
            plugin.getConfigSettings().getFormattedLanguageString("command.no-access"));
        return;
      }
    } else {
      player = Bukkit.getPlayer(playerName);
    }
    if (player == null) {
      sender.sendMessage(
          plugin.getConfigSettings().getFormattedLanguageString("command.player-does-not-exist"));
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
        ItemStack itemStack = null;
        boolean hasDurability = false;
        if (customItem == null) {
          CustomItem ci = CustomItemMap.getInstance().getRandomWithChance();
          if (ci != null) {
            itemStack = ci.toItemStack();
            hasDurability = ci.hasDurability();
          }
        } else {
          itemStack = customItem.toItemStack();
          hasDurability = customItem.hasDurability();
        }
        if (itemStack == null) {
          continue;
        }
        if (!hasDurability && itemStack.getItemMeta() instanceof Damageable) {
          ((Damageable) itemStack.getItemMeta())
              .setDamage(ItemStackUtil.getDurabilityForMaterial(itemStack.getType(), minDura,
                  maxDura));
        }
        player.getInventory().addItem(itemStack);
        amountGiven++;
      } catch (Exception ignored) {
      }
    }
    player.sendMessage(
        plugin.getConfigSettings().getFormattedLanguageString("command.give-custom-receiver",
            new String[][]{{"%amount%", String
                .valueOf(amountGiven)}}
        )
    );
    if (!player.equals(sender)) {
      sender.sendMessage(plugin.getConfigSettings()
          .getFormattedLanguageString("command.give-custom-sender",
              new String[][]{{"%amount%",
                  String
                      .valueOf(amountGiven)},
                  {"%receiver%",
                      player.getName()}}
          ));
    }
  }

  @Command(identifier = "mythicdrops gem", description = "Gives MythicDrops gems",
      permissions = "mythicdrops.command.gem")
  @Flags(identifier = {"a", "g"}, description = {"Amount to spawn", "Socket Gem to spawn"})
  public void gemSubcommand(CommandSender sender,
      @Arg(name = "player", def = "self") String playerName,
      @Arg(name = "amount", def = "1")
      @FlagArg("a") int amount,
      @Arg(name = "item", def = "*") @FlagArg("g") String
          itemName) {
    Player player;
    if (playerName.equalsIgnoreCase("self")) {
      if (sender instanceof Player) {
        player = (Player) sender;
      } else {
        sender
            .sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.no-access",
                new String[][]{}));
        return;
      }
    } else {
      player = Bukkit.getPlayer(playerName);
    }
    if (player == null) {
      sender.sendMessage(
          plugin.getConfigSettings().getFormattedLanguageString("command.player-does-not-exist",
              new String[][]{})
      );
      return;
    }
    SocketGem socketGem = null;
    if (!itemName.equalsIgnoreCase("*")) {
      try {
        socketGem = SocketGemUtil.getSocketGemFromName(itemName);
      } catch (NullPointerException e) {
        sender.sendMessage(plugin.getConfigSettings()
            .getFormattedLanguageString("command.socket-gem-does-not-exist",
                new String[][]{}));
        return;
      }
    }
    int amountGiven = 0;
    for (int i = 0; i < amount; i++) {
      try {
        ItemStack itemStack;
        if (socketGem == null) {
          Material material = SocketGemUtil.getRandomSocketGemMaterial();
          itemStack = new SocketItem(material, SocketGemUtil.getRandomSocketGemWithChance());
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
    player.sendMessage(
        plugin.getConfigSettings().getFormattedLanguageString("command.give-gem-receiver",
            new String[][]{{"%amount%", String
                .valueOf(amountGiven)}}
        )
    );
    if (!sender.equals(player)) {
      sender.sendMessage(plugin.getConfigSettings()
          .getFormattedLanguageString("command.give-gem-sender",
              new String[][]{{"%amount%",
                  String
                      .valueOf(amountGiven)},
                  {"%receiver%",
                      player.getName()}}
          ));
    }
  }

  @Command(identifier = "mythicdrops unidentified", description = "Gives Unidentified Item",
      permissions = "mythicdrops.command.unidentified")
  @Flags(identifier = {"a"}, description = {"Amount to spawn"})
  public void unidentifiedSubcommand(CommandSender sender,
      @Arg(name = "player", def = "self") String playerName,
      @Arg(name = "amount", def = "1") @FlagArg("a") int amount) {
    Player player;
    if (playerName.equalsIgnoreCase("self")) {
      if (sender instanceof Player) {
        player = (Player) sender;
      } else {
        sender.sendMessage(
            plugin.getConfigSettings().getFormattedLanguageString("command.no-access"));
        return;
      }
    } else {
      player = Bukkit.getPlayer(playerName);
    }
    if (player == null) {
      sender.sendMessage(
          plugin.getConfigSettings().getFormattedLanguageString("command.player-does-not-exist"));
      return;
    }
    int amountGiven = 0;
    for (int i = 0; i < amount; i++) {
      Tier t = TierMap.INSTANCE.getRandomTierWithChance();
      if (t == null) {
        continue;
      }
      Collection<Material> materials = ItemUtil.getMaterialsFromTier(t);
      Material material = ItemUtil.getRandomMaterialFromCollection(materials);
      player.getInventory().addItem(new UnidentifiedItem(material));
      amountGiven++;
    }
    player.sendMessage(
        plugin.getConfigSettings().getFormattedLanguageString("command.give-unidentified-receiver",
            new String[][]{{"%amount%", String
                .valueOf(amountGiven)}}
        )
    );
    if (!player.equals(sender)) {
      sender.sendMessage(
          plugin.getConfigSettings().getFormattedLanguageString("command.give-unidentified-sender",
              new String[][]{{"%amount%", String
                  .valueOf(amountGiven)},
                  {"%receiver%", player
                      .getName()}}
          )
      );
    }
  }

  @Command(identifier = "mythicdrops tome", description = "Gives Identity Tome",
      permissions = "mythicdrops.command.tome")
  @Flags(identifier = {"a"}, description = {"Amount to spawn"})
  public void tomeSubcommand(CommandSender sender,
      @Arg(name = "player", def = "self") String playerName,
      @Arg(name = "amount", def = "1") @FlagArg("a") int amount) {
    Player player;
    if (playerName.equalsIgnoreCase("self")) {
      if (sender instanceof Player) {
        player = (Player) sender;
      } else {
        sender.sendMessage(
            plugin.getConfigSettings().getFormattedLanguageString("command.no-access"));
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
            ".give-tome-receiver",
        new String[][]{
            {"%amount%", String
                .valueOf(
                amountGiven)}}
    ));
    if (player != sender) {
      sender.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command" +
              ".give-tome-sender",
          new String[][]{
              {"%amount%",
                  String.valueOf(
                      amountGiven)},
              {"%receiver%",
                  player
                      .getName()}}
      ));
    }
  }

  @Command(identifier = "mythicdrops tiers", description = "Lists all Tiers",
      permissions = "mythicdrops.command.tiers")
  public void tiersCommand(CommandSender sender) {
    List<String> loadedTierNames = new ArrayList<>();
    for (Tier t : TierMap.INSTANCE.values()) {
      loadedTierNames.add(t.getName());
    }
    sender.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.tier-list",
        new String[][]{
            {"%tiers%",
                loadedTierNames
                    .toString()
                    .replace("[",
                        "")
                    .replace("]",
                    "")}}
    ));
  }

  @Command(identifier = "mythicdrops modify name", description = "Adds a name to the item in hand",
      permissions = "mythicdrops.command.modify.name")
  public void modifyNameCommand(CommandSender sender,
      @Wildcard @Arg(name = "item name") String name) {
    if (!(sender instanceof Player)) {
      sender
          .sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.no-access"));
      return;
    }
    Player p = (Player) sender;
    ItemStack itemInHand = p.getEquipment().getItemInMainHand();
    if (itemInHand.getType() == Material.AIR) {
      p.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.cannot-modify"));
      return;
    }
    String newName = name.replace('&', '\u00A7').replace("\u00A7\u00A7", "&");
    ItemMeta
        im =
        itemInHand.hasItemMeta() ? itemInHand.getItemMeta() : Bukkit.getItemFactory().getItemMeta
            (itemInHand.getType());
    im.setDisplayName(newName);
    itemInHand.setItemMeta(im);
    p.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.modify-name"));
  }

  @Command(identifier = "mythicdrops modify lore add",
      description = "Adds a line of lore to the item in hand",
      permissions = "mythicdrops.command.modify.lore")
  public void modifyLoreAddCommand(CommandSender sender,
      @Wildcard @Arg(name = "lore line") String line) {
    if (!(sender instanceof Player)) {
      sender
          .sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.no-access"));
      return;
    }
    Player p = (Player) sender;
    ItemStack itemInHand = p.getEquipment().getItemInMainHand();
    if (itemInHand.getType() == Material.AIR) {
      p.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.cannot-modify"));
      return;
    }
    String newLine = line.replace('&', '\u00A7').replace("\u00A7\u00A7", "&");
    ItemMeta
        im =
        itemInHand.hasItemMeta() ? itemInHand.getItemMeta() : Bukkit.getItemFactory().getItemMeta
            (itemInHand.getType());
    List<String> lore = im.hasLore() ? im.getLore() : new ArrayList<String>();
    lore.add(newLine);
    im.setLore(lore);
    itemInHand.setItemMeta(im);
    p.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.add-lore"));
  }

  @Command(identifier = "mythicdrops modify lore remove",
      description = "Removes a line of lore to the item in hand",
      permissions = "mythicdrops.command.modify.lore")
  public void modifyLoreAddCommand(CommandSender sender,
      @Arg(name = "line to remove") int lineNumber) {
    if (!(sender instanceof Player)) {
      sender
          .sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.no-access"));
      return;
    }
    Player p = (Player) sender;
    ItemStack itemInHand = p.getEquipment().getItemInMainHand();
    if (itemInHand.getType() == Material.AIR) {
      p.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.cannot-modify"));
      return;
    }
    ItemMeta
        im =
        itemInHand.hasItemMeta() ? itemInHand.getItemMeta() : Bukkit.getItemFactory().getItemMeta
            (itemInHand.getType());
    List<String> lore = im.hasLore() ? im.getLore() : new ArrayList<String>();
    lore.remove(Math.max(Math.min(lineNumber - 1, lore.size()), 0));
    im.setLore(lore);
    itemInHand.setItemMeta(im);
    p.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.remove-lore"));
  }

  @Command(identifier = "mythicdrops modify lore insert",
      description = "Adds a line of lore to the item in hand",
      permissions = "mythicdrops.command.modify.lore")
  public void modifyLoreAddCommand(CommandSender sender, @Arg(name = "index") int index,
      @Wildcard @Arg(name = "lore line") String line) {
    if (!(sender instanceof Player)) {
      sender
          .sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.no-access"));
      return;
    }
    Player p = (Player) sender;
    ItemStack itemInHand = p.getEquipment().getItemInMainHand();
    if (itemInHand.getType() == Material.AIR) {
      p.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.cannot-modify"));
      return;
    }
    String newLine = line.replace('&', '\u00A7').replace("\u00A7\u00A7", "&");
    ItemMeta
        im =
        itemInHand.hasItemMeta() ? itemInHand.getItemMeta() : Bukkit.getItemFactory().getItemMeta
            (itemInHand.getType());
    List<String> lore = im.hasLore() ? im.getLore() : new ArrayList<String>();
    lore = StringListUtil.addString(lore, index, newLine, false);
    im.setLore(lore);
    itemInHand.setItemMeta(im);
    p.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.insert-lore"));
  }

  @Command(identifier = "mythicdrops modify lore modify",
      description = "Modifies a line of lore to the item in " +
          "hand", permissions = "mythicdrops.command.modify.lore")
  public void modifyLoreModifyCommand(CommandSender sender, @Arg(name = "index") int index,
      @Wildcard @Arg(name = "lore line") String line) {
    if (!(sender instanceof Player)) {
      sender
          .sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.no-access"));
      return;
    }
    Player p = (Player) sender;
    ItemStack itemInHand = p.getEquipment().getItemInMainHand();
    if (itemInHand.getType() == Material.AIR) {
      p.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.cannot-modify"));
      return;
    }
    String newLine = line.replace('&', '\u00A7').replace("\u00A7\u00A7", "&");
    ItemMeta
        im =
        itemInHand.hasItemMeta() ? itemInHand.getItemMeta() : Bukkit.getItemFactory().getItemMeta
            (itemInHand.getType());
    List<String> lore = im.hasLore() ? im.getLore() : new ArrayList<String>();
    if (lore.size() >= index) {
      lore.remove(index);
    }
    lore = StringListUtil.addString(lore, index, newLine, false);
    im.setLore(lore);
    itemInHand.setItemMeta(im);
    p.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.insert-lore"));
  }

  @Command(identifier = "mythicdrops modify enchantment add",
      description = "Adds an enchantment to the item in " +
          "hand", permissions = "mythicdrops.command.modify.enchantments")
  public void modifyEnchantmentAddCommand(CommandSender sender,
      @Arg(name = "enchantment") Enchantment enchantment,
      @Arg(name = "level", def = "1") int level) {
    if (!(sender instanceof Player)) {
      sender
          .sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.no-access"));
      return;
    }
    Player p = (Player) sender;
    ItemStack itemInHand = p.getEquipment().getItemInMainHand();
    if (itemInHand.getType() == Material.AIR) {
      p.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.cannot-modify"));
      return;
    }
    itemInHand.addUnsafeEnchantment(enchantment, level);
    p.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.add-enchantment"));
  }

  @Command(identifier = "mythicdrops modify enchantment remove",
      description = "Adds an enchantment to the item in " +
          "hand", permissions = "mythicdrops.command.modify.enchantments")
  public void modifyEnchantmentRemoveCommand(CommandSender sender,
      @Arg(name = "enchantment") Enchantment
          enchantment) {
    if (!(sender instanceof Player)) {
      sender
          .sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.no-access"));
      return;
    }
    Player p = (Player) sender;
    ItemStack itemInHand = p.getEquipment().getItemInMainHand();
    if (itemInHand.getType() == Material.AIR) {
      p.sendMessage(plugin.getConfigSettings().getFormattedLanguageString("command.cannot-modify"));
      return;
    }
    itemInHand.removeEnchantment(enchantment);
    p.sendMessage(
        plugin.getConfigSettings().getFormattedLanguageString("command.remove-enchantment"));
  }

}
