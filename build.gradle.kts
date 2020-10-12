import com.moowork.gradle.node.yarn.YarnTask
import org.gradle.process.internal.ExecAction
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.10"
    kotlin("kapt") version "1.4.10"
    id("com.diffplug.spotless") version "5.6.1"
    id("io.gitlab.arturbosch.detekt") version "1.14.1"
    id("org.jetbrains.dokka") version "1.4.10"
    id("nebula.nebula-bintray") version "8.5.0"
    id("nebula.maven-resolved-dependencies") version "17.3.2"
    id("nebula.release") version "15.3.0"
    id("com.moowork.node") version "1.3.1"
    id("com.github.johnrengelman.shadow") version "6.1.0"
    id("io.pixeloutlaw.gradle.buildconfigkt") version "1.0.6"
}

description = "MythicDrops"

dependencies {
    compileOnly("org.spigotmc:spigot-api:_")

    api(platform("io.pixeloutlaw.spigot-commons:spigot-commons-bom:_"))
    api("io.pixeloutlaw.spigot-commons:bandsaw")
    api("io.pixeloutlaw.spigot-commons:config")
    api("io.pixeloutlaw.spigot-commons:hilt")
    api("io.pixeloutlaw.minecraft.spigot:config-migrator:_")
    api("org.apache.commons:commons-text:_")
    api("org.bstats:bstats-bukkit:_")
    api("io.pixeloutlaw.minecraft.spigot:plugin-yml-annotations:_")
    api("co.aikar:acf-paper:_")
    api("io.pixeloutlaw.worldguard:adapter-lib:_")
    api("io.papermc:paperlib:_")
    api("com.github.shyiko.klob:klob:_")

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:_")

    kapt("io.pixeloutlaw.minecraft.spigot:plugin-yml-processor:_")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:_")

    testImplementation("org.spigotmc:spigot-api:_")
    testImplementation("org.mockito:mockito-core:_")
    testImplementation(platform("org.junit:junit-bom:_"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:_")
    testImplementation("org.jetbrains.kotlin:kotlin-reflect:_")
    testImplementation("io.mockk:mockk:_")
}

bintray {
    pkgName.value("MythicDrops")
    repo.value("mythicdrops")
    userOrg.value("pixeloutlaw")
    syncToMavenCentral.value(false)
}

buildConfigKt {
    appName = "MythicDrops"
}

detekt {
    baseline = file("baseline.xml")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

node {
    nodeModulesDir = rootProject.file("/website")
}

spotless {
    java {
        target("src/**/*.java")
        googleJavaFormat()
        trimTrailingWhitespace()
        endWithNewline()
    }
    format("mythicDropsJava") {
        target("src/*/java/com/tealcube/**/*.java")
        if (file("HEADER").exists()) {
            licenseHeaderFile("HEADER", "package ")
        }
    }
    kotlin {
        target("src/**/*.kt")
        ktlint()
        trimTrailingWhitespace()
        endWithNewline()
        if (file("HEADER").exists()) {
            licenseHeaderFile("HEADER")
        }
    }
}

tasks.findByName("assemble")?.dependsOn("assembleDist")

tasks.create("assembleDist", Zip::class.java) {
    dependsOn("shadowJar")
    archiveBaseName.value(project.description)
    from("${project.buildDir}/libs") {
        include("${project.name}-${project.version}-all.jar")
        rename { it.replace("-all.jar", ".jar") }
    }
    from("${project.buildDir}/resources/main") {
        include("*.yml")
        exclude("plugin.yml")
        include("resources/**/*")
        include("tiers/*.yml")
        include("variables.txt")
        into("MythicDrops")
    }
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

tasks.create("javadocJar", Jar::class.java) {
    dependsOn("dokkaJavadoc")
    from(tasks.getByName("dokkaJavadoc"))
    archiveClassifier.set("javadoc")
    archiveExtension.set("jar")
    group = "build"
}

tasks.create("sourcesJar", Jar::class.java) {
    dependsOn("classes")
    from(sourceSets.main.get().allSource)
    archiveClassifier.set("sources")
    archiveExtension.set("jar")
    group = "build"
}

tasks.withType<JavaCompile> {
    dependsOn("spotlessJavaApply", "spotlessMythicDropsJavaApply")
    options.compilerArgs.add("-parameters")
    options.isFork = true
    options.forkOptions.executable = "javac"
}

tasks.withType<KotlinCompile> {
    dependsOn("spotlessKotlinApply")
    kotlinOptions {
        javaParameters = true
        jvmTarget = "1.8"
    }
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    mergeServiceFiles()
    relocate("com.github.zafarkhaja", "com.tealcube.minecraft.bukkit.mythicdrops.shade.jsemver")
    relocate("se.ranzdo.bukkit.methodcommand", "com.tealcube.minecraft.bukkit.mythicdrops.shade.methodcommand")
    relocate("mkremins", "com.tealcube.minecraft.bukkit.mythicdrops.shade.fanciful")
    relocate("org.apache.commons.lang3", "com.tealcube.minecraft.bukkit.mythicdrops.shade.apache.commons.lang3")
    relocate("org.apache.commons.text", "com.tealcube.minecraft.bukkit.mythicdrops.shade.apache.commons.text")
    relocate("org.bstats", "com.tealcube.minecraft.bukkit.mythicdrops.shade.bstats")
    relocate("okio", "com.tealcube.minecraft.bukkit.mythicdrops.shade.okio")
    relocate("com.squareup.moshi", "com.tealcube.minecraft.bukkit.mythicdrops.shade.moshi")
    relocate("net.amoebaman.util", "com.tealcube.minecraft.bukkit.mythicdrops.shade.amoebaman")
    relocate("javassist", "com.tealcube.minecraft.bukkit.mythicdrops.shade.javassist")
    relocate("co.aikar.commands", "com.tealcube.minecraft.bukkit.mythicdrops.shade.acf")
    relocate("kotlin", "io.pixeloutlaw.minecraft.spigot.mythicdrops.shade.kotlin")
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("passed", "failed", "skipped")
    }
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

// Publishing is down here because order matters
publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = rootProject.group.toString()
            artifactId = rootProject.name
            version = rootProject.version.toString()

            from(components["java"])
            artifact(project.tasks.getByName("sourcesJar", Jar::class))
            artifact(project.tasks.getByName("javadocJar", Jar::class))

            pom {
                withXml {
                    val root = asNode()
                    val dependencies = project.configurations.compileOnly.get().dependencies
                    if (dependencies.size > 0) {
                        val deps = root.children().find {
                            it is groovy.util.Node && it.name().toString()
                                .endsWith("dependencies")
                        } as groovy.util.Node? ?: root.appendNode("dependencies")
                        dependencies.forEach { dependency ->
                            deps.appendNode("dependency").apply {
                                appendNode("groupId", dependency.group)
                                appendNode("artifactId", dependency.name)
                                appendNode("version", dependency.version)
                                appendNode("scope", "provided")
                            }
                        }
                    }
                }
            }
        }
    }
}