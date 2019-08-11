/*
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2019 Richard Harrah
 *
 * Permission is hereby granted, free of charge,
 * to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.tealcube.minecraft.bukkit.mythicdrops.items;

import com.google.common.base.Joiner;
import com.tealcube.minecraft.bukkit.mythicdrops.ListExtensionsKt;
import com.tealcube.minecraft.bukkit.mythicdrops.MythicDropsPlugin;
import com.tealcube.minecraft.bukkit.mythicdrops.StringExtensionsKt;
import com.tealcube.minecraft.bukkit.mythicdrops.api.MythicDrops;
import com.tealcube.minecraft.bukkit.mythicdrops.api.enchantments.MythicEnchantment;
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.ItemGenerationReason;
import com.tealcube.minecraft.bukkit.mythicdrops.api.items.builders.DropBuilder;
import com.tealcube.minecraft.bukkit.mythicdrops.api.names.NameType;
import com.tealcube.minecraft.bukkit.mythicdrops.api.relations.Relation;
import com.tealcube.minecraft.bukkit.mythicdrops.api.relations.RelationManager;
import com.tealcube.minecraft.bukkit.mythicdrops.api.settings.SettingsManager;
import com.tealcube.minecraft.bukkit.mythicdrops.api.tiers.Tier;
import com.tealcube.minecraft.bukkit.mythicdrops.events.RandomItemGenerationEvent;
import com.tealcube.minecraft.bukkit.mythicdrops.logging.JulLoggerFactory;
import com.tealcube.minecraft.bukkit.mythicdrops.names.NameMap;
import com.tealcube.minecraft.bukkit.mythicdrops.tiers.TierMap;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.ItemStackUtil;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.ItemUtil;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.LeatherArmorUtil;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.RandomRangeUtil;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.SkullUtil;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.StringUtil;
import com.tealcube.minecraft.bukkit.mythicdrops.utils.TemplatingUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import kotlin.Pair;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.text.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public final class MythicDropBuilder implements DropBuilder {

  private static final Logger LOGGER = JulLoggerFactory.INSTANCE.getLogger(MythicDropBuilder.class);
  private RelationManager relationManager;
  private SettingsManager settingsManager;
  private Tier tier;
  private Material material;
  private ItemGenerationReason itemGenerationReason;
  private boolean useDurability;
  private boolean callEvent;

  public MythicDropBuilder(MythicDrops mythicDrops) {
    this(mythicDrops.getRelationManager(), mythicDrops.getSettingsManager());
  }

  public MythicDropBuilder(RelationManager relationManager, SettingsManager settingsManager) {
    this.relationManager = relationManager;
    this.settingsManager = settingsManager;
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
  public DropBuilder withItemGenerationReason(ItemGenerationReason reason) {
    this.itemGenerationReason = reason;
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
    if (!settingsManager.getConfigSettings().getOptions().isAllowItemsToBeRepairedByAnvil()) {
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
    io.pixeloutlaw.minecraft.spigot.hilt.ItemStackExtensionsKt.setUnbreakable(
        nis, t.isInfiniteDurability());

    String tierName = tier.getDisplayName();
    String enchantment = getEnchantmentTypeName(nis);

    String name = generateName(nis, tierName, enchantment);
    ItemStackExtensionsKt.setDisplayNameChatColorized(nis, name);
    List<String> lore = generateLore(nis, tierName, enchantment);
    ItemStackExtensionsKt.setLoreChatColorized(nis, lore);
    if (settingsManager.getConfigSettings().getOptions().isRandomizeLeatherColors()) {
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

    int toAdd =
        RandomRangeUtil.randomRange(
            tier.getMinimumBonusEnchantments(), tier.getMaximumBonusEnchantments());
    List<MythicEnchantment> bonusEnchantments =
        tier.getBonusEnchantments().stream()
            // filter out any null enchantments (only if not originally created by mythicdrops)
            .filter(Objects::nonNull)
            .filter(
                mythicEnchantment ->
                    !tier.isSafeBonusEnchantments()
                        || mythicEnchantment.getEnchantment().canEnchantItem(itemStack))
            .collect(Collectors.toList());

    if (bonusEnchantments.isEmpty()) {
      LOGGER.fine(
          String.format(
              "bonusEnchantments.isEmpty() - material=%s, tier=%s",
              itemStack.getType(), tier.getName()));
      return map;
    }

    for (int i = 0; i < toAdd; i++) {
      MythicEnchantment randomMythicEnchantment =
          bonusEnchantments.get(RandomUtils.nextInt(0, bonusEnchantments.size()));
      Enchantment enchantment = randomMythicEnchantment.getEnchantment();
      int randomizedLevelOfEnchantment = randomMythicEnchantment.getRandomLevel();

      // if the map of bonus enchantments already includes the given enchantment,
      // we combine the existing enchantment with the new roll to get a higher level enchantment
      if (map.containsKey(enchantment)) {
        randomizedLevelOfEnchantment =
            Math.min(
                randomMythicEnchantment.getMaximumLevel(),
                randomizedLevelOfEnchantment + map.get(enchantment));
      }
      // if we can't have high enchantment rolls, trim it
      if (!tier.isAllowHighBonusEnchantments()) {
        randomizedLevelOfEnchantment =
            getAcceptableEnchantmentLevel(enchantment, randomizedLevelOfEnchantment);
      }
      map.put(enchantment, randomizedLevelOfEnchantment);
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

  private List<String> generateLore(ItemStack itemStack, String tierName, String enchantment) {
    List<String> tempLore = new ArrayList<>();
    if (itemStack == null || tier == null) {
      return tempLore;
    }
    List<String> tooltipFormat =
        settingsManager.getConfigSettings().getDisplay().getTooltipFormat();

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

    List<String> generalLore = Arrays.asList(generalLoreString.split("/n"));
    List<String> materialLore = Arrays.asList(materialLoreString.split("/n"));
    List<String> tierLore = Arrays.asList(tierLoreString.split("/n"));
    List<String> enchantmentLore = Arrays.asList(enchantmentLoreString.split("/n"));

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
    if (settingsManager.getConfigSettings().getComponents().isSocketingEnabled()
        && c < tier.getChanceToHaveSockets()) {
      int numberOfSockets =
          RandomRangeUtil.randomRange(tier.getMinimumSockets(), tier.getMaximumSockets());
      if (numberOfSockets > 0) {
        for (int i = 0; i < numberOfSockets; i++) {
          String line =
              settingsManager.getSocketingSettings().getItems().getSocketedItem().getSocket();
          socketGemLore.add(line);
        }
        socketableLore.addAll(
            settingsManager.getSocketingSettings().getItems().getSocketedItem().getLore());
      }
    }

    List<String> socketLore = new ArrayList<>(socketGemLore);
    socketLore.addAll(socketableLore);

    String displayName =
        io.pixeloutlaw.minecraft.spigot.hilt.ItemStackExtensionsKt.getDisplayName(itemStack);
    List<String> relationLore = new ArrayList<>();
    if (displayName != null) {
      for (String s : ChatColor.stripColor(displayName).split(" ")) {
        Relation relation = relationManager.getById(s);
        if (relation != null) {
          relationLore.addAll(relation.getLore());
        }
      }
    }

    tempLore =
        ListExtensionsKt.replaceWithCollections(
            tempLore,
            Arrays.asList(
                new Pair<>("%baselore%", baseLore),
                new Pair<>("%bonuslore%", bonusLore),
                new Pair<>("%socketgemlore%", socketGemLore),
                new Pair<>("%socketablelore%", socketableLore),
                new Pair<>("%socketlore%", socketLore),
                new Pair<>("%generallore%", generalLore),
                new Pair<>("%tierlore%", tierLore),
                new Pair<>("%materiallore%", materialLore),
                new Pair<>("%enchantmentlore%", enchantmentLore),
                new Pair<>("%relationlore%", relationLore)));

    String[][] args = {
      {"%basematerial%", minecraftName != null ? minecraftName : ""},
      {"%mythicmaterial%", mythicName != null ? mythicName : ""},
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
      return StringExtensionsKt.chatColorize(
          settingsManager
              .getLanguageSettings()
              .getDisplayNames()
              .getOrDefault("Ordinary", "Ordinary"));
    }
    String ench = settingsManager.getLanguageSettings().getDisplayNames().get(enchantment.getKey());
    if (ench != null) {
      return ench;
    }
    return "Ordinary";
  }

  private String getMythicMaterialName(Material matData) {
    String comb = matData.name();
    String mythicMatName = settingsManager.getLanguageSettings().getDisplayNames().get(comb);
    if (mythicMatName == null || mythicMatName.equals(comb)) {
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

  private String generateName(ItemStack itemStack, String tierName, String enchantment) {
    Validate.notNull(itemStack, "ItemStack cannot be null");
    Validate.notNull(tier, "Tier cannot be null");

    String format = settingsManager.getConfigSettings().getDisplay().getItemDisplayNameFormat();
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

    String[][] args = {
      {"%basematerial%", minecraftName},
      {"%mythicmaterial%", mythicName},
      {"%generalprefix%", generalPrefix},
      {"%generalsuffix%", generalSuffix},
      {"%materialprefix%", materialPrefix},
      {"%materialsuffix%", materialSuffix},
      {"%tierprefix%", tierPrefix},
      {"%tiersuffix%", tierSuffix},
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
