import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.gradle.spotless.SpotlessPlugin
import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import pl.allegro.tech.build.axion.release.domain.TagNameSerializationConfig

plugins {
    base
    `build-scan`
    kotlin("jvm") version Versions.org_jetbrains_kotlin_jvm_gradle_plugin apply false
    id("com.diffplug.gradle.spotless") version Versions.com_diffplug_gradle_spotless_gradle_plugin apply false
    id("de.fayard.buildSrcVersions") version Versions.de_fayard_buildsrcversions_gradle_plugin
    id("pl.allegro.tech.build.axion-release") version Versions.pl_allegro_tech_build_axion_release_gradle_plugin
    id("io.gitlab.arturbosch.detekt") version Versions.io_gitlab_arturbosch_detekt apply false
}

// order matters for this plugin, so we configure it first
scmVersion {
    tag(closureOf<TagNameSerializationConfig> {
        prefix = ""
    })
}

rootProject.version = scmVersion.version

subprojects {
    this@subprojects.version = rootProject.version
    pluginManager.withPlugin("java") {
        this@subprojects.pluginManager.apply(SpotlessPlugin::class.java)
        this@subprojects.configure<SpotlessExtension> {
            java {
                target("src/**/*.java")
                googleJavaFormat()
                trimTrailingWhitespace()
                endWithNewline()
            }
            format("mythicDropsJava") {
                target("src/*/java/com/tealcube/**/*.java")
                if (this@subprojects.file("HEADER").exists()) {
                    licenseHeaderFile("HEADER", "package ")
                }
            }
        }

        this@subprojects.tasks.withType<JavaCompile> {
            dependsOn("spotlessJavaApply", "spotlessMythicDropsJavaApply")
        }
    }
    pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
        this@subprojects.pluginManager.apply(DetektPlugin::class.java)
        this@subprojects.pluginManager.apply(SpotlessPlugin::class.java)
        this@subprojects.configure<DetektExtension> {
            baseline = this@subprojects.file("baseline.xml")
        }
        this@subprojects.configure<SpotlessExtension> {
            kotlin {
                target("src/**/*.kt")
                ktlint()
                trimTrailingWhitespace()
                endWithNewline()
                if (this@subprojects.file("HEADER").exists()) {
                    licenseHeaderFile("HEADER")
                }
            }
        }
        this@subprojects.tasks.withType<KotlinCompile> {
            dependsOn("spotlessKotlinApply")
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }

        this@subprojects.configurations {
            "compileClasspath" {
                resolutionStrategy.force(
                    "com.sk89q.worldedit:worldedit-bukkit:7.0.0",
                    "org.bukkit:bukkit:1.14.4-R0.1-SNAPSHOT"
                )
            }
        }
    }
}

buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"

    publishAlways()
}