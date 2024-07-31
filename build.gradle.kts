plugins {
    kotlin("jvm") version "2.0.0"
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

detekt {
    baseline = file("baseline.xml")
}

mythicDropsRelease {
    assets.from(
        project(":mythicdrops").layout.buildDirectory.file("distributions/MythicDrops-v${rootProject.version}.zip"),
        project(":mythicdrops").layout.buildDirectory.file("libs/mythicdrops-${rootProject.version}-all.jar")
    )
    repository.set("MythicDrops/MythicDrops")
}
