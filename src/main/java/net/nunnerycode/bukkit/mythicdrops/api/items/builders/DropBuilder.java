package net.nunnerycode.bukkit.mythicdrops.api.items.builders;

import net.nunnerycode.bukkit.mythicdrops.api.items.ItemGenerationReason;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public interface DropBuilder {

  DropBuilder withTier(Tier tier);

  DropBuilder withTier(String tierName);

  DropBuilder withMaterial(Material material);

  @Deprecated
  DropBuilder withMaterialData(MaterialData materialData);

  @Deprecated
  DropBuilder withMaterialData(String materialDataString);

  DropBuilder withItemGenerationReason(ItemGenerationReason reason);

  @Deprecated
  DropBuilder inWorld(World world);

  @Deprecated
  DropBuilder inWorld(String worldName);

  DropBuilder useDurability(boolean b);

  ItemStack build();

}
