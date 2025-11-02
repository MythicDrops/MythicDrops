pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

plugins {
    // See https://jmfayard.github.io/refreshVersions
    id("de.fayard.refreshVersions") version "0.60.5"
////                            # available:"0.60.6"
// //                            # available:"0.60.6"
    id("com.gradle.develocity") version "4.0.1"
////                        # available:"4.0.2"
////                        # available:"4.0.3"
////                        # available:"4.1"
////                        # available:"4.1.1"
////                        # available:"4.2"
////                        # available:"4.2.1"
////                        # available:"4.2.2"
// //                        # available:"4.0.2"
// //                        # available:"4.0.3"
// //                        # available:"4.1"
// //                        # available:"4.1.1"
// //                        # available:"4.2"
// //                        # available:"4.2.1"
// //                        # available:"4.2.2"
}

develocity {
    buildScan {
        publishing.onlyIf { it.buildResult.failures.isNotEmpty() && !System.getenv("CI").isNullOrEmpty() }
        termsOfUseUrl = "https://gradle.com/terms-of-service"
        termsOfUseAgree = "yes"
    }
}

gradle.allprojects {
    group = "io.pixeloutlaw.mythicdrops"

    repositories {
        mavenLocal()
        mavenCentral()
        maven {
            url = uri("https://oss.sonatype.org/content/repositories/snapshots")
        }
        maven {
            url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots")
        }
        maven {
            url = uri("https://repo.papermc.io/repository/maven-public")
        }
        maven {
            url = uri("https://repo.aikar.co/nexus/content/repositories/aikar-snapshots")
        }
    }
}

rootProject.name = "mythicdrops-aggregator"

include("api", "spigot", "bom")
project(":api").name = "mythicdrops-api"
project(":spigot").name = "mythicdrops"
project(":bom").name = "mythicdrops-bom"
