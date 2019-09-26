plugins {
    kotlin("jvm")
    kotlin("kapt")
}

dependencies {
    compileOnly(Libs.spigot_api)
    implementation(Libs.kotlin_stdlib_jdk8)
}
