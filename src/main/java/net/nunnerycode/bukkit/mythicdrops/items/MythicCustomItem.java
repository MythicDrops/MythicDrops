package net.nunnerycode.bukkit.mythicdrops.items;

import net.nunnerycode.bukkit.mythicdrops.api.items.CustomItem;
import net.nunnerycode.bukkit.mythicdrops.tiers.DefaultTier;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.HashMap;
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
		if (name == null) {
			this.name = "";
		}
        this.displayName = displayName;
		if (displayName == null) {
			this.displayName = this.name;
		}
        this.lore = lore;
		if (lore == null) {
			this.lore = new ArrayList<String>();
		}
        this.enchantments = enchantments;
		if (enchantments == null) {
			this.enchantments = new HashMap<Enchantment, Integer>();
		}
        this.matData = matData;
		if (matData == null) {
			this.matData = new MaterialData(Material.AIR);
		}
        this.chanceToBeGivenToAMonster = chanceToBeGivenToAMonster;
        this.chanceToDropOnMonsterDeath = chanceToDropOnMonsterDeath;
    }

    public MythicCustomItem() {
        this.name = "";
        this.displayName = this.name;
        this.lore = new ArrayList<String>();
        this.enchantments = new HashMap<Enchantment, Integer>();
        this.matData = new MaterialData(Material.AIR);
        this.chanceToBeGivenToAMonster = 1.0;
        this.chanceToDropOnMonsterDeath = 1.0;
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
            displayName = DefaultTier.CUSTOM_ITEM.getTierDisplayColor() + displayName.replace('&',
                    '\u00A7').replace("\u00A7\u00A7", "&") + DefaultTier.CUSTOM_ITEM.getTierIdentificationColor();
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof MythicCustomItem)) return false;

		MythicCustomItem that = (MythicCustomItem) o;

		if (Double.compare(that.chanceToBeGivenToAMonster, chanceToBeGivenToAMonster) != 0) return false;
		if (Double.compare(that.chanceToDropOnMonsterDeath, chanceToDropOnMonsterDeath) != 0) return false;
		if (!displayName.equals(that.displayName)) return false;
		if (!enchantments.equals(that.enchantments)) return false;
		if (!lore.equals(that.lore)) return false;
		if (!matData.equals(that.matData)) return false;
		if (!name.equals(that.name)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result;
		long temp;
		result = name.hashCode();
		result = 31 * result + displayName.hashCode();
		result = 31 * result + lore.hashCode();
		result = 31 * result + enchantments.hashCode();
		result = 31 * result + matData.hashCode();
		temp = Double.doubleToLongBits(chanceToBeGivenToAMonster);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(chanceToDropOnMonsterDeath);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
}