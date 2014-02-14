package net.nunnerycode.bukkit.mythicdrops.items;

import net.nunnerycode.bukkit.mythicdrops.api.items.CustomItem;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.material.MaterialData;

import java.util.List;
import java.util.Map;

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

  @Deprecated
  public CustomItemBuilder withMaterialData(MaterialData materialData) {
    // do nothing
    return this;
  }

  public CustomItemBuilder withMaterial(Material material) {
    customItem.setMaterial(material);
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

  public CustomItemBuilder withBroadcastOnFind(boolean b) {
    customItem.setBroadcastOnFind(b);
    return this;
  }

  public CustomItem build() {
    return customItem;
  }
}
