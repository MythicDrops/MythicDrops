/**
 * Find which updates are available by running
 *     `$ ./gradlew buildSrcVersions`
 * This will only update the comments.
 *
 * YOU are responsible for updating manually the dependency version. */
object Versions {
    const val com_diffplug_gradle_spotless_gradle_plugin: String = "3.23.1" // available: "3.24.0"

    const val com_github_johnrengelman_shadow_gradle_plugin: String = "5.1.0" 

    const val auto_service: String = "1.0-rc5" 

    const val truth: String = "1.0" 

    const val com_gradle_build_scan_gradle_plugin: String = "2.3" 

    const val worldguard_bukkit: String = "7.0.0" 

    const val de_fayard_buildsrcversions_gradle_plugin: String = "0.3.2" 

    const val io_gitlab_arturbosch_detekt: String = "1.0.0-RC16" 

    const val mockk: String = "1.9.3" 

    const val io_pixeloutlaw_gradle_buildconfigkt_gradle_plugin: String = "1.0.5" 

    const val io_pixeloutlaw_spigot_commons: String = "1.14.4.2" 

    const val junit: String = "4.12" 

    const val commons_text: String = "1.1" // available: "1.7"

    const val bstats_bukkit: String = "1.5" 

    const val org_jetbrains_dokka_gradle_plugin: String = "0.9.18" 

    const val org_jetbrains_kotlin_jvm_gradle_plugin: String = "1.3.41" 

    const val org_jetbrains_kotlin: String = "1.3.41" 

    const val mockito_core: String = "2.26.0" // available: "3.0.0"

    const val spigot_api: String = "1.14.4-R0.1-SNAPSHOT" 

    const val pl_allegro_tech_build_axion_release_gradle_plugin: String = "1.10.2" 

    /**
     *
     *   To update Gradle, edit the wrapper file at path:
     *      ./gradle/wrapper/gradle-wrapper.properties
     */
    object Gradle {
        const val runningVersion: String = "5.5.1"

        const val currentVersion: String = "5.5.1"

        const val nightlyVersion: String = "5.7-20190729220032+0000"

        const val releaseCandidate: String = "5.6-rc-1"
    }
}
