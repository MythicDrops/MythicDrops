plugins {
    `java-platform`
}

description = "MythicDrops :: Bill of Materials (BOM)"

dependencies {
    constraints {
        rootProject.subprojects.filterNot { it.name.endsWith("-bom") }.forEach {
            api(project(":${it.name}"))
        }
    }
}

javaPlatform {
    allowDependencies()
}
