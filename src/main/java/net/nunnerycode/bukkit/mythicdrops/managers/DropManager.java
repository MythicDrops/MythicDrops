package net.nunnerycode.bukkit.mythicdrops.managers;

import com.conventnunnery.libraries.utils.ItemStackUtils;
import com.conventnunnery.libraries.utils.RandomUtils;
import net.nunnerycode.bukkit.mythicdrops.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.items.ItemGenerationReason;
import net.nunnerycode.bukkit.mythicdrops.api.items.MythicEnchantment;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import net.nunnerycode.bukkit.mythicdrops.tiers.DefaultTier;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DropManager {
    private final MythicDrops plugin;

    public DropManager(final MythicDrops plugin) {
        this.plugin = plugin;
    }

    public ItemStack constructItemStackFromTier(Tier tier, ItemGenerationReason reason) throws NullPointerException {
        ItemStack is = null;
        if (tier == null) {
            throw new NullPointerException("Tier is null");
        }
        if (tier.equals(DefaultTier.IDENTITY_TOME)) {
            // identity tome
            return is;
        }
        if (tier.equals(DefaultTier.SOCKET_GEM)) {
            // socket gem
            return is;
        }
        if (tier.equals(DefaultTier.UNIDENTIFIED_ITEM)) {
            // unidentified item
            return is;
        }
        Set<MaterialData> materialDataSet = getPlugin().getItemManager().getMaterialDataSetForTier(tier);
        MaterialData materialData = materialDataSet.toArray(new MaterialData[materialDataSet.size()])[((int) RandomUtils
                .randomRangeWholeExclusive(0, materialDataSet.size()))];
        if (materialData == null || materialData.getItemTypeId() == 0
                || materialData.getItemType() == Material.AIR) {
            return is;
        }
        is = materialData.toItemStack(1);
        if (reason != null && reason != ItemGenerationReason.COMMAND) {
            is.setDurability(ItemStackUtils.getAcceptableDurability(materialData.getItemType(),
                    ItemStackUtils.getDurabilityForMaterial(materialData.getItemType(), tier.getMinimumDurabilityPercentage(),
                            tier.getMaximumDurabilityPercentage())));
        }
        for (MythicEnchantment me : tier.getBaseEnchantments()) {
            if (me.getEnchantment() == null) {
                continue;
            }
            if (tier.isSafeBaseEnchantments() && me.getEnchantment().canEnchantItem(is)) {
                EnchantmentWrapper enchantmentWrapper = new EnchantmentWrapper(me.getEnchantment().getId());
                int minimumLevel = Math.max(me.getMinimumLevel(), enchantmentWrapper.getStartLevel());
                int maximumLevel = Math.min(me.getMaximumLevel(), enchantmentWrapper.getMaxLevel());
                is.addEnchantment(me.getEnchantment(), getAcceptableEnchantmentLevel(me.getEnchantment(),
                        (int) RandomUtils.randomRangeWholeInclusive(minimumLevel, maximumLevel)));
            } else if (!tier.isSafeBaseEnchantments()) {
                is.addUnsafeEnchantment(me.getEnchantment(),
                        (int) RandomUtils.randomRangeWholeInclusive(me.getMinimumLevel(), me.getMaximumLevel()));
            }
        }
        if (tier.getMaximumAmountOfBonusEnchantments() > 0) {
            int randEnchs = (int) RandomUtils
                    .randomRangeWholeInclusive(tier.getMinimumAmountOfBonusEnchantments(),
                            tier.getMaximumAmountOfBonusEnchantments());
            Set<MythicEnchantment> allowEnchs = tier.getBonusEnchantments();
            List<Enchantment> stackEnchs = new ArrayList<Enchantment>();
            for (Enchantment e : Enchantment.values()) {
                if (tier.isSafeBonusEnchantments() && e.canEnchantItem(is)) {
                    stackEnchs.add(e);
                }
            }
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
                    MythicEnchantment ench = actual.get((int) RandomUtils.randomRangeWholeExclusive(0, actual.size()));
                    int lev = (int) RandomUtils
                            .randomRangeWholeInclusive(ench.getMinimumLevel(), ench.getMaximumLevel());
                    if (tier.isSafeBonusEnchantments()) {
                        if (!tier.isSafeBonusEnchantments()) {
                            is.addEnchantment(
                                    ench.getEnchantment(),
                                    getAcceptableEnchantmentLevel(ench.getEnchantment(),
                                            lev <= 0 ? 1 : Math.abs(lev)));
                        } else {
                            is.addUnsafeEnchantment(ench.getEnchantment(), lev <= 0 ? 1 : Math.abs(lev));
                        }
                    } else {
                        is.addUnsafeEnchantment(ench.getEnchantment(), lev <= 0 ? 1 : Math.abs(lev));
                    }
                }
            }
        }
        return is;
    }

    public MythicDrops getPlugin() {
        return plugin;
    }

    private int getAcceptableEnchantmentLevel(Enchantment ench, int level) {
        EnchantmentWrapper ew = new EnchantmentWrapper(ench.getId());
        int i = level;
        if (i > ew.getMaxLevel()) {
            i = ew.getMaxLevel();
        } else if (i < ew.getStartLevel()) {
            i = ew.getStartLevel();
        }
        return i;
    }
}
