plugins {
    kotlin("jvm")
    kotlin("kapt")
}

dependencies {
    compileOnly("com.github.EngineHub.WorldGuard:worldguard-legacy:hotfix~1.12.2-explosion-SNAPSHOT")
    implementation(Libs.kotlin_stdlib_jdk8)
    api(project(":worldguard-adapters:worldguard-adapter-api"))
}
