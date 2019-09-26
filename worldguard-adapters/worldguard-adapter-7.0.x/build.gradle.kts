plugins {
    kotlin("jvm")
    kotlin("kapt")
}

dependencies {
    compileOnly(Libs.worldguard_bukkit)
    implementation(Libs.kotlin_stdlib_jdk8)
    api(project(":worldguard-adapters:worldguard-adapter-api"))
}
