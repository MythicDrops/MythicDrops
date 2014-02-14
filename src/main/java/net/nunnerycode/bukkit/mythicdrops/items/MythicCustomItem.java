package net.nunnerycode.bukkit.mythicdrops.items;

import net.nunnerycode.bukkit.mythicdrops.api.items.CustomItem;
import net.nunnerycode.bukkit.mythicdrops.api.items.MythicItemStack;

import org.bukkit.Material;
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
  private Material material;
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
  @Deprecated
  public MaterialData getMaterialData() {
    return new MaterialData(material);
  }

  @Deprecated
  public void setMaterialData(MaterialData materialData) {
    // do nothing
  }

  public Material getMaterial() {
    return this.material;
  }

  /**
   * Converts the CustomItem to an {@link org.bukkit.inventory.ItemStack}.
   *
   * @return CustomItem as an ItemStack
   */
  @Override
  public ItemStack toItemStack() {
    return new MythicItemStack(material, 1, (short) 0, displayName, lore,
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

    return !(displayName != null ? !displayName.equals(that.displayName) : that.displayName != null)
           && !(enchantments != null ? !enchantments.equals(that.enchantments)
                                     : that.enchantments != null) && !(lore != null ? !lore
        .equals(that.lore) : that.lore != null) && !(material != null ? !material
        .equals(that.material) : that.material != null) && !(name != null ? !name.equals(that.name)
                                                                          : that.name != null);
  }

  @Override
  public int hashCode() {
    int result;
    result = name != null ? name.hashCode() : 0;
    result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
    result = 31 * result + (enchantments != null ? enchantments.hashCode() : 0);
    result = 31 * result + (lore != null ? lore.hashCode() : 0);
    result = 31 * result + (material != null ? material.hashCode() : 0);
    return result;
  }

  @Override
  public boolean isBroadcastOnFind() {
    return broadcastOnFind;
  }

  public void setBroadcastOnFind(boolean broadcastOnFind) {
    this.broadcastOnFind = broadcastOnFind;
  }

  public void setMaterial(Material material) {
    this.material = material;
  }
}
