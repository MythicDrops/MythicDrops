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
import com.tealcube.minecraft.bukkit.mythicdrops.ListExtensionsKt;
import com.tealcube.minecraft.bukkit.mythicdrops.MythicDropsPlugin;
import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDrops;
import com.tealcube.minecraft.bukkit.mythicdrops.api.enchantments.MythicEnchantment;
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGenerationReason;
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.builders.DropBuilder;
import com.tealcube.minecraft.bukkit.mythicdrops.api.names.NameType;
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier;
import com.tealcube.minecraft.bukkit.mythicdrops.events.RandomItemGenerationEvent;
import com.tealcube.minecraft.bukkit.mythicdrops.logging.JulLoggerFactory;
import com.tealcube.minecraft.bukkit.mythicdrops.names.NameMap;
import com.tealcube.minecraft.bukkit.mythicdrops.tiers.TierMap;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.*;
import kotlin.Pair;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.text.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public final class MythicDropBuilder implements DropBuilder {

  private static final Logger LOGGER = JulLoggerFactory.INSTANCE.getLogger(MythicDropBuilder.class);
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

    Material mat =
        material != null
            ? material
            : ItemUtil.getRandomMaterialFromCollection(ItemUtil.getMaterialsFromTier(t));

    if (mat == null || mat == Material.AIR) {
      return null;
    }

    ItemStack nis = new ItemStack(mat, 1);
    if (!mythicDrops.getConfigSettings().isAllowRepairingUsingAnvil()) {
      LOGGER.fine("Spawning nonrepairable item");
      ItemStackExtensionsKt.setRepairCost(nis);
    }

    Map<Enchantment, Integer> baseEnchantmentMap = getBaseEnchantments(nis, t);
    Map<Enchantment, Integer> bonusEnchantmentMap = getBonusEnchantments(nis, t);

    nis.addUnsafeEnchantments(baseEnchantmentMap);
    nis.addUnsafeEnchantments(bonusEnchantmentMap);

    if (useDurability) {
      ItemStackUtil.setDurabilityForItemStack(
          nis, t.getMinimumDurabilityPercentage(), t.getMaximumDurabilityPercentage());
    }

    String itemType = getItemTypeName(ItemUtil.getItemTypeFromMaterial(nis.getType()));
    String materialType = getItemTypeName(ItemUtil.getMaterialTypeFromMaterial(nis.getType()));
    String tierName = tier.getDisplayName();
    String enchantment = getEnchantmentTypeName(nis);

    String name = generateName(nis, itemType, materialType, tierName, enchantment);
    ItemStackExtensionsKt.setDisplayNameChatColorized(nis, name);
    List<String> lore = generateLore(nis, itemType, materialType, tierName, enchantment);
    ItemStackExtensionsKt.setLoreChatColorized(nis, lore);
    if (mythicDrops.getConfigSettings().isRandomizeLeatherColors()) {
      LeatherArmorUtil.INSTANCE.setRandomizedColor(nis);
    }
    SkullUtil.INSTANCE.setSkullOwner(nis);

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

  private Map<Enchantment, Integer> getBonusEnchantments(ItemStack itemStack, Tier tier) {
    Validate.notNull(itemStack, "MythicItemStack cannot be null");
    Validate.notNull(tier, "Tier cannot be null");

    if (tier.getBonusEnchantments() == null || tier.getBonusEnchantments().isEmpty()) {
      return new HashMap<>();
    }

    Map<Enchantment, Integer> map = new HashMap<>();

    int added = 0;
    int attempts = 0;
    int range =
        RandomRangeUtil.randomRange(
            tier.getMinimumBonusEnchantments(), tier.getMaximumBonusEnchantments());
    MythicEnchantment[] array = tier.getBonusEnchantments().toArray(new MythicEnchantment[0]);
    while (added < range && attempts < 10) {
      MythicEnchantment chosenEnch = array[RandomUtils.nextInt(0, array.length)];
      if (chosenEnch == null || chosenEnch.getEnchantment() == null) {
        attempts++;
        continue;
      }
      Enchantment e = chosenEnch.getEnchantment();
      int randLevel =
          RandomRangeUtil.randomRange(chosenEnch.getMinimumLevel(), chosenEnch.getMaximumLevel());
      if (map.containsKey(e)) {
        if (randLevel + map.get(e) > chosenEnch.getMaximumLevel()) {
          attempts++;
          continue;
        }
        randLevel += map.get(e);
      }
      if (tier.isSafeBonusEnchantments()) {
        if (!e.getItemTarget().includes(itemStack.getType())) {
          attempts++;
          continue;
        }
        if (!tier.isAllowHighBonusEnchantments()) {
          randLevel = getAcceptableEnchantmentLevel(e, randLevel);
        }
      } else {
        if (!tier.isAllowHighBonusEnchantments()) {
          randLevel = getAcceptableEnchantmentLevel(e, randLevel);
        }
      }
      map.put(e, randLevel);
      added++;
    }
    return map;
  }

  private Map<Enchantment, Integer> getBaseEnchantments(ItemStack itemStack, Tier tier) {
    Validate.notNull(itemStack, "MythicItemStack cannot be null");
    Validate.notNull(tier, "Tier cannot be null");

    if (tier.getBaseEnchantments() == null || tier.getBaseEnchantments().isEmpty()) {
      return new HashMap<>();
    }

    Map<Enchantment, Integer> map = new HashMap<>();

    for (MythicEnchantment me : tier.getBaseEnchantments()) {
      if (me == null) {
        continue;
      }
      Enchantment e = me.getEnchantment();
      if (e == null) {
        continue;
      }
      int minimumLevel = Math.max(me.getMinimumLevel(), e.getStartLevel());
      int maximumLevel = Math.min(me.getMaximumLevel(), e.getMaxLevel());
      if (tier.isSafeBaseEnchantments() && e.canEnchantItem(itemStack)) {
        if (tier.isAllowHighBaseEnchantments()) {
          map.put(e, RandomRangeUtil.randomRange(minimumLevel, me.getMaximumLevel()));
        } else {
          map.put(
              e,
              getAcceptableEnchantmentLevel(
                  e, RandomRangeUtil.randomRange(minimumLevel, maximumLevel)));
        }
      } else if (!tier.isSafeBaseEnchantments()) {
        map.put(e, RandomRangeUtil.randomRange(me.getMinimumLevel(), me.getMaximumLevel()));
      }
    }
    return map;
  }

  private int getAcceptableEnchantmentLevel(Enchantment ench, int level) {
    return Math.max(Math.min(level, ench.getMaxLevel()), ench.getStartLevel());
  }

  private List<String> generateLore(
      ItemStack itemStack,
      String itemType,
      String materialType,
      String tierName,
      String enchantment) {
    List<String> tempLore = new ArrayList<>();
    if (itemStack == null || tier == null) {
      return tempLore;
    }
    List<String> tooltipFormat = mythicDrops.getConfigSettings().getTooltipFormat();

    String minecraftName = getMinecraftMaterialName(itemStack.getType());
    String mythicName = getMythicMaterialName(itemStack.getType());
    String generalLoreString = NameMap.getInstance().getRandom(NameType.GENERAL_LORE, "");
    String materialLoreString =
        NameMap.getInstance()
            .getRandom(NameType.MATERIAL_LORE, itemStack.getType().name().toLowerCase());
    String tierLoreString =
        NameMap.getInstance().getRandom(NameType.TIER_LORE, tier.getName().toLowerCase());
    String enchantmentLoreString =
        NameMap.getInstance()
            .getRandom(
                NameType.ENCHANTMENT_LORE, enchantment != null ? enchantment.toLowerCase() : "");
    String itemTypeLoreString =
        NameMap.getInstance()
            .getRandom(
                NameType.ITEMTYPE_LORE, ItemUtil.getItemTypeFromMaterial(itemStack.getType()));

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
    int numOfBonusLore =
        RandomRangeUtil.randomRange(tier.getMinimumBonusLore(), tier.getMaximumBonusLore());
    List<String> chosenLore = new ArrayList<>();
    for (int i = 0; i < numOfBonusLore; i++) {
      if (tier.getBonusLore() == null
          || tier.getBonusLore().isEmpty()
          || chosenLore.size() == tier.getBonusLore().size()) {
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

    List<String> socketGemLore = new ArrayList<>();
    List<String> socketableLore = new ArrayList<>();
    if (mythicDrops.getConfigSettings().isSockettingEnabled()
            && c < tier.getChanceToHaveSockets()) {
      int numberOfSockets =
              RandomRangeUtil.randomRange(tier.getMinimumSockets(), tier.getMaximumSockets());
      if (numberOfSockets > 0) {
        for (int i = 0; i < numberOfSockets; i++) {
          String line = mythicDrops.getSockettingSettings().getSockettedItemString();
          socketGemLore.add(line);
        }
        socketableLore.addAll(mythicDrops.getSockettingSettings().getSockettedItemLore());
      }
    }

    List<String> socketLore = new ArrayList<>(socketGemLore);
    socketLore.addAll(socketableLore);

    String displayName =
        io.pixeloutlaw.minecraft.spigot.hilt.ItemStackExtensionsKt.getDisplayName(itemStack);
    List<String> relationLore = new ArrayList<>();
    if (displayName != null) {
      for (String s : ChatColor.stripColor(displayName).split(" ")) {
        relationLore.addAll(mythicDrops.getRelationSettings().getLoreFromName(s));
      }
    }

    tempLore =
        ListExtensionsKt.replaceWithCollections(
            tempLore,
            Arrays.asList(
                new Pair<>("%baselore%", baseLore),
                new Pair<>("%generallore%", generalLore),
                new Pair<>("%materiallore%", materialLore),
                new Pair<>("%tierlore%", tierLore),
                new Pair<>("%enchantmentlore%", enchantmentLore),
                new Pair<>("%itemtypelore%", itemTypeLore),
                new Pair<>(
                    "%baselore%",
                    baseLore), // not sure why this is here twice but I'm pretty sure I had a reason
                new Pair<>("%bonuslore%", bonusLore),
                new Pair<>("%socketlore%", socketLore),
                new Pair<>("%socketgemlore%", socketGemLore),
                new Pair<>("%socketablelore%", socketableLore),
                new Pair<>("%relationlore%", relationLore)));

    String[][] args = {
      {"%basematerial%", minecraftName != null ? minecraftName : ""},
      {"%mythicmaterial%", mythicName != null ? mythicName : ""},
      {"%mythicmaterial%", mythicName != null ? mythicName : ""},
      {"%itemtype%", itemType != null ? itemType : ""},
      {"%materialtype%", materialType != null ? materialType : ""},
      {"%tiername%", tierName != null ? tierName : ""},
      {"%enchantment%", enchantment != null ? enchantment : ""},
      {"%tiercolor%", tier.getDisplayColor() + ""}
    };

    List<String> lore =
        tempLore.stream()
            .map(s -> StringUtil.colorString(StringUtil.replaceArgs(s, args)))
            .collect(Collectors.toList());

    return randomVariableReplace(lore);
  }

  private List<String> randomVariableReplace(List<String> list) {
    List<String> newList = new ArrayList<>();
    for (String s : list) {
      newList.add(TemplatingUtil.INSTANCE.template(s));
    }
    return newList;
  }

  private String getEnchantmentTypeName(ItemStack itemStack) {
    Enchantment enchantment = ItemStackUtil.getHighestEnchantment(itemStack);
    if (enchantment == null) {
      return mythicDrops.getConfigSettings().getFormattedLanguageString("displayNames.Ordinary");
    }
    String ench =
        mythicDrops
            .getConfigSettings()
            .getFormattedLanguageString("displayNames." + enchantment.getName());
    if (ench != null) {
      return ench;
    }
    return "Ordinary";
  }

  private String getMythicMaterialName(Material matData) {
    String comb = matData.name();
    String mythicMatName =
        mythicDrops.getConfigSettings().getFormattedLanguageString("displayNames." + comb);
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
    String mythicMatName =
        mythicDrops
            .getConfigSettings()
            .getFormattedLanguageString("displayNames." + itemType.toLowerCase());
    if (mythicMatName == null) {
      mythicMatName = itemType;
    }
    return WordUtils.capitalizeFully(mythicMatName);
  }

  private String generateName(
      ItemStack itemStack,
      String itemType,
      String materialType,
      String tierName,
      String enchantment) {
    Validate.notNull(itemStack, "ItemStack cannot be null");
    Validate.notNull(tier, "Tier cannot be null");

    String format = mythicDrops.getConfigSettings().getItemDisplayNameFormat();
    if (format == null || format.isEmpty()) {
      return "Mythic Item";
    }
    String minecraftName = getMinecraftMaterialName(itemStack.getType());
    String mythicName = getMythicMaterialName(itemStack.getType());
    String generalPrefix = NameMap.getInstance().getRandom(NameType.GENERAL_PREFIX, "");
    String generalSuffix = NameMap.getInstance().getRandom(NameType.GENERAL_SUFFIX, "");
    String materialPrefix =
        NameMap.getInstance()
            .getRandom(NameType.MATERIAL_PREFIX, itemStack.getType().name().toLowerCase());
    String materialSuffix =
        NameMap.getInstance()
            .getRandom(NameType.MATERIAL_SUFFIX, itemStack.getType().name().toLowerCase());
    String tierPrefix =
        NameMap.getInstance().getRandom(NameType.TIER_PREFIX, tier.getName().toLowerCase());
    String tierSuffix =
        NameMap.getInstance().getRandom(NameType.TIER_SUFFIX, tier.getName().toLowerCase());
    Enchantment highestEnch = ItemStackUtil.getHighestEnchantment(itemStack);
    String enchantmentPrefix =
        NameMap.getInstance()
            .getRandom(
                NameType.ENCHANTMENT_PREFIX,
                highestEnch != null ? highestEnch.getName().toLowerCase() : "");
    String enchantmentSuffix =
        NameMap.getInstance()
            .getRandom(
                NameType.ENCHANTMENT_SUFFIX,
                highestEnch != null ? highestEnch.getName().toLowerCase() : "");
    String itemTypePrefix =
        NameMap.getInstance()
            .getRandom(
                NameType.ITEMTYPE_PREFIX, ItemUtil.getItemTypeFromMaterial(itemStack.getType()));
    String itemTypeSuffix =
        NameMap.getInstance()
            .getRandom(
                NameType.ITEMTYPE_SUFFIX, ItemUtil.getItemTypeFromMaterial(itemStack.getType()));

    String[][] args = {
      {"%basematerial%", minecraftName},
      {"%mythicmaterial%", mythicName},
      {"%generalprefix%", generalPrefix},
      {"%generalsuffix%", generalSuffix},
      {"%materialprefix%", materialPrefix},
      {"%materialsuffix%", materialSuffix},
      {"%tierprefix%", tierPrefix},
      {"%tiersuffix%", tierSuffix},
      {"%itemtypeprefix%", itemTypePrefix},
      {"%itemtypesuffix%", itemTypeSuffix},
      {"%itemtype%", itemType},
      {"%materialtype%", materialType},
      {"%tiername%", tierName},
      {"%enchantment%", enchantment},
      {"%enchantmentprefix%", enchantmentPrefix},
      {"%enchantmentsuffix%", enchantmentSuffix}
    };

    return tier.getDisplayColor()
        + StringUtil.colorString(StringUtil.replaceArgs(format, args)).trim()
        + tier.getIdentificationColor();
  }
}
