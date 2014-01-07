package net.nunnerycode.bukkit.mythicdrops.sockets;

import net.nunnerycode.bukkit.mythicdrops.api.sockets.GemType;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.enchantments.Enchantment;

import java.util.List;
import java.util.Map;

public class SocketGem {

	private final String name;
	private final GemType gemType;
	private final List<SocketPotionEffect> socketPotionEffects;
	private final List<SocketParticleEffect> socketParticleEffects;
	private final double chance;
	private final String prefix;
	private final String suffix;
	private final List<String> lore;
	private final Map<Enchantment, Integer> enchantments;
	private final List<SocketCommand> commands;

	public SocketGem(String name, GemType gemType, List<SocketPotionEffect> socketPotionEffects,
					 List<SocketParticleEffect> socketParticleEffects, double chance, String prefix, String suffix,
					 List<String> lore, Map<Enchantment,  Integer> enchantments, List<SocketCommand> commands) {
		this.name = name;
		this.gemType = gemType;
		this.socketPotionEffects = socketPotionEffects;
		this.socketParticleEffects = socketParticleEffects;
		this.chance = chance;
		this.prefix = prefix;
		this.suffix = suffix;
		this.lore = lore;
		this.enchantments = enchantments;
		this.commands = commands;
	}

	public List<SocketCommand> getCommands() {
		return commands;
	}

	public Map<Enchantment, Integer> getEnchantments() {
		return enchantments;
	}

	public String getName() {
		return name;
	}

	public GemType getGemType() {
		return gemType;
	}

	public String getPresentableType() {
		return WordUtils.capitalize(getGemType().getName());
	}

	public List<SocketPotionEffect> getSocketPotionEffects() {
		return socketPotionEffects;
	}

	public double getChance() {
		return chance;
	}

	public String getPrefix() {
		return prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public List<String> getLore() {
		return lore;
	}

	public List<SocketParticleEffect> getSocketParticleEffects() {
		return socketParticleEffects;
	}
}
