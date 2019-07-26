import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `build-scan`
    kotlin("jvm") version "1.3.41"
    // id("com.diffplug.gradle.spotless") version "3.23.1"
    id("de.fayard.buildSrcVersions") version "0.3.2"
    id("pl.allegro.tech.build.axion-release") version "1.10.2"
    id("com.github.johnrengelman.shadow") version "5.1.0"
    `maven-publish`
}

configurations {
    "compileClasspath" {
        resolutionStrategy.force(
            "com.sk89q.worldedit:worldedit-bukkit:7.0.0",
            "org.bukkit:bukkit:1.14.4-R0.1-SNAPSHOT"
        )
    }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.14.4-R0.1-SNAPSHOT")
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.0")

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.41")
    implementation("io.pixeloutlaw.spigot-commons:amp-menus:1.14.4.2")
    implementation("io.pixeloutlaw.spigot-commons:config:1.14.4.2")
    implementation("io.pixeloutlaw.spigot-commons:fanciful:1.14.4.2")
    implementation("io.pixeloutlaw.spigot-commons:hilt:1.14.4.2")
    implementation("io.pixeloutlaw.spigot-commons:method-command:1.14.4.2")
    implementation("org.apache.commons:commons-text:1.1")
    implementation("org.bstats:bstats-bukkit:1.5")

    testImplementation("org.spigotmc:spigot-api:1.14.4-R0.1-SNAPSHOT")
    testImplementation("junit:junit:4.12")
    testImplementation("org.mockito:mockito-core:2.26.0")
}

buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"

    publishAlways()
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()

            from(components["java"])
        }
        create<MavenPublication>("shadow") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()

            project.shadow.component(this@create)
        }
    }
}

// spotless {
//     kotlin {
//         target("src/**/*.kt")
//         ktlint()
//     }
// }

tasks.findByName("build")?.dependsOn("shadowJar")

tasks.withType<KotlinCompile> {
    // dependsOn("spotlessKotlinApply")
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

tasks.withType<ShadowJar> {
    archiveClassifier.value(null)
    mergeServiceFiles()
    relocate("com.github.zafarkhaja", "com.github.zafarkhaja.mythicdrops")
    relocate("se.ranzdo.bukkit.methodcommand", "se.ranzdo.bukkit.mythicdrops.methodcommand")
    relocate("mkremins", "mkremins.mythicdrops")
    relocate("ninja.amp.ampmenus", "ninja.amp.ampmenus.mythicdrops")
    relocate("org.apache", "org.apache.mythicdrops")
    relocate("org.bstats", "org.bstats.mythicdrops")
}