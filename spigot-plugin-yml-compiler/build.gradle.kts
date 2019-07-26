plugins {
    kotlin("jvm")
    kotlin("kapt")
    `maven-publish`
}

dependencies {
    compileOnly(Libs.spigot_api)
    implementation(Libs.kotlin_stdlib_jdk8)
    implementation(project(":spigot-plugin-yml-annotations"))
    implementation("com.google.auto.service:auto-service:1.0-rc5")

    kapt("com.google.auto.service:auto-service:1.0-rc5")

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
