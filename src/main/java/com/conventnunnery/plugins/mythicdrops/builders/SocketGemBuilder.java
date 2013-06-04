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

package com.conventnunnery.plugins.mythicdrops.builders;

import com.conventnunnery.plugins.mythicdrops.MythicDrops;
import com.conventnunnery.plugins.mythicdrops.configuration.MythicConfigurationFile;
import com.conventnunnery.plugins.mythicdrops.objects.SocketEffect;
import com.conventnunnery.plugins.mythicdrops.objects.SocketGem;
import com.conventnunnery.plugins.mythicdrops.objects.enums.EffectTarget;
import com.conventnunnery.plugins.mythicdrops.objects.enums.GemType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SocketGemBuilder {
    private final MythicDrops plugin;


    public SocketGemBuilder(MythicDrops plugin) {
        this.plugin = plugin;
    }

    public MythicDrops getPlugin() {
        return plugin;
    }

    public void build() {
        getPlugin().getSocketGemManager().getSocketGems().clear();
        FileConfiguration fc = getPlugin().getConfigurationManager().getConfiguration(
                MythicConfigurationFile.SOCKETGEM);
        for (String key : fc.getKeys(false)) {
            if (!fc.isConfigurationSection(key)) {
                continue;
            }
            ConfigurationSection cs = fc.getConfigurationSection(key);
            GemType gemType = GemType.getFromName(cs.getString("type"));
            if (gemType == null) {
                gemType = GemType.ANY;
            }
            List<SocketEffect> socketEffects = buildSocketEffects(cs);
            double chance = cs.getDouble("chance");
            String prefix = cs.getString("prefix");
            if (prefix != null && !prefix.equalsIgnoreCase("")) {
                getPlugin().getSocketGemManager().getSocketGemPrefixes().add(prefix);
            }
            String suffix = cs.getString("suffix");
            if (suffix != null && !suffix.equalsIgnoreCase("")) {
                getPlugin().getSocketGemManager().getSocketGemSuffixes().add(suffix);
            }
            List<String> lore = cs.getStringList("lore");
            Map<Enchantment, Integer> enchantments = new HashMap<Enchantment, Integer>();
            if (cs.isConfigurationSection("enchantments")) {
                ConfigurationSection enchCS = cs.getConfigurationSection("enchantments");
                for (String key1 : enchCS.getKeys(false)) {
                    Enchantment ench = null;
                    for (Enchantment ec : Enchantment.values()) {
                        if (ec.getName().equalsIgnoreCase(key1)) {
                            ench = ec;
                            break;
                        }
                    }
                    if (ench == null) {
                        continue;
                    }
                    int level = enchCS.getInt(key1);
                    enchantments.put(ench, level);
                }
            }
            List<String> commands = cs.getStringList("commands");
            getPlugin().getSocketGemManager().getSocketGems()
                    .add(new SocketGem(key, gemType, socketEffects, chance, prefix, suffix, lore, enchantments,
                            commands));
        }
    }

    private List<SocketEffect> buildSocketEffects(ConfigurationSection cs) {
        List<SocketEffect> socketEffectList = new ArrayList<SocketEffect>();
        if (!cs.isConfigurationSection("effects")) {
            return socketEffectList;
        }
        ConfigurationSection cs1 = cs.getConfigurationSection("effects");
        for (String key : cs1.getKeys(false)) {
            PotionEffectType pet = PotionEffectType.getByName(key);
            if (pet == null) {
                continue;
            }
            int duration = cs1.getInt(key + ".duration");
            int intensity = cs1.getInt(key + ".intensity");
            String target = cs1.getString(key + ".target");
            EffectTarget et = EffectTarget.getFromName(target);
            if (et == null) {
                et = EffectTarget.NONE;
            }
            socketEffectList.add(new SocketEffect(pet, intensity, duration, et));
        }
        return socketEffectList;
    }

}
