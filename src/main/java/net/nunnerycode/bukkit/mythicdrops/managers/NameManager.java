/*
 * Copyright (C) 2013 Richard Harrah
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.nunnerycode.bukkit.mythicdrops.managers;

import com.modcrafting.diablodrops.name.NamesLoader;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import net.nunnerycode.bukkit.mythicdrops.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

/**
 * A manager for dealing with names and lore.
 */
public class NameManager {

	private final MythicDrops plugin;
	private final List<String> generalPrefixes;
	private final List<String> generalSuffixes;
	private final List<String> generalLore;
	private final Map<Material, List<String>> materialPrefixes;
	private final Map<Material, List<String>> materialSuffixes;
	private final Map<Material, List<String>> materialLore;
	private final Map<Tier, List<String>> tierPrefixes;
	private final Map<Tier, List<String>> tierSuffixes;
	private final Map<Tier, List<String>> tierLore;
	private final Map<Enchantment, List<String>> enchantmentPrefixes;
	private final Map<Enchantment, List<String>> enchantmentSuffixes;
	private final Map<Enchantment, List<String>> enchantmentLore;
	private final NamesLoader namesLoader;

	public NameManager(final MythicDrops plugin) {
		this.plugin = plugin;
		// Initializing the general Maps
		generalPrefixes = new ArrayList<String>();
		generalSuffixes = new ArrayList<String>();
		generalLore = new ArrayList<String>();
		// Initializing the Material Maps
		materialPrefixes = new HashMap<Material, List<String>>();
		materialSuffixes = new HashMap<Material, List<String>>();
		materialLore = new HashMap<Material, List<String>>();
		// Initializing the Tier Maps
		tierPrefixes = new HashMap<Tier, List<String>>();
		tierSuffixes = new HashMap<Tier, List<String>>();
		tierLore = new HashMap<Tier, List<String>>();
		// Initializing the Enchantment Maps
		enchantmentPrefixes = new HashMap<Enchantment, List<String>>();
		enchantmentSuffixes = new HashMap<Enchantment, List<String>>();
		enchantmentLore = new HashMap<Enchantment, List<String>>();
		// Initializing the NamesLoader
		namesLoader = new NamesLoader(plugin);
		namesLoader.writeDefault("/modules/readme.txt", false, true);
		namesLoader.writeDefault("variables.txt", false, false);
		// Loading all prefixes, suffixes, and lore
		loadGeneralPrefixes();
		loadGeneralSuffixes();
		loadGeneralLore();
		loadMaterialPrefixes();
		loadMaterialSuffixes();
		loadMaterialLore();
		loadTierPrefixes();
		loadTierSuffixes();
		loadTierLore();
		loadEnchantmentPrefixes();
		loadEnchantmentSuffixes();
		loadEnchantmentLore();
	}

	public final void loadEnchantmentLore() {
		enchantmentLore.clear();
		File folderLoc = new File(plugin.getDataFolder(), "/resources/lore/enchantments/");

		if (!folderLoc.exists() && !folderLoc.mkdirs()) {
			return;
		}

		namesLoader.writeDefault("/resources/lore/enchantments/damage_all.txt", false, true);

		try {
			for (File f : folderLoc.listFiles()) {
				if (f.getName().endsWith(".txt")) {
					namesLoader.loadEnchantmentFile(enchantmentLore, "resources/lore/enchantments/" + f.getName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			plugin.debug(Level.WARNING, "Could not load lore file");
		}
	}

	public final void loadEnchantmentPrefixes() {
		enchantmentPrefixes.clear();
		File folderLoc = new File(plugin.getDataFolder(), "/resources/prefixes/enchantments/");

		if (!folderLoc.exists() && !folderLoc.mkdirs()) {
			return;
		}

		namesLoader.writeDefault("/resources/prefixes/enchantments/damage_all.txt", false, true);

		try {
			for (File f : folderLoc.listFiles()) {
				if (f.getName().endsWith(".txt")) {
					namesLoader.loadEnchantmentFile(enchantmentPrefixes, "resources/prefixes/enchantments/" + f.getName
							());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			plugin.debug(Level.WARNING, "Could not load prefix file");
		}
	}

	public final void loadEnchantmentSuffixes() {
		enchantmentSuffixes.clear();
		File folderLoc = new File(plugin.getDataFolder(), "resources/suffixes/enchantments/");

		if (!folderLoc.exists() && !folderLoc.mkdirs()) {
			return;
		}

		namesLoader.writeDefault("/resources/suffixes/enchantments/damage_all.txt", false, true);

		try {
			for (File f : folderLoc.listFiles()) {
				if (f.getName().endsWith(".txt")) {
					namesLoader.loadEnchantmentFile(enchantmentSuffixes, "resources/suffixes/enchantments/" + f.getName
							());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			plugin.debug(Level.WARNING, "Could not load suffix file");
		}
	}

	public final void loadGeneralPrefixes() {
		generalPrefixes.clear();

		try {
			namesLoader.writeDefault("resources/prefixes/general.txt", false, false);
		} catch (Exception e) {
			plugin.debug(Level.WARNING, "Could not write general prefix file");
		}

		try {
			namesLoader.loadFile(generalPrefixes, "resources/prefixes/general.txt");
		} catch (Exception e) {
			plugin.debug(Level.WARNING, "Could not load general prefixes");
		}
	}

	public final void loadMaterialPrefixes() {
		materialPrefixes.clear();
		File folderLoc = new File(plugin.getDataFolder(), "/resources/prefixes/materials/");

		if (!folderLoc.exists() && !folderLoc.mkdirs()) {
			return;
		}

		namesLoader.writeDefault("/resources/prefixes/materials/diamond_sword.txt", false, true);

		try {
			for (File f : folderLoc.listFiles()) {
				if (f.getName().endsWith(".txt")) {
					namesLoader.loadMaterialFile(materialPrefixes, "resources/prefixes/materials/" + f.getName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			plugin.debug(Level.WARNING, "Could not load prefix file");
		}
	}

	public final void loadTierPrefixes() {
		tierPrefixes.clear();
		File folderLoc = new File(plugin.getDataFolder(), "/resources/prefixes/tiers/");

		if (!folderLoc.exists() && !folderLoc.mkdirs()) {
			return;
		}

		namesLoader.writeDefault("/resources/prefixes/tiers/legendary.txt", false, true);

		try {
			for (File f : folderLoc.listFiles()) {
				if (f.getName().endsWith(".txt")) {
					namesLoader.loadTierFile(tierPrefixes, "resources/prefixes/tiers/" + f.getName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			plugin.debug(Level.WARNING, "Could not load prefix file");
		}
	}

	public final void loadGeneralSuffixes() {
		generalSuffixes.clear();

		try {
			namesLoader.writeDefault("resources/suffixes/general.txt", false, false);
		} catch (Exception e) {
			plugin.debug(Level.WARNING, "Could not write general suffix file");
		}

		try {
			namesLoader.loadFile(generalSuffixes, "resources/suffixes/general.txt");
		} catch (Exception e) {
			e.printStackTrace();
			plugin.debug(Level.WARNING, "Could not load general suffixes");
		}
	}

	public final void loadMaterialSuffixes() {
		materialSuffixes.clear();
		File folderLoc = new File(plugin.getDataFolder(), "resources/suffixes/materials/");

		if (!folderLoc.exists() && !folderLoc.mkdirs()) {
			return;
		}

		namesLoader.writeDefault("/resources/suffixes/materials/diamond_sword.txt", false, true);

		try {
			for (File f : folderLoc.listFiles()) {
				if (f.getName().endsWith(".txt")) {
					namesLoader.loadMaterialFile(materialSuffixes, "resources/suffixes/materials/" + f.getName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			plugin.debug(Level.WARNING, "Could not load suffix file");
		}
	}

	public final void loadTierSuffixes() {
		tierSuffixes.clear();
		File folderLoc = new File(plugin.getDataFolder(), "resources/suffixes/tiers/");

		if (!folderLoc.exists() && !folderLoc.mkdirs()) {
			return;
		}

		namesLoader.writeDefault("/resources/suffixes/tiers/legendary.txt", false, true);

		try {
			for (File f : folderLoc.listFiles()) {
				if (f.getName().endsWith(".txt")) {
					namesLoader.loadTierFile(tierSuffixes, "resources/suffixes/tiers/" + f.getName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			plugin.debug(Level.WARNING, "Could not load suffix file");
		}
	}

	public final void loadGeneralLore() {
		generalLore.clear();

		try {
			namesLoader.writeDefault("resources/lore/general.txt", false, false);
		} catch (Exception e) {
			plugin.debug(Level.WARNING, "Could not write general lore file");
		}

		try {
			namesLoader.loadFile(generalLore, "resources/lore/general.txt");
		} catch (Exception e) {
			plugin.debug(Level.WARNING, "Could not load general lore");
		}
	}

	public final void loadMaterialLore() {
		materialLore.clear();
		File folderLoc = new File(plugin.getDataFolder(), "/resources/lore/materials/");

		if (!folderLoc.exists() && !folderLoc.mkdirs()) {
			return;
		}

		namesLoader.writeDefault("/resources/lore/materials/diamond_sword.txt", false, true);

		try {
			for (File f : folderLoc.listFiles()) {
				if (f.getName().endsWith(".txt")) {
					namesLoader.loadMaterialFile(materialLore, "/resources/lore/materials/" + f.getName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			plugin.debug(Level.WARNING, "Could not load lore file");
		}
	}

	public final void loadTierLore() {
		tierLore.clear();
		File folderLoc = new File(plugin.getDataFolder(), "/resources/lore/tiers/");

		if (!folderLoc.exists() && !folderLoc.mkdirs()) {
			return;
		}

		namesLoader.writeDefault("/resources/lore/tiers/legendary.txt", false, true);

		try {
			for (File f : folderLoc.listFiles()) {
				if (f.getName().endsWith(".txt")) {
					namesLoader.loadTierFile(tierLore, "resources/lore/tiers/" + f.getName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			plugin.debug(Level.WARNING, "Could not load lore file");
		}
	}

	public Map<Enchantment, List<String>> getEnchantmentLore() {
		return enchantmentLore;
	}

	public Map<Tier, List<String>> getTierPrefixes() {
		return tierPrefixes;
	}

	public Map<Tier, List<String>> getTierSuffixes() {
		return tierSuffixes;
	}

	public final void debugNames() {
		plugin.debug(Level.INFO, "General prefixes: " + String.valueOf(generalPrefixes.size()) + " | " +
				"General suffixes: " + String.valueOf(generalSuffixes.size()) + " | General lore: " +
				String.valueOf(generalLore.size()));
		int mp = 0, ms = 0, ml = 0, tp = 0, ts = 0, tl = 0, ep = 0, es = 0, el = 0;
		for (Map.Entry<Material, List<String>> e : materialPrefixes.entrySet()) {
			mp += e.getValue().size();
		}
		for (Map.Entry<Material, List<String>> e : materialSuffixes.entrySet()) {
			ms += e.getValue().size();
		}
		for (Map.Entry<Material, List<String>> e : materialLore.entrySet()) {
			ml += e.getValue().size();
		}
		for (Map.Entry<Tier, List<String>> e : tierPrefixes.entrySet()) {
			tp += e.getValue().size();
		}
		for (Map.Entry<Tier, List<String>> e : tierSuffixes.entrySet()) {
			ts += e.getValue().size();
		}
		for (Map.Entry<Tier, List<String>> e : tierLore.entrySet()) {
			tl += e.getValue().size();
		}
		for (Map.Entry<Enchantment, List<String>> e : enchantmentPrefixes.entrySet()) {
			ep += e.getValue().size();
		}
		for (Map.Entry<Enchantment, List<String>> e : enchantmentSuffixes.entrySet()) {
			es += e.getValue().size();
		}
		for (Map.Entry<Enchantment, List<String>> e : enchantmentLore.entrySet()) {
			el += e.getValue().size();
		}
		plugin.debug(Level.INFO,
				"Material prefixes: " + String.valueOf(mp) + " | Material " +
						"suffixes: " + String.valueOf(ms) + " | Material lore: " +
						String.valueOf(ml));
		plugin.debug(Level.INFO,
				"Tier prefixes: " + String.valueOf(tp) + " | Tier " +
						"suffixes: " + String.valueOf(ts) + " | Tier lore: " +
						String.valueOf(tl));
		plugin.debug(Level.INFO,
				"Enchantment prefixes: " + String.valueOf(ep) + " | Enchantment " +
						"suffixes: " + String.valueOf(es) + " | Enchantment lore: " +
						String.valueOf(el));
	}

	public NamesLoader getNamesLoader() {
		return namesLoader;
	}

	/**
	 * Gets the list of general prefixes from the plugin's resources.
	 *
	 * @return list of general prefixes
	 */
	public List<String> getGeneralPrefixes() {
		return generalPrefixes;
	}

	/**
	 * Gets the list of general suffixes from the plugin's resources.
	 *
	 * @return list of general suffixes
	 */
	public List<String> getGeneralSuffixes() {
		return generalSuffixes;
	}

	/**
	 * Gets a Map containing prefixes for various {@link Material}s.
	 *
	 * @return map of prefixes for Materials
	 */
	public Map<Material, List<String>> getMaterialPrefixes() {
		return materialPrefixes;
	}

	/**
	 * Gets a Map containing suffixes for various {@link Material}s.
	 *
	 * @return map of suffixes for Materials
	 */
	public Map<Material, List<String>> getMaterialSuffixes() {
		return materialSuffixes;
	}

	public List<String> randomGeneralLore() {
		String string = generalLore.get(RandomUtils.nextInt(generalLore.size())).replace('&',
				'\u00A7').replace("\u00A7\u00A7", "&");
		return Arrays.asList(string.split("/n"));
	}

	public List<String> randomMaterialLore(Material material) {
		if (!materialLore.containsKey(material)) {
			return new ArrayList<String>();
		}
		String string = materialLore.get(material).get(RandomUtils.nextInt(materialLore.get(material).size())).replace('&',
				'\u00A7').replace("\u00A7\u00A7", "&");
		return Arrays.asList(string.split("/n"));
	}

	public List<String> randomTierLore(Tier tier) {
		if (!tierLore.containsKey(tier)) {
			return new ArrayList<String>();
		}
		String string = tierLore.get(tier).get(RandomUtils.nextInt(tierLore.get(tier).size())).replace('&',
				'\u00A7').replace("\u00A7\u00A7", "&");
		return Arrays.asList(string.split("/n"));
	}

	public List<String> randomEnchantmentLore(Enchantment enchantment) {
		if (!enchantmentLore.containsKey(enchantment)) {
			return new ArrayList<String>();
		}
		String string = enchantmentLore.get(enchantment).get(RandomUtils.nextInt(enchantmentLore.get(enchantment).size
				())).replace('&', '\u00A7').replace("\u00A7\u00A7", "&");
		return Arrays.asList(string.split("/n"));
	}

	public String randomFormattedName(ItemStack itemStack, Tier tier) {
		if (itemStack == null || tier == null) {
			return "Mythic Item";
		}
		String format = getPlugin().getSettingsManager()
				.getItemDisplayNameFormat();
		if (format == null) {
			return "Mythic Item";
		}
		String minecraftName = getMinecraftMaterialName(itemStack.getData()
				.getItemType());
		String mythicName = getMythicMaterialName(itemStack.getData());
		String generalPrefix = randomGeneralPrefix();
		String generalSuffix = randomGeneralSuffix();
		String materialPrefix = randomMaterialPrefix(itemStack.getType());
		String materialSuffix = randomMaterialSuffix(itemStack.getType());
		String tierPrefix = randomTierPrefix(tier);
		String tierSuffix = randomTierSuffix(tier);
		String itemType = getItemTypeName(itemStack.getData());
		String tierName = tier.getTierDisplayName();
		String enchantment = getEnchantmentTypeName(itemStack);

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
		return tier.getTierDisplayColor() + name.replace('&', '\u00A7').replace("\u00A7\u00A7", "&") +
				tier.getTierIdentificationColor();
	}

	public String randomGeneralPrefix() {
		return generalPrefixes.get(RandomUtils.nextInt(generalPrefixes
				.size())).replace('&', '\u00A7').replace("\u00A7\u00A7", "&");
	}

	public String randomGeneralSuffix() {
		return generalSuffixes.get(RandomUtils.nextInt(generalSuffixes
				.size())).replace('&', '\u00A7').replace("\u00A7\u00A7", "&");
	}

	public String randomMaterialPrefix(Material material) {
		if (!materialPrefixes.containsKey(material)) {
			return "";
		}
		return materialPrefixes.get(material).get(RandomUtils.nextInt(materialPrefixes.
				get(material).size())).replace('&', '\u00A7').replace("\u00A7\u00A7", "&");
	}

	public String randomMaterialSuffix(Material material) {
		if (!materialSuffixes.containsKey(material)) {
			return "";
		}
		return materialSuffixes.get(material).get(RandomUtils.nextInt(materialSuffixes.
				get(material).size())).replace('&', '\u00A7').replace("\u00A7\u00A7", "&");
	}

	public String randomTierPrefix(Tier tier) {
		if (!tierPrefixes.containsKey(tier)) {
			return "";
		}
		return tierPrefixes.get(tier).get(RandomUtils.nextInt(tierPrefixes.
				get(tier).size())).replace('&', '\u00A7').replace("\u00A7\u00A7", "&");
	}

	public String randomTierSuffix(Tier tier) {
		if (!tierSuffixes.containsKey(tier)) {
			return "";
		}
		return tierSuffixes.get(tier).get(RandomUtils.nextInt(tierSuffixes.
				get(tier).size())).replace('&', '\u00A7').replace("\u00A7\u00A7", "&");
	}

	public String getMinecraftMaterialName(Material material) {
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

	public String getMythicMaterialName(MaterialData matData) {
		String comb =
				String.format("%s;%s", String.valueOf(matData.getItemTypeId()), String.valueOf(matData.getData()));
		String comb2;
		if (matData.getData() == (byte) 0) {
			comb2 = String.valueOf(matData.getItemTypeId());
		} else {
			comb2 = comb;
		}
		String mythicMatName = getPlugin().getLanguageManager().getMessage("displayNames." + comb.toLowerCase());
		if (mythicMatName == null) {
			mythicMatName = getPlugin().getLanguageManager().getMessage("displayNames." + comb2.toLowerCase());
			if (mythicMatName == null) {
				mythicMatName = getMinecraftMaterialName(matData.getItemType());
			}
		}
		return WordUtils.capitalize(mythicMatName);
	}

	public String getEnchantmentTypeName(ItemStack itemStack) {
		Enchantment enchantment = null;
		Integer level = 0;
		for (Map.Entry<Enchantment, Integer> e : itemStack.getEnchantments().entrySet()) {
			if (e.getValue() > level) {
				enchantment = e.getKey();
				level = e.getValue();
			}
		}
		if (enchantment == null) {
			return getPlugin().getLanguageManager().getMessage("displayNames.Ordinary");
		}
		String ench = getPlugin().getLanguageManager().getMessage("displayNames." + enchantment.getName());
		if (ench != null) {
			return ench;
		}
		return "Ordinary";
	}

	/**
	 * Gets the instance of MythicDrops being run.
	 *
	 * @return instance of MythicDrops
	 */
	public MythicDrops getPlugin() {
		return plugin;
	}

	public String getItemTypeName(MaterialData matData) {
		String itemType = getPlugin().getItemManager().itemTypeFromMatData(matData);
		if (itemType == null) {
			return null;
		}
		String mythicMatName = getPlugin().getLanguageManager().getMessage("displayNames." + itemType.toLowerCase());
		if (mythicMatName == null) {
			mythicMatName = itemType;
		}
		return WordUtils.capitalize(mythicMatName);
	}

	public List<String> randomLore(Material material, Tier tier) {
		List<String> lore = new ArrayList<String>(getGeneralLore());
		if (getMaterialLore().containsKey(material)) {
			lore.addAll(getMaterialLore().get(material));
		}
		if (getTierLore().containsKey(tier)) {
			lore.addAll(getTierLore().get(tier));
		}
		return Arrays.asList(lore.get(RandomUtils.nextInt(lore.size())).split("/n"));
	}

	public Map<Tier, List<String>> getTierLore() {
		return tierLore;
	}

	/**
	 * Gets the list of general lore from the plugin's resources.
	 *
	 * @return list of general lore
	 */
	public List<String> getGeneralLore() {
		return generalLore;
	}

	/**
	 * Gets a Map containing lore for various {@link Material}s.
	 *
	 * @return map of lore for Materials
	 */
	public Map<Material, List<String>> getMaterialLore() {
		return materialLore;
	}

	public Map<Enchantment, List<String>> getEnchantmentPrefixes() {
		return enchantmentPrefixes;
	}

	public Map<Enchantment, List<String>> getEnchantmentSuffixes() {
		return enchantmentSuffixes;
	}
}