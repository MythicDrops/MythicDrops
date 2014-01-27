package net.nunnerycode.bukkit.mythicdrops.armorsets;

import net.nunnerycode.bukkit.mythicdrops.api.armorsets.ArmorSet;
import net.nunnerycode.bukkit.mythicdrops.api.socketting.EffectTarget;
import net.nunnerycode.bukkit.mythicdrops.api.socketting.SocketEffect;
import net.nunnerycode.bukkit.mythicdrops.utils.ArmorSetUtil;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ArmorSetListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
		if (event.isCancelled()) {
			return;
		}
		Entity e = event.getEntity();
		Entity d = event.getDamager();
		if (!(e instanceof LivingEntity)) {
			return;
		}
		LivingEntity lee = (LivingEntity) e;
		LivingEntity led;
		if (d instanceof LivingEntity) {
			led = (LivingEntity) d;
		} else if (d instanceof Projectile) {
			led = ((Projectile) d).getShooter();
		} else {
			return;
		}
		applyEffects(led, lee);
	}

	public void applyEffects(LivingEntity attacker, LivingEntity defender) {
		if (attacker == null || defender == null) {
			return;
		}
		Map<ArmorSet, Integer> attackArmorSets = ArmorSetUtil.getArmorSetsFromLivingEntity(attacker);
		Map<ArmorSet, Integer> defendArmorSets = ArmorSetUtil.getArmorSetsFromLivingEntity(defender);

		for (Map.Entry<ArmorSet, Integer> entry : attackArmorSets.entrySet()) {
			List<SocketEffect> socketEffects = new ArrayList<>();
			switch (entry.getValue()) {
				case 5:
					socketEffects.addAll(entry.getKey().getFiveItemEffects());
				case 4:
					socketEffects.addAll(entry.getKey().getFourItemEffects());
				case 3:
					socketEffects.addAll(entry.getKey().getThreeItemEffects());
				case 2:
					socketEffects.addAll(entry.getKey().getTwoItemEffects());
				case 1:
					socketEffects.addAll(entry.getKey().getOneItemEffects());
				default:
					break;
			}
			for (SocketEffect se : socketEffects) {
				if (se.getEffectTarget() == EffectTarget.SELF) {
					se.apply(attacker);
				} else if (se.getEffectTarget() == EffectTarget.OTHER) {
					se.apply(defender);
				} else if (se.getEffectTarget() == EffectTarget.AREA) {
					for (Entity e : attacker.getNearbyEntities(se.getRadius(), se.getRadius(), se.getRadius())) {
						if (!(e instanceof LivingEntity)) {
							continue;
						}
						LivingEntity le = (LivingEntity) e;
						if (le.equals(defender) && se.isAffectsTarget()) {
							se.apply(le);
							continue;
						}
						se.apply(le);
					}
					if (se.isAffectsWielder()) {
						se.apply(attacker);
					}
				}
			}
		}

		for (Map.Entry<ArmorSet, Integer> entry : defendArmorSets.entrySet()) {
			List<SocketEffect> socketEffects = new ArrayList<>();
			switch (entry.getValue()) {
				case 5:
					socketEffects.addAll(entry.getKey().getFiveItemEffects());
				case 4:
					socketEffects.addAll(entry.getKey().getFourItemEffects());
				case 3:
					socketEffects.addAll(entry.getKey().getThreeItemEffects());
				case 2:
					socketEffects.addAll(entry.getKey().getTwoItemEffects());
				case 1:
					socketEffects.addAll(entry.getKey().getOneItemEffects());
				default:
					break;
			}
			for (SocketEffect se : socketEffects) {
				if (se.getEffectTarget() == EffectTarget.SELF) {
					se.apply(defender);
				} else if (se.getEffectTarget() == EffectTarget.OTHER) {
					se.apply(attacker);
				} else if (se.getEffectTarget() == EffectTarget.AREA) {
					for (Entity e : defender.getNearbyEntities(se.getRadius(), se.getRadius(), se.getRadius())) {
						if (!(e instanceof LivingEntity)) {
							continue;
						}
						LivingEntity le = (LivingEntity) e;
						if (le.equals(attacker) && se.isAffectsTarget()) {
							se.apply(le);
							continue;
						}
						se.apply(le);
					}
					if (se.isAffectsWielder()) {
						se.apply(defender);
					}
				}
			}
		}
	}

}
