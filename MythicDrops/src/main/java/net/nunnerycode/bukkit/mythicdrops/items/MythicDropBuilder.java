package net.nunnerycode.bukkit.mythicdrops.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.nunnerycode.bukkit.mythicdrops.MythicDropsPlugin;
import net.nunnerycode.bukkit.mythicdrops.api.enchantments.MythicEnchantment;
import net.nunnerycode.bukkit.mythicdrops.api.items.ItemGenerationReason;
import net.nunnerycode.bukkit.mythicdrops.api.items.MythicItemStack;
import net.nunnerycode.bukkit.mythicdrops.api.items.NonrepairableItemStack;
import net.nunnerycode.bukkit.mythicdrops.api.items.builders.DropBuilder;
import net.nunnerycode.bukkit.mythicdrops.api.names.NameType;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import net.nunnerycode.bukkit.mythicdrops.names.NameMap;
import net.nunnerycode.bukkit.mythicdrops.utils.ItemStackUtil;
import net.nunnerycode.bukkit.mythicdrops.utils.ItemUtil;
import net.nunnerycode.bukkit.mythicdrops.utils.RandomRangeUtil;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.material.MaterialData;

public final class MythicDropBuilder implements DropBuilder {

	private Tier tier;
	private MaterialData materialData;
	private ItemGenerationReason itemGenerationReason;
	private World world;
	private boolean useDurability;

	public MythicDropBuilder() {
		tier = null;
		itemGenerationReason = ItemGenerationReason.DEFAULT;
		world = Bukkit.getServer().getWorlds().get(0);
		useDurability = false;
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
	public DropBuilder withMaterialData(MaterialData materialData) {
		this.materialData = materialData;
		return this;
	}

	@Override
	public DropBuilder withMaterialData(String materialDataString) {
		MaterialData matData = null;
		if (materialDataString.contains(";")) {
			String[] split = materialDataString.split(";");
			matData = new MaterialData(NumberUtils.toInt(split[0], 0), (byte) NumberUtils.toInt(split[1], 0));
		} else {
			matData = new MaterialData(NumberUtils.toInt(materialDataString, 0));
		}
		this.materialData = matData;
		return this;
	}

	@Override
	public DropBuilder withItemGenerationReason(ItemGenerationReason reason) {
		this.itemGenerationReason = reason;
		return this;
	}

	@Override
	public DropBuilder inWorld(World world) {
		this.world = world;
		return this;
	}

	@Override
	public DropBuilder inWorld(String worldName) {
		this.world = Bukkit.getWorld(worldName);
		return this;
	}

	@Override
	public DropBuilder useDurability(boolean b) {
		this.useDurability = b;
		return this;
	}

	@Override
	public MythicItemStack build() {
		World w = world != null ? world : Bukkit.getWorlds().get(0);
		Tier t = (tier != null) ? tier : TierMap.getInstance().getRandomWithChance(w.getName());

		if (t == null) {
			t = TierMap.getInstance().getRandomWithChance("default");
			if (t == null) {
				return null;
			}
		}

		MaterialData md = (materialData != null) ? materialData : ItemUtil.getRandomMaterialDataFromCollection
				(ItemUtil.getMaterialDatasFromTier(t));
		NonrepairableItemStack nis = new NonrepairableItemStack(md.getItemType(), 1, (short) 0, "");
		Map<Enchantment, Integer> baseEnchantmentMap = getBaseEnchantments(nis, t);
		Map<Enchantment, Integer> bonusEnchantmentMap = getBaseEnchantments(nis, t);

		for (Map.Entry<Enchantment, Integer> baseEnch : baseEnchantmentMap.entrySet()) {
			nis.getItemMeta().addEnchant(baseEnch.getKey(), baseEnch.getValue(), true);
		}
		for (Map.Entry<Enchantment, Integer> bonusEnch : bonusEnchantmentMap.entrySet()) {
			nis.getItemMeta().addEnchant(bonusEnch.getKey(), bonusEnch.getValue(), true);
		}

		if (useDurability) {
			nis.setDurability(ItemStackUtil.getDurabilityForMaterial(nis.getType(), t.getMinimumDurabilityPercentage
					(), t.getMaximumDurabilityPercentage()));
		}
		String name = generateName(nis);
		List<String> lore = generateLore(nis);
		nis.getItemMeta().setDisplayName(name);
		nis.getItemMeta().setLore(lore);
		if (nis.getItemMeta() instanceof LeatherArmorMeta) {
			((LeatherArmorMeta) nis.getItemMeta()).setColor(Color.fromRGB(RandomUtils.nextInt(255),
					RandomUtils.nextInt(255), RandomUtils.nextInt(255)));
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
		MythicEnchantment[] array = t.getBonusEnchantments().toArray(new MythicEnchantment[t.getBonusEnchantments()
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
							(int) RandomRangeUtil.randomRangeLongInclusive(minimumLevel, maximumLevel)));
				}
			} else if (!t.isSafeBaseEnchantments()) {
				map.put(e, (int) RandomRangeUtil.randomRangeLongInclusive
						(minimumLevel, maximumLevel));
			}
		}
		return map;
	}

	private int getAcceptableEnchantmentLevel(Enchantment ench, int level) {
		EnchantmentWrapper ew = new EnchantmentWrapper(ench.getId());
		return Math.max(Math.min(level, ew.getMaxLevel()), ew.getStartLevel());
	}

	private List<String> generateLore(ItemStack itemStack) {
		List<String> lore = new ArrayList<String>();
		if (itemStack == null || tier == null) {
			return lore;
		}
		List<String> tooltipFormat = MythicDropsPlugin.getInstance().getConfigSettings().getTooltipFormat();

		String minecraftName = getMinecraftMaterialName(itemStack.getData().getItemType());
		String mythicName = getMythicMaterialName(itemStack.getData());
		String itemType = getItemTypeName(itemStack.getData());
		String tierName = tier.getDisplayName();
		String enchantment = getEnchantmentTypeName(itemStack);

		for (String s : tooltipFormat) {
			String line = s;
			line = line.replace("%basematerial%", minecraftName != null ? minecraftName : "");
			line = line.replace("%mythicmaterial%", mythicName != null ? mythicName : "");
			line = line.replace("%itemtype%", itemType != null ? itemType : "");
			line = line.replace("%tiername%", tierName != null ? tierName : "");
			line = line.replace("%enchantment%", enchantment != null ? enchantment : "");
			line = line.replace('&', '\u00A7').replace("\u00A7\u00A7", "&");

			lore.add(line);
		}

		return lore;
	}

	private String getEnchantmentTypeName(ItemStack itemStack) {
		Enchantment enchantment = ItemStackUtil.getHighestEnchantment(itemStack);
		if (enchantment == null) {
			return MythicDropsPlugin.getInstance().getConfigSettings().getFormattedLanguageString("displayNames" +
					".Ordinary");
		}
		String ench = MythicDropsPlugin.getInstance().getConfigSettings().getFormattedLanguageString("displayNames."
				+ enchantment.getName());
		if (ench != null) {
			return ench;
		}
		return "Ordinary";
	}

	private String getMythicMaterialName(MaterialData matData) {
		String comb =
				String.format("%s;%s", String.valueOf(matData.getItemTypeId()), String.valueOf(matData.getData()));
		String comb2;
		if (matData.getData() == (byte) 0) {
			comb2 = String.valueOf(matData.getItemTypeId());
		} else {
			comb2 = comb;
		}
		String mythicMatName = MythicDropsPlugin.getInstance().getConfigSettings().getFormattedLanguageString(
				"displayNames." + comb.toLowerCase());
		if (mythicMatName == null) {
			mythicMatName = MythicDropsPlugin.getInstance().getConfigSettings().getFormattedLanguageString(
					"displayNames." + comb2.toLowerCase());
			if (mythicMatName == null) {
				mythicMatName = getMinecraftMaterialName(matData.getItemType());
			}
		}
		return WordUtils.capitalize(mythicMatName);
	}

	private String getMinecraftMaterialName(Material material) {
		String prettyMaterialName = "";
		String matName = material.name();
		String[] split = matName.split("_");
		for (String s : split) {
			if (s.equals(split[split.length - 1])) {
				prettyMaterialName = String
						.format("%s%s%s", prettyMaterialName, s.substring(0, 1).toUpperCase(), s.substring(1,
								s.length()).toLowerCase());
			} else {
				prettyMaterialName = prettyMaterialName
						+ (String.format("%s%s", s.substring(0, 1).toUpperCase(), s.substring(1,
						s.length()).toLowerCase())) + " ";
			}
		}
		return WordUtils.capitalize(prettyMaterialName);
	}

	private String getItemTypeName(MaterialData matData) {
		String itemType = getItemTypeFromMaterialData(matData);
		if (itemType == null) {
			return null;
		}
		String mythicMatName = MythicDropsPlugin.getInstance().getConfigSettings().getFormattedLanguageString(
				"displayNames." + itemType.toLowerCase());
		if (mythicMatName == null) {
			mythicMatName = itemType;
		}
		return WordUtils.capitalize(mythicMatName);
	}

	private String getItemTypeFromMaterialData(MaterialData matData) {
		String comb =
				String.format("%s;%s", String.valueOf(matData.getItemTypeId()), String.valueOf(matData.getData()));
		String comb2;
		if (matData.getData() == (byte) 0) {
			comb2 = String.valueOf(matData.getItemTypeId());
		} else {
			comb2 = comb;
		}
		String comb3 = String.valueOf(matData.getItemTypeId());
		Map<String, List<String>> ids = new HashMap<String, List<String>>();
		ids.putAll(MythicDropsPlugin.getInstance().getConfigSettings().getItemTypesWithIds());
		for (Map.Entry<String, List<String>> e : ids.entrySet()) {
			if (e.getValue().contains(comb)
					|| e.getValue().contains(comb2) || e.getValue().contains(comb3)) {
				if (MythicDropsPlugin.getInstance().getConfigSettings().getMaterialTypes().contains(e.getKey())) {
					continue;
				}
				return e.getKey();
			}
		}
		return null;
	}

	private String generateName(ItemStack itemStack) {
		Validate.notNull(itemStack, "ItemStack cannot be null");
		Validate.notNull(tier, "Tier cannot be null");

		String format = MythicDropsPlugin.getInstance().getConfigSettings().getItemDisplayNameFormat();
		if (format == null) {
			return "Mythic Item";
		}
		String minecraftName = getMinecraftMaterialName(itemStack.getData().getItemType());
		String mythicName = getMythicMaterialName(itemStack.getData());
		String generalPrefix = NameMap.getInstance().getRandom(NameType.GENERAL_PREFIX, "");
		String generalSuffix = NameMap.getInstance().getRandom(NameType.GENERAL_SUFFIX, "");
		String materialPrefix = NameMap.getInstance().getRandom(NameType.MATERIAL_PREFIX, itemStack.getType().name());
		String materialSuffix = NameMap.getInstance().getRandom(NameType.MATERIAL_SUFFIX, itemStack.getType().name());
		String tierPrefix = NameMap.getInstance().getRandom(NameType.TIER_PREFIX, tier.getName());
		String tierSuffix = NameMap.getInstance().getRandom(NameType.TIER_SUFFIX, tier.getName());
		String itemType = getItemTypeName(itemStack.getData());
		String tierName = tier.getDisplayName();
		String enchantment = getEnchantmentTypeName(itemStack);
		Enchantment highestEnch = ItemStackUtil.getHighestEnchantment(itemStack.getItemMeta());
		String enchantmentPrefix = NameMap.getInstance().getRandom(NameType.ENCHANTMENT_PREFIX,
				highestEnch != null ? highestEnch.getName() : "");
		String enchantmentSuffix = NameMap.getInstance().getRandom(NameType.ENCHANTMENT_SUFFIX,
				highestEnch != null ? highestEnch.getName() : "");

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
		return tier.getDisplayColor() + name.replace('&', '\u00A7').replace("\u00A7\u00A7", "&").trim() +
				tier.getIdentificationColor();
	}

}
