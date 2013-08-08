package net.nunnerycode.bukkit.mythicdrops.savers;

import com.conventnunnery.libraries.config.ConventConfiguration;
import net.nunnerycode.bukkit.mythicdrops.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.items.MythicEnchantment;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import net.nunnerycode.bukkit.mythicdrops.api.utils.MythicSaver;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class MythicTierSaver implements MythicSaver {

	private MythicDrops plugin;

	public MythicTierSaver(MythicDrops plugin) {
		this.plugin = plugin;
	}

	@Override
	public void save() {
		ConventConfiguration c = getPlugin().getConventConfigurationGroup().getConventConfiguration("tier.yml");
		if (c == null) {
			return;
		}
		FileConfiguration fc = c.getFileConfiguration();
		for (Tier t : getPlugin().getTierManager().getTiers()) {
			fc.set(t.getTierName() + ".displayName", t.getTierDisplayName());
			fc.set(t.getTierName() + ".displayColor", t.getTierDisplayColor().name());
			fc.set(t.getTierName() + ".identificationColor", t.getTierIdentificationColor().name());
			fc.set(t.getTierName() + ".enchantments.safeBaseEnchantments", t.isSafeBaseEnchantments());
			fc.set(t.getTierName() + ".enchantments.safeBonusEnchantments", t.isSafeBonusEnchantments());
			fc.set(t.getTierName() + ".enchantments.allowHighBaseEnchantments", t.isAllowHighBaseEnchantments());
			fc.set(t.getTierName() + ".enchantments.allowHighBonusEnchantments", t.isAllowHighBonusEnchantments());
			fc.set(t.getTierName() + ".enchantments.minimumBonusEnchantments", t.getMinimumAmountOfBonusEnchantments());
			fc.set(t.getTierName() + ".enchantments.maximumBonusEnchantments", t.getMinimumAmountOfBonusEnchantments
					());
			List<String> enchantments = new ArrayList<String>();
			for (MythicEnchantment me : t.getBaseEnchantments()) {
				enchantments.add(me.toConfigString());
			}
			fc.set(t.getTierName() + ".enchantments.baseEnchantments", enchantments);
			enchantments.clear();
			for (MythicEnchantment me : t.getBonusEnchantments()) {
				enchantments.add(me.toConfigString());
			}
			fc.set(t.getTierName() + ".enchantments.bonusEnchantments", enchantments);
			fc.set(t.getTierName() + ".chanceToSpawnOnAMonster", t.getChanceToSpawnOnAMonster());
			fc.set(t.getTierName() + ".chanceToDropOnMonsterDeath", t.getChanceToDropOnMonsterDeath());
			fc.set(t.getTierName() + ".minimumDurability", t.getMinimumDurabilityPercentage());
			fc.set(t.getTierName() + ".maximumDurability", t.getMaximumDurabilityPercentage());
			fc.set(t.getTierName() + ".allowedGroups", t.getAllowedGroups());
			fc.set(t.getTierName() + ".disallowedGroups", t.getDisallowedGroups());
			fc.set(t.getTierName() + ".allowedIds", t.getAllowedIds());
			fc.set(t.getTierName() + ".disallowedIds", t.getDisallowedIds());
		}
		c.save();
	}

	public MythicDrops getPlugin() {
		return plugin;
	}
}
