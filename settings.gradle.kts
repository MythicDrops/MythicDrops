import de.fayard.refreshVersions.bootstrapRefreshVersions

buildscript {
    repositories { gradlePluginPortal() }
    dependencies.classpath("de.fayard.refreshVersions:refreshVersions:0.9.7")
}

bootstrapRefreshVersions()

gradle.allprojects {
    group = "io.pixeloutlaw.mythicdrops"

    repositories {
        jcenter()
        maven {
            url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots")
        }
        maven {
            url = uri("https://papermc.io/repo/repository/maven-releases")
        }
        maven {
            url = uri("https://papermc.io/repo/repository/maven-snapshots")
        }
        maven {
            url = uri("https://oss.sonatype.org/content/repositories/snapshots")
        }
        maven {
            url = uri("https://repo.codemc.org/repository/maven-public")
        }
        maven {
            url = uri("https://repo.aikar.co/nexus/content/repositories/aikar-snapshots")
        }
        maven {
            url = uri("https://dl.bintray.com/pixeloutlaw/pixeloutlaw-jars")
        }
    }
}

rootProject.name = "mythicdrops"

// include(
//     "mythicdrops"
// )
