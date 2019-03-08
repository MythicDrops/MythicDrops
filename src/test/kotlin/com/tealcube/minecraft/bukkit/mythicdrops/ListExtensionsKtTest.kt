package com.tealcube.minecraft.bukkit.mythicdrops

import org.junit.Assert
import org.junit.Test

class ListExtensionsKtTest {
    @Test
    fun `does replaceArgs not replace if no args found`() {
        val expected = listOf("dank memes", "danker memes", "dankest memes")
        val actual = expected.replaceArgs("lame memes" to "danker memes")
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `does replaceArgs replace any found args`() {
        val template = listOf("lame memes", "kinda dank memes", "sorta dank memes")
        val expected = listOf("barely dank memes", "kinda dank memes", "sorta dank memes")
        val actual = template.replaceArgs("lame memes" to "barely dank memes")
        Assert.assertEquals(expected, actual)
    }
}