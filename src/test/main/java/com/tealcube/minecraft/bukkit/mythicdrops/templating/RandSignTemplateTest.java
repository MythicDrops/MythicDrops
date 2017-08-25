package com.tealcube.minecraft.bukkit.mythicdrops.templating;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RandSignTemplateTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RandSignTemplate.class);

    @Test
    public void doesApplyReturnAboutHalfPlus() {
        RandSignTemplate randSignTemplate = new RandSignTemplate();
        String templateToTest = "%randsign%";

        int amount = 0;
        for (int i = 0; i < 100; i++) {
            String val = randSignTemplate.apply(templateToTest);
            amount += (val.equalsIgnoreCase("+") ? 1 : 0);
        }

        Assert.assertTrue(amount > 35);
    }

}