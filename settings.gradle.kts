plugins {
    // See https://jmfayard.github.io/refreshVersions
    id("de.fayard.refreshVersions") version "0.11.0"
}

gradle.allprojects {
    group = "io.pixeloutlaw.mythicdrops"

    repositories {
        mavenCentral()
        maven {
            url = uri("https://repo.minebench.de/")
        }
        maven {
            url = uri("https://oss.sonatype.org/content/repositories/snapshots")
        }
        maven {
            url = uri("https://repo.codemc.org/repository/nms")
        }
        maven {
            url = uri("https://repo.codemc.org/repository/maven-public")
        }
        maven {
            url = uri("https://repo.aikar.co/nexus/content/repositories/aikar-snapshots")
        }
        maven {
            url = uri("https://mvn.intellectualsites.com/content/repositories/thirdparty")
        }
    }
}

rootProject.name = "mythicdrops"
