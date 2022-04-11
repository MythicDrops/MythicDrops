plugins {
    kotlin("jvm") version "1.7.20" apply false
    id("dev.mythicdrops.gradle.project")
    idea
}

description = "MythicDrops"

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
        project(":mythicdrops").buildDir.resolve("distributions")
            .resolve("MythicDrops-v${rootProject.version}.zip")
    )
    repository.set("MythicDrops/MythicDrops")
}
