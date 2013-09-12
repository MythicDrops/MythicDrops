package net.nunnerycode.bukkit.mythicdrops.savers;

import com.conventnunnery.libraries.config.ConventConfiguration;
import net.nunnerycode.bukkit.mythicdrops.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.items.MythicEnchantment;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import net.nunnerycode.bukkit.mythicdrops.api.utils.MythicSaver;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MythicTierSaver implements MythicSaver {

	private MythicDrops plugin;

	public MythicTierSaver(MythicDrops plugin) {
		this.plugin = plugin;
	}

	@Override
	public synchronized void save() {
		ConventConfiguration c = getPlugin().getTierYAML();
		if (c == null) {
			return;
		}
		FileConfiguration fc = c.getFileConfiguration();
		Iterator<Tier> iterator = getPlugin().getTierManager().getTiers().iterator();
		while (iterator.hasNext()) {
			Tier t = iterator.next();
			fc.set(t.getTierName() + ".displayName", t.getTierDisplayName());
			fc.set(t.getTierName() + ".displayColor", t.getTierDisplayColor().name());
			fc.set(t.getTierName() + ".identifierColor", t.getTierIdentificationColor().name());
			fc.set(t.getTierName() + ".enchantments.safeBaseEnchantments", t.isSafeBaseEnchantments());
			fc.set(t.getTierName() + ".enchantments.safeBonusEnchantments", t.isSafeBonusEnchantments());
			fc.set(t.getTierName() + ".enchantments.allowHighBaseEnchantments", t.isAllowHighBaseEnchantments());
			fc.set(t.getTierName() + ".enchantments.allowHighBonusEnchantments", t.isAllowHighBonusEnchantments());
			fc.set(t.getTierName() + ".enchantments.minimumBonusEnchantments", t.getMinimumAmountOfBonusEnchantments());
			fc.set(t.getTierName() + ".enchantments.maximumBonusEnchantments", t.getMinimumAmountOfBonusEnchantments
					());
			List<String> enchantments1 = new ArrayList<String>();
			Iterator<MythicEnchantment> iterator1 = t.getBaseEnchantments().iterator();
			while (iterator1.hasNext()) {
				MythicEnchantment me = iterator1.next();
				enchantments1.add(me.toConfigString());
			}
			if (!enchantments1.isEmpty()) {
				fc.set(t.getTierName() + ".enchantments.baseEnchantments", enchantments1);
			} else {
				fc.set(t.getTierName() + ".enchantments.baseEnchantments", null);
			}
			List<String> enchantments2 = new ArrayList<String>();
			Iterator<MythicEnchantment> iterator2 = t.getBonusEnchantments().iterator();
			while (iterator2.hasNext()) {
				MythicEnchantment me = iterator2.next();
				enchantments2.add(me.toConfigString());
			}
			if (!enchantments2.isEmpty()) {
				fc.set(t.getTierName() + ".enchantments.bonusEnchantments", enchantments2);
			} else {
				fc.set(t.getTierName() + ".enchantments.bonusEnchantments", null);
			}
			fc.set(t.getTierName() + ".chanceToSpawnOnAMonster", t.getChanceToSpawnOnAMonster());
			fc.set(t.getTierName() + ".chanceToDropOnMonsterDeath", t.getChanceToDropOnMonsterDeath());
			fc.set(t.getTierName() + ".minimumDurability", t.getMinimumDurabilityPercentage());
			fc.set(t.getTierName() + ".maximumDurability", t.getMaximumDurabilityPercentage());
			if (!t.getAllowedGroups().isEmpty()) {
				fc.set(t.getTierName() + ".itemTypes.allowedGroups", new ArrayList<String>(t.getAllowedGroups()));
			} else {
				fc.set(t.getTierName() + ".itemTypes.allowedGroups", null);
			}
			if (!t.getDisallowedGroups().isEmpty()) {
				fc.set(t.getTierName() + ".itemTypes.disallowedGroups", new ArrayList<String>(t.getDisallowedGroups()));
			} else {
				fc.set(t.getTierName() + ".itemTypes.disallowedGroups", null);
			}
			if (!t.getAllowedIds().isEmpty()) {
				fc.set(t.getTierName() + ".itemTypes.allowedItemIds", new ArrayList<String>(t.getAllowedIds()));
			} else {
				fc.set(t.getTierName() + ".itemTypes.allowedItemIds", null);
			}
			if (!t.getDisallowedIds().isEmpty()) {
				fc.set(t.getTierName() + ".itemTypes.disallowedItemIds", new ArrayList<String>(t.getDisallowedIds()));
			} else {
				fc.set(t.getTierName() + ".itemTypes.disallowedItemIds", null);
			}
		}
		c.save();
	}

	public MythicDrops getPlugin() {
		return plugin;
	}
}
