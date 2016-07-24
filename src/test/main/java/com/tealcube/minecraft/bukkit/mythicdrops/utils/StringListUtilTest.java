package com.tealcube.minecraft.bukkit.mythicdrops.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class StringListUtilTest {
    @Test
    public void removeIfMatchesNegative() throws Exception {
        List<String> list1 = Arrays.asList("I", "Like", "Big", "Butts", "and", "I", "Cannot", "Lie");
        List<String> list2 = Arrays.asList("Like", "Butts", "I");

        List<String> actual = StringListUtil.removeIfMatches(list1, list2);

        Assert.assertEquals(list1, actual);
    }

    @Test
    public void removeIfMatchesPositive() throws Exception {
        List<String> list1 = Arrays.asList("I", "Like", "Big", "Butts", "and", "I", "Cannot", "Lie");
        List<String> list2 = Arrays.asList("Big", "Butts", "and");

        List<String> expected = Arrays.asList("I", "Like", "I", "Cannot", "Lie");
        List<String> actual = StringListUtil.removeIfMatches(list1, list2);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void removeIfMatchesEmptyArg1() throws Exception {
        List<String> list1 = new ArrayList<>();
        List<String> list2 = Arrays.asList("Big", "Butts", "and");

        List<String> actual = StringListUtil.removeIfMatches(list1, list2);

        Assert.assertEquals(list1, actual);
    }

    @Test
    public void removeIfMatchesEmptyArg2() throws Exception {
        List<String> list1 = Arrays.asList("I", "Like", "Big", "Butts", "and", "I", "Cannot", "Lie");
        List<String> list2 = new ArrayList<>();

        List<String> actual = StringListUtil.removeIfMatches(list1, list2);

        Assert.assertEquals(list1, actual);
    }

    @Test
    public void removeIfMatchesColorlessNegative() throws Exception {
        List<String> list1 = Arrays.asList("I", "Like", "Big", "Butts", "and", "I", "Cannot", "Lie");
        List<String> list2 = Arrays.asList("Like", "Butts", "I");

        List<String> actual = StringListUtil.removeIfMatchesColorless(list1, list2);

        Assert.assertEquals(list1, actual);
    }

    @Test
    public void removeIfMatchesColorlessPositive() throws Exception {
        List<String> list1 = Arrays.asList("I", "Like", "Big", "Butts", "and", "I", "Cannot", "Lie");
        List<String> list2 = Arrays.asList("Big", "Butts", "and");

        List<String> expected = Arrays.asList("I", "Like", "I", "Cannot", "Lie");
        List<String> actual = StringListUtil.removeIfMatchesColorless(list1, list2);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void removeIfMatchesColorlessEmptyArg1() throws Exception {
        List<String> list1 = new ArrayList<>();
        List<String> list2 = Arrays.asList("Big", "Butts", "and");

        List<String> actual = StringListUtil.removeIfMatchesColorless(list1, list2);

        Assert.assertEquals(list1, actual);
    }

    @Test
    public void removeIfMatchesColorlessEmptyArg2() throws Exception {
        List<String> list1 = Arrays.asList("I", "Like", "Big", "Butts", "and", "I", "Cannot", "Lie");
        List<String> list2 = new ArrayList<>();

        List<String> actual = StringListUtil.removeIfMatchesColorless(list1, list2);

        Assert.assertEquals(list1, actual);
    }
}