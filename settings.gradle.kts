import de.fayard.refreshVersions.bootstrapRefreshVersions

buildscript {
    repositories { gradlePluginPortal() }
    dependencies.classpath("de.fayard.refreshVersions:refreshVersions:0.9.7")
}

bootstrapRefreshVersions()

gradle.allprojects {
    group = "io.pixeloutlaw.mythicdrops"

    repositories {
        mavenLocal()
        mavenCentral()
        jcenter() // remove this once koin is in maven central
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
