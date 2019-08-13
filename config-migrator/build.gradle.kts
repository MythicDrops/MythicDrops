plugins {
    kotlin("jvm")
    kotlin("kapt")
    `maven-publish`
}

dependencies {
    compileOnly(Libs.spigot_api)

    api(Libs.config)
    api("com.github.zafarkhaja:java-semver:0.9.0")
    api("org.reflections:reflections:0.9.11")
    api("com.squareup.moshi:moshi:1.8.0")
    api("com.squareup.moshi:moshi-adapters:1.8.0")

    implementation(Libs.kotlin_stdlib_jdk8)

    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.8.0")

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