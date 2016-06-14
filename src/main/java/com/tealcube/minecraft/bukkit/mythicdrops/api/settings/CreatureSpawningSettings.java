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
package com.tealcube.minecraft.bukkit.mythicdrops.api.settings;

import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier;
import org.bukkit.entity.EntityType;

import java.util.Set;

public interface CreatureSpawningSettings {

    boolean isPreventSpawner();

    boolean isPreventSpawnEgg();

    boolean isPreventCustom();

    double getEntityTypeChanceToSpawn(EntityType entityType);

    @Deprecated
    double getEntityTypeChanceToSpawn(EntityType entityType, String worldName);

    Set<Tier> getEntityTypeTiers(EntityType entityType);

    @Deprecated
    Set<Tier> getEntityTypeTiers(EntityType entityType, String worldName);

    int getSpawnHeightLimit(String worldName);

    @Deprecated
    boolean isEnabled();

    boolean isPreventReinforcements();
}
