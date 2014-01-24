package net.nunnerycode.bukkit.mythicdrops.armorsets;

import net.nunnerycode.bukkit.mythicdrops.api.armorsets.ArmorSet;
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
				case 1:
					socketEffects.addAll(entry.getKey().getOneItemEffects());
					break;
				case 2:
					socketEffects.addAll(entry.getKey().getTwoItemEffects());
					break;
				case 3:
					socketEffects.addAll(entry.getKey().getThreeItemEffects());
					break;
				case 4:
					socketEffects.addAll(entry.getKey().getFourItemEffects());
					break;
				case 5:
					socketEffects.addAll(entry.getKey().getFiveItemEffects());
					break;
				default:
					break;
			}
			for (SocketEffect se : socketEffects) {
				se.apply(attacker);
			}
		}

		for (Map.Entry<ArmorSet, Integer> entry : defendArmorSets.entrySet()) {
			List<SocketEffect> socketEffects = new ArrayList<>();
			switch (entry.getValue()) {
				case 1:
					socketEffects.addAll(entry.getKey().getOneItemEffects());
					break;
				case 2:
					socketEffects.addAll(entry.getKey().getTwoItemEffects());
					break;
				case 3:
					socketEffects.addAll(entry.getKey().getThreeItemEffects());
					break;
				case 4:
					socketEffects.addAll(entry.getKey().getFourItemEffects());
					break;
				case 5:
					socketEffects.addAll(entry.getKey().getFiveItemEffects());
					break;
				default:
					break;
			}
			for (SocketEffect se : socketEffects) {
				se.apply(defender);
			}
		}
	}

}
