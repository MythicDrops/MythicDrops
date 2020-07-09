
import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.gradle.spotless.SpotlessPlugin
import com.moowork.gradle.node.yarn.YarnTask
import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import nebula.plugin.bintray.BintrayPlugin
import nebula.plugin.responsible.NebulaResponsiblePlugin
import org.gradle.process.internal.ExecAction
import org.jetbrains.dokka.gradle.DokkaPlugin
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    base
    kotlin("jvm") version "1.3.72" apply false
    id("com.diffplug.gradle.spotless") version "4.5.1" apply false
    id("io.gitlab.arturbosch.detekt") version "1.10.0" apply false
    id("org.jetbrains.dokka") version "0.10.1"
    id("nebula.maven-publish") version "17.3.0" apply false
    id("nebula.nebula-bintray") version "8.5.0"
    id("nebula.project") version "7.0.9" apply false
    id("nebula.release") version "15.0.3"
    id("com.moowork.node") version "1.3.1"
    id("se.bjurr.gitchangelog.git-changelog-gradle-plugin") version "1.64"
}

subprojects {
    this@subprojects.version = rootProject.version
    pluginManager.withPlugin("java") {
        this@subprojects.pluginManager.apply(NebulaResponsiblePlugin::class.java)
        this@subprojects.pluginManager.apply(BintrayPlugin::class.java)
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
            options.compilerArgs.add("-parameters")
            options.isFork = true
            options.forkOptions.executable = "javac"
        }
        this@subprojects.tasks.withType<Test> {
            useJUnitPlatform()
        }

        this@subprojects.dependencies {
            "testImplementation"("org.spigotmc:spigot-api:_")
            "testImplementation"("org.mockito:mockito-core:_")
            "testImplementation"(platform("org.junit:junit-bom:_"))
            "testImplementation"("org.junit.jupiter:junit-jupiter")
            "testRuntimeOnly"("org.junit.platform:junit-platform-launcher") {
                because("allows tests to run from IDEs that bundle older version of launcher")
            }
            "testImplementation"("org.assertj:assertj-core:_")
        }
    }
    pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
        this@subprojects.pluginManager.apply(DetektPlugin::class.java)
        this@subprojects.pluginManager.apply(DokkaPlugin::class.java)
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
                javaParameters = true
                jvmTarget = "1.8"
            }
        }
        this@subprojects.tasks.getByName<DokkaTask>("dokka") {
            outputFormat = "html"
            configuration {
                jdkVersion = 8
            }
        }
        val dokkaJavadoc = this@subprojects.tasks.create("dokkaJavadoc", DokkaTask::class.java) {
            outputDirectory = "${project.buildDir}/javadoc"
            outputFormat = "javadoc"
            configuration {
                jdkVersion = 8
            }
        }
        this@subprojects.tasks.getByName<Jar>("javadocJar") {
            dependsOn(dokkaJavadoc)
        }

        this@subprojects.dependencies {
            "testImplementation"("org.jetbrains.kotlin:kotlin-reflect:_")
            "testImplementation"("io.mockk:mockk:_")
        }
    }
}

bintray {
    pkgName.value("MythicDrops")
    repo.value("mythicdrops")
    userOrg.value("pixeloutlaw")
    syncToMavenCentral.value(false)
}

node {
    nodeModulesDir = rootProject.file("/website")
}

tasks.withType<Wrapper> {
    gradleVersion = "6.5"
    distributionType = Wrapper.DistributionType.ALL
}

tasks.withType<YarnTask> {
    setExecOverrides(closureOf<ExecAction> {
        workingDir = rootProject.file("/website")
    })
}

tasks.register("gitChangelog", se.bjurr.gitchangelog.plugin.gradle.GitChangelogTask::class.java) {
    file = file("CHANGELOG.md")
    fromRef = "v6.0.0"
    templateContent = """
        Changelog of MythicDrops.

        {{#tags}}
        ## {{name}}
         {{#issues}}
          {{#hasIssue}}
           {{#hasLink}}
        ### {{name}} [{{issue}}]({{link}}) {{title}} {{#hasIssueType}} *{{issueType}}* {{/hasIssueType}} {{#hasLabels}} {{#labels}} *{{.}}* {{/labels}} {{/hasLabels}}
           {{/hasLink}}
           {{^hasLink}}
        ### {{name}} {{issue}} {{title}} {{#hasIssueType}} *{{issueType}}* {{/hasIssueType}} {{#hasLabels}} {{#labels}} *{{.}}* {{/labels}} {{/hasLabels}}
           {{/hasLink}}
          {{/hasIssue}}
          {{^hasIssue}}
        ### {{name}}
          {{/hasIssue}}

          {{#commits}}
        **{{{messageTitle}}}**

        {{#messageBodyItems}}
         * {{.}}
        {{/messageBodyItems}}

        [{{hash}}](https://github.com/{{ownerName}}/{{repoName}}/commit/{{hash}}) {{authorName}} *{{commitTime}}*

          {{/commits}}

         {{/issues}}
        {{/tags}}
    """.trimIndent()
}

tasks.findByName("release")?.finalizedBy("build", "gitChangelog")