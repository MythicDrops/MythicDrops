import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("com.github.johnrengelman.shadow") version Versions.com_github_johnrengelman_shadow_gradle_plugin
    id("io.pixeloutlaw.gradle.buildconfigkt") version Versions.io_pixeloutlaw_gradle_buildconfigkt_gradle_plugin
}

description = "MythicDrops"

dependencies {
    compileOnly(Libs.spigot_api)

    implementation(Libs.kotlin_stdlib_jdk8)
    implementation(Libs.config)
    implementation(Libs.config_migrator)
    implementation(Libs.fanciful)
    implementation(Libs.hilt)
    implementation(Libs.commons_text)
    implementation(Libs.bstats_bukkit)
    implementation(Libs.plugin_yml_annotations)
    implementation(Libs.acf_paper)
    implementation(Libs.adapter_lib)

    kapt(Libs.plugin_yml_processor)
    kapt(Libs.moshi_kotlin_codegen)
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
