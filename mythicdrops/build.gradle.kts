import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("com.github.johnrengelman.shadow") version Versions.com_github_johnrengelman_shadow_gradle_plugin
    id("io.pixeloutlaw.gradle.buildconfigkt") version "1.0.5"
    `maven-publish`
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
    implementation(project(":spigot-plugin-yml-annotations"))

    kapt(project(":spigot-plugin-yml-compiler"))

    testImplementation("org.spigotmc:spigot-api:1.14.4-R0.1-SNAPSHOT")
    testImplementation("junit:junit:4.12")
    testImplementation("org.mockito:mockito-core:2.26.0")
}


java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

publishing {
    publications {
        create<MavenPublication>("shadow") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()

            project.shadow.component(this@create)
        }
    }
}

tasks.findByName("build")?.dependsOn("shadowJar")

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