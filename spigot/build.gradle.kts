plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlinx.binary-compatibility-validator")
    id("com.github.johnrengelman.shadow")
    id("io.pixeloutlaw.gradle.buildconfigkt")
    `jvm-test-suite`
}

description = "MythicDrops"

dependencies {
    compileOnly("org.spigotmc:spigot-api:_")
    compileOnly("com.arcaniax:HeadDatabase-API:_")

    api(project(":mythicdrops-api"))

    implementation(kotlin("stdlib"))
    implementation("io.pixeloutlaw.worldguard:adapter-lib:_")
    implementation("io.pixeloutlaw:kindling:_")
    implementation("co.aikar:acf-paper:_")
    implementation("io.insert-koin:koin-core-jvm:_")
    implementation("io.insert-koin:koin-annotations:1.3.1")
    implementation("net.kyori:adventure-platform-bukkit:_")

    ksp("io.insert-koin:koin-ksp-compiler:1.3.1")

    testImplementation("org.spigotmc:spigot-api:_")
    testImplementation(platform("org.junit:junit-bom:_"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:_")
    testImplementation("com.willowtreeapps.assertk:assertk-jvm:_")
    testImplementation("io.mockk:mockk:_")
    testImplementation("io.insert-koin:koin-test-junit5:_")
    testImplementation("com.github.seeseemelk:MockBukkit-v1.20:_")
}

buildConfigKt {
    appName = "MythicDrops"
}

tasks {
    findByName("dokkaJavadoc")?.dependsOn("generateBuildConfigKt")
    findByName("runKtlintCheckOverMainSourceSet")?.dependsOn("generateBuildConfigKt")
    findByName("runKtlintFormatOverMainSourceSet")?.dependsOn("generateBuildConfigKt")
    findByName("sourcesJar")?.dependsOn("generateBuildConfigKt")
    findByName("kotlinSourcesJar")?.dependsOn("generateBuildConfigKt")

    create("assembleDist", Zip::class.java) {
        dependsOn("shadowJar")
        archiveBaseName.value(project.description)
        archiveVersion.value("v${archiveVersion.get()}")
        from(getByName("shadowJar")) {
            rename { it.replace("-all.jar", ".jar") }
        }
        from(getByName("processResources")) {
            include("*.yml")
            exclude("plugin.yml")
            include("resources/**/*")
            include("tiers/*.yml")
            include("variables.txt")
            into("MythicDrops")
        }
    }
    findByName("assemble")?.dependsOn("assembleDist")

    withType<ProcessResources> {
        filesMatching("plugin.yml") {
            expand("project" to project)
        }
    }

    withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
        mergeServiceFiles()
        relocate("co.aikar.commands", "com.tealcube.minecraft.bukkit.mythicdrops.shade.acf")
        relocate("co.aikar.locale", "com.tealcube.minecraft.bukkit.mythicdrops.shade.aikar.locale")
        relocate("kotlin", "io.pixeloutlaw.minecraft.spigot.mythicdrops.shade.kotlin")
        relocate("org.koin", "io.pixeloutlaw.minecraft.spigot.mythicdrops.shade.koin")
        relocate("net.kyori", "io.pixeloutlaw.minecraft.spigot.mythicdrops.shade.kyori")
        relocate("de.themoep", "io.pixeloutlaw.minecraft.spigot.mythicdrops.shade.themoep")
    }
}
