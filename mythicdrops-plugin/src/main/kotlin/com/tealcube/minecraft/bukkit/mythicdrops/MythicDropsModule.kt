package com.tealcube.minecraft.bukkit.mythicdrops

import io.pixeloutlaw.minecraft.spigot.config.migration.ConfigMigrationModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@ComponentScan
@Module(
    includes = [ConfigMigrationModule::class]
)
class MythicDropsModule
