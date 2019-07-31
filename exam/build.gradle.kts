plugins {
    kotlin("jvm")
    `maven-publish`
}

dependencies {
    api(Libs.spigot_api)
    api(Libs.junit)
    api(Libs.truth)
    api(Libs.kotlin_reflect)
    api(Libs.mockito_core)
    api(Libs.mockk)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()

            from(components["java"])
        }
    }
}
