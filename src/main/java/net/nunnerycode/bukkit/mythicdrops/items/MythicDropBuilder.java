package net.nunnerycode.bukkit.mythicdrops.items;

import com.google.common.base.Joiner;

import net.nunnerycode.bukkit.libraries.ivory.utils.StringListUtils;
import net.nunnerycode.bukkit.mythicdrops.api.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.enchantments.MythicEnchantment;
import net.nunnerycode.bukkit.mythicdrops.api.items.ItemGenerationReason;
import net.nunnerycode.bukkit.mythicdrops.api.items.MythicItemStack;
import net.nunnerycode.bukkit.mythicdrops.api.items.NonrepairableItemStack;
import net.nunnerycode.bukkit.mythicdrops.api.items.builders.DropBuilder;
import net.nunnerycode.bukkit.mythicdrops.api.names.NameType;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import net.nunnerycode.bukkit.mythicdrops.events.RandomItemGenerationEvent;
import net.nunnerycode.bukkit.mythicdrops.names.NameMap;
import net.nunnerycode.bukkit.mythicdrops.tiers.TierMap;
import net.nunnerycode.bukkit.mythicdrops.utils.ItemStackUtil;
import net.nunnerycode.bukkit.mythicdrops.utils.ItemUtil;
import net.nunnerycode.bukkit.mythicdrops.utils.RandomRangeUtil;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MythicDropBuilder implements DropBuilder {

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

  public DropBuilder withCallEvent(boolean b) {
    this.callEvent = b;
    return this;
  }

  @Override
  public DropBuilder withTier(Tier tier) {
    this.tier = tier;
    return this;
  }

  @Override
  public DropBuilder withTier(String tierName) {
    this.tier = TierMap.getInstance().get(tierName);
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
    Tier t = (tier != null) ? tier : TierMap.getInstance().getRandomWithChance();

    if (t == null) {
      t = TierMap.getInstance().getRandomWithChance();
      if (t == null) {
        return null;
      }
    }

    tier = t;

    Material mat = material != null ? material : ItemUtil.getRandomMaterialFromCollection
        (ItemUtil.getMaterialsFromTier(t));
    NonrepairableItemStack nis = new NonrepairableItemStack(mat, 1, (short) 0, "");
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
      nis.setDurability(
          ItemStackUtil.getDurabilityForMaterial(nis.getType(), t.getMinimumDurabilityPercentage
              (), t.getMaximumDurabilityPercentage()));
    }
    String name = generateName(nis, im);
    List<String> lore = generateLore(nis, im);
    im.setDisplayName(name);
    im.setLore(lore);
    if (nis.getItemMeta() instanceof LeatherArmorMeta) {
      ((LeatherArmorMeta) im).setColor(Color.fromRGB(RandomUtils.nextInt(255),
                                                     RandomUtils.nextInt(255),
                                                     RandomUtils.nextInt(255)));
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

    if (t.getBonusEnchantments().isEmpty()) {
      return new HashMap<>();
    }

    Map<Enchantment, Integer> map = new HashMap<>();

    int added = 0;
    int attempts = 0;
    int range = (int) RandomRangeUtil.randomRangeDoubleInclusive(t.getMinimumBonusEnchantments(),
                                                                 t.getMaximumBonusEnchantments());
    MythicEnchantment[]
        array =
        t.getBonusEnchantments().toArray(new MythicEnchantment[t.getBonusEnchantments()
            .size()]);
    while (added < range && attempts < 10) {
      MythicEnchantment chosenEnch = array[RandomUtils.nextInt(array.length)];
      if (chosenEnch == null || chosenEnch.getEnchantment() == null) {
        attempts++;
        continue;
      }
      Enchantment e = chosenEnch.getEnchantment();
      int randLevel = (int) RandomRangeUtil.randomRangeLongInclusive(chosenEnch.getMinimumLevel(),
                                                                     chosenEnch.getMaximumLevel());
      if (is.containsEnchantment(e)) {
        randLevel += is.getEnchantmentLevel(e);
      }
      if (t.isSafeBonusEnchantments() && e.canEnchantItem(is)) {
        if (t.isAllowHighBonusEnchantments()) {
          map.put(e, randLevel);
        } else {
          map.put(e, getAcceptableEnchantmentLevel(e, randLevel));
        }
      } else if (!t.isSafeBonusEnchantments()) {
        if (t.isAllowHighBonusEnchantments()) {
          map.put(e, randLevel);
        } else {
          map.put(e, getAcceptableEnchantmentLevel(e, randLevel));
        }
      } else {
        continue;
      }
      added++;
    }
    return map;
  }

  private Map<Enchantment, Integer> getBaseEnchantments(MythicItemStack is, Tier t) {
    Validate.notNull(is, "MythicItemStack cannot be null");
    Validate.notNull(t, "Tier cannot be null");

    if (t.getBaseEnchantments().isEmpty()) {
      return new HashMap<>();
    }

    Map<Enchantment, Integer> map = new HashMap<>();

    for (MythicEnchantment me : t.getBaseEnchantments()) {
      if (me == null || me.getEnchantment() == null) {
        continue;
      }
      Enchantment e = me.getEnchantment();
      int minimumLevel = Math.max(me.getMinimumLevel(), e.getStartLevel());
      int maximumLevel = Math.min(me.getMaximumLevel(), e.getMaxLevel());
      if (t.isSafeBaseEnchantments() && e.canEnchantItem(is)) {
        if (t.isAllowHighBaseEnchantments()) {
          map.put(e, (int) RandomRangeUtil.randomRangeLongInclusive
              (minimumLevel, maximumLevel));
        } else {
          map.put(e, getAcceptableEnchantmentLevel(e,
                                                   (int) RandomRangeUtil
                                                       .randomRangeLongInclusive(minimumLevel,
                                                                                 maximumLevel)));
        }
      } else if (!t.isSafeBaseEnchantments()) {
        map.put(e, (int) RandomRangeUtil.randomRangeLongInclusive
            (minimumLevel, maximumLevel));
      }
    }
    return map;
  }

  private int getAcceptableEnchantmentLevel(Enchantment ench, int level) {
    return Math.max(Math.min(level, ench.getMaxLevel()), ench.getStartLevel());
  }

  private List<String> generateLore(ItemStack itemStack, ItemMeta itemMeta) {
    List<String> lore = new ArrayList<>();
    if (itemStack == null || tier == null) {
      return lore;
    }
    List<String>
        tooltipFormat =
        mythicDrops.getConfigSettings().getTooltipFormat();

    String minecraftName = getMinecraftMaterialName(itemStack.getData().getItemType());
    String mythicName = getMythicMaterialName(itemStack.getType());
    String itemType = getItemTypeName(ItemUtil.getItemTypeFromMaterial(itemStack.getType()));
    String materialType =
        getItemTypeName(ItemUtil.getMaterialTypeFromMaterial(itemStack.getType()));
    String tierName = tier.getDisplayName();

    String enchantment = getEnchantmentTypeName(itemMeta);

    String generalLoreString = NameMap.getInstance().getRandom(NameType.GENERAL_LORE, "");
    String materialLoreString = NameMap.getInstance().getRandom(NameType.MATERIAL_LORE,
                                                                itemStack.getType().name()
                                                                    .toLowerCase());
    String
        tierLoreString =
        NameMap.getInstance().getRandom(NameType.TIER_LORE, tier.getName().toLowerCase());
    String enchantmentLoreString = NameMap.getInstance().getRandom(NameType.ENCHANTMENT_LORE,
                                                                   enchantment != null
                                                                   ? enchantment.toLowerCase()
                                                                   : "");

    List<String> generalLore = null;
    if (generalLoreString != null && !generalLoreString.isEmpty()) {
      generalLore = Arrays.asList(generalLoreString.replace('&',
                                                            '\u00A7').replace("\u00A7\u00A7", "&")
                                      .split("/n"));
    }
    List<String> materialLore = null;
    if (materialLoreString != null && !materialLoreString.isEmpty()) {
      materialLore =
          Arrays.asList(materialLoreString.replace('&', '\u00A7').replace("\u00A7\u00A7",
                                                                          "&").split("/n"));
    }
    List<String> tierLore = null;
    if (tierLoreString != null && !tierLoreString.isEmpty()) {
      tierLore = Arrays.asList(tierLoreString.replace('&', '\u00A7').replace("\u00A7\u00A7",
                                                                             "&").split("/n"));
    }
    List<String> enchantmentLore = null;
    if (enchantmentLoreString != null && !enchantmentLoreString.isEmpty()) {
      enchantmentLore = Arrays.asList(enchantmentLoreString.replace('&',
                                                                    '\u00A7')
                                          .replace("\u00A7\u00A7", "&").split("/n"));
    }

    if (generalLore != null && !generalLore.isEmpty()) {
      lore = StringListUtils.replaceWithList(lore, "%generallore%", generalLore);
    }
    if (materialLore != null && !materialLore.isEmpty()) {
      lore = StringListUtils.replaceWithList(lore, "%materiallore%", materialLore);
    }
    if (tierLore != null && !tierLore.isEmpty()) {
      lore = StringListUtils.replaceWithList(lore, "%tierlore%", tierLore);
    }
    if (enchantmentLore != null && !enchantmentLore.isEmpty()) {
      lore = StringListUtils.replaceWithList(lore, "%enchantmentlore%", enchantmentLore);
    }

    for (String s : tooltipFormat) {
      String line = s;
      line = line.replace("%basematerial%", minecraftName != null ? minecraftName : "");
      line = line.replace("%mythicmaterial%", mythicName != null ? mythicName : "");
      line = line.replace("%itemtype%", itemType != null ? itemType : "");
      line = line.replace("%materialtype%", materialType != null ? materialType : "");
      line = line.replace("%tiername%", tierName != null ? tierName : "");
      line = line.replace("%enchantment%", enchantment != null ? enchantment : "");
      line = line.replace("%tiercolor%", tier.getDisplayColor() + "");
      line = line.replace('&', '\u00A7').replace("\u00A7\u00A7", "&");

      lore.add(line);
    }

    for (String s : tier.getBaseLore()) {
      String line = s;
      line = line.replace("%basematerial%", minecraftName != null ? minecraftName : "");
      line = line.replace("%mythicmaterial%", mythicName != null ? mythicName : "");
      line = line.replace("%itemtype%", itemType != null ? itemType : "");
      line = line.replace("%materialtype%", materialType != null ? materialType : "");
      line = line.replace("%tiername%", tierName != null ? tierName : "");
      line = line.replace("%enchantment%", enchantment != null ? enchantment : "");
      line = line.replace("%tiercolor%", tier.getDisplayColor() + "");
      line = line.replace('&', '\u00A7').replace("\u00A7\u00A7", "&");
      String[] strings = line.split("/n");
      lore.addAll(Arrays.asList(strings));
    }

    int numOfBonusLore = (int) RandomRangeUtil.randomRangeLongInclusive(tier.getMinimumBonusLore(),
                                                                        tier.getMaximumBonusLore());
    List<String> chosenLore = new ArrayList<>();
    for (int i = 0; i < numOfBonusLore; i++) {
      if (tier.getBonusLore() == null || tier.getBonusLore().isEmpty() || chosenLore.size() == tier
          .getBonusLore().size()) {
        continue;
      }
      // choose a random String out of the tier's bonus lore
      String s = tier.getBonusLore().get(RandomUtils.nextInt(tier.getBonusLore().size()));
      if (chosenLore.contains(s)) {
        i--;
        continue;
      }
      chosenLore.add(s);
      // split on the next line /n
      String[] strings = s.replace('&', '\u00A7').replace("\u00A7\u00A7", "&").split("/n");
      // add to lore by wrapping in Arrays.asList(Object...)
      lore.addAll(Arrays.asList(strings));
    }

    if (mythicDrops.getSockettingSettings().isEnabled()
        && RandomUtils.nextDouble() < tier
        .getChanceToHaveSockets()) {
      int numberOfSockets = (int) RandomRangeUtil.randomRangeLongInclusive(tier.getMinimumSockets(),
                                                                           tier.getMaximumSockets());
      for (int i = 0; i < numberOfSockets; i++) {
        String line = mythicDrops.getSockettingSettings().getSockettedItemString();
        line = line.replace("%basematerial%", minecraftName != null ? minecraftName : "");
        line = line.replace("%mythicmaterial%", mythicName != null ? mythicName : "");
        line = line.replace("%itemtype%", itemType != null ? itemType : "");
        line = line.replace("%materialtype%", materialType != null ? materialType : "");
        line = line.replace("%tiername%", tierName != null ? tierName : "");
        line = line.replace("%enchantment%", enchantment != null ? enchantment : "");
        line = line.replace("%tiercolor%", tier.getDisplayColor() + "");
        line = line.replace('&', '\u00A7').replace("\u00A7\u00A7", "&");
        lore.add(line);
      }
      if (numberOfSockets > 0) {
        for (String s : mythicDrops.getSockettingSettings().getSockettedItemLore()) {
          String line = s;
          line = line.replace("%basematerial%", minecraftName != null ? minecraftName : "");
          line = line.replace("%mythicmaterial%", mythicName != null ? mythicName : "");
          line = line.replace("%itemtype%", itemType != null ? itemType : "");
          line = line.replace("%materialtype%", materialType != null ? materialType : "");
          line = line.replace("%tiername%", tierName != null ? tierName : "");
          line = line.replace("%enchantment%", enchantment != null ? enchantment : "");
          line = line.replace("%tiercolor%", tier.getDisplayColor() + "");
          line = line.replace('&', '\u00A7').replace("\u00A7\u00A7", "&");
          lore.add(line);
        }
      }
    }

    return lore;
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
      return null;
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
    if (format == null) {
      return "Mythic Item";
    }
    String minecraftName = getMinecraftMaterialName(itemStack.getData().getItemType());
    String mythicName = getMythicMaterialName(itemStack.getType());
    String generalPrefix = NameMap.getInstance().getRandom(NameType.GENERAL_PREFIX, "");
    String generalSuffix = NameMap.getInstance().getRandom(NameType.GENERAL_SUFFIX, "");
    String materialPrefix = NameMap.getInstance().getRandom(NameType.MATERIAL_PREFIX,
                                                            itemStack.getType().name()
                                                                .toLowerCase());
    String materialSuffix = NameMap.getInstance().getRandom(NameType.MATERIAL_SUFFIX,
                                                            itemStack.getType().name()
                                                                .toLowerCase());
    String
        tierPrefix =
        NameMap.getInstance().getRandom(NameType.TIER_PREFIX, tier.getName().toLowerCase());
    String
        tierSuffix =
        NameMap.getInstance().getRandom(NameType.TIER_SUFFIX, tier.getName().toLowerCase());
    String itemType = ItemUtil.getItemTypeFromMaterial(itemStack.getType());
    String materialType = ItemUtil.getMaterialTypeFromMaterial(itemStack.getType());
    String tierName = tier.getDisplayName();
    String enchantment = getEnchantmentTypeName(itemMeta);
    Enchantment highestEnch = ItemStackUtil.getHighestEnchantment(itemMeta);
    String enchantmentPrefix = NameMap.getInstance().getRandom(NameType.ENCHANTMENT_PREFIX,
                                                               highestEnch != null ? highestEnch
                                                                   .getName().toLowerCase() : "");
    String enchantmentSuffix = NameMap.getInstance().getRandom(NameType.ENCHANTMENT_SUFFIX,
                                                               highestEnch != null ? highestEnch
                                                                   .getName().toLowerCase() : "");

    String name = format;

    if (name.contains("%basematerial%")) {
      name = name.replace("%basematerial%", minecraftName);
    }
    if (name.contains("%mythicmaterial%")) {
      name = name.replace("%mythicmaterial%", mythicName);
    }
    if (name.contains("%generalprefix%")) {
      name = name.replace("%generalprefix%", generalPrefix);
    }
    if (name.contains("%generalsuffix%")) {
      name = name.replace("%generalsuffix%", generalSuffix);
    }
    if (name.contains("%materialprefix%")) {
      name = name.replace("%materialprefix%", materialPrefix);
    }
    if (name.contains("%materialsuffix%")) {
      name = name.replace("%materialsuffix%", materialSuffix);
    }
    if (name.contains("%tierprefix%")) {
      name = name.replace("%tierprefix%", tierPrefix);
    }
    if (name.contains("%tiersuffix%")) {
      name = name.replace("%tiersuffix%", tierSuffix);
    }
    if (name.contains("%itemtype%")) {
      name = name.replace("%itemtype%", itemType);
    }
    if (name.contains("%materialtype%")) {
      name = name.replace("%materialtype%", materialType);
    }
    if (name.contains("%tiername%")) {
      name = name.replace("%tiername%", tierName);
    }
    if (name.contains("%enchantment%")) {
      name = name.replace("%enchantment%", enchantment);
    }
    if (name.contains("%enchantmentprefix%")) {
      name = name.replace("%enchantmentprefix%", enchantmentPrefix);
    }
    if (name.contains("%enchantmentsuffix%")) {
      name = name.replace("%enchantmentsuffix%", enchantmentSuffix);
    }
    return tier.getDisplayColor() + name.replace('&', '\u00A7').replace("\u00A7\u00A7", "&").trim()
           +
           tier.getIdentificationColor();
  }

}
