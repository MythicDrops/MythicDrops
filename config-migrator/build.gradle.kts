plugins {
    kotlin("jvm")
    kotlin("kapt")
    `maven-publish`
}

dependencies {
    compileOnly(Libs.spigot_api)

    api(Libs.java_semver)
    api(Libs.moshi)
    api(Libs.moshi_adapters)

    implementation(Libs.kotlin_stdlib_jdk8)

    kapt(Libs.moshi_kotlin_codegen)
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