package net.nunnerycode.bukkit.mythicdrops.identification;

import net.nunnerycode.bukkit.mythicdrops.MythicDropsPlugin;
import net.nunnerycode.bukkit.mythicdrops.api.items.ItemGenerationReason;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import net.nunnerycode.bukkit.mythicdrops.utils.ItemUtil;
import net.nunnerycode.bukkit.mythicdrops.utils.TierUtil;

import org.bukkit.Bukkit;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class IdentifyingListener implements Listener {

  private Map<String, ItemStack> heldIdentify;
  private MythicDropsPlugin plugin;

  public IdentifyingListener(MythicDropsPlugin plugin) {
    this.plugin = plugin;
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
    if (event.getAction() != Action.RIGHT_CLICK_AIR
        && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
      return;
    }
    if (event.getItem() == null) {
      return;
    }
    Player player = event.getPlayer();
    ItemStack itemInHand = event.getItem();
    String itemType = ItemUtil.getItemTypeFromMaterial(itemInHand.getType());
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

  private void addHeldIdentify(PlayerInteractEvent event, final Player player,
                               ItemStack itemInHand) {
    if (!itemInHand.hasItemMeta()) {
      return;
    }
    ItemMeta im = itemInHand.getItemMeta();
    ItemStack identityTome = new IdentityTome();
    if (!im.hasDisplayName() || !identityTome.getItemMeta().hasDisplayName() || !im.getDisplayName()
        .equals
            (identityTome.getItemMeta().getDisplayName())) {
      return;
    }
    player.sendMessage(
        plugin.getConfigSettings().getFormattedLanguageString("command.identifying-instructions"));
    heldIdentify.put(player.getName(), itemInHand);
    Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, new Runnable() {
      @Override
      public void run() {
        heldIdentify.remove(player.getName());
      }
    }, 20L * 30);
    cancelResults(event);
    player.updateInventory();
  }

  private void identifyItem(PlayerInteractEvent event, Player player, ItemStack itemInHand,
                            String itemType) {
    if (ItemUtil.isArmor(itemType) || ItemUtil.isTool(itemType)) {
      if (!itemInHand.hasItemMeta() || !itemInHand.getItemMeta().hasDisplayName()) {
        cannotUse(event, player);
        return;
      }
      if (!player.getInventory().contains(heldIdentify.get(player.getName()))) {
        player.sendMessage(plugin.getConfigSettings()
                               .getFormattedLanguageString("command.identifying-do-not-have"));
        cancelResults(event);
        heldIdentify.remove(player.getName());
        player.updateInventory();
        return;
      }
      UnidentifiedItem uid = new UnidentifiedItem(itemInHand.getData().getItemType());
      boolean
          b =
          itemInHand.getItemMeta().getDisplayName().equals(uid.getItemMeta().getDisplayName());
      if (!b) {
        cannotUse(event, player);
        return;
      }
      List<Tier>
          iihTiers =
          new ArrayList<>(ItemUtil.getTiersFromMaterial(itemInHand.getType()));
      Tier iihTier = TierUtil.randomTierWithIdentifyChance(iihTiers);
      if (iihTier == null) {
        cannotUse(event, player);
        return;
      }

      ItemStack iih = MythicDropsPlugin.getNewDropBuilder().withItemGenerationReason
          (ItemGenerationReason.EXTERNAL).withMaterial(itemInHand.getType()).withTier(iihTier)
          .useDurability(false).build();
      iih.setDurability(itemInHand.getDurability());

      ItemMeta itemMeta = iih.getItemMeta();
      List<String> lore = new ArrayList<>();
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
      player.sendMessage(
          plugin.getConfigSettings().getFormattedLanguageString("command.identifying-success"));
      cancelResults(event);
      heldIdentify.remove(player.getName());
      player.updateInventory();
    } else {
      cannotUse(event, player);
    }
  }

  private void cancelResults(PlayerInteractEvent event) {
    event.setCancelled(true);
    event.setUseInteractedBlock(Event.Result.DENY);
    event.setUseItemInHand(Event.Result.DENY);
  }

  private void cannotUse(PlayerInteractEvent event, Player player) {
    player.sendMessage(
        plugin.getConfigSettings().getFormattedLanguageString("command.identifying-cannot-use"));
    cancelResults(event);
    heldIdentify.remove(player.getName());
    player.updateInventory();
  }

}
