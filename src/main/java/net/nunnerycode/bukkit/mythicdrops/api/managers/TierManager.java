package net.nunnerycode.bukkit.mythicdrops.api.managers;

import java.util.Set;
import net.nunnerycode.bukkit.mythicdrops.api.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

public interface TierManager {

	Set<Tier> getTiersFromStringSet(Set<String> set);

	Tier getTierFromName(String name);

	Tier getTierFromDisplayName(String displayName);

	Tier getTierFromColors(ChatColor displayColor, ChatColor identificationColor);

	void debugTiers();

	Tier getFilteredRandomTier();

	Tier getFilteredRandomTierFromSet(Set<Tier> tier);

	Tier getRandomTierFromSet(Set<Tier> tiers);

	Tier getFilteredRandomTierWithChance();

	Tier getFilteredRandomTierFromSetWithChance(Set<Tier> tier);

	Tier getRandomTierFromSetWithChance(Set<Tier> tiers);

	Tier getRandomTier();

	Tier getRandomTierWithChance();

	Tier getTierFromItemStack(ItemStack itemStack);

	Set<Tier> getTiers();

	MythicDrops getPlugin();

}
