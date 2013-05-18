/*
 * Copyright (c) 2013. ToppleTheNun
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.conventnunnery.plugins.mythicdrops.objects;

import com.conventnunnery.plugins.mythicdrops.objects.enums.EffectTarget;
import org.bukkit.potion.PotionEffectType;

public class SocketEffect {

    private final PotionEffectType potionEffectType;
    private final int intensity;
    private final int duration;
    private final EffectTarget effectTarget;

    public SocketEffect(PotionEffectType potionEffectType, int intensity, int duration,
                        EffectTarget effectTarget) {
        this.potionEffectType = potionEffectType;
        this.intensity = intensity;
        this.duration = duration;
        this.effectTarget = effectTarget;
    }

    public PotionEffectType getPotionEffectType() {
        return potionEffectType;
    }

    public int getIntensity() {
        return intensity;
    }

    public int getDuration() {
        return duration;
    }

    public EffectTarget getEffectTarget() {
        return effectTarget;
    }


}
