plugins {
    kotlin("jvm") version "1.9.10" apply false
    id("dev.mythicdrops.gradle.project")
    idea
}

description = "MythicDrops :: Root"

contacts {
    addPerson(
        "topplethenunnery@gmail.com",
        closureOf<nebula.plugin.contacts.Contact> {
            moniker = "ToppleTheNun"
            github = "ToppleTheNun"
        }
    )
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

mythicDropsRelease {
    assets.from(
        project(":mythicdrops").buildDir.resolve("distributions").resolve("MythicDrops-v${rootProject.version}.zip"),
        project(":mythicdrops").buildDir.resolve("libs").resolve("mythicdrops-${rootProject.version}-all.jar")
    )
    repository.set("MythicDrops/MythicDrops")
}
