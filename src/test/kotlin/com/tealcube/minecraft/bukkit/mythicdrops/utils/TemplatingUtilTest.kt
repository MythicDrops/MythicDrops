package com.tealcube.minecraft.bukkit.mythicdrops.utils

import org.apache.commons.lang3.StringUtils
import org.junit.Assert
import org.junit.Test

class TemplatingUtilTest {

    @Test
    fun testOpsStringRand() {
        val randTemplateString = "rand 2-4"
        val randOpsString = TemplatingUtil.opsString(randTemplateString)
        Assert.assertEquals("rand", randOpsString.operation)
        Assert.assertEquals("2-4", randOpsString.arguments)
    }

    @Test
    fun testOpsStringRandSign() {
        val randSignTemplateString = "randsign"
        val randOpsString = TemplatingUtil.opsString(randSignTemplateString)
        Assert.assertEquals("randsign", randOpsString.operation)
        Assert.assertEquals("", randOpsString.arguments)
    }

    @Test
    fun doesRandTemplateReturnAccurateValue() {
        val randTemplateString = "+%rand 2-4% Memes"

        val actual = TemplatingUtil.template(randTemplateString)
        Assert.assertTrue(StringUtils.equalsAny(actual, "+2 Memes", "+3 Memes", "+4 Memes"))
    }

    @Test
    fun doesRandsignTemplateReturnAccurateValue() {
        val randsignTemplateString = "%randsign%3 Memes"

        val actual = TemplatingUtil.template(randsignTemplateString)
        Assert.assertTrue(StringUtils.equalsAny(actual, "-3 Memes", "+3 Memes"))
    }

    @Test
    fun doesRandAndRandsignWorkTogether() {
        val randsignTemplateString = "%randsign%%rand 2-4% Memes"

        val actual = TemplatingUtil.template(randsignTemplateString)
        Assert.assertTrue(
            StringUtils.equalsAny(
                actual, "+2 Memes", "+3 Memes", "+4 Memes",
                "-2 Memes", "-3 Memes", "-4 Memes"
            )
        )
    }

    @Test
    fun doMultipleRandsWork() {
        val randTemplateString = "%rand 1-4%%rand 1-4%"

        val actual = TemplatingUtil.template(randTemplateString)
        Assert.assertTrue(
            StringUtils
                .equalsAny(
                    actual, "11", "12", "13", "14", "21", "22", "23", "24", "31", "32", "33", "34", "41", "42", "43",
                    "44"
                )
        )
    }
}