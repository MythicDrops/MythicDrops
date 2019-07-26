import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("com.github.johnrengelman.shadow") version Versions.com_github_johnrengelman_shadow_gradle_plugin
    id("io.pixeloutlaw.gradle.buildconfigkt") version Versions.io_pixeloutlaw_gradle_buildconfigkt_gradle_plugin
    `maven-publish`
}

dependencies {
    compileOnly(Libs.spigot_api)
    compileOnly(Libs.worldguard_bukkit)

    implementation(Libs.kotlin_stdlib_jdk8)
    implementation(Libs.amp_menus)
    implementation(Libs.config)
    implementation(Libs.fanciful)
    implementation(Libs.hilt)
    implementation(Libs.method_command)
    implementation(Libs.commons_text)
    implementation(Libs.bstats_bukkit)
    implementation(project(":spigot-plugin-yml-annotations"))

    kapt(project(":spigot-plugin-yml-compiler"))

    testImplementation(project(":exam"))
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