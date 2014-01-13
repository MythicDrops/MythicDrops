package net.nunnerycode.bukkit.mythicdrops.api.names;

public enum NameType {
	ENCHANTMENT_PREFIX("enchantment.prefix."), ENCHANTMENT_SUFFIX("enchantment.suffix."),
	ENCHANTMENT_LORE("enchantment.lore."), GENERAL_PREFIX("general.prefix"), GENERAL_SUFFIX("general.suffix"),
	GENERAL_LORE("general.lore"), MATERIAL_PREFIX("material.prefix."), MATERIAL_SUFFIX("material.suffix."),
	MATERIAL_LORE("material.lore."), TIER_PREFIX("tier.prefix."), TIER_SUFFIX("tier.suffix."),
	TIER_LORE("tier.lore."), MOB_NAME("mob.name");
	private final String format;

	private NameType(String format) {
		this.format = format;
	}

	public String getFormat() {
		return format;
	}
}
