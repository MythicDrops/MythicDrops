package net.nunnerycode.bukkit.mythicdrops.settings;

import net.nunnerycode.bukkit.mythicdrops.api.settings.RepairingSettings;

public final class MythicRepairingSettings implements RepairingSettings {

    private boolean playSounds;
    private boolean cancelMcMMORepair;

    @Override
    public boolean isCancelMcMMORepair() {
        return cancelMcMMORepair;
    }

    public void setCancelMcMMORepair(boolean cancelMcMMORepair) {
        this.cancelMcMMORepair = cancelMcMMORepair;
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

}
