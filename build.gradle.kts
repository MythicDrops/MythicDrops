plugins {
    kotlin("jvm") version "1.6.20"
    id("org.jetbrains.kotlinx.binary-compatibility-validator")
    id("dev.mythicdrops.gradle.project")
    id("com.github.node-gradle.node")
    id("com.github.johnrengelman.shadow")
    id("io.pixeloutlaw.gradle.buildconfigkt")
    idea
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
    testImplementation(platform("org.junit:junit-bom:_"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:_")
    testImplementation("com.willowtreeapps.assertk:assertk-jvm:_")
    testImplementation("io.mockk:mockk:_")
    testImplementation("io.insert-koin:koin-test:_")
    testImplementation("io.insert-koin:koin-test-junit5:_")
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

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

ktlint {
    filter {
        exclude("**/BuildConfig.kt")
    }
}

mythicDropsRelease {
    assets.from(rootProject.buildDir.resolve("distributions").resolve("MythicDrops-v${rootProject.version}.zip"))
    repository.set("MythicDrops/MythicDrops")
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
