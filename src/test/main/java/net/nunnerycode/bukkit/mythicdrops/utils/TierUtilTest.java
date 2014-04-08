package net.nunnerycode.bukkit.mythicdrops.utils;

import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import net.nunnerycode.bukkit.mythicdrops.tiers.MythicTierBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class TierUtilTest {

    @Test
    public void testSkewTierCollectionToRarer1() throws Exception {
        Collection<Tier> tiers = new ArrayList<>();

        tiers.add(new TierImp("common", "default", 0.75).tier());
        tiers.add(new TierImp("uncommon", "default", 0.5).tier());
        tiers.add(new TierImp("rare", "default", 0.25).tier());

        List<Tier> newTiers = new ArrayList<>(TierUtil.skewTierCollectionToRarer(tiers, 3));
        List<String> strings = new ArrayList<>();
        for (Tier t : newTiers) {
            strings.add(t.getName());
        }

        String expected = "[rare, uncommon, common]";
        String actual = strings.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSkewTierCollectionToRarer2() throws Exception {
        Collection<Tier> tiers = new ArrayList<>();

        tiers.add(new TierImp("common", "default", 0.75).tier());
        tiers.add(new TierImp("uncommon", "default", 0.5).tier());
        tiers.add(new TierImp("rare", "default", 0.25).tier());

        List<Tier> newTiers = new ArrayList<>(TierUtil.skewTierCollectionToRarer(tiers, 2));
        List<String> strings = new ArrayList<>();
        for (Tier t : newTiers) {
            strings.add(t.getName());
        }

        String expected = "[rare, uncommon]";
        String actual = strings.toString();
        Assert.assertEquals(expected, actual);
    }

    class TierImp {

        private Tier tier = null;

        protected TierImp(String name) {
            tier = new MythicTierBuilder(name).build();
        }

        protected TierImp(String name, String worldName, double chance) {
            Map<String, Double> worldChances = new HashMap<>();
            worldChances.put(worldName, chance);
            tier = new MythicTierBuilder(name).withWorldSpawnChanceMap(worldChances).build();
        }

        protected Tier tier() {
            return tier;
        }

    }

}
