package net.nunnerycode.bukkit.mythicdrops.settings;

import net.nunnerycode.bukkit.mythicdrops.api.settings.IdentifyingSettings;

import java.util.ArrayList;
import java.util.List;

public final class MythicIdentifyingSettings implements IdentifyingSettings {

    private String identityTomeName;
    private List<String> identityTomeLore;
    private String unidentifiedItemName;
    private List<String> unidentifiedItemLore;

    public MythicIdentifyingSettings() {
        identityTomeLore = new ArrayList<>();
        unidentifiedItemLore = new ArrayList<>();
    }

    @Override
    public String getIdentityTomeName() {
        return identityTomeName;
    }

    public void setIdentityTomeName(String identityTomeName) {
        this.identityTomeName = identityTomeName;
    }

    @Override
    public List<String> getIdentityTomeLore() {
        return identityTomeLore;
    }

    public void setIdentityTomeLore(List<String> identityTomeLore) {
        this.identityTomeLore = identityTomeLore;
    }

    @Override
    public String getUnidentifiedItemName() {
        return unidentifiedItemName;
    }

    public void setUnidentifiedItemName(String unidentifiedItemName) {
        this.unidentifiedItemName = unidentifiedItemName;
    }

    @Override
    public List<String> getUnidentifiedItemLore() {
        return unidentifiedItemLore;
    }

    public void setUnidentifiedItemLore(List<String> unidentifiedItemLore) {
        this.unidentifiedItemLore = unidentifiedItemLore;
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

}
