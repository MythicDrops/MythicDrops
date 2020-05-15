rootProject.name = "mythicdrops"

gradle.allprojects {
    group = "io.pixeloutlaw.mythicdrops"

    repositories {
        jcenter()
        maven {
            url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots")
        }
        maven {
            url = uri("https://papermc.io/repo/repository/maven-public")
        }
        maven {
            url = uri("http://maven.sk89q.com/artifactory/repo")
        }
        maven {
            url = uri("https://repo.codemc.org/repository/maven-public")
        }
        maven {
            url = uri("https://repo.aikar.co/nexus/content/groups/aikar")
        }
        maven {
            url = uri("https://dl.bintray.com/pixeloutlaw/pixeloutlaw-jars")
        }
        mavenLocal()
        maven {
            url = uri("https://jitpack.io")
        }
    }
}

include(
    "mythicdrops"
)
