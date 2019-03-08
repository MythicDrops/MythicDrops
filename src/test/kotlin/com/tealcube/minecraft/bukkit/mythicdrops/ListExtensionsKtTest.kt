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
    fun `does replaceArgs replace any found args that match full strings`() {
        val template = listOf("lame memes", "kinda dank memes", "sorta dank memes")
        val expected = listOf("barely dank memes", "kinda dank memes", "sorta dank memes")
        val actual = template.replaceArgs("lame memes" to "barely dank memes")
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `does replaceArgs replace any found args that match partial strings`() {
        val template = listOf("lame memes", "kinda dank memes", "sorta dank memes")
        val expected = listOf("lame meemees", "kinda dank meemees", "sorta dank meemees")
        val actual = template.replaceArgs("memes" to "meemees")
        Assert.assertEquals(expected, actual)
    }
}