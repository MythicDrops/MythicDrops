package com.tealcube.minecraft.bukkit.mythicdrops.templating;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

public class RandTemplateTest {

    @Test
    public void doesApplyWorkWithXToX() throws Exception {
        RandTemplate randTemplate = new RandTemplate();
        String argument = "1-1";
        Assert.assertEquals("1", randTemplate.apply(argument));
    }

    @Test
    public void doesApplyWorkWithXToY() throws Exception {
        RandTemplate randTemplate = new RandTemplate();
        String argument = "1-3";
        String actual = randTemplate.apply(argument);
        Assert.assertTrue(StringUtils.equalsAnyIgnoreCase(actual, "1", "2", "3"));
    }

}