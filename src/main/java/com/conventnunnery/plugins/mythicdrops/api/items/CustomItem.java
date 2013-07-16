package com.conventnunnery.plugins.mythicdrops.api.items;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.List;
import java.util.Map;

public interface CustomItem {

    double getChanceToBeGivenToAMonster();

    double getChanceToDropOnDeath();

    String getName();

    String getDisplayName();

    Map<Enchantment, Integer> getEnchantments();

    List<String> getLore();

    MaterialData getMaterialData();

    ItemStack toItemStack();

}
