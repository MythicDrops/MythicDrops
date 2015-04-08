package com.tealcube.minecraft.bukkit.mythicdrops.settings;

/*
 * #%L
 * MythicDrops
 * %%
 * Copyright (C) 2013 - 2015 TealCube
 * %%
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
 * provided that the above copyright notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
 * INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF
 * THIS SOFTWARE.
 * #L%
 */


import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.RepairingSettings;

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
