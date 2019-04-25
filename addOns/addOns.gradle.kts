import org.zaproxy.gradle.addon.AddOnPlugin
import org.zaproxy.gradle.addon.AddOnPluginExtension
import org.zaproxy.gradle.addon.manifest.ManifestExtension
import org.zaproxy.gradle.addon.manifest.tasks.ConvertChangelogToChanges
import org.zaproxy.gradle.addon.wiki.WikiGenExtension
import org.zaproxy.gradle.addon.zapversions.ZapVersionsExtension

plugins {
    eclipse
    id("org.zaproxy.add-on") version "0.1.0" apply false
}

eclipse {
    project {
        // Prevent collision with zap-extensions' addOns project.
        name = "addOnsHelp"
    }
}

description = "Common configuration of the add-ons."

val mainAddOns = listOf("help")
val weeklyAddOns = mainAddOns + listOf()

mapOf("main" to mainAddOns, "weekly" to weeklyAddOns).forEach { entry ->
    tasks {
        val name = entry.key
        val nameCapitalized = name.capitalize()
        register("copy${nameCapitalized}AddOns") {
            group = "ZAP"
            description = "Copies the $name release add-ons to zaproxy project."
            childProjects(entry.value) {
                dependsOn(it.tasks.named(AddOnPlugin.COPY_ADD_ON_TASK_NAME))
            }
        }

        register("list${nameCapitalized}AddOns") {
            group = "ZAP"
            description = "Lists the $name release add-ons."
            doLast {
                childProjects(entry.value) { println(it.name) }
            }
        }
    }
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "org.zaproxy.add-on")

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "utf-8"
        options.compilerArgs = listOf("-Xlint:all", "-Xlint:-path", "-Xlint:-options", "-Werror")
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    val generateManifestChanges by tasks.registering(ConvertChangelogToChanges::class) {
        changelog.set(file("CHANGELOG.md"))
        manifestChanges.set(file("$buildDir/zapAddOn/manifest-changes.html"))
    }

    tasks.named<Jar>("jarJavaHelpDataForWiki") {
        from(configurations.named("runtimeClasspath").map { it.files.map { file -> if (file.isDirectory) file else project.zipTree(file) } })
    }

    zapAddOn {
        zapVersion.set("2.7.0")

        manifest {
            author.set("ZAP Crowdin Team")
            url.set("https://github.com/zaproxy/zap-core-help/")
            changesFile.set(generateManifestChanges.flatMap { it.manifestChanges })
        }

        wikiGen {
            wikiFilesPrefix.set("Help")
        }
    }

    dependencies {
        "implementation"(project(":commonFiles"))
    }

    afterEvaluate {
        val language = ext["language"]

        description = "$language version of the ZAP help file."

        zapAddOn {
            addOnName.set("Help - $language")
        }
    }
}

fun childProjects(addOns: List<String>, action: (Project) -> Unit) =
    childProjects.values.filter { addOns.contains(it.name) }.forEach(action)

fun Project.java(configure: JavaPluginExtension.() -> Unit): Unit =
    (this as ExtensionAware).extensions.configure("java", configure)

fun Project.zapAddOn(configure: AddOnPluginExtension.() -> Unit): Unit =
    (this as ExtensionAware).extensions.configure("zapAddOn", configure)

fun AddOnPluginExtension.manifest(configure: ManifestExtension.() -> Unit): Unit =
    (this as ExtensionAware).extensions.configure("manifest", configure)

fun AddOnPluginExtension.wikiGen(configure: WikiGenExtension.() -> Unit): Unit =
    (this as ExtensionAware).extensions.configure("wikiGen", configure)

fun AddOnPluginExtension.zapVersions(configure: ZapVersionsExtension.() -> Unit): Unit =
    (this as ExtensionAware).extensions.configure("zapVersions", configure)
