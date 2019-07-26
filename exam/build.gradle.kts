plugins {
    kotlin("jvm")
    `maven-publish`
}

dependencies {
    api("junit:junit:4.12")
    api("com.google.truth:truth:1.0")
    api("org.jetbrains.kotlin:kotlin-reflect:1.3.41")
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
