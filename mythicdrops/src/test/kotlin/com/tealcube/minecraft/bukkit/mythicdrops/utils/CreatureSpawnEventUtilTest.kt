package com.tealcube.minecraft.bukkit.mythicdrops.utils

import org.bukkit.entity.Cow
import org.bukkit.entity.EnderDragon
import org.bukkit.entity.Ghast
import org.bukkit.entity.MagmaCube
import org.bukkit.entity.Slime
import org.bukkit.entity.Zombie
import org.bukkit.event.entity.CreatureSpawnEvent
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class CreatureSpawnEventUtilTest {
    val spawnReason = CreatureSpawnEvent.SpawnReason.DEFAULT
    @Mock
    lateinit var slime: Slime
    @Mock
    lateinit var magmaCube: MagmaCube
    @Mock
    lateinit var enderDragon: EnderDragon
    @Mock
    lateinit var zombie: Zombie
    @Mock
    lateinit var ghast: Ghast
    @Mock
    lateinit var cow: Cow

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun doesShouldCancelDropsBasedOnCreatureSpawnEventReturnTrueForCancelled() {
        val event = CreatureSpawnEvent(zombie, spawnReason)
        event.isCancelled = true

        Assert.assertTrue(CreatureSpawnEventUtil.shouldCancelDropsBasedOnCreatureSpawnEvent(event))
    }

    @Test
    fun doesShouldCancelDropsBasedOnCreatureSpawnEventReturnFalseForSlime() {
        val event = CreatureSpawnEvent(slime, spawnReason)

        Assert.assertFalse(CreatureSpawnEventUtil.shouldCancelDropsBasedOnCreatureSpawnEvent(event))
    }

    @Test
    fun doesShouldCancelDropsBasedOnCreatureSpawnEventReturnFalseForMagmaCube() {
        val event = CreatureSpawnEvent(magmaCube, spawnReason)

        Assert.assertFalse(CreatureSpawnEventUtil.shouldCancelDropsBasedOnCreatureSpawnEvent(event))
    }

    @Test
    fun doesShouldCancelDropsBasedOnCreatureSpawnEventReturnFalseForEnderDragon() {
        val event = CreatureSpawnEvent(enderDragon, spawnReason)

        Assert.assertFalse(CreatureSpawnEventUtil.shouldCancelDropsBasedOnCreatureSpawnEvent(event))
    }

    @Test
    fun doesShouldCancelDropsBasedOnCreatureSpawnEventReturnFalseForZombie() {
        val event = CreatureSpawnEvent(zombie, spawnReason)

        Assert.assertFalse(CreatureSpawnEventUtil.shouldCancelDropsBasedOnCreatureSpawnEvent(event))
    }

    @Test
    fun doesShouldCancelDropsBasedOnCreatureSpawnEventReturnFalseForGhast() {
        val event = CreatureSpawnEvent(ghast, spawnReason)

        Assert.assertFalse(CreatureSpawnEventUtil.shouldCancelDropsBasedOnCreatureSpawnEvent(event))
    }

    @Test
    fun doesShouldCancelDropsBasedOnCreatureSpawnEventReturnTrueForCow() {
        val event = CreatureSpawnEvent(cow, spawnReason)

        Assert.assertTrue(CreatureSpawnEventUtil.shouldCancelDropsBasedOnCreatureSpawnEvent(event))
    }
}