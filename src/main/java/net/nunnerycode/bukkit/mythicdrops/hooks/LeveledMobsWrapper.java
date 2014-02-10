package net.nunnerycode.bukkit.mythicdrops.hooks;

import net.nunnerycode.bukkit.mythicdrops.api.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.items.ItemGenerationReason;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import net.nunnerycode.bukkit.mythicdrops.events.EntityEquipEvent;
import net.nunnerycode.bukkit.mythicdrops.items.MythicDropBuilder;
import net.nunnerycode.bukkit.mythicdrops.utils.TierUtil;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

import mainLeveledMobs.LeveledMobs;

public final class LeveledMobsWrapper implements Listener {

  private MythicDrops mythicDrops;
  private LeveledMobs leveledMobs;

  public LeveledMobsWrapper(MythicDrops mythicDrops) {
    this.mythicDrops = mythicDrops;
    leveledMobs = (LeveledMobs) Bukkit.getPluginManager().getPlugin("LeveledMobs");
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onRandomItemGenerationEvent(EntityEquipEvent event) {
    if (event.isCancelled()) {
      return;
    }

    Collection<Tier> originalTiers = mythicDrops.getCreatureSpawningSettings().getEntityTypeTiers
        (event.getLivingEntity().getType());
    Collection<Tier> skewedTiers = TierUtil.skewTierCollectionToRarer(originalTiers,
                                                                      Math.max(
                                                                          originalTiers.size() - (
                                                                              leveledMobs
                                                                                  .getCreatureLevel(
                                                                                      event
                                                                                          .getLivingEntity())
                                                                              -
                                                                              1), 1));
    Tier t = TierUtil.randomTierWithChance(skewedTiers);
    ItemStack
        is =
        new MythicDropBuilder(mythicDrops).useDurability(true).withTier(t)
            .withItemGenerationReason(ItemGenerationReason
                                          .MONSTER_SPAWN).build();
    event.setItemStack(is);
  }

}
