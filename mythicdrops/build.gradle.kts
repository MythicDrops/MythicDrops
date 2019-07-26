import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import de.undercouch.gradle.tasks.download.Download

plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("com.github.johnrengelman.shadow") version Versions.com_github_johnrengelman_shadow_gradle_plugin
    id("io.pixeloutlaw.gradle.buildconfigkt") version Versions.io_pixeloutlaw_gradle_buildconfigkt_gradle_plugin
    id("de.undercouch.download")
    `maven-publish`
    distribution
}

description = "MythicDrops"

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

distributions {
    main {
        baseName = "MythicDrops"
        contents {
            from(project.tasks.getByName<ShadowJar>("shadowJar")) {
                rename { it.replace(project.name, project.description ?: project.name) }
            }
            from(project.buildDir.resolve("resources/main")) {
                into("MythicDrops")
            }
        }
    }
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

tasks.create("downloadPaper", Download::class.java) {
    group = "development"
    src("https://papermc.io/ci/job/Paper-1.14/lastSuccessfulBuild/artifact/paperclip.jar")
    dest(project.buildDir.resolve("server/paperclip.jar"))
    onlyIfModified(true)
}
tasks.create("copyPluginToDevServer", Copy::class.java) {
    dependsOn("downloadPaper", "build")
    group = "development"
    from(project.tasks.getByName<ShadowJar>("shadowJar"))
    into(project.buildDir.resolve("server/plugins"))
}
tasks.create("dev", Exec::class.java) {
    group = "development"
    dependsOn("copyPluginToDevServer")
    workingDir(project.buildDir.resolve("server"))
    standardInput = System.`in`

    executable("java")
    args(
        listOf(
            "-Xmx1024M",
            "-Dcom.mojang.eula.agree=true",
            "-jar",
            "paperclip.jar"
        )
    )
}

tasks.findByName("assemble")?.dependsOn("shadowJar", "assembleDist")
tasks.getByName<Tar>("distTar") {
    archiveVersion.value(null)
    includeEmptyDirs = false
}
tasks.getByName<Zip>("distZip") {
    archiveVersion.value(null)
    includeEmptyDirs = false
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