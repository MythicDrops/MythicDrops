package net.nunnerycode.bukkit.mythicdrops.api.names;

public enum NameType {
    ENCHANTMENT_PREFIX("enchantment.prefix."), ENCHANTMENT_SUFFIX("enchantment.suffix."),
    ENCHANTMENT_LORE("enchantment.lore."), GENERAL_PREFIX("general.prefix"), GENERAL_SUFFIX(
            "general.suffix"),
    GENERAL_LORE("general.lore"), MATERIAL_PREFIX("material.prefix."), MATERIAL_SUFFIX(
            "material.suffix."),
    MATERIAL_LORE("material.lore."), TIER_PREFIX("tier.prefix."), TIER_SUFFIX("tier.suffix."),
    TIER_LORE("tier.lore."), SPECIFIC_MOB_NAME("mobname."), GENERAL_MOB_NAME("mobname"), ITEMTYPE_PREFIX(
            "itemtype.prefix."), ITEMTYPE_SUFFIX("itemtype.suffix."), ITEMTYPE_LORE("itemtype.lore.");
    private final String format;

    private NameType(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }
}
