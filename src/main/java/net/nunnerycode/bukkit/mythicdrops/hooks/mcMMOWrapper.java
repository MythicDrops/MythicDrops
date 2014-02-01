package net.nunnerycode.bukkit.mythicdrops.hooks;

import com.gmail.nossr50.events.skills.repair.McMMOPlayerRepairCheckEvent;

import net.nunnerycode.bukkit.mythicdrops.api.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import net.nunnerycode.bukkit.mythicdrops.utils.TierUtil;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class mcMMOWrapper implements Listener {

  private MythicDrops mythicDrops;

  public mcMMOWrapper(MythicDrops mythicDrops) {
    this.mythicDrops = mythicDrops;
  }

  @EventHandler
  public void onRepairItemCheckEvent(McMMOPlayerRepairCheckEvent event) {
    Tier t = TierUtil.getTierFromItemStack(event.getRepairedObject());
    if (t != null && mythicDrops.getRepairingSettings().isCancelMcMMORepair()) {
      event.setCancelled(true);
    }
  }

}
