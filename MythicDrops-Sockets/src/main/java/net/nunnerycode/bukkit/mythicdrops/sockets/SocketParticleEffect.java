package net.nunnerycode.bukkit.mythicdrops.sockets;

import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Effect;
import org.bukkit.entity.LivingEntity;

public class SocketParticleEffect implements SocketEffect {

	private final Effect particleEffect;
	private final int intensity;
	private final int duration;
	private final int radius;
	private final EffectTarget effectTarget;
	private final boolean affectsWielder;
	private final boolean affectsTarget;
	private static final int MS_PER_TICK = 50;

	public SocketParticleEffect(Effect particleEffect, int intensity, int duration,
								int radius, EffectTarget effectTarget, boolean affectsWielder, boolean affectsTarget) {
		this.particleEffect = particleEffect;
		this.intensity = intensity;
		this.duration = duration;
		this.radius = radius;
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
	public boolean isAffectsWielder() {
		return affectsWielder;
	}

	@Override
	public boolean isAffectsTarget() {
		return affectsTarget;
	}

	@Override
	public void apply(LivingEntity target) {
		if (particleEffect == null) {
			return;
		}
		target.getWorld().playEffect(target.getEyeLocation(), particleEffect, RandomUtils.nextInt(4));
	}

}
