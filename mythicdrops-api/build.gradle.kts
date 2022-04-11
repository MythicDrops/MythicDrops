plugins {
    kotlin("jvm")
    id("org.jetbrains.kotlinx.binary-compatibility-validator")
    id("io.pixeloutlaw.gradle.buildconfigkt")
}

description = "MythicDrops :: API"

dependencies {
    compileOnly("org.spigotmc:spigot-api:_")
}

buildConfigKt {
    appName = "MythicDrops-API"
}

// ktlint {
//     filter {
//         exclude("**/BuildConfig.kt")
//     }
// }

tasks.findByName("dokkaJavadoc")?.dependsOn("generateBuildConfigKt")
tasks.findByName("runKtlintCheckOverMainSourceSet")?.dependsOn("generateBuildConfigKt")
tasks.findByName("sourceJar")?.dependsOn("generateBuildConfigKt")
