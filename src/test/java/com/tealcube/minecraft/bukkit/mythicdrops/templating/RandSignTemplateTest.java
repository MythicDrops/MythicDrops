package com.tealcube.minecraft.bukkit.mythicdrops.templating;

import org.junit.Assert;
import org.junit.Test;

public class RandSignTemplateTest {

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