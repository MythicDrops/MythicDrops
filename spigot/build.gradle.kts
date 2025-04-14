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
    implementation("io.insert-koin:koin-annotations:2.0.0")
    implementation("net.kyori:adventure-platform-bukkit:_")

    ksp("io.insert-koin:koin-ksp-compiler:2.0.0")

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
        relocate("co.aikar.commands", "dev.mythicdrops.spigot.shade.acf")
        relocate("co.aikar.locale", "dev.mythicdrops.spigot.shade.aikar.locale")
        relocate("kotlin", "dev.mythicdrops.spigot.shade.kotlin")
        relocate("org.koin", "dev.mythicdrops.spigot.shade.koin")
        relocate("net.kyori", "dev.mythicdrops.spigot.shade.kyori")
        relocate("de.themoep", "dev.mythicdrops.spigot.shade.themoep")
    }
}

// See https://github.com/google/ksp/issues/1242
afterEvaluate {
    tasks.findByName("kspKotlin")?.dependsOn("generateBuildConfigKt")
}
