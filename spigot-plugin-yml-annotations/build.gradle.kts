plugins {
    java
    `maven-publish`
}

dependencies {
    compileOnly(Libs.spigot_api)

    testImplementation(project(":exam"))
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
