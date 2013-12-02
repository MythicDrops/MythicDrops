package net.nunnerycode.bukkit.mythicdrops.names.builders;

import net.nunnerycode.bukkit.mythicdrops.MythicDropsPlugin;
import net.nunnerycode.bukkit.mythicdrops.api.items.MythicItemStack;
import net.nunnerycode.bukkit.mythicdrops.api.names.builders.NameFactory;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import net.nunnerycode.bukkit.mythicdrops.items.TierMap;

public final class MythicNameBuilder implements NameFactory {

	private String format;
	private MythicItemStack mythicItemStack;
	private Tier tier;

	public MythicNameBuilder() {
		format = MythicDropsPlugin.getInstance().getConfigSettings().getItemDisplayNameFormat();
		mythicItemStack = null;
		tier = null;
	}

	@Override
	public NameFactory withFormat(String format) {
		this.format = format;
		return this;
	}

	@Override
	public NameFactory withMythicItemStack(MythicItemStack mis) {
		this.mythicItemStack = mis;
		return this;
	}

	@Override
	public NameFactory withTier(Tier t) {
		this.tier = t;
		return this;
	}

	@Override
	public NameFactory withTier(String tierName) {
		this.tier = TierMap.getInstance().get(tierName);
		return this;
	}

	@Override
	public String build() {
		// TODO: write a name building method
		return null;
	}
}
