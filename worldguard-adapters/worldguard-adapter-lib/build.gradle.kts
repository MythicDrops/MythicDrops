plugins {
    kotlin("jvm")
    kotlin("kapt")
}

dependencies {
    compileOnly(Libs.spigot_api)

    api(project(":worldguard-adapters:worldguard-adapter-6.2.x"))
    api(project(":worldguard-adapters:worldguard-adapter-7.0.x"))

    implementation(Libs.kotlin_stdlib_jdk8)
}
