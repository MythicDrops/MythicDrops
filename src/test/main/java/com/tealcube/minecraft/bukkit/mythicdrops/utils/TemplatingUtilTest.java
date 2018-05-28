package com.tealcube.minecraft.bukkit.mythicdrops.utils;

import com.tealcube.minecraft.bukkit.mythicdrops.templating.OpString;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

public class TemplatingUtilTest {

    @Test
    public void testOpsStringRand() {
        String randTemplateString = "rand 2-4";
        OpString randOpsString = TemplatingUtil.opsString(randTemplateString);
        Assert.assertEquals("rand", randOpsString.getOperation());
        Assert.assertEquals("2-4", randOpsString.getArguments());
    }

    @Test
    public void testOpsStringRandSign() {
        String randSignTemplateString = "randsign";
        OpString randOpsString = TemplatingUtil.opsString(randSignTemplateString);
        Assert.assertEquals("randsign", randOpsString.getOperation());
        Assert.assertEquals("", randOpsString.getArguments());
    }

    @Test
    public void doesRandTemplateReturnAccurateValue() {
        String randTemplateString = "+%rand 2-4% Memes";

        String actual = TemplatingUtil.template(randTemplateString);
        Assert.assertTrue(StringUtils.equalsAny(actual, "+2 Memes", "+3 Memes", "+4 Memes"));
    }

    @Test
    public void doesRandsignTemplateReturnAccurateValue() {
        String randsignTemplateString = "%randsign%3 Memes";

        String actual = TemplatingUtil.template(randsignTemplateString);
        Assert.assertTrue(StringUtils.equalsAny(actual, "-3 Memes", "+3 Memes"));
    }

    @Test
    public void doesRandAndRandsignWorkTogether() {
        String randsignTemplateString = "%randsign%%rand 2-4% Memes";

        String actual = TemplatingUtil.template(randsignTemplateString);
        Assert.assertTrue(StringUtils.equalsAny(actual, "+2 Memes", "+3 Memes", "+4 Memes",
                "-2 Memes", "-3 Memes", "-4 Memes"));
    }

}