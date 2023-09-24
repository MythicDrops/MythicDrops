pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

plugins {
    // See https://jmfayard.github.io/refreshVersions
    id("de.fayard.refreshVersions") version "0.60.2"
    id("com.gradle.enterprise") version "3.15"
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
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
            url = uri("https://repo.minebench.de/")
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
