import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("com.github.johnrengelman.shadow") version "5.2.0"
    id("io.pixeloutlaw.gradle.buildconfigkt") version "1.0.5"
}

description = "MythicDrops"

dependencies {
    compileOnly("org.spigotmc:spigot-api:_")

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:_")
    implementation("io.pixeloutlaw.spigot-commons:config:_")
    implementation("io.pixeloutlaw.minecraft.spigot:config-migrator:_")
    implementation("io.pixeloutlaw.spigot-commons:fanciful:_")
    implementation("io.pixeloutlaw.spigot-commons:hilt:_")
    implementation("org.apache.commons:commons-text:_")
    implementation("org.bstats:bstats-bukkit:_")
    implementation("io.pixeloutlaw.minecraft.spigot:plugin-yml-annotations:_")
    implementation("co.aikar:acf-paper:_")
    implementation("io.pixeloutlaw.worldguard:adapter-lib:_")

    kapt("io.pixeloutlaw.minecraft.spigot:plugin-yml-processor:_")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:_")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.create("assembleDist", Zip::class.java) {
    dependsOn("assemble")
    archiveBaseName.value(project.description)
    from("${project.buildDir}/libs") {
        include("${project.name}-${project.version}-all.jar")
        rename { it.replace("-all.jar", ".jar") }
    }
    from("${project.buildDir}/resources/main") {
        include("*.yml")
        exclude("plugin.yml")
        include("resources/**/*")
        include("tiers/*.yml")
        into("MythicDrops")
    }
}

tasks.findByName("assemble")?.dependsOn("shadowJar")
tasks.findByName("build")?.dependsOn("assembleDist")

tasks.withType<ShadowJar> {
    mergeServiceFiles()
    relocate("com.github.zafarkhaja", "com.tealcube.minecraft.bukkit.mythicdrops.shade.jsemver")
    relocate("se.ranzdo.bukkit.methodcommand", "com.tealcube.minecraft.bukkit.mythicdrops.shade.methodcommand")
    relocate("mkremins", "com.tealcube.minecraft.bukkit.mythicdrops.shade.fanciful")
    relocate("org.apache.commons.lang3", "com.tealcube.minecraft.bukkit.mythicdrops.shade.apache.commons.lang3")
    relocate("org.apache.commons.text", "com.tealcube.minecraft.bukkit.mythicdrops.shade.apache.commons.text")
    relocate("org.bstats", "com.tealcube.minecraft.bukkit.mythicdrops.shade.bstats")
    relocate("okio", "com.tealcube.minecraft.bukkit.mythicdrops.shade.okio")
    relocate("com.squareup.moshi", "com.tealcube.minecraft.bukkit.mythicdrops.shade.moshi")
    relocate("net.amoebaman.util", "com.tealcube.minecraft.bukkit.mythicdrops.shade.amoebaman")
    relocate("javassist", "com.tealcube.minecraft.bukkit.mythicdrops.shade.javassist")
    relocate("co.aikar.commands", "com.tealcube.minecraft.bukkit.mythicdrops.shade.acf")
}

publishing {
    publications {
        create<MavenPublication>("shadow") {
            project.shadow.component(this@create)
        }
    }
}

buildConfigKt {
    appName = "MythicDrops"
}
