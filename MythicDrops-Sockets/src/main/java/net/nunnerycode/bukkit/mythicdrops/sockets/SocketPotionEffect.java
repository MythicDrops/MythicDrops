package net.nunnerycode.bukkit.mythicdrops.sockets;

import org.bukkit.potion.PotionEffectType;

public class SocketPotionEffect {

	private final PotionEffectType potionEffectType;
	private final int intensity;
	private final int duration;
	private final int radius;
	private final EffectTarget effectTarget;
	private final boolean affectsWielder;
	private final boolean affectsTarget;

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

	public int getIntensity() {
		return intensity;
	}

	public int getDuration() {
		return duration;
	}

	public EffectTarget getEffectTarget() {
		return effectTarget;
	}

	public int getRadius() {
		return radius;
	}

	public boolean isAffectsWielder() {
		return affectsWielder;
	}

	public boolean isAffectsTarget() {
		return affectsTarget;
	}
}
