package net.nunnerycode.bukkit.mythicdrops.socketting;

import net.nunnerycode.bukkit.mythicdrops.api.socketting.EffectTarget;
import net.nunnerycode.bukkit.mythicdrops.api.socketting.SocketEffect;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class SocketPotionEffect implements SocketEffect {

	private final PotionEffectType potionEffectType;
	private final int intensity;
	private final int duration;
	private final int radius;
	private final EffectTarget effectTarget;
	private final boolean affectsWielder;
	private final boolean affectsTarget;
	private static final int MS_PER_TICK = 50;

	public SocketPotionEffect(PotionEffectType potionEffectType, int intensity, int duration,
							  int radius, EffectTarget effectTarget, boolean affectsWielder, boolean affectsTarget) {
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
		target.addPotionEffect(new PotionEffect(potionEffectType, duration / MS_PER_TICK, intensity), true);
	}
}
