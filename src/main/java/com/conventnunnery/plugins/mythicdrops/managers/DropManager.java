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

import com.conventnunnery.plugins.conventlib.utils.ItemStackUtils;
import com.conventnunnery.plugins.conventlib.utils.RandomUtils;
import com.conventnunnery.plugins.mythicdrops.MythicDrops;
import com.conventnunnery.plugins.mythicdrops.objects.CustomItem;
import com.conventnunnery.plugins.mythicdrops.objects.IdentityTome;
import com.conventnunnery.plugins.mythicdrops.objects.MythicEnchantment;
import com.conventnunnery.plugins.mythicdrops.objects.SocketGem;
import com.conventnunnery.plugins.mythicdrops.objects.SocketItem;
import com.conventnunnery.plugins.mythicdrops.objects.Tier;
import com.conventnunnery.plugins.mythicdrops.objects.UnidentifiedItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * A class that handles all of the plugin's drop creation.
 */
public class DropManager {
    private final MythicDrops plugin;
    private List<CustomItem> customItems;

    /**
     * Instantiates a new Drop API.
     *
     * @param plugin the plugin
     */
    public DropManager(MythicDrops plugin) {
        this.plugin = plugin;
        customItems = new ArrayList<CustomItem>();
    }

    /**
     * Construct a random ItemStack.
     *
     * @param reason the reason
     * @return random ItemStack
     */
    public ItemStack constructItemStack(GenerationReason reason) {
        switch (reason) {
            case MOB_SPAWN:
                if (getPlugin().getPluginSettings().isAllowCustomToSpawn() &&
                        getPlugin().getPluginSettings().isOnlyCustomItems() ||
                        getPlugin().getRandom().nextDouble() <=
                                getPlugin().getPluginSettings().getPercentageCustomDrop() &&
                                !customItems.isEmpty()) {
                    return randomCustomItemWithChance().toItemStack();
                }
                if (getPlugin().getPluginSettings().isSocketGemsEnabled()
                        && getPlugin().getRandom().nextDouble() < getPlugin()
                        .getPluginSettings().getSocketGemsChance()) {
                    MaterialData materialData = getPlugin().getSocketGemManager().getRandomSocketGemMaterial();
                    SocketGem socketGem = getPlugin().getSocketGemManager().getRandomSocketGemWithChance();
                    if (materialData != null && socketGem != null) {
                        return new SocketItem(materialData, socketGem);
                    }
                }
                if (getPlugin().getPluginSettings().isIdentityTomeEnabled() && getPlugin().getRandom().nextDouble() <
                        getPlugin().getPluginSettings().getIdentityTomeChance()) {
                    return new IdentityTome();
                }
                if (getPlugin().getPluginSettings().isUnidentifiedItemsEnabled() && getPlugin().getRandom()
                        .nextDouble() < getPlugin().getPluginSettings().getUnidentifiedItemsChance()) {
                    return new UnidentifiedItem(getPlugin().getItemManager().getMatDataFromTier(getPlugin()
                            .getTierManager().randomTierWithChance()));
                }
                return constructItemStack(getPlugin().getTierManager().randomTierWithChance(), reason);
            case COMMAND:
                return constructItemStack(getPlugin().getTierManager()
                        .randomTierWithChance(), reason);
            case EXTERNAL:
                return constructItemStack(getPlugin().getTierManager()
                        .randomTierWithChance(), reason);
            default:
                return constructItemStack(getPlugin().getTierManager()
                        .randomTierWithChance(), reason);
        }
    }

    public ItemStack constructItemStack(MaterialData matData, GenerationReason reason) {
        ItemStack itemstack = null;
        Tier tier;
        if (reason == GenerationReason.IDENTIFYING) {
            tier = getPlugin().getTierManager().getRandomTierWithIdentifyChance(getPlugin().getItemManager()
                    .getTiersForMaterialData(matData));
        } else {
            tier = getPlugin().getTierManager().getRandomTierWithChance(
                    getPlugin().getItemManager().getTiersForMaterialData
                            (matData));
        }
        if (tier == null) {
            return null;
        }
        if (matData == null || matData.getItemTypeId() == 0
                || matData.getItemType() == Material.AIR) {
            return itemstack;
        }
        itemstack = matData.toItemStack(1);
        if (itemstack == null) {
            return itemstack;
        }
        if (reason != null && reason == GenerationReason.MOB_SPAWN) {
            itemstack.setDurability(ItemStackUtils.getAcceptableDurability(matData.getItemType(),
                    ItemStackUtils.getDurabilityForMaterial(matData.getItemType(), tier.getMinimumDurability(),
                            tier.getMaximumDurability())));
        }
        for (MythicEnchantment me : tier.getBaseEnchantments()) {
            if (me.getEnchantment() == null) {
                continue;
            }
            if (tier.isSafeBaseEnchantments() && me.getEnchantment().canEnchantItem(itemstack)) {
                EnchantmentWrapper enchantmentWrapper = new EnchantmentWrapper(me.getEnchantment().getId());
                int minimumLevel = Math.max(me.getMinimumLevel(), enchantmentWrapper.getStartLevel());
                int maximumLevel = Math.min(me.getMaximumLevel(), enchantmentWrapper.getMaxLevel());
                itemstack.addEnchantment(me.getEnchantment(), getAcceptableEnchantmentLevel(me.getEnchantment(),
                        (int) RandomUtils.randomRangeWholeInclusive(minimumLevel, maximumLevel)));
            } else if (!tier.isSafeBaseEnchantments()) {
                itemstack.addUnsafeEnchantment(me.getEnchantment(),
                        (int) RandomUtils.randomRangeWholeInclusive(me.getMinimumLevel(), me.getMaximumLevel()));
            }
        }
        if (tier.getMaximumBonusEnchantments() > 0) {
            int randEnchs = (int) RandomUtils
                    .randomRangeWholeInclusive(tier.getMinimumBonusEnchantments(), tier.getMaximumBonusEnchantments());
            Set<MythicEnchantment> allowEnchs = tier.getBonusEnchantments();
            List<Enchantment> stackEnchs = getEnchantStack(itemstack);
            List<MythicEnchantment> actual = new ArrayList<MythicEnchantment>();
            for (MythicEnchantment te : allowEnchs) {
                if (te.getEnchantment() == null) {
                    continue;
                }
                if (stackEnchs.contains(te.getEnchantment())) {
                    actual.add(te);
                }
            }
            for (int i = 0; i < randEnchs; i++) {
                if (actual.size() > 0) {
                    MythicEnchantment ench = actual.get(getPlugin().getRandom()
                            .nextInt(actual.size()));
                    int lev = (int) RandomUtils
                            .randomRangeWholeInclusive(ench.getMinimumLevel(), ench.getMaximumLevel());
                    if (tier.isSafeBonusEnchantments()) {
                        if (!getPlugin().getPluginSettings().isAllowEnchantsPastNormalLevel()) {
                            itemstack.addEnchantment(
                                    ench.getEnchantment(),
                                    getAcceptableEnchantmentLevel(ench.getEnchantment(),
                                            lev <= 0 ? 1 : Math.abs(lev)));
                        } else {
                            itemstack.addUnsafeEnchantment(ench.getEnchantment(), lev <= 0 ? 1 : Math.abs(lev));
                        }
                    } else {
                        itemstack.addUnsafeEnchantment(ench.getEnchantment(), lev <= 0 ? 1 : Math.abs(lev));
                    }
                }
            }
        }
        if (matData.getItemType() == null) {
            return itemstack;
        }
        ItemMeta im;
        if (itemstack.hasItemMeta()) {
            im = itemstack.getItemMeta();
        } else {
            im = Bukkit.getItemFactory().getItemMeta(matData.getItemType());
        }
        im.setDisplayName(getPlugin().getNameManager().randomFormattedName(
                itemstack, tier));
        List<String> toolTips = getPlugin().getPluginSettings()
                .getAdvancedToolTipFormat();
        List<String> tt = new ArrayList<String>();
        for (String s : toolTips) {
            tt.add(ChatColor.translateAlternateColorCodes(
                    '&',
                    s.replace("%itemtype%",
                            getPlugin().getNameManager().getItemTypeName(matData))
                            .replace("%tiername%",
                                    tier.getDisplayColor() + tier.getDisplayName())
                            .replace(
                                    "%basematerial%",
                                    getPlugin().getNameManager()
                                            .getMinecraftMaterialName(
                                                    itemstack.getType()))
                            .replace(
                                    "%mythicmaterial%",
                                    getPlugin().getNameManager()
                                            .getMythicMaterialName(
                                                    itemstack.getData())).replace("%enchantment%",
                            tier.getDisplayColor() + getPlugin().getNameManager().getEnchantmentTypeName(itemstack) +
                                    tier.getIdentificationColor())));
        }
        if (getPlugin().getPluginSettings().isSockettedItemsEnabled() &&
                getPlugin().getRandom().nextDouble() <= getPlugin().getPluginSettings().getSpawnWithSocketChance()) {
            int amtTT = 0;
            for (long i = 0;
                 i < RandomUtils.randomRangeWholeInclusive(tier.getMinimumSockets(), tier.getMaximumSockets()); i++) {
                tt.add(ChatColor.GOLD + "(Socket)");
                amtTT++;
            }
            if (amtTT > 0) {
                tt.add(ChatColor.GRAY + "Find a " + ChatColor.GOLD + "Socket Gem" + ChatColor.GRAY + " to fill a " +
                        ChatColor.GOLD + "(Socket)");
            }
        }
        if (getPlugin().getPluginSettings().isRandomLoreEnabled() &&
                getPlugin().getRandom().nextDouble() <= getPlugin().getPluginSettings().getRandomLoreChance() &&
                !getPlugin().getNameManager().getBasicLore().isEmpty()) {
            tt.addAll(getPlugin().getNameManager().randomLore());
        }
        im.setLore(tt);
        if (im instanceof Repairable) {
            Repairable r = (Repairable) im;
            r.setRepairCost(1000);
            itemstack.setItemMeta((ItemMeta) r);
        } else {
            itemstack.setItemMeta(im);
        }
        return itemstack;
    }

    public ItemStack constructItemStack(Tier tier, MaterialData materialData, GenerationReason reason) {
        ItemStack itemstack = null;
        MaterialData matData = materialData;
        int attempts = 0;
        if (tier == null) {
            return null;
        }
        if (tier.equals(getPlugin().getTierManager().getIdentityTomeTier())) {
            return new IdentityTome();
        }
        if (tier.equals(getPlugin().getTierManager().getSocketGemTier())) {
            MaterialData mat = getPlugin().getSocketGemManager().getRandomSocketGemMaterial();
            SocketGem socketGem = getPlugin().getSocketGemManager().getRandomSocketGemWithChance();
            while (materialData == null || socketGem == null) {
                if (getPlugin().getSocketGemManager().getSocketGems().isEmpty() || getPlugin().getPluginSettings()
                        .getSocketGemMaterials().isEmpty()) {
                    return null;
                }
                mat = getPlugin().getSocketGemManager().getRandomSocketGemMaterial();
                socketGem = getPlugin().getSocketGemManager().getRandomSocketGemWithChance();
            }
            return new SocketItem(mat, socketGem);
        }
        if (tier.equals(getPlugin().getTierManager().getUnidentifiedItemTier())) {
            return new UnidentifiedItem(getPlugin().getItemManager().getMatDataFromTier(getPlugin().getTierManager()
                    .randomTierWithChance()));
        }
        while (matData == null && attempts < 10) {
            matData = getPlugin().getItemManager().getMatDataFromTier(tier);
            attempts++;
        }
        if (matData == null || matData.getItemTypeId() == 0
                || matData.getItemType() == Material.AIR) {
            return itemstack;
        }
        itemstack = matData.toItemStack(1);
        if (itemstack == null) {
            return itemstack;
        }
        if (reason != null && reason != GenerationReason.COMMAND) {
            itemstack.setDurability(ItemStackUtils.getAcceptableDurability(matData.getItemType(),
                    ItemStackUtils.getDurabilityForMaterial(matData.getItemType(), tier.getMinimumDurability(),
                            tier.getMaximumDurability())));
        }
        for (MythicEnchantment me : tier.getBaseEnchantments()) {
            if (me.getEnchantment() == null) {
                continue;
            }
            if (tier.isSafeBaseEnchantments() && me.getEnchantment().canEnchantItem(itemstack)) {
                EnchantmentWrapper enchantmentWrapper = new EnchantmentWrapper(me.getEnchantment().getId());
                int minimumLevel = Math.max(me.getMinimumLevel(), enchantmentWrapper.getStartLevel());
                int maximumLevel = Math.min(me.getMaximumLevel(), enchantmentWrapper.getMaxLevel());
                itemstack.addEnchantment(me.getEnchantment(), getAcceptableEnchantmentLevel(me.getEnchantment(),
                        (int) RandomUtils.randomRangeWholeInclusive(minimumLevel, maximumLevel)));
            } else if (!tier.isSafeBaseEnchantments()) {
                itemstack.addUnsafeEnchantment(me.getEnchantment(),
                        (int) RandomUtils.randomRangeWholeInclusive(me.getMinimumLevel(), me.getMaximumLevel()));
            }
        }
        if (tier.getMaximumBonusEnchantments() > 0) {
            int randEnchs = (int) RandomUtils
                    .randomRangeWholeInclusive(tier.getMinimumBonusEnchantments(), tier.getMaximumBonusEnchantments());
            Set<MythicEnchantment> allowEnchs = tier.getBonusEnchantments();
            List<Enchantment> stackEnchs = getEnchantStack(itemstack);
            List<MythicEnchantment> actual = new ArrayList<MythicEnchantment>();
            for (MythicEnchantment te : allowEnchs) {
                if (te.getEnchantment() == null) {
                    continue;
                }
                if (stackEnchs.contains(te.getEnchantment())) {
                    actual.add(te);
                }
            }
            for (int i = 0; i < randEnchs; i++) {
                if (actual.size() > 0) {
                    MythicEnchantment ench = actual.get(getPlugin().getRandom()
                            .nextInt(actual.size()));
                    int lev = (int) RandomUtils
                            .randomRangeWholeInclusive(ench.getMinimumLevel(), ench.getMaximumLevel());
                    if (tier.isSafeBonusEnchantments()) {
                        if (!getPlugin().getPluginSettings().isAllowEnchantsPastNormalLevel()) {
                            itemstack.addEnchantment(
                                    ench.getEnchantment(),
                                    getAcceptableEnchantmentLevel(ench.getEnchantment(),
                                            lev <= 0 ? 1 : Math.abs(lev)));
                        } else {
                            itemstack.addUnsafeEnchantment(ench.getEnchantment(), lev <= 0 ? 1 : Math.abs(lev));
                        }
                    } else {
                        itemstack.addUnsafeEnchantment(ench.getEnchantment(), lev <= 0 ? 1 : Math.abs(lev));
                    }
                }
            }
        }
        if (matData.getItemType() == null) {
            return itemstack;
        }
        ItemMeta im;
        if (itemstack.hasItemMeta()) {
            im = itemstack.getItemMeta();
        } else {
            im = Bukkit.getItemFactory().getItemMeta(matData.getItemType());
        }
        im.setDisplayName(getPlugin().getNameManager().randomFormattedName(
                itemstack, tier));
        List<String> toolTips = getPlugin().getPluginSettings()
                .getAdvancedToolTipFormat();
        List<String> tt = new ArrayList<String>();
        for (String s : toolTips) {
            tt.add(ChatColor.translateAlternateColorCodes(
                    '&',
                    s.replace("%itemtype%",
                            getPlugin().getNameManager().getItemTypeName(matData))
                            .replace("%tiername%",
                                    tier.getDisplayColor() + tier.getDisplayName())
                            .replace(
                                    "%basematerial%",
                                    getPlugin().getNameManager()
                                            .getMinecraftMaterialName(
                                                    itemstack.getType()))
                            .replace(
                                    "%mythicmaterial%",
                                    getPlugin().getNameManager()
                                            .getMythicMaterialName(
                                                    itemstack.getData())).replace("%enchantment%",
                            tier.getDisplayColor() + getPlugin().getNameManager().getEnchantmentTypeName(itemstack) +
                                    tier.getIdentificationColor())));
        }
        if (getPlugin().getPluginSettings().isSockettedItemsEnabled() &&
                getPlugin().getRandom().nextDouble() <= getPlugin().getPluginSettings().getSpawnWithSocketChance()) {
            int amtTT = 0;
            for (long i = 0;
                 i < RandomUtils.randomRangeWholeInclusive(tier.getMinimumSockets(), tier.getMaximumSockets()); i++) {
                tt.add(ChatColor.GOLD + "(Socket)");
                amtTT++;
            }
            if (amtTT > 0) {
                tt.add(ChatColor.GRAY + "Find a " + ChatColor.GOLD + "Socket Gem" + ChatColor.GRAY + " to fill a " +
                        ChatColor.GOLD + "(Socket)");
            }
        }
        if (getPlugin().getPluginSettings().isRandomLoreEnabled() &&
                getPlugin().getRandom().nextDouble() <= getPlugin().getPluginSettings().getRandomLoreChance() &&
                !getPlugin().getNameManager().getBasicLore().isEmpty()) {
            tt.addAll(getPlugin().getNameManager().randomLore());
        }
        im.setLore(tt);
        if (im instanceof Repairable) {
            Repairable r = (Repairable) im;
            r.setRepairCost(1000);
            itemstack.setItemMeta((ItemMeta) r);
        } else {
            itemstack.setItemMeta(im);
        }
        return itemstack;
    }

    /**
     * Construct an ItemStack based on a Tier.
     *
     * @param tier   Tier to base the ItemStack on
     * @param reason reason to generate the ItemStack
     * @return constructed ItemStack
     */
    public ItemStack constructItemStack(Tier tier, GenerationReason reason) {
        ItemStack itemstack = null;
        MaterialData matData = null;
        int attempts = 0;
        if (tier == null) {
            return null;
        }
        if (tier.equals(getPlugin().getTierManager().getIdentityTomeTier())) {
            return new IdentityTome();
        }
        if (tier.equals(getPlugin().getTierManager().getSocketGemTier())) {
            MaterialData materialData = getPlugin().getSocketGemManager().getRandomSocketGemMaterial();
            SocketGem socketGem = getPlugin().getSocketGemManager().getRandomSocketGemWithChance();
            while (materialData == null || socketGem == null) {
                if (getPlugin().getSocketGemManager().getSocketGems().isEmpty() || getPlugin().getPluginSettings()
                        .getSocketGemMaterials().isEmpty()) {
                    return null;
                }
                materialData = getPlugin().getSocketGemManager().getRandomSocketGemMaterial();
                socketGem = getPlugin().getSocketGemManager().getRandomSocketGemWithChance();
            }
            return new SocketItem(materialData, socketGem);
        }
        if (tier.equals(getPlugin().getTierManager().getUnidentifiedItemTier())) {
            return new UnidentifiedItem(getPlugin().getItemManager().getMatDataFromTier(getPlugin().getTierManager()
                    .randomTierWithChance()));
        }
        while (matData == null && attempts < 10) {
            matData = getPlugin().getItemManager().getMatDataFromTier(tier);
            attempts++;
        }
        if (matData == null || matData.getItemTypeId() == 0
                || matData.getItemType() == Material.AIR) {
            return itemstack;
        }
        itemstack = matData.toItemStack(1);
        if (itemstack == null) {
            return itemstack;
        }
        if (reason != null && reason != GenerationReason.COMMAND) {
            itemstack.setDurability(ItemStackUtils.getAcceptableDurability(matData.getItemType(),
                    ItemStackUtils.getDurabilityForMaterial(matData.getItemType(), tier.getMinimumDurability(),
                            tier.getMaximumDurability())));
        }
        for (MythicEnchantment me : tier.getBaseEnchantments()) {
            if (me.getEnchantment() == null) {
                continue;
            }
            if (tier.isSafeBaseEnchantments() && me.getEnchantment().canEnchantItem(itemstack)) {
                EnchantmentWrapper enchantmentWrapper = new EnchantmentWrapper(me.getEnchantment().getId());
                int minimumLevel = Math.max(me.getMinimumLevel(), enchantmentWrapper.getStartLevel());
                int maximumLevel = Math.min(me.getMaximumLevel(), enchantmentWrapper.getMaxLevel());
                itemstack.addEnchantment(me.getEnchantment(), getAcceptableEnchantmentLevel(me.getEnchantment(),
                        (int) RandomUtils.randomRangeWholeInclusive(minimumLevel, maximumLevel)));
            } else if (!tier.isSafeBaseEnchantments()) {
                itemstack.addUnsafeEnchantment(me.getEnchantment(),
                        (int) RandomUtils.randomRangeWholeInclusive(me.getMinimumLevel(), me.getMaximumLevel()));
            }
        }
        if (tier.getMaximumBonusEnchantments() > 0) {
            int randEnchs = (int) RandomUtils
                    .randomRangeWholeInclusive(tier.getMinimumBonusEnchantments(), tier.getMaximumBonusEnchantments());
            Set<MythicEnchantment> allowEnchs = tier.getBonusEnchantments();
            List<Enchantment> stackEnchs = getEnchantStack(itemstack);
            List<MythicEnchantment> actual = new ArrayList<MythicEnchantment>();
            for (MythicEnchantment te : allowEnchs) {
                if (te.getEnchantment() == null) {
                    continue;
                }
                if (stackEnchs.contains(te.getEnchantment())) {
                    actual.add(te);
                }
            }
            for (int i = 0; i < randEnchs; i++) {
                if (actual.size() > 0) {
                    MythicEnchantment ench = actual.get(getPlugin().getRandom()
                            .nextInt(actual.size()));
                    int lev = (int) RandomUtils
                            .randomRangeWholeInclusive(ench.getMinimumLevel(), ench.getMaximumLevel());
                    if (tier.isSafeBonusEnchantments()) {
                        if (!getPlugin().getPluginSettings().isAllowEnchantsPastNormalLevel()) {
                            itemstack.addEnchantment(
                                    ench.getEnchantment(),
                                    getAcceptableEnchantmentLevel(ench.getEnchantment(),
                                            lev <= 0 ? 1 : Math.abs(lev)));
                        } else {
                            itemstack.addUnsafeEnchantment(ench.getEnchantment(), lev <= 0 ? 1 : Math.abs(lev));
                        }
                    } else {
                        itemstack.addUnsafeEnchantment(ench.getEnchantment(), lev <= 0 ? 1 : Math.abs(lev));
                    }
                }
            }
        }
        if (matData.getItemType() == null) {
            return itemstack;
        }
        ItemMeta im;
        if (itemstack.hasItemMeta()) {
            im = itemstack.getItemMeta();
        } else {
            im = Bukkit.getItemFactory().getItemMeta(matData.getItemType());
        }
        im.setDisplayName(getPlugin().getNameManager().randomFormattedName(
                itemstack, tier));
        List<String> toolTips = getPlugin().getPluginSettings()
                .getAdvancedToolTipFormat();
        List<String> tt = new ArrayList<String>();
        for (String s : toolTips) {
            tt.add(ChatColor.translateAlternateColorCodes(
                    '&',
                    s.replace("%itemtype%",
                            getPlugin().getNameManager().getItemTypeName(matData))
                            .replace("%tiername%",
                                    tier.getDisplayColor() + tier.getDisplayName())
                            .replace(
                                    "%basematerial%",
                                    getPlugin().getNameManager()
                                            .getMinecraftMaterialName(
                                                    itemstack.getType()))
                            .replace(
                                    "%mythicmaterial%",
                                    getPlugin().getNameManager()
                                            .getMythicMaterialName(
                                                    itemstack.getData())).replace("%enchantment%",
                            tier.getDisplayColor() + getPlugin().getNameManager().getEnchantmentTypeName(itemstack) +
                                    tier.getIdentificationColor())));
        }
        if (getPlugin().getPluginSettings().isSockettedItemsEnabled() &&
                getPlugin().getRandom().nextDouble() <= getPlugin().getPluginSettings().getSpawnWithSocketChance()) {
            int amtTT = 0;
            for (long i = 0;
                 i < RandomUtils.randomRangeWholeInclusive(tier.getMinimumSockets(), tier.getMaximumSockets()); i++) {
                tt.add(ChatColor.GOLD + "(Socket)");
                amtTT++;
            }
            if (amtTT > 0) {
                tt.add(ChatColor.GRAY + "Find a " + ChatColor.GOLD + "Socket Gem" + ChatColor.GRAY + " to fill a " +
                        ChatColor.GOLD + "(Socket)");
            }
        }
        if (getPlugin().getPluginSettings().isRandomLoreEnabled() &&
                getPlugin().getRandom().nextDouble() <= getPlugin().getPluginSettings().getRandomLoreChance() &&
                !getPlugin().getNameManager().getBasicLore().isEmpty()) {
            tt.addAll(getPlugin().getNameManager().randomLore());
        }
        im.setLore(tt);
        if (im instanceof Repairable) {
            Repairable r = (Repairable) im;
            r.setRepairCost(1000);
            itemstack.setItemMeta((ItemMeta) r);
        } else {
            itemstack.setItemMeta(im);
        }
        return itemstack;
    }

    /**
     * Gets acceptable Enchantment level.
     *
     * @param ench  the Enchantment
     * @param level the level
     * @return the acceptable Enchantment level
     */
    public int getAcceptableEnchantmentLevel(Enchantment ench, int level) {
        EnchantmentWrapper ew = new EnchantmentWrapper(ench.getId());
        int i = level;
        if (i > ew.getMaxLevel()) {
            i = ew.getMaxLevel();
        } else if (i < ew.getStartLevel()) {
            i = ew.getStartLevel();
        }
        return i;
    }

    /**
     * Gets custom items.
     *
     * @return the custom items
     */
    public List<CustomItem> getCustomItems() {
        return customItems;
    }

    /**
     * Gets a list of Enchantments that can go on an ItemStack.
     *
     * @param ci ItemStack to check
     * @return list of possible Enchantments
     */
    public List<Enchantment> getEnchantStack(final ItemStack ci) {
        List<Enchantment> set = new ArrayList<Enchantment>();
        if (ci == null) {
            return set;
        }
        boolean bln = getPlugin().getPluginSettings().isSafeEnchantsOnly();
        for (Enchantment e : Enchantment.values()) {
            if (bln) {
                if (e.canEnchantItem(ci)) {
                    set.add(e);
                }
            } else {
                set.add(e);
            }
        }
        return set;
    }

    /**
     * Gets plugin.
     *
     * @return the plugin
     */
    public MythicDrops getPlugin() {
        return plugin;
    }

    public void debugCustomItems() {
        List<String> customItemNames = new ArrayList<String>();
        for (CustomItem ci : customItems) {
            customItemNames.add(ci.getName());
        }
        getPlugin().getDebug().debug(
                "Loaded custom items: "
                        + customItemNames.toString().replace("[", "")
                        .replace("]", ""));
    }

    /**
     * Random custom item.
     *
     * @return the custom item
     */
    @SuppressWarnings("unused")
    public CustomItem randomCustomItem() {
        return customItems.get(getPlugin().getRandom().nextInt(customItems.size()));
    }

    public CustomItem getCustomItemByName(String name) {
        for (CustomItem i : customItems) {
            if (name.equalsIgnoreCase(i.getName())) {
                return i;
            }
        }
        return null;
    }

    /**
     * Random custom item with chance.
     *
     * @return the custom item
     */
    public CustomItem randomCustomItemWithChance() {
        CustomItem ci = null;
        if (customItems == null || customItems.isEmpty()) {
            return ci;
        }
        while (ci == null) {
            for (CustomItem c : customItems) {
                double d = plugin.getRandom().nextDouble();
                if (d <= c.getChance()) {
                    ci = c;
                    break;
                }
            }
        }
        return ci;
    }

    /**
     * Enum of GenerationReasons.
     */
    public enum GenerationReason {
        /**
         * Use when spawning a mob
         */
        MOB_SPAWN,
        /**
         * Use for commands
         */
        COMMAND,
        /**
         * Use for anything else
         */
        EXTERNAL,
        /**
         * Use for Identifying
         */
        IDENTIFYING

    }

}
