plugins {
    kotlin("jvm")
    id("org.jetbrains.kotlinx.binary-compatibility-validator")
    id("io.pixeloutlaw.gradle.buildconfigkt")
    `jvm-test-suite`
}

description = "MythicDrops :: API"

dependencies {
    compileOnly("org.spigotmc:spigot-api:_")
}

buildConfigKt {
    appName = "MythicDrops-API"
}

tasks {
    findByName("dokkaJavadoc")?.dependsOn("generateBuildConfigKt")
    findByName("runKtlintCheckOverMainSourceSet")?.dependsOn("generateBuildConfigKt")
    findByName("runKtlintFormatOverMainSourceSet")?.dependsOn("generateBuildConfigKt")
    findByName("sourcesJar")?.dependsOn("generateBuildConfigKt")
    findByName("kotlinSourcesJar")?.dependsOn("generateBuildConfigKt")
}
