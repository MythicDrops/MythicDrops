package com.conventnunnery.plugins.mythicdrops.items;

import com.conventnunnery.plugins.mythicdrops.api.items.CustomItem;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MythicCustomItem implements CustomItem {

    private String name;
    private String displayName;
    private List<String> lore;
    private Map<Enchantment, Integer> enchantments;
    private MaterialData matData;
    private double chanceToBeGivenToAMonster;
    private double chanceToDropOnMonsterDeath;

    public MythicCustomItem(String name, String displayName, List<String> lore,
                            Map<Enchantment, Integer> enchantments, MaterialData matData, double chanceToBeGivenToAMonster,
                            double chanceToDropOnMonsterDeath) {
        this.name = name;
        this.displayName = displayName;
        this.lore = lore;
        this.enchantments = enchantments;
        this.matData = matData;
        this.chanceToBeGivenToAMonster = chanceToBeGivenToAMonster;
        this.chanceToDropOnMonsterDeath = chanceToDropOnMonsterDeath;
    }

    public void setChanceToDropOnMonsterDeath(double chanceToDropOnMonsterDeath) {
        this.chanceToDropOnMonsterDeath = chanceToDropOnMonsterDeath;
    }

    @Override
    public double getChanceToBeGivenToAMonster() {
        return chanceToBeGivenToAMonster;
    }

    @Override
    public double getChanceToDropOnDeath() {
        return chanceToDropOnMonsterDeath;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDisplayName() {
        return displayName != null ? displayName : name;
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
        return matData;
    }

    @Override
    public ItemStack toItemStack() {
        ItemStack is = getMaterialData().toItemStack(1);
        ItemMeta im;
        if (is.hasItemMeta()) {
            im = is.getItemMeta();
        } else {
            im = Bukkit.getItemFactory().getItemMeta(is.getType());
        }
        String displayName = getDisplayName();
        if (displayName.contains("&")) {
            displayName = displayName.replace('&', '\u00A7').replace("\u00A7\u00A7", "&");
        }
        im.setDisplayName(displayName);
        List<String> lore = new ArrayList<String>();
        for (String s : getLore()) {
            lore.add(s.replace('&', '\u00A7').replace("\u00A7\u00A7", "&"));
        }
        im.setLore(lore);
        is.setItemMeta(im);
        is.addUnsafeEnchantments(getEnchantments());
        return is;
    }

    public void setMaterialData(MaterialData matData) {
        this.matData = matData;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public void setEnchantments(Map<Enchantment, Integer> enchantments) {
        this.enchantments = enchantments;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setChanceToBeGivenToAMonster(double chanceToBeGivenToAMonster) {
        this.chanceToBeGivenToAMonster = chanceToBeGivenToAMonster;
    }
}
