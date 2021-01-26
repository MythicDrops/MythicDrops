plugins {
    kotlin("jvm")
    id("io.pixeloutlaw.single")
    id("nebula.release")
    id("com.github.node-gradle.node")
    id("com.github.johnrengelman.shadow")
    id("io.pixeloutlaw.gradle.buildconfigkt")
}

description = "MythicDrops"

dependencies {
    compileOnly("org.spigotmc:spigot-api:_")
    compileOnly("me.arcaniax:HeadDatabase-API:1.1.0")

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:_")
    implementation("io.pixeloutlaw:plumbing-lib:_")
    implementation("io.pixeloutlaw.worldguard:adapter-lib:_")
    implementation("co.aikar:acf-paper:_")
    implementation("io.papermc:paperlib:_")
    implementation("com.github.shyiko.klob:klob:_")
    implementation("saschpe.log4k:log4k-jvm:_")
    implementation("org.koin:koin-core:_")

    testImplementation("org.spigotmc:spigot-api:_")
    testImplementation("org.mockito:mockito-core:_")
    testImplementation(platform("org.junit:junit-bom:_"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:_")
    testImplementation("com.willowtreeapps.assertk:assertk-jvm:_")
    testImplementation("io.mockk:mockk:_")
    testImplementation("org.koin:koin-test-junit5:_")
}

bintray {
    pkgName.set("mythicdrops")
    repo.set("mythicdrops")
    userOrg.set("pixeloutlaw")
    syncToMavenCentral.set(false)
}

buildConfigKt {
    appName = "MythicDrops"
}

detekt {
    baseline = file("baseline.xml")
}

node {
    nodeModulesDir = rootProject.file("/website")
}

tasks.findByName("assemble")?.dependsOn("assembleDist")

tasks.create("assembleDist", Zip::class.java) {
    dependsOn("shadowJar")
    archiveBaseName.value(project.description)
    archiveVersion.value("v${archiveVersion.get()}")
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

tasks.withType<ProcessResources> {
    filesMatching("plugin.yml") {
        expand("project" to project)
    }
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    mergeServiceFiles()
    relocate("co.aikar.commands", "com.tealcube.minecraft.bukkit.mythicdrops.shade.acf")
    relocate("co.aikar.locale", "com.tealcube.minecraft.bukkit.mythicdrops.shade.aikar.locale")
    relocate("kotlin", "io.pixeloutlaw.minecraft.spigot.mythicdrops.shade.kotlin")
    relocate("saschpe.log4k", "io.pixeloutlaw.minecraft.spigot.mythicdrops.shade.log4k")
}

tasks.withType<Wrapper> {
    gradleVersion = "6.8.1"
    distributionType = Wrapper.DistributionType.ALL
}

tasks.withType<com.moowork.gradle.node.yarn.YarnTask> {
    setExecOverrides(
        closureOf<org.gradle.process.internal.ExecAction> {
            workingDir = rootProject.file("/website")
        }
    )
}
