plugins {
    kotlin("jvm")
    kotlin("kapt")
}

dependencies {
    compileOnly(Libs.spigot_api)
    implementation(Libs.kotlin_stdlib_jdk8)
    implementation(project(":spigot-plugin-yml-annotations"))
    implementation(Libs.auto_service)

    kapt(Libs.auto_service)
}
