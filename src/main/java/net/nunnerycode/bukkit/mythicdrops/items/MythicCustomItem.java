package net.nunnerycode.bukkit.mythicdrops.items;

import net.nunnerycode.bukkit.mythicdrops.api.items.CustomItem;
import net.nunnerycode.bukkit.mythicdrops.api.items.MythicItemStack;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.List;
import java.util.Map;

public final class MythicCustomItem implements CustomItem {

  private final String name;
  private double chanceToBeGivenToAMonster;
  private double chanceToDropOnDeath;
  private String displayName;
  private Map<Enchantment, Integer> enchantments;
  private List<String> lore;
  private MaterialData materialData;
  private boolean broadcastOnFind;

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
    return new MythicItemStack(materialData.getItemType(), 1, (short) 0, displayName, lore,
                               enchantments);
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof MythicCustomItem)) {
      return false;
    }

    MythicCustomItem that = (MythicCustomItem) o;

    if (displayName != null ? !displayName.equals(that.displayName) : that.displayName != null) {
      return false;
    }
    if (enchantments != null ? !enchantments.equals(that.enchantments)
                             : that.enchantments != null) {
      return false;
    }
    if (lore != null ? !lore.equals(that.lore) : that.lore != null) {
      return false;
    }
    if (materialData != null ? !materialData.equals(that.materialData)
                             : that.materialData != null) {
      return false;
    }
    if (name != null ? !name.equals(that.name) : that.name != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    result = name != null ? name.hashCode() : 0;
    result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
    result = 31 * result + (enchantments != null ? enchantments.hashCode() : 0);
    result = 31 * result + (lore != null ? lore.hashCode() : 0);
    result = 31 * result + (materialData != null ? materialData.hashCode() : 0);
    return result;
  }

  @Override
  public boolean isBroadcastOnFind() {
    return broadcastOnFind;
  }

  public void setBroadcastOnFind(boolean broadcastOnFind) {
    this.broadcastOnFind = broadcastOnFind;
  }
}
