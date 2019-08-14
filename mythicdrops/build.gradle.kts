import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("com.github.johnrengelman.shadow") version Versions.com_github_johnrengelman_shadow_gradle_plugin
    id("io.pixeloutlaw.gradle.buildconfigkt") version Versions.io_pixeloutlaw_gradle_buildconfigkt_gradle_plugin
    id("org.jetbrains.dokka") version Versions.org_jetbrains_dokka_gradle_plugin
    id("de.undercouch.download")
    `maven-publish`
}

description = "MythicDrops"

dependencies {
    compileOnly(Libs.spigot_api)
    compileOnly(Libs.worldguard_bukkit)

    implementation(Libs.kotlin_stdlib_jdk8)
    implementation(Libs.amp_menus)
    implementation(Libs.config)
    implementation(project(":config-migrator"))
    implementation(Libs.fanciful)
    implementation(Libs.hilt)
    implementation(Libs.method_command)
    implementation(Libs.commons_text)
    implementation(Libs.bstats_bukkit)
    implementation(project(":spigot-plugin-yml-annotations"))
    implementation("ch.qos.logback:logback-classic:1.2.3")

    kapt(project(":spigot-plugin-yml-compiler"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.getByName<DokkaTask>("dokka") {
    outputFormat = "html"
    jdkVersion = 8
}
val dokkaJavadoc = tasks.create("dokkaJavadoc", DokkaTask::class.java) {
    outputDirectory = "${project.buildDir}/javadoc"
    outputFormat = "javadoc"
    jdkVersion = 8
}
val javadocJar = tasks.create("javadocJar", Jar::class.java) {
    dependsOn(dokkaJavadoc)
    archiveClassifier.value("javadoc")
    from(dokkaJavadoc.outputDirectory)
}
val sourcesJar = tasks.create("sourcesJar", Jar::class.java) {
    archiveClassifier.value("sources")
    from(sourceSets.main.get().allSource)
}
tasks.create("assembleDist", Zip::class.java) {
    baseName = project.description
    from("${project.buildDir}/libs") {
        include("${project.name}-${project.version}.jar")
    }
    from("${project.buildDir}/resources/main") {
        include("*.yml")
        include("resources/**/*")
        include("tiers/*.yml")
        into("MythicDrops")
    }
}

tasks.findByName("assemble")?.dependsOn("shadowJar")
tasks.findByName("build")?.dependsOn("assembleDist")

tasks.withType<ShadowJar> {
    archiveClassifier.value(null)
    mergeServiceFiles()
    relocate("com.github.zafarkhaja", "com.github.zafarkhaja.mythicdrops")
    relocate("com.google", "com.google.mythicdrops") {
        exclude("com.google.gson.*")
    }
    relocate("se.ranzdo.bukkit.methodcommand", "se.ranzdo.bukkit.methodcommand.mythicdrops")
    relocate("mkremins", "mkremins.mythicdrops")
    relocate("ninja.amp.ampmenus", "ninja.amp.ampmenus.mythicdrops")
    relocate("org.apache", "org.apache.mythicdrops")
    relocate("org.bstats", "org.bstats.mythicdrops")
    relocate("org.reflections", "org.reflections.mythicdrops")
    relocate("okio", "okio.mythicdrops")
    relocate("com.squareup.moshi", "okio.mythicdrops")
    relocate("net.amoebaman.util", "net.amoebaman.util.mythicdrops")
    relocate("javassist", "javassist.mythicdrops")
}

publishing {
    publications {
        create<MavenPublication>("shadow") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()

            project.shadow.component(this@create)
            artifact(javadocJar)
            artifact(sourcesJar)
        }
    }
}

buildConfigKt {
    appName = "MythicDrops"
}