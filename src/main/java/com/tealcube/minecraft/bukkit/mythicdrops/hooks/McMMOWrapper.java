/**
 * The MIT License
 * Copyright (c) 2013 Teal Cube Games
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
package com.tealcube.minecraft.bukkit.mythicdrops.hooks;

import com.gmail.nossr50.events.skills.repair.McMMOPlayerRepairCheckEvent;
import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDrops;
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.TierUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class McMMOWrapper implements Listener {

    private MythicDrops mythicDrops;

    public McMMOWrapper(MythicDrops mythicDrops) {
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
