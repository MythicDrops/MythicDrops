/*
 * Copyright (c) 2013. ToppleTheNun
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.conventnunnery.plugins.mythicdrops.managers;

import com.conventnunnery.libraries.utils.StringUtils;
import com.conventnunnery.plugins.mythicdrops.MythicDrops;
import com.conventnunnery.plugins.mythicdrops.configuration.MythicConfigurationFile;
import com.conventnunnery.plugins.mythicdrops.objects.Tier;
import com.modcrafting.diablodrops.name.NameLoader;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * The type NameManager.
 */
public class NameManager {
    private final MythicDrops plugin;
    private final List<String> basicPrefixes;
    private final List<String> basicSuffixes;
    private final List<String> basicLore;
    private final NameLoader nameLoader;

    /**
     * Instantiates a new NameManager.
     *
     * @param plugin the plugin
     */
    public NameManager(MythicDrops plugin) {
        this.plugin = plugin;
        basicPrefixes = new ArrayList<String>();
        basicSuffixes = new ArrayList<String>();
        basicLore = new ArrayList<String>();
        nameLoader = new NameLoader(this.plugin);
        loadPrefixes();
        loadSuffixes();
        loadLore();
    }

    public List<String> getBasicLore() {
        return basicLore;
    }

    /**
     * Gets basic prefixes.
     *
     * @return the basicPrefixes
     */
    @SuppressWarnings("unused")
    public List<String> getBasicPrefixes() {
        return basicPrefixes;
    }

    /**
     * Gets basic suffixes.
     *
     * @return the basicSuffixes
     */
    @SuppressWarnings("unused")
    public List<String> getBasicSuffixes() {
        return basicSuffixes;
    }

    /**
     * Gets item type name.
     *
     * @param matData the mat data
     * @return the item type name
     */
    public String getItemTypeName(MaterialData matData) {
        String itemType = getPlugin().getItemManager().itemTypeFromMatData(matData);
        if (itemType == null) {
            return null;
        }
        String mythicMatName = getPlugin().getConfigurationManager()
                .getConfiguration(MythicConfigurationFile.LANGUAGE)
                .getString(itemType.toLowerCase());
        if (mythicMatName == null) {
            mythicMatName = itemType;
        }
        return StringUtils.getInitCappedString(mythicMatName.split(" "));
    }

    /**
     * Gets Minecraft material name.
     *
     * @param material the material
     * @return the minecraft material name
     */
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
        return StringUtils.getInitCappedString(prettyMaterialName.split(" "));
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
            return getPlugin().getConfigurationManager()
                    .getConfiguration(MythicConfigurationFile.LANGUAGE).getString("Ordinary");
        }
        String ench = getPlugin().getConfigurationManager()
                .getConfiguration(MythicConfigurationFile.LANGUAGE).getString(enchantment.getName());
        if (ench != null) {
            return ench;
        }
        return "Ordinary";
    }

    /**
     * Gets mythic material name.
     *
     * @param matData the mat data
     * @return the mythic material name
     */
    public String getMythicMaterialName(MaterialData matData) {
        String comb =
                String.format("%s;%s", String.valueOf(matData.getItemTypeId()), String.valueOf(matData.getData()));
        String comb2;
        if (matData.getData() == (byte) 0) {
            comb2 = String.valueOf(matData.getItemTypeId());
        } else {
            comb2 = comb;
        }
        String mythicMatName = getPlugin().getConfigurationManager()
                .getConfiguration(MythicConfigurationFile.LANGUAGE).getString(comb.toLowerCase());
        if (mythicMatName == null) {
            mythicMatName = getPlugin().getConfigurationManager()
                    .getConfiguration(MythicConfigurationFile.LANGUAGE)
                    .getString(comb2.toLowerCase());
            if (mythicMatName == null) {
                mythicMatName = getMinecraftMaterialName(matData.getItemType());
            }
        }
        return StringUtils.getInitCappedString(mythicMatName.split(" "));
    }

    /**
     * Gets plugin.
     *
     * @return the plugin
     */
    public MythicDrops getPlugin() {
        return plugin;
    }

    public void debugPrefixesAndSuffixes() {
        plugin.getDebug().debug(
                "Loaded basic prefixes: " + basicPrefixes.size());
        plugin.getDebug().debug(
                "Loaded basic suffixes: " + basicSuffixes.size());
    }

    /**
     * Load prefixes.
     */
    public final void loadPrefixes() {
        basicPrefixes.clear();
        try {
            nameLoader.writeDefault("resources/prefix.txt", false);
        } catch (IOException e) {
            getPlugin().getLogger().warning("Could not write prefix file");
        }
        try {
            nameLoader.loadFile(basicPrefixes, "resources/prefix.txt");
        } catch (IOException e) {
            getPlugin().getLogger().warning("Could not read prefix file");
        }
    }

    public final void loadLore() {
        basicLore.clear();
        try {
            nameLoader.writeDefault("resources/lore.txt", false);
        } catch (IOException e) {
            getPlugin().getLogger().warning("Could not write lore file");
        }
        try {
            nameLoader.loadFile(basicLore, "resources/lore.txt");
        } catch (IOException e) {
            getPlugin().getLogger().warning("Could not read lore file");
        }
    }

    /**
     * Load suffixes.
     */
    public final void loadSuffixes() {
        basicSuffixes.clear();
        try {
            nameLoader.writeDefault("resources/suffix.txt", false);
        } catch (IOException e) {
            getPlugin().getLogger().warning("Could not write suffix file");
        }
        try {
            nameLoader.loadFile(basicSuffixes, "resources/suffix.txt");
        } catch (IOException e) {
            getPlugin().getLogger().warning("Could not read suffix file");
        }
    }

    public List<String> randomLore() {
        String string = basicLore.get(getPlugin().getRandom().nextInt(basicLore.size())).replace('&', '\u00A7').replace(
                "\u00A7\u00A7", "&");
        return Arrays.asList(string.split("/n"));
    }

    /**
     * Random basic prefix.
     *
     * @return the string
     */
    public String randomBasicPrefix() {
        return basicPrefixes.get(getPlugin().getRandom().nextInt(basicPrefixes
                .size())).replace('&', '\u00A7').replace("\u00A7\u00A7", "&");
    }

    /**
     * Random basic suffix.
     *
     * @return the string
     */
    public String randomBasicSuffix() {
        return basicSuffixes.get(getPlugin().getRandom().nextInt(basicSuffixes
                .size())).replace('&', '\u00A7').replace("\u00A7\u00A7", "&");
    }

    /**
     * Random formatted name.
     *
     * @param itemStack the mat data
     * @param tier      the tier
     * @return the string
     */
    public String randomFormattedName(ItemStack itemStack, Tier tier) {
        if (itemStack == null || tier == null) {
            return "Mythic Item";
        }
        String format = getPlugin().getPluginSettings()
                .getDisplayItemNameFormat();
        if (format == null) {
            return "Mythic Item";
        }
        String minecraftName = getMinecraftMaterialName(itemStack.getData()
                .getItemType());
        String mythicName = getMythicMaterialName(itemStack.getData());
        String prefix = randomBasicPrefix();
        String suffix = randomBasicSuffix();
        String itemType = getItemTypeName(itemStack.getData());
        String tierName = tier.getDisplayName();
        String enchantment = getEnchantmentTypeName(itemStack);

        String name = format;

        if (name.contains("%basematerial%")) {
            name = name.replace("%basematerial%", minecraftName);
        }
        if (name.contains("%mythicmaterial%")) {
            name = name.replace("%mythicmaterial%", mythicName);
        }
        if (name.contains("%basicprefix%")) {
            name = name.replace("%basicprefix%", prefix);
        }
        if (name.contains("%basicsuffix%")) {
            name = name.replace("%basicsuffix%", suffix);
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
        return tier.getDisplayColor() + name.replace('&', '\u00A7').replace("\u00A7\u00A7", "&") +
                tier.getIdentificationColor();

    }
}
