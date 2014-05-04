package net.nunnerycode.bukkit.mythicdrops.settings;

import net.nunnerycode.bukkit.mythicdrops.api.repair.RepairItem;
import net.nunnerycode.bukkit.mythicdrops.api.settings.RepairingSettings;

import java.util.HashMap;
import java.util.Map;

public final class MythicRepairingSettings implements RepairingSettings {

    private boolean playSounds;
    private boolean cancelMcMMORepair;

    @Override
    public boolean isCancelMcMMORepair() {
        return cancelMcMMORepair;
    }

    @Override
    @Deprecated
    public boolean isEnabled() {
        return true;
    }

    @Deprecated
    public void setEnabled(boolean enabled) {
        // do nothing
    }

    @Override
    public boolean isPlaySounds() {
        return playSounds;
    }

    public void setPlaySounds(boolean playSounds) {
        this.playSounds = playSounds;
    }

    public void setCancelMcMMORepair(boolean cancelMcMMORepair) {
        this.cancelMcMMORepair = cancelMcMMORepair;
    }

}
