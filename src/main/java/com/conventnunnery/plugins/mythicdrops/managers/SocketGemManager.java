/*
 * Copyright (c) 2013. ToppleTheNun
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.conventnunnery.plugins.mythicdrops.managers;

import com.conventnunnery.plugins.mythicdrops.MythicDrops;
import com.conventnunnery.plugins.mythicdrops.objects.SocketEffect;
import com.conventnunnery.plugins.mythicdrops.objects.SocketGem;
import com.conventnunnery.plugins.mythicdrops.objects.enums.GemType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SocketGemManager {

    public static final int MS_PER_TICK = 50;
    public static final int CHAR_167 = 167;
    private final MythicDrops plugin;
    private final List<SocketGem> socketGems;
    private final List<String> socketGemPrefixes;
    private final List<String> socketGemSuffixes;
    private static final String SELF_STRING = "%SELF%";

    public SocketGemManager(MythicDrops plugin) {
        this.plugin = plugin;
        socketGems = new ArrayList<SocketGem>();
        socketGemPrefixes = new ArrayList<String>();
        socketGemSuffixes = new ArrayList<String>();
    }

    public List<String> getSocketGemPrefixes() {
        return socketGemPrefixes;
    }

    public List<String> getSocketGemSuffixes() {
        return socketGemSuffixes;
    }

    public boolean socketGemTypeMatchesItemStack(SocketGem socketGem, ItemStack itemStack) {
        String itemType = getPlugin().getItemManager().itemTypeFromMatData(itemStack.getData());
        if (itemType == null) {
            return false;
        }
        switch (socketGem.getGemType()) {
            case TOOL:
                return getPlugin().getItemManager().isTool(itemType);
            case ARMOR:
                return getPlugin().getItemManager().isArmor(itemType);
            case ANY:
                return getPlugin().getItemManager().isTool(itemType) || getPlugin().getItemManager().isArmor(itemType);
        }
        return false;
    }

    public ItemStack prefixItemStack(ItemStack itemStack, SocketGem socketGem) {
        ItemMeta im;
        if (itemStack.hasItemMeta()) {
            im = itemStack.getItemMeta();
        } else {
            im = Bukkit.getItemFactory().getItemMeta(itemStack.getType());
        }
        String name = im.getDisplayName();
        if (name == null) {
            return itemStack;
        }
        ChatColor beginColor = findColor(name);
        if (beginColor == null) {
            beginColor = ChatColor.WHITE;
        }
        String prefix = socketGem.getPrefix();
        if (prefix == null || prefix.equalsIgnoreCase("")) {
            return itemStack;
        }
        if (getPlugin().getPluginSettings().isPreventMultipleChangesFromSockets() &&
                ChatColor.stripColor(name).contains(prefix) ||
                containsAnyFromList(ChatColor.stripColor(name), socketGemPrefixes)) {
            return itemStack;
        }
        im.setDisplayName(beginColor + prefix + " " + name);
        itemStack.setItemMeta(im);
        return itemStack;
    }

    public boolean containsAnyFromList(String string, List<String> list) {
        for (String s : list) {
            if (string.toUpperCase().contains(s.toUpperCase())) {
                return true;
            }
        }
        return false;
    }

    public ItemStack suffixItemStack(ItemStack itemStack, SocketGem socketGem) {
        ItemMeta im;
        if (!itemStack.hasItemMeta()) {
            im = Bukkit.getItemFactory().getItemMeta(itemStack.getType());
        } else {
            im = itemStack.getItemMeta();
        }
        String name = im.getDisplayName();
        if (name == null) {
            return itemStack;
        }
        ChatColor beginColor = findColor(name);
        String lastColors = ChatColor.getLastColors(name);
        if (beginColor == null) {
            beginColor = ChatColor.WHITE;
        }
        String suffix = socketGem.getSuffix();
        if (suffix == null || suffix.equalsIgnoreCase("")) {
            return itemStack;
        }
        if (getPlugin().getPluginSettings().isPreventMultipleChangesFromSockets() &&
                ChatColor.stripColor(name).contains(suffix) ||
                containsAnyFromList(ChatColor.stripColor(name), socketGemSuffixes)) {
            return itemStack;
        }
        im.setDisplayName(name + " " + beginColor + suffix + lastColors);
        itemStack.setItemMeta(im);
        return itemStack;
    }

    public void runCommands(LivingEntity attacker, LivingEntity defender) {
        if (attacker == null || defender == null) {
            return;
        }
        if (attacker instanceof Player) {
            if (getPlugin().getPluginSettings().isUseAttackerArmorEquipped()) {
                for (ItemStack attackersItem : attacker.getEquipment().getArmorContents()) {
                    if (attackersItem == null) {
                        continue;
                    }
                    List<SocketGem> attackerSocketGems = getSocketGems(attackersItem);
                    if (attackerSocketGems != null && !attackerSocketGems.isEmpty()) {
                        for (SocketGem sg : attackerSocketGems) {
                            if (sg == null) {
                                continue;
                            }
                            for (String command : sg.getCommands()) {
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace(SELF_STRING,
                                        ((Player) attacker).getName()));
                            }
                        }
                    }
                }
            }
            if (getPlugin().getPluginSettings().isUseAttackerItemInHand() && attacker.getEquipment().getItemInHand() != null) {
                List<SocketGem> attackerSocketGems = getSocketGems(attacker.getEquipment().getItemInHand());
                if (attackerSocketGems != null && !attackerSocketGems.isEmpty()) {
                    for (SocketGem sg : attackerSocketGems) {
                        if (sg == null) {
                            continue;
                        }
                        for (String command : sg.getCommands()) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace(SELF_STRING,
                                    ((Player) attacker).getName()));
                        }
                    }
                }
            }
        }
        if (defender instanceof Player) {
            if (getPlugin().getPluginSettings().isUseDefenderArmorEquipped()) {
                for (ItemStack defendersItem : defender.getEquipment().getArmorContents()) {
                    if (defendersItem == null) {
                        continue;
                    }
                    List<SocketGem> defenderSocketGems = getSocketGems(defendersItem);
                    if (defenderSocketGems != null && !defenderSocketGems.isEmpty()) {
                        for (SocketGem sg : defenderSocketGems) {
                            if (sg == null) {
                                continue;
                            }
                            for (String command : sg.getCommands()) {
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace(SELF_STRING,
                                        ((Player) defender).getName()));
                            }
                        }
                    }
                }
            }
            if (getPlugin().getPluginSettings().isUseDefenderItemInHand() && defender.getEquipment().getItemInHand() != null) {
                List<SocketGem> defenderSocketGems = getSocketGems(defender.getEquipment().getItemInHand());
                if (defenderSocketGems != null && !defenderSocketGems.isEmpty()) {
                    for (SocketGem sg : defenderSocketGems) {
                        if (sg == null) {
                            continue;
                        }
                        for (String command : sg.getCommands()) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace(SELF_STRING,
                                    ((Player) defender).getName()));
                        }
                    }
                }
            }
        }
    }

    public void applyEffects(LivingEntity attacker, LivingEntity defender) {
        if (attacker == null || defender == null) {
            return;
        }
        // handle attacker
        if (getPlugin().getPluginSettings().isUseAttackerArmorEquipped()) {
            for (ItemStack attackersItem : attacker.getEquipment().getArmorContents()) {
                if (attackersItem == null) {
                    continue;
                }
                List<SocketGem> attackerSocketGems = getSocketGems(attackersItem);
                if (attackerSocketGems != null && !attackerSocketGems.isEmpty()) {
                    for (SocketGem sg : attackerSocketGems) {
                        if (sg == null) {
                            continue;
                        }
                        if (sg.getGemType() != GemType.TOOL && sg.getGemType() != GemType.ANY) {
                            continue;
                        }
                        for (SocketEffect se : sg.getSocketEffects()) {
                            if (se == null) {
                                continue;
                            }
                            switch (se.getEffectTarget()) {
                                case SELF:
                                    attacker.addPotionEffect(
                                            new PotionEffect(se.getPotionEffectType(), se.getDuration() / MS_PER_TICK,
                                                    se.getIntensity()),
                                            true);
                                    break;
                                case OTHER:
                                    defender.addPotionEffect(
                                            new PotionEffect(se.getPotionEffectType(), se.getDuration() / MS_PER_TICK,
                                                    se.getIntensity()),
                                            true);
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
            }
        }
        if (getPlugin().getPluginSettings().isUseAttackerItemInHand() && attacker.getEquipment().getItemInHand() != null) {
            List<SocketGem> attackerSocketGems = getSocketGems(attacker.getEquipment().getItemInHand());
            if (attackerSocketGems != null && !attackerSocketGems.isEmpty()) {
                for (SocketGem sg : attackerSocketGems) {
                    if (sg == null) {
                        continue;
                    }
                    if (sg.getGemType() != GemType.TOOL && sg.getGemType() != GemType.ANY) {
                        continue;
                    }
                    for (SocketEffect se : sg.getSocketEffects()) {
                        if (se == null) {
                            continue;
                        }
                        switch (se.getEffectTarget()) {
                            case SELF:
                                attacker.addPotionEffect(
                                        new PotionEffect(se.getPotionEffectType(), se.getDuration() / MS_PER_TICK,
                                                se.getIntensity()),
                                        true);
                                break;
                            case OTHER:
                                defender.addPotionEffect(
                                        new PotionEffect(se.getPotionEffectType(), se.getDuration() / MS_PER_TICK,
                                                se.getIntensity()),
                                        true);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }
        // handle defender
        if (getPlugin().getPluginSettings().isUseDefenderArmorEquipped()) {
            for (ItemStack defenderItem : defender.getEquipment().getArmorContents()) {
                if (defenderItem == null) {
                    continue;
                }
                List<SocketGem> defenderSocketGems = getSocketGems(defenderItem);
                for (SocketGem sg : defenderSocketGems) {
                    if (sg.getGemType() != GemType.ARMOR && sg.getGemType() != GemType.ANY) {
                        continue;
                    }
                    for (SocketEffect se : sg.getSocketEffects()) {
                        if (se == null) {
                            continue;
                        }
                        switch (se.getEffectTarget()) {
                            case SELF:
                                defender.addPotionEffect(
                                        new PotionEffect(se.getPotionEffectType(), se.getDuration() / MS_PER_TICK,
                                                se.getIntensity()),
                                        true);
                                break;
                            case OTHER:
                                attacker.addPotionEffect(
                                        new PotionEffect(se.getPotionEffectType(), se.getDuration() / MS_PER_TICK,
                                                se.getIntensity()),
                                        true);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }
        if (getPlugin().getPluginSettings().isUseDefenderItemInHand() && defender.getEquipment().getItemInHand() != null) {
            List<SocketGem> defenderSocketGems = getSocketGems(defender.getEquipment().getItemInHand());
            if (defenderSocketGems != null && !defenderSocketGems.isEmpty()) {
                for (SocketGem sg : defenderSocketGems) {
                    if (sg == null) {
                        continue;
                    }
                    if (sg.getGemType() != GemType.TOOL && sg.getGemType() != GemType.ANY) {
                        continue;
                    }
                    for (SocketEffect se : sg.getSocketEffects()) {
                        if (se == null) {
                            continue;
                        }
                        switch (se.getEffectTarget()) {
                            case SELF:
                                attacker.addPotionEffect(
                                        new PotionEffect(se.getPotionEffectType(), se.getDuration() / MS_PER_TICK,
                                                se.getIntensity()),
                                        true);
                                break;
                            case OTHER:
                                defender.addPotionEffect(
                                        new PotionEffect(se.getPotionEffectType(), se.getDuration() / MS_PER_TICK,
                                                se.getIntensity()),
                                        true);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }
    }

    public void debugSocketGems() {
        List<String> socketGemNames = new ArrayList<String>();
        for (SocketGem sg : socketGems) {
            List<String> seList = new ArrayList<String>();
            for (SocketEffect se : sg.getSocketEffects()) {
                seList.add(se.getPotionEffectType().getName());
            }
            socketGemNames.add(sg.getName() + " (" + seList.toString().replace("[", "").replace("]", "") + ")");
        }
        getPlugin().getDebug().debug(
                "Loaded socket gems: "
                        + socketGemNames.toString().replace("[", "")
                        .replace("]", ""));
    }

    public MaterialData getRandomSocketGemMaterial() {
        if (getPlugin().getPluginSettings().getSocketGemMaterials() == null ||
                getPlugin().getPluginSettings().getSocketGemMaterials().isEmpty()) {
            return null;
        }
        return getPlugin().getPluginSettings().getSocketGemMaterials()
                .get(getPlugin().getRandom().nextInt(getPlugin().getPluginSettings().getSocketGemMaterials().size()));
    }

    public SocketGem getRandomSocketGemWithChance() {
        if (socketGems == null || socketGems.isEmpty()) {
            return null;
        }
        for (SocketGem socket : socketGems) {
            if (getPlugin().getRandom().nextDouble() < socket.getChance()) {
                return socket;
            }
        }
        return null;
    }

    @SuppressWarnings("unused")
    public SocketGem getRandomSocketGem() {
        if (socketGems == null ||
                socketGems.isEmpty()) {
            return null;
        }
        return socketGems.get(getPlugin().getRandom().nextInt(socketGems.size()));
    }

    public List<SocketGem> getSocketGems(ItemStack itemStack) {
        List<SocketGem> socketGemList = new ArrayList<SocketGem>();
        ItemMeta im;
        if (itemStack.hasItemMeta()) {
            im = itemStack.getItemMeta();
        } else {
            return socketGemList;
        }
        List<String> lore = im.getLore();
        if (lore == null) {
            return socketGemList;
        }
        for (String s : lore) {
            SocketGem sg = getSocketGemFromName(ChatColor.stripColor(s));
            if (sg == null) {
                continue;
            }
            socketGemList.add(sg);
        }
        return socketGemList;
    }

    public ItemStack enchantmentItemStack(ItemStack itemStack, SocketGem socketGem) {
        if (itemStack == null || socketGem == null) {
            return itemStack;
        }
        Map<Enchantment, Integer> itemStackEnchantments =
                new HashMap<Enchantment, Integer>(itemStack.getEnchantments());
        for (Map.Entry<Enchantment, Integer> entry : socketGem.getEnchantments().entrySet()) {
            if (itemStackEnchantments.containsKey(entry.getKey())) {
                itemStack.removeEnchantment(entry.getKey());
                int level = Math.abs(itemStackEnchantments.get(entry.getKey()) + entry.getValue());
                if (level <= 0) {
                    continue;
                }
                itemStack.addUnsafeEnchantment(entry.getKey(), level);
            } else {
                itemStack.addUnsafeEnchantment(entry.getKey(),
                        entry.getValue() <= 0 ? Math.abs(entry.getValue()) == 0 ? 1 : Math.abs(entry.getValue()) :
                                entry.getValue());
            }
        }
        return itemStack;
    }

    public ItemStack loreItemStack(ItemStack itemStack, SocketGem socketGem) {
        ItemMeta im;
        if (itemStack.hasItemMeta()) {
            im = itemStack.getItemMeta();
        } else {
            im = Bukkit.getItemFactory().getItemMeta(itemStack.getType());
        }
        if (!im.hasLore()) {
            im.setLore(new ArrayList<String>());
        }
        List<String> lore = new ArrayList<String>(im.getLore());
        if (lore.containsAll(socketGem.getLore())) {
            return itemStack;
        }
        for (String s : socketGem.getLore()) {
            lore.add(s.replace('&', '\u00A7').replace("\u00A7\u00A7", "&"));
        }
        im.setLore(lore);
        itemStack.setItemMeta(im);
        return itemStack;
    }

    public ChatColor findColor(final String s) {
        char[] c = s.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == (char) CHAR_167 && i + 1 < c.length) {
                return ChatColor.getByChar(c[i + 1]);
            }
        }
        return null;
    }

    public SocketGem getSocketGemFromName(String name) {
        for (SocketGem sg : socketGems) {
            if (sg.getName().equalsIgnoreCase(name)) {
                return sg;
            }
        }
        return null;
    }

    public List<SocketGem> getSocketGems() {
        return socketGems;
    }

    public MythicDrops getPlugin() {
        return plugin;
    }

}
