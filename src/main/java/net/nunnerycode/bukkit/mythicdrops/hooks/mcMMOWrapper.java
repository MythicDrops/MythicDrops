package net.nunnerycode.bukkit.mythicdrops.hooks;

import com.gmail.nossr50.events.skills.repair.McMMOPlayerRepairCheckEvent;

import net.nunnerycode.bukkit.mythicdrops.api.MythicDrops;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class mcMMOWrapper implements Listener {

  private MythicDrops mythicDrops;

  public mcMMOWrapper(MythicDrops mythicDrops) {
    this.mythicDrops = mythicDrops;
  }

  @EventHandler
  public void onRepairItemCheckEvent(McMMOPlayerRepairCheckEvent event) {
    if (!mythicDrops.getRepairingSettings().isEnabled()) {
      event.setCancelled(true);
    }
  }

}
