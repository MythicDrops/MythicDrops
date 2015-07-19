package com.tealcube.minecraft.bukkit.mythicdrops.socketting;

/*
 * #%L
 * MythicDrops
 * %%
 * Copyright (C) 2013 - 2015 TealCube
 * %%
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby
 * granted,
 * provided that the above copyright notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
 * INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER
 * IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
 * PERFORMANCE OF
 * THIS SOFTWARE.
 * #L%
 */


import com.tealcube.minecraft.bukkit.mythicdrops.api.socketting.EffectTarget;
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketting.SocketEffect;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class SocketPotionEffect implements SocketEffect {

    private static final int MS_PER_TICK = 50;
    private final PotionEffectType potionEffectType;
    private final int intensity;
    private final int duration;
    private final int radius;
    private final EffectTarget effectTarget;
    private final boolean affectsWielder;
    private final boolean affectsTarget;

    public SocketPotionEffect(PotionEffectType potionEffectType, int intensity, int duration,
                              int radius, EffectTarget effectTarget, boolean affectsWielder,
                              boolean affectsTarget) {
        this.potionEffectType = potionEffectType;
        this.intensity = intensity;
        this.duration = duration;
        this.radius = radius;
        this.effectTarget = effectTarget;
        this.affectsWielder = affectsWielder;
        this.affectsTarget = affectsTarget;
    }

    public PotionEffectType getPotionEffectType() {
        return potionEffectType;
    }

    @Override
    public int getIntensity() {
        return intensity;
    }

    @Override
    public int getDuration() {
        return duration;
    }

    @Override
    public EffectTarget getEffectTarget() {
        return effectTarget;
    }

    @Override
    public int getRadius() {
        return radius;
    }

    @Override
    public boolean isAffectsWielder() {
        return affectsWielder;
    }

    @Override
    public boolean isAffectsTarget() {
        return affectsTarget;
    }

    @Override
    public void apply(LivingEntity target) {
        if (potionEffectType == null) {
            return;
        }
        int dur = duration / MS_PER_TICK;
        if (target.hasPotionEffect(potionEffectType)) {
            PotionEffect potionEffect = null;
            for (PotionEffect potEff : target.getActivePotionEffects()) {
                if (potEff.getType() == potionEffectType) {
                    potionEffect = potEff;
                    break;
                }
            }
            if (potionEffect != null) {
                dur += potionEffect.getDuration();
            }
        }
        target.addPotionEffect(new PotionEffect(potionEffectType, dur, intensity));
    }

    @Override
    public int hashCode() {
        int result = potionEffectType != null ? potionEffectType.hashCode() : 0;
        result = 31 * result + intensity;
        result = 31 * result + duration;
        result = 31 * result + radius;
        result = 31 * result + (effectTarget != null ? effectTarget.hashCode() : 0);
        result = 31 * result + (affectsWielder ? 1 : 0);
        result = 31 * result + (affectsTarget ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SocketPotionEffect that = (SocketPotionEffect) o;

        if (affectsTarget != that.affectsTarget) {
            return false;
        }
        if (affectsWielder != that.affectsWielder) {
            return false;
        }
        if (duration != that.duration) {
            return false;
        }
        if (intensity != that.intensity) {
            return false;
        }
        if (radius != that.radius) {
            return false;
        }
        if (effectTarget != that.effectTarget) {
            return false;
        }
        return !(potionEffectType != null ? !potionEffectType.equals(that.potionEffectType)
                                          : that.potionEffectType != null);
    }
}
