package net.nunnerycode.bukkit.mythicdrops.items;

import java.util.List;
import java.util.Map;
import net.nunnerycode.bukkit.mythicdrops.api.items.CustomItem;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.material.MaterialData;

public final class CustomItemBuilder {

	public final MythicCustomItem customItem;

	public CustomItemBuilder(String name) {
		customItem = new MythicCustomItem(name);
	}

	public CustomItemBuilder withDisplayName(String displayName) {
		customItem.setDisplayName(displayName);
		return this;
	}

	public CustomItemBuilder withLore(List<String> lore) {
		customItem.setLore(lore);
		return this;
	}

	public CustomItemBuilder withEnchantments(Map<Enchantment, Integer> enchantments) {
		customItem.setEnchantments(enchantments);
		return this;
	}

	public CustomItemBuilder withMaterialData(MaterialData materialData) {
		customItem.setMaterialData(materialData);
		return this;
	}

	public CustomItemBuilder withChanceToBeGivenToMonster(double chance) {
		customItem.setChanceToBeGivenToAMonster(chance);
		return this;
	}

	public CustomItemBuilder withChanceToDropOnDeath(double chance) {
		customItem.setChanceToDropOnDeath(chance);
		return this;
	}

	public CustomItem build() {
		return customItem;
	}
}
