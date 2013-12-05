package net.nunnerycode.bukkit.mythicdrops.items;

import java.util.List;
import java.util.Map;
import net.nunnerycode.bukkit.mythicdrops.api.items.CustomItem;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class MythicCustomItem implements CustomItem {

	private final String name;
	private double chanceToBeGivenToAMonster;
	private double chanceToDropOnDeath;
	private String displayName;
	private Map<Enchantment, Integer> enchantments;
	private List<String> lore;
	private MaterialData materialData;

	public MythicCustomItem(String name) {
		this.name = name;
	}

	@Override
	public double getChanceToBeGivenToAMonster() {
		return chanceToBeGivenToAMonster;
	}

	@Override
	public double getChanceToDropOnDeath() {
		return chanceToDropOnDeath;
	}

	void setChanceToDropOnDeath(double chanceToDropOnDeath) {
		this.chanceToDropOnDeath = chanceToDropOnDeath;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDisplayName() {
		return displayName;
	}

	@Override
	public Map<Enchantment, Integer> getEnchantments() {
		return enchantments;
	}

	@Override
	public List<String> getLore() {
		return lore;
	}

	@Override
	public MaterialData getMaterialData() {
		return materialData;
	}

	void setMaterialData(MaterialData materialData) {
		this.materialData = materialData;
	}

	/**
	 * Converts the CustomItem to an {@link org.bukkit.inventory.ItemStack}.
	 *
	 * @return CustomItem as an ItemStack
	 */
	@Override
	public ItemStack toItemStack() {
		return null;
	}

	void setLore(List<String> lore) {
		this.lore = lore;
	}

	void setEnchantments(Map<Enchantment, Integer> enchantments) {
		this.enchantments = enchantments;
	}

	void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	void setChanceToBeGivenToAMonster(double chanceToBeGivenToAMonster) {
		this.chanceToBeGivenToAMonster = chanceToBeGivenToAMonster;
	}
}
