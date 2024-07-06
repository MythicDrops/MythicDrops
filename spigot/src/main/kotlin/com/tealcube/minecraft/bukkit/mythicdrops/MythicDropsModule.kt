package com.tealcube.minecraft.bukkit.mythicdrops

import com.tealcube.minecraft.bukkit.mythicdrops.koin.MythicDropsConfigModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@ComponentScan
@Module(includes = [MythicDropsConfigModule::class])
internal class MythicDropsModule
