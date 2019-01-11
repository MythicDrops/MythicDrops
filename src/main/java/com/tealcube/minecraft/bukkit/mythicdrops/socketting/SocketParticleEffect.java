/*
 * The MIT License
 * Copyright © 2013 Richard Harrah
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
package com.tealcube.minecraft.bukkit.mythicdrops.socketting;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.tealcube.minecraft.bukkit.mythicdrops.MythicDropsPlugin;
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketting.EffectTarget;
import com.tealcube.minecraft.bukkit.mythicdrops.api.socketting.SocketEffect;
import org.apache.commons.lang3.RandomUtils;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.entity.LivingEntity;

public final class SocketParticleEffect implements SocketEffect {

  private final Effect particleEffect;
  private final int intensity;
  private final int duration;
  private final int radius;
  private final double chanceToTrigger;
  private final EffectTarget effectTarget;
  private final boolean affectsWielder;
  private final boolean affectsTarget;

  public SocketParticleEffect(Effect particleEffect, int intensity, int duration,
      int radius, double chanceToTrigger, EffectTarget effectTarget, boolean affectsWielder,
      boolean affectsTarget) {
    this.particleEffect = particleEffect;
    this.intensity = intensity;
    this.duration = duration;
    this.radius = radius;
    this.chanceToTrigger = chanceToTrigger;
    this.effectTarget = effectTarget;
    this.affectsWielder = affectsWielder;
    this.affectsTarget = affectsTarget;
  }

  public Effect getParticleEffect() {
    return particleEffect;
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
  public double getChanceToTrigger() {
    return chanceToTrigger;
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
  public void apply(final LivingEntity target) {
    if (particleEffect == null) {
      return;
    }
    if (RandomUtils.nextDouble(0D, 1D) > chanceToTrigger) {
      return;
    }
    for (int i = 0; i < duration; i++) {
      Bukkit.getScheduler()
          .scheduleSyncDelayedTask(MythicDropsPlugin.getInstance(), () -> target.getWorld()
              .playEffect(target.getEyeLocation(), particleEffect, RandomUtils.nextInt(0, 4)), i * 10L);
    }
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("particleEffect", particleEffect)
        .add("intensity", intensity)
        .add("duration", duration)
        .add("radius", radius)
        .add("chanceToTrigger", chanceToTrigger)
        .add("effectTarget", effectTarget)
        .add("affectsWielder", affectsWielder)
        .add("affectsTarget", affectsTarget)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SocketParticleEffect that = (SocketParticleEffect) o;
    return intensity == that.intensity &&
        duration == that.duration &&
        radius == that.radius &&
        Double.compare(that.chanceToTrigger, chanceToTrigger) == 0 &&
        affectsWielder == that.affectsWielder &&
        affectsTarget == that.affectsTarget &&
        particleEffect == that.particleEffect &&
        effectTarget == that.effectTarget;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(particleEffect, intensity, duration, radius, chanceToTrigger, effectTarget, affectsWielder,
        affectsTarget);
  }
}
