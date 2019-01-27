/*
 * The MIT License
 * Copyright © 2013 Richard Harrah
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.tealcube.minecraft.bukkit.mythicdrops.items;

import com.google.common.base.Joiner;
import com.tealcube.minecraft.bukkit.mythicdrops.MythicDropsPlugin;
import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDrops;
import com.tealcube.minecraft.bukkit.mythicdrops.api.enchantments.MythicEnchantment;
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGenerationReason;
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.MythicItemStack;
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.NonrepairableItemStack;
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.builders.DropBuilder;
import com.tealcube.minecraft.bukkit.mythicdrops.api.names.NameType;
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier;
import com.tealcube.minecraft.bukkit.mythicdrops.events.RandomItemGenerationEvent;
import com.tealcube.minecraft.bukkit.mythicdrops.logging.MythicLoggerFactory;
import com.tealcube.minecraft.bukkit.mythicdrops.names.NameMap;
import com.tealcube.minecraft.bukkit.mythicdrops.tiers.TierMap;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.ItemStackUtil;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.ItemUtil;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.RandomRangeUtil;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.StringUtil;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.TemplatingUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import net.nunnerycode.bukkit.libraries.ivory.collections.IvoryStringList;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.text.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;

public final class MythicDropBuilder implements DropBuilder {

  private static final Logger LOGGER = MythicLoggerFactory.getLogger(MythicDropBuilder.class);
  private MythicDrops mythicDrops;
  private Tier tier;
  private Material material;
  private ItemGenerationReason itemGenerationReason;
  private boolean useDurability;
  private boolean callEvent;

  public MythicDropBuilder(MythicDrops mythicDrops) {
    this.mythicDrops = mythicDrops;
    tier = null;
    itemGenerationReason = ItemGenerationReason.DEFAULT;
    useDurability = false;
    callEvent = true;
  }

  @Override
  public DropBuilder withTier(Tier tier) {
    this.tier = tier;
    return this;
  }

  @Override
  public DropBuilder withTier(String tierName) {
    this.tier = TierMap.INSTANCE.get(tierName);
    return this;
  }

  @Override
  public DropBuilder withMaterial(Material material) {
    this.material = material;
    return this;
  }

  @Override
  @Deprecated
  public DropBuilder withMaterialData(MaterialData materialData) {
    return withMaterial(materialData != null ? materialData.getItemType() : Material.AIR);
  }

  @Override
  @Deprecated
  public DropBuilder withMaterialData(String materialDataString) {
    // do nothing
    return this;
  }

  @Override
  public DropBuilder withItemGenerationReason(ItemGenerationReason reason) {
    this.itemGenerationReason = reason;
    return this;
  }

  @Override
  @Deprecated
  public DropBuilder inWorld(World world) {
    // do nothing
    return this;
  }

  @Override
  @Deprecated
  public DropBuilder inWorld(String worldName) {
    // do nothing
    return this;
  }

  @Override
  public DropBuilder useDurability(boolean b) {
    this.useDurability = b;
    return this;
  }

  @Override
  public ItemStack build() {
    Tier t = (tier != null) ? tier : TierMap.INSTANCE.getRandomTierWithChance();

    if (t == null) {
      t = TierMap.INSTANCE.getRandomTierWithChance();
      if (t == null) {
        return null;
      }
    }

    tier = t;

    Material mat = material != null ? material : ItemUtil.getRandomMaterialFromCollection
        (ItemUtil.getMaterialsFromTier(t));

    if (mat == null || mat == Material.AIR) {
      return null;
    }

    MythicItemStack nis;
    if (mythicDrops.getConfigSettings().isAllowRepairingUsingAnvil()) {
      LOGGER.fine("Spawning repairable item");
      nis = new MythicItemStack(mat, 1, (short) 0, "");
    } else {
      LOGGER.fine("Spawning nonrepairable item");
      nis = new NonrepairableItemStack(mat, 1, (short) 0, "");
    }
    ItemMeta im = nis.getItemMeta();

    Map<Enchantment, Integer> baseEnchantmentMap = getBaseEnchantments(nis, t);
    Map<Enchantment, Integer> bonusEnchantmentMap = getBonusEnchantments(nis, t);

    for (Map.Entry<Enchantment, Integer> baseEnch : baseEnchantmentMap.entrySet()) {
      im.addEnchant(baseEnch.getKey(), baseEnch.getValue(), true);
    }
    for (Map.Entry<Enchantment, Integer> bonusEnch : bonusEnchantmentMap.entrySet()) {
      im.addEnchant(bonusEnch.getKey(), bonusEnch.getValue(), true);
    }

    if (useDurability) {
      ItemStackUtil
          .setDurabilityForItemStack(nis, t.getMinimumDurabilityPercentage(), t.getMaximumDurabilityPercentage());
    }
    String name = generateName(nis, im);
    im.setDisplayName(name);
    List<String> lore = generateLore(nis, im);
    im.setLore(lore);
    if (mythicDrops.getConfigSettings().isRandomizeLeatherColors() &&
        nis.getItemMeta() instanceof LeatherArmorMeta) {
      ((LeatherArmorMeta) im).setColor(Color.fromRGB(
          RandomUtils.nextInt(0, 255),
          RandomUtils.nextInt(0, 255),
          RandomUtils.nextInt(0, 255)
      ));
    }
    if (nis.getItemMeta() instanceof SkullMeta) {
      ((SkullMeta) im).setOwner("ToppleTheNun");
    }
    nis.setItemMeta(im);

    if (callEvent) {
      RandomItemGenerationEvent rige = new RandomItemGenerationEvent(t, nis, itemGenerationReason);
      Bukkit.getPluginManager().callEvent(rige);

      if (rige.isCancelled()) {
        return null;
      }

      return rige.getItemStack();
    }
    return nis;
  }

  private Map<Enchantment, Integer> getBonusEnchantments(MythicItemStack is, Tier t) {
    Validate.notNull(is, "MythicItemStack cannot be null");
    Validate.notNull(t, "Tier cannot be null");

    if (t.getBonusEnchantments() == null || t.getBonusEnchantments().isEmpty()) {
      return new HashMap<>();
    }

    Map<Enchantment, Integer> map = new HashMap<>();

    int added = 0;
    int attempts = 0;
    int range = RandomRangeUtil.randomRange(t.getMinimumBonusEnchantments(),
        t.getMaximumBonusEnchantments());
    MythicEnchantment[]
        array =
        t.getBonusEnchantments().toArray(new MythicEnchantment[t.getBonusEnchantments()
            .size()]);
    while (added < range && attempts < 10) {
      MythicEnchantment chosenEnch = array[RandomUtils.nextInt(0, array.length)];
      if (chosenEnch == null || chosenEnch.getEnchantment() == null) {
        attempts++;
        continue;
      }
      Enchantment e = chosenEnch.getEnchantment();
      int randLevel = RandomRangeUtil.randomRange(chosenEnch.getMinimumLevel(),
          chosenEnch.getMaximumLevel());
      if (map.containsKey(e)) {
        if (randLevel + map.get(e) > chosenEnch.getMaximumLevel()) {
          attempts++;
          continue;
        }
        randLevel += map.get(e);
      }
      if (t.isSafeBonusEnchantments()) {
        if (e.getItemTarget() == null) {
          attempts++;
          continue;
        }
        if (!e.getItemTarget().includes(is.getType())) {
          attempts++;
          continue;
        }
        if (!t.isAllowHighBonusEnchantments()) {
          randLevel = getAcceptableEnchantmentLevel(e, randLevel);
        }
      } else {
        if (!t.isAllowHighBonusEnchantments()) {
          randLevel = getAcceptableEnchantmentLevel(e, randLevel);
        }
      }
      map.put(e, randLevel);
      added++;
    }
    return map;
  }

  private Map<Enchantment, Integer> getBaseEnchantments(MythicItemStack is, Tier t) {
    Validate.notNull(is, "MythicItemStack cannot be null");
    Validate.notNull(t, "Tier cannot be null");

    if (t.getBaseEnchantments() == null || t.getBaseEnchantments().isEmpty()) {
      return new HashMap<>();
    }

    Map<Enchantment, Integer> map = new HashMap<>();

    for (MythicEnchantment me : t.getBaseEnchantments()) {
      if (me == null) {
        continue;
      }
      Enchantment e = me.getEnchantment();
      if (e == null) {
        continue;
      }
      int minimumLevel = Math.max(me.getMinimumLevel(), e.getStartLevel());
      int maximumLevel = Math.min(me.getMaximumLevel(), e.getMaxLevel());
      if (t.isSafeBaseEnchantments() && e.canEnchantItem(is)) {
        if (t.isAllowHighBaseEnchantments()) {
          map.put(e, RandomRangeUtil.randomRange
              (minimumLevel, me.getMaximumLevel()));
        } else {
          map.put(e, getAcceptableEnchantmentLevel(e,
              RandomRangeUtil
                  .randomRange(minimumLevel, maximumLevel)
          ));
        }
      } else if (!t.isSafeBaseEnchantments()) {
        map.put(e, RandomRangeUtil.randomRange
            (me.getMinimumLevel(), me.getMaximumLevel()));
      }
    }
    return map;
  }

  private int getAcceptableEnchantmentLevel(Enchantment ench, int level) {
    return Math.max(Math.min(level, ench.getMaxLevel()), ench.getStartLevel());
  }

  private List<String> generateLore(ItemStack itemStack, ItemMeta itemMeta) {
    IvoryStringList tempLore = new IvoryStringList();
    if (itemStack == null || tier == null || itemMeta == null) {
      return tempLore;
    }
    List<String> tooltipFormat = mythicDrops.getConfigSettings().getTooltipFormat();

    String minecraftName = getMinecraftMaterialName(itemStack.getData().getItemType());
    String mythicName = getMythicMaterialName(itemStack.getType());
    String itemType = getItemTypeName(ItemUtil.getItemTypeFromMaterial(itemStack.getType()));
    String materialType = getItemTypeName(ItemUtil.getMaterialTypeFromMaterial(itemStack.getType()));
    String tierName = tier.getDisplayName();
    String enchantment = getEnchantmentTypeName(itemMeta);
    String generalLoreString = NameMap.getInstance().getRandom(NameType.GENERAL_LORE, "");
    String materialLoreString = NameMap.getInstance().getRandom(NameType.MATERIAL_LORE,
        itemStack.getType().name().toLowerCase());
    String tierLoreString =
        NameMap.getInstance().getRandom(NameType.TIER_LORE, tier.getName().toLowerCase());
    String enchantmentLoreString = NameMap.getInstance().getRandom(NameType.ENCHANTMENT_LORE,
        enchantment != null ? enchantment.toLowerCase()
            : "");
    String itemTypeLoreString = NameMap.getInstance().getRandom(NameType.ITEMTYPE_LORE,
        ItemUtil
            .getItemTypeFromMaterial(
                itemStack.getType()));

    List<String> generalLore = Arrays.asList(generalLoreString.split("/n"));
    List<String> materialLore = Arrays.asList(materialLoreString.split("/n"));
    List<String> tierLore = Arrays.asList(tierLoreString.split("/n"));
    List<String> enchantmentLore = Arrays.asList(enchantmentLoreString.split("/n"));
    List<String> itemTypeLore = Arrays.asList(itemTypeLoreString.split("/n"));

    tempLore.addAll(tooltipFormat);

    List<String> baseLore = new ArrayList<>();
    for (String s : tier.getBaseLore()) {
      String[] strings = s.split("/n");
      baseLore.addAll(Arrays.asList(strings));
    }

    List<String> bonusLore = new ArrayList<>();
    int numOfBonusLore = RandomRangeUtil.randomRange(tier.getMinimumBonusLore(),
        tier.getMaximumBonusLore());
    List<String> chosenLore = new ArrayList<>();
    for (int i = 0; i < numOfBonusLore; i++) {
      if (tier.getBonusLore() == null || tier.getBonusLore().isEmpty() || chosenLore.size() == tier
          .getBonusLore().size()) {
        continue;
      }
      // choose a random String out of the tier's bonus lore
      String s = tier.getBonusLore().get(RandomUtils.nextInt(0, tier.getBonusLore().size()));
      if (chosenLore.contains(s)) {
        i--;
        continue;
      }
      chosenLore.add(s);
      // split on the next line /n
      String[] strings = s.split("/n");

      bonusLore.addAll(Arrays.asList(strings));
    }

    double c = MythicDropsPlugin.getInstance().getRandom().nextDouble();

    List<String> socketLore = new ArrayList<>();
    if (mythicDrops.getConfigSettings().isSockettingEnabled() && c < tier.getChanceToHaveSockets()) {
      int numberOfSockets = RandomRangeUtil.randomRange(tier.getMinimumSockets(), tier.getMaximumSockets());
      if (numberOfSockets > 0) {
        for (int i = 0; i < numberOfSockets; i++) {
          String line = mythicDrops.getSockettingSettings().getSockettedItemString();
          socketLore.add(line);
        }
        socketLore.addAll(mythicDrops.getSockettingSettings().getSockettedItemLore());
      }
    }

    List<String> relationLore = new ArrayList<>();
    for (String s : ChatColor.stripColor(itemMeta.getDisplayName()).split(" ")) {
      relationLore.addAll(mythicDrops.getRelationSettings().getLoreFromName(s));
    }

    tempLore.replaceWithList("%baselore%", baseLore);
    tempLore.replaceWithList("%generallore%", generalLore);
    tempLore.replaceWithList("%materiallore%", materialLore);
    tempLore.replaceWithList("%tierlore%", tierLore);
    tempLore.replaceWithList("%enchantmentlore%", enchantmentLore);
    tempLore.replaceWithList("%itemtypelore%", itemTypeLore);
    tempLore.replaceWithList("%baselore%", baseLore);
    tempLore.replaceWithList("%bonuslore%", bonusLore);
    tempLore.replaceWithList("%socketlore%", socketLore);
    tempLore.replaceWithList("%relationlore%", relationLore);

    String[][] args = {{"%basematerial%", minecraftName != null ? minecraftName : ""},
        {"%mythicmaterial%", mythicName != null ? mythicName : ""},
        {"%mythicmaterial%", mythicName != null ? mythicName : ""},
        {"%itemtype%", itemType != null ? itemType : ""},
        {"%materialtype%", materialType != null ? materialType : ""},
        {"%tiername%", tierName != null ? tierName : ""},
        {"%enchantment%", enchantment != null ? enchantment : ""},
        {"%tiercolor%", tier.getDisplayColor() + ""}};

    List<String> lore = new ArrayList<>();
    for (String s : tempLore) {
      lore.add(StringUtil.colorString(StringUtil.replaceArgs(s, args)));
    }

    return randomVariableReplace(lore);
  }

  private List<String> randomVariableReplace(List<String> list) {
    List<String> newList = new ArrayList<>();
    for (String s : list) {
      newList.add(TemplatingUtil.INSTANCE.template(s));
    }
    return newList;
  }

  private String getEnchantmentTypeName(ItemMeta itemMeta) {
    Enchantment enchantment = ItemStackUtil.getHighestEnchantment(itemMeta);
    if (enchantment == null) {
      return mythicDrops.getConfigSettings().getFormattedLanguageString("displayNames.Ordinary");
    }
    String ench = mythicDrops.getConfigSettings()
        .getFormattedLanguageString("displayNames." + enchantment.getName());
    if (ench != null) {
      return ench;
    }
    return "Ordinary";
  }

  private String getMythicMaterialName(Material matData) {
    String comb = matData.name();
    String
        mythicMatName =
        mythicDrops.getConfigSettings().getFormattedLanguageString(
            "displayNames." + comb);
    if (mythicMatName == null || mythicMatName.equals("displayNames." + comb)) {
      mythicMatName = getMinecraftMaterialName(matData);
    }
    return WordUtils.capitalize(mythicMatName);
  }

  private String getMinecraftMaterialName(Material material) {
    String matName = material.name();
    String[] split = matName.split("_");
    String prettyMaterialName = Joiner.on(" ").skipNulls().join(split);
    return WordUtils.capitalizeFully(prettyMaterialName);
  }

  private String getItemTypeName(String itemType) {
    if (itemType == null) {
      return "";
    }
    String
        mythicMatName =
        mythicDrops.getConfigSettings().getFormattedLanguageString(
            "displayNames." + itemType.toLowerCase());
    if (mythicMatName == null) {
      mythicMatName = itemType;
    }
    return WordUtils.capitalizeFully(mythicMatName);
  }

  private String generateName(ItemStack itemStack, ItemMeta itemMeta) {
    Validate.notNull(itemStack, "ItemStack cannot be null");
    Validate.notNull(tier, "Tier cannot be null");

    String format = mythicDrops.getConfigSettings().getItemDisplayNameFormat();
    if (format == null || format.isEmpty()) {
      return "Mythic Item";
    }
    String minecraftName = getMinecraftMaterialName(itemStack.getData().getItemType());
    String mythicName = getMythicMaterialName(itemStack.getType());
    String generalPrefix = NameMap.getInstance().getRandom(NameType.GENERAL_PREFIX, "");
    String generalSuffix = NameMap.getInstance().getRandom(NameType.GENERAL_SUFFIX, "");
    String materialPrefix = NameMap.getInstance().getRandom(NameType.MATERIAL_PREFIX,
        itemStack.getType().name().toLowerCase());
    String materialSuffix = NameMap.getInstance().getRandom(NameType.MATERIAL_SUFFIX,
        itemStack.getType().name().toLowerCase());
    String tierPrefix =
        NameMap.getInstance().getRandom(NameType.TIER_PREFIX, tier.getName().toLowerCase());
    String tierSuffix =
        NameMap.getInstance().getRandom(NameType.TIER_SUFFIX, tier.getName().toLowerCase());
    String itemType = getItemTypeName(ItemUtil.getItemTypeFromMaterial(itemStack.getType()));
    String materialType = getItemTypeName(ItemUtil.getMaterialTypeFromMaterial(itemStack.getType
        ()));
    String tierName = tier.getDisplayName();
    String enchantment = getEnchantmentTypeName(itemMeta);
    Enchantment highestEnch = ItemStackUtil.getHighestEnchantment(itemMeta);
    String enchantmentPrefix = NameMap.getInstance().getRandom(NameType.ENCHANTMENT_PREFIX,
        highestEnch != null ? highestEnch.getName()
            .toLowerCase() :
            "");
    String enchantmentSuffix = NameMap.getInstance().getRandom(NameType.ENCHANTMENT_SUFFIX,
        highestEnch != null ? highestEnch.getName()
            .toLowerCase() :
            "");
    String itemTypePrefix =
        NameMap.getInstance().getRandom(NameType.ITEMTYPE_PREFIX,
            ItemUtil.getItemTypeFromMaterial(itemStack.getType()));
    String itemTypeSuffix =
        NameMap.getInstance().getRandom(NameType.ITEMTYPE_SUFFIX,
            ItemUtil.getItemTypeFromMaterial(itemStack.getType()));

    String[][] args = {{"%basematerial%", minecraftName}, {"%mythicmaterial%", mythicName},
        {"%generalprefix%", generalPrefix}, {"%generalsuffix%", generalSuffix},
        {"%materialprefix%", materialPrefix}, {"%materialsuffix%", materialSuffix},
        {"%tierprefix%", tierPrefix}, {"%tiersuffix%", tierSuffix},
        {"%itemtypeprefix%", itemTypePrefix},
        {"%itemtypesuffix%", itemTypeSuffix}, {"%itemtype%", itemType},
        {"%materialtype%", materialType},
        {"%tiername%", tierName}, {"%enchantment%", enchantment},
        {"%enchantmentprefix%", enchantmentPrefix},
        {"%enchantmentsuffix%", enchantmentSuffix}};

    return tier.getDisplayColor() + StringUtil.colorString(StringUtil.replaceArgs(format, args)).trim()
        + tier.getIdentificationColor();
  }

}
