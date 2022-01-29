plugins {
    kotlin("jvm") version "1.6.10"
    id("org.jetbrains.kotlinx.binary-compatibility-validator")
    id("dev.mythicdrops.gradle.project")
    id("com.github.node-gradle.node")
    id("com.github.johnrengelman.shadow")
    id("io.pixeloutlaw.gradle.buildconfigkt")
}

description = "MythicDrops"

dependencies {
    compileOnly("org.spigotmc:spigot-api:_")
    compileOnly("com.arcaniax:HeadDatabase-API:_")

    implementation(kotlin("stdlib-jdk8"))
    implementation("io.pixeloutlaw:plumbing-lib:_")
    implementation("io.pixeloutlaw.worldguard:adapter-lib:_")
    implementation("io.pixeloutlaw:kindling:_")
    implementation("co.aikar:acf-paper:_")
    implementation("com.github.shyiko.klob:klob:_")
    implementation("io.insert-koin:koin-core-jvm:_")
    implementation("net.kyori:adventure-platform-bukkit:_")
    implementation("net.kyori:adventure-text-serializer-gson:_") {
        exclude(group = "com.google.code.gson", module = "gson")
    }

    testImplementation("org.spigotmc:spigot-api:_")
    testImplementation("org.mockito:mockito-core:_")
    testImplementation(platform("org.junit:junit-bom:_"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:_")
    testImplementation("com.willowtreeapps.assertk:assertk-jvm:_")
    testImplementation("io.mockk:mockk:_")
    testImplementation("io.insert-koin:koin-test-junit5:_")
    testImplementation("com.github.seeseemelk:MockBukkit-v1.17:_")
}

buildConfigKt {
    appName = "MythicDrops"
}

contacts {
    addPerson(
        "topplethenunnery@gmail.com",
        closureOf<nebula.plugin.contacts.Contact> {
            moniker = "ToppleTheNun"
            github = "ToppleTheNun"
        }
    )
}

detekt {
    baseline = file("baseline.xml")
}

ktlint {
    filter {
        exclude("**/BuildConfig.kt")
    }
}

// spigot {
//    name = "MythicDrops"
//    apiVersion = "1.17"
//    load = kr.entree.spigradle.data.Load.STARTUP
//    permissions {
//        register("mythicdrops.identify") {
//            description = "Allows a player to identify items."
//            defaults = "true"
//        }
//        register("mythicdrops.socket") {
//            description = "Allows a player to use socket gems."
//            defaults = "true"
//        }
//        register("mythicdrops.repair") {
//            description = "Allows a player to repair items."
//            defaults = "true"
//        }
//        register("mythicdrops.command.combiners.list") {
//            description = "Allows player to use \"/mythicdrops combiners list\" command."
//        }
//        register("mythicdrops.command.combiners.add") {
//            description = "Allows player to use \"/mythicdrops combiners add\" command."
//        }
//        register("mythicdrops.command.combiners.remove") {
//            description = "Allows player to use \"/mythicdrops combiners remove\" command."
//        }
//        register("mythicdrops.command.combiners.open") {
//            description = "Allows player to use \"/mythicdrops combiners open\" command."
//        }
//        register("mythicdrops.command.combiners.*") {
//            description = "Allows player to use all \"/mythicdrops combiners\" commands."
//            children = mapOf(
//                "mythicdrops.command.combiners.list" to true,
//                "mythicdrops.command.combiners.add" to true,
//                "mythicdrops.command.combiners.remove" to true
//            )
//        }
//        register("mythicdrops.command.customcreate") {
//            description = "Allows player to use \"/mythicdrops customcreate\" command."
//        }
//        register("mythicdrops.command.customitems") {
//            description = "Allows player to use \"/mythicdrops customitems\" command."
//        }
//        register("mythicdrops.command.debug") {
//            description = "Allows player to use \"/mythicdrops debug\" command."
//        }
//        register("mythicdrops.command.errors") {
//            description = "Allows player to use \"/mythicdrops errors\" command."
//        }
//        register("mythicdrops.command.toggledebug") {
//            description = "Allows player to use \"/mythicdrops toggledebug\" command."
//        }
//        register("mythicdrops.command.drop.custom") {
//            description = "Allows player to use \"/mythicdrops drop custom\" command."
//        }
//        register("mythicdrops.command.drop.extender") {
//            description = "Allows player to use \"/mythicdrops drop extender\" command."
//        }
//        register("mythicdrops.command.drop.gem") {
//            description = "Allows player to use \"/mythicdrops drop gem\" command."
//        }
//        register("mythicdrops.command.drop.tier") {
//            description = "Allows player to use \"/mythicdrops drop tier\" command."
//        }
//        register("mythicdrops.command.drop.tome") {
//            description = "Allows player to use \"/mythicdrops drop tome\" command."
//        }
//        register("mythicdrops.command.drop.unidentified") {
//            description = "Allows player to use \"/mythicdrops drop unidentified\" command."
//        }
//        register("mythicdrops.command.drop.*") {
//            description = "Allows player to use all \"/mythicdrops drop\" commands."
//            children = mapOf(
//                "mythicdrops.command.drop.custom" to true,
//                "mythicdrops.command.drop.extender" to true,
//                "mythicdrops.command.drop.gem" to true,
//                "mythicdrops.command.drop.tier" to true,
//                "mythicdrops.command.drop.tome" to true,
//                "mythicdrops.command.drop.unidentified" to true
//            )
//        }
//        register("mythicdrops.command.give.custom") {
//            description = "Allows player to use \"/mythicdrops give custom\" command."
//        }
//        register("mythicdrops.command.give.extender") {
//            description = "Allows player to use \"/mythicdrops give extender\" command."
//        }
//        register("mythicdrops.command.give.gem") {
//            description = "Allows player to use \"/mythicdrops give gem\" command."
//        }
//        register("mythicdrops.command.give.tier") {
//            description = "Allows player to use \"/mythicdrops give tier\" command."
//        }
//        register("mythicdrops.command.give.tome") {
//            description = "Allows player to use \"/mythicdrops give tome\" command."
//        }
//        register("mythicdrops.command.give.unidentified") {
//            description = "Allows player to use \"/mythicdrops give unidentified\" command."
//        }
//        register("mythicdrops.command.give.*") {
//            description = "Allows player to use all \"/mythicdrops give\" commands."
//            children = mapOf(
//                "mythicdrops.command.give.custom" to true,
//                "mythicdrops.command.give.extender" to true,
//                "mythicdrops.command.give.gem" to true,
//                "mythicdrops.command.give.tier" to true,
//                "mythicdrops.command.give.tome" to true,
//                "mythicdrops.command.give.unidentified" to true
//            )
//        }
//        register("mythicdrops.command.itemgroups") {
//            description = "Allows player to use \"/mythicdrops itemgroups\" command."
//        }
//        register("mythicdrops.command.modify.name") {
//            description = "Allows player to use \"/mythicdrops modify name\" command."
//        }
//        register("mythicdrops.command.modify.lore.add") {
//            description = "Allows player to use \"/mythicdrops modify lore add\" command."
//        }
//        register("mythicdrops.command.modify.lore.remove") {
//            description = "Allows player to use \"/mythicdrops modify lore remove\" command."
//        }
//        register("mythicdrops.command.modify.lore.insert") {
//            description = "Allows player to use \"/mythicdrops modify lore insert\" command."
//        }
//        register("mythicdrops.command.modify.lore.set") {
//            description = "Allows player to use \"/mythicdrops modify lore set\" command."
//        }
//        register("mythicdrops.command.modify.lore.*") {
//            description = "Allows player to use \"/mythicdrops modify lore\" commands."
//            children = mapOf(
//                "mythicdrops.command.modify.lore.add" to true,
//                "mythicdrops.command.modify.lore.remove" to true,
//                "mythicdrops.command.modify.lore.insert" to true,
//                "mythicdrops.command.modify.lore.set" to true
//            )
//        }
//        register("mythicdrops.command.modify.enchantment.add") {
//            description = "Allows player to use \"/mythicdrops modify enchantment add\" command."
//        }
//        register("mythicdrops.command.modify.enchantment.remove") {
//            description = "Allows player to use \"/mythicdrops modify enchantment remove\" command."
//        }
//        register("mythicdrops.command.modify.enchantment.*") {
//            description = "Allows player to use \"/mythicdrops modify enchantment\" commands."
//            children = mapOf(
//                "mythicdrops.command.modify.enchantment.add" to true,
//                "mythicdrops.command.modify.enchantment.remove" to true
//            )
//        }
//        register("mythicdrops.command.modify.*") {
//            description = "Allows player to use \"/mythicdrops modify\" commands."
//            children = mapOf(
//                "mythicdrops.command.modify.name" to true,
//                "mythicdrops.command.modify.lore.*" to true,
//                "mythicdrops.command.modify.enchantment.*" to true
//            )
//        }
//        register("mythicdrops.command.load") {
//            description = "Allows player to reload configuration files."
//        }
//        register("mythicdrops.command.rates") {
//            description = "Allows player to use \"/mythicdrops rates\" command."
//        }
//        register("mythicdrops.command.socketgems") {
//            description = "Allows player to use \"/mythicdrops socketgems\" command."
//        }
//        register("mythicdrops.command.spawn.custom") {
//            description = "Allows player to use \"/mythicdrops spawn custom\" command."
//        }
//        register("mythicdrops.command.spawn.extender") {
//            description = "Allows player to use \"/mythicdrops spawn extender\" command."
//        }
//        register("mythicdrops.command.spawn.gem") {
//            description = "Allows player to use \"/mythicdrops spawn gem\" command."
//        }
//        register("mythicdrops.command.spawn.tier") {
//            description = "Allows player to use \"/mythicdrops spawn tier\" command."
//        }
//        register("mythicdrops.command.spawn.tome") {
//            description = "Allows player to use \"/mythicdrops spawn tome\" command."
//        }
//        register("mythicdrops.command.spawn.unidentified") {
//            description = "Allows player to use \"/mythicdrops spawn unidentified\" command."
//        }
//        register("mythicdrops.command.spawn.*") {
//            description = "Allows player to use all \"/mythicdrops spawn\" commands."
//            children = mapOf(
//                "mythicdrops.command.spawn.custom" to true,
//                "mythicdrops.command.spawn.extender" to true,
//                "mythicdrops.command.spawn.gem" to true,
//                "mythicdrops.command.spawn.tier" to true,
//                "mythicdrops.command.spawn.tome" to true,
//                "mythicdrops.command.spawn.unidentified" to true
//            )
//        }
//        register("mythicdrops.command.tiers") {
//            description = "Allows player to use \"/mythicdrops tiers\" command."
//        }
//        register("mythicdrops.command.*") {
//            description = "Allows player to use all commands."
//            children = mapOf(
//                "mythicdrops.command.combiners.*" to true,
//                "mythicdrops.command.customcreate" to true,
//                "mythicdrops.command.customitems" to true,
//                "mythicdrops.command.debug" to true,
//                "mythicdrops.command.errors" to true,
//                "mythicdrops.command.toggledebug" to true,
//                "mythicdrops.command.drop.*" to true,
//                "mythicdrops.command.give.*" to true,
//                "mythicdrops.command.itemgroups" to true,
//                "mythicdrops.command.modify.*" to true,
//                "mythicdrops.command.load" to true,
//                "mythicdrops.command.rate" to true,
//                "mythicdrops.command.socketgems" to true,
//                "mythicdrops.command.spawn.*" to true,
//                "mythicdrops.command.tiers.*" to true
//            )
//        }
//        register("mythicdrops.*") {
//            description = "Allows player to do all MythicDrops tasks."
//            children = mapOf(
//                "mythicdrops.identify" to true,
//                "mythicdrops.socket" to true,
//                "mythicdrops.repair" to true,
//                "mythicdrops.command.*" to true
//            )
//        }
//    }
//    debug {
//        args = listOf("nogui")
//        jvmArgs = listOf(
//            "-Xms1G",
//            "-Xmx1G",
//            "-XX:+UseG1GC",
//            "-XX:+ParallelRefProcEnabled",
//            "-XX:MaxGCPauseMillis=200",
//            "-XX:+UnlockExperimentalVMOptions",
//            "-XX:+DisableExplicitGC",
//            "-XX:+AlwaysPreTouch",
//            "-XX:G1NewSizePercent=30",
//            "-XX:G1MaxNewSizePercent=40",
//            "-XX:G1HeapRegionSize=8M",
//            "-XX:G1ReservePercent=20",
//            "-XX:G1HeapWastePercent=5",
//            "-XX:G1MixedGCCountTarget=4",
//            "-XX:InitiatingHeapOccupancyPercent=15",
//            "-XX:G1MixedGCLiveThresholdPercent=90",
//            "-XX:G1RSetUpdatingPauseTimePercent=5",
//            "-XX:SurvivorRatio=32",
//            "-XX:+PerfDisableSharedMem",
//            "-XX:MaxTenuringThreshold=1",
//            "-Dusing.aikars.flags=https://mcflags.emc.gs",
//            "-Daikars.new.flags=tru"
//        )
//    }
// }

mythicDropsRelease {
    repository = "MythicDrops/MythicDrops"
}

node {
    nodeProjectDir.set(rootProject.file("/website"))
}

tasks.findByName("assemble")?.dependsOn("assembleDist")

tasks.findByName("dokkaJavadoc")?.dependsOn("generateBuildConfigKt")

tasks.findByName("runKtlintCheckOverMainSourceSet")?.dependsOn("generateBuildConfigKt")

tasks.create("assembleDist", Zip::class.java) {
    dependsOn("shadowJar")
    archiveBaseName.value(project.description)
    archiveVersion.value("v${archiveVersion.get()}")
    from(tasks.getByName("shadowJar")) {
        rename { it.replace("-all.jar", ".jar") }
    }
    from(tasks.getByName("processResources")) {
        include("*.yml")
        exclude("plugin.yml")
        include("resources/**/*")
        include("tiers/*.yml")
        include("variables.txt")
        into("MythicDrops")
    }
}

tasks.withType<ProcessResources> {
    filesMatching("plugin.yml") {
        expand("project" to project)
    }
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    mergeServiceFiles()
    relocate("co.aikar.commands", "com.tealcube.minecraft.bukkit.mythicdrops.shade.acf")
    relocate("co.aikar.locale", "com.tealcube.minecraft.bukkit.mythicdrops.shade.aikar.locale")
    relocate("kotlin", "io.pixeloutlaw.minecraft.spigot.mythicdrops.shade.kotlin")
    relocate("org.koin", "io.pixeloutlaw.minecraft.spigot.mythicdrops.shade.koin")
    relocate("net.kyori", "io.pixeloutlaw.minecraft.spigot.mythicdrops.shade.kyori")
    relocate("de.themoep", "io.pixeloutlaw.minecraft.spigot.mythicdrops.shade.themoep")
}
