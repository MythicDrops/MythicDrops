package com.tealcube.minecraft.bukkit.mythicdrops

import org.junit.Assert
import org.junit.Test

class StringExtensionsKtTest {
    @Test
    fun `does replaceArgs not affect Strings without args`() {
        val expected = "dank memes"
        val actual = expected.replaceArgs("lame" to "danker")
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `does replaceArgs replace args`() {
        val template = "lame memes"
        val expected = "dank memes"
        val actual = template.replaceArgs("lame" to "dank")
        Assert.assertEquals(expected, actual)
    }
}