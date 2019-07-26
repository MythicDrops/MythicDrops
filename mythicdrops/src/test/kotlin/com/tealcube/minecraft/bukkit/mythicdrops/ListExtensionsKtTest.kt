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

    @Test
    fun `does replaceWithCollection not replace if element not found`() {
        val expected = listOf("barely dank memes", "kinda dank memes", "sorta dank memes", "lame memes")
        val actual = expected.replaceWithCollection("dank memes", listOf("dank memes", "very dank memes"))
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `does replaceWithCollection replace if element found`() {
        val template = listOf("barely dank memes", "kinda dank memes", "sorta dank memes", "lame memes")
        val expected =
            listOf("barely dank memes", "kinda dank memes", "sorta dank memes", "dank memes", "very dank memes")
        val actual = template.replaceWithCollection("lame memes", listOf("dank memes", "very dank memes"))
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `does replaceWithCollections not replace if elements not found`() {
        val expected = listOf("dank", "memes", "man")
        val actual = expected.replaceWithCollections(
            "lame" to listOf("kinda dank", "sorta dank", "dank"),
            "memeroonis" to listOf("meemees", "maymays", "memes"),
            "manbearpig" to listOf("child", "woman", "man")
        )
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `does replaceWithCollections replace if elements found`() {
        val template = listOf("dank", "memes", "man")
        val expected = listOf("kinda dank", "sorta dank", "dank", "meemees", "maymays", "memes", "child", "woman", "man")
        val actual = template.replaceWithCollections(
            "dank" to listOf("kinda dank", "sorta dank", "dank"),
            "memes" to listOf("meemees", "maymays", "memes"),
            "man" to listOf("child", "woman", "man")
        )
        Assert.assertEquals(expected, actual)
    }
}