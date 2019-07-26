rootProject.name = "mythicdrops"

gradle.rootProject {
    group = "io.pixeloutlaw.mythicdrops"
    version = "6.0.0-SNAPSHOT"

    repositories {
        mavenCentral()
        jcenter()
        maven {
            url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots")
        }
        maven {
            url = uri("https://papermc.io/repo/repository/maven-public/")
        }
        maven {
            url = uri("http://maven.sk89q.com/artifactory/repo/")
        }
        maven {
            url = uri("https://repo.codemc.org/repository/maven-public")
        }
        maven {
            url = uri("https://jitpack.io")
        }
    }
}
