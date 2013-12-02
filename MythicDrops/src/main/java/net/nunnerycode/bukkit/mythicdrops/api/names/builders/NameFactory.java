package net.nunnerycode.bukkit.mythicdrops.api.names.builders;

import net.nunnerycode.bukkit.mythicdrops.api.items.MythicItemStack;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;

public interface NameFactory {

	NameFactory withFormat(String format);

	NameFactory withMythicItemStack(MythicItemStack mis);

	NameFactory withTier(Tier t);

	NameFactory withTier(String tierName);

	String build();

}
