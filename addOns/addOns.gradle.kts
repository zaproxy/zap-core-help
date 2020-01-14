import org.zaproxy.gradle.addon.AddOnPlugin
import org.zaproxy.gradle.addon.AddOnPluginExtension
import org.zaproxy.gradle.addon.manifest.ManifestExtension
import org.zaproxy.gradle.addon.misc.ConvertMarkdownToHtml
import org.zaproxy.gradle.addon.misc.CreateGitHubRelease
import org.zaproxy.gradle.addon.misc.ExtractLatestChangesFromChangelog

plugins {
    eclipse
    id("org.zaproxy.add-on") version "0.3.0" apply false
}

eclipse {
    project {
        // Prevent collision with zap-extensions' addOns project.
        name = "addOnsHelp"
    }
}

description = "Common configuration of the add-ons."

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

    zapAddOn {
        zapVersion.set("2.7.0")

        releaseLink.set(project.provider { "https://github.com/zaproxy/zap-core-help/releases/${zapAddOn.addOnId.get()}-v@CURRENT_VERSION@" })

        manifest {
            author.set("ZAP Crowdin Team")
            repo.set("https://github.com/zaproxy/zap-core-help/")
            changesFile.set(tasks.named<ConvertMarkdownToHtml>("generateManifestChanges").flatMap { it.html })
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

System.getenv("GITHUB_REF")?.let { ref ->
    if ("refs/tags/" !in ref || !ref.contains(Regex(".*-v.*"))) {
        return@let
    }

    tasks.register<CreateGitHubRelease>("createReleaseFromGitHubRef") {
        val targetTag = ref.removePrefix("refs/tags/")
        val (targetAddOnId, targetAddOnVersion) = targetTag.split("-v")
        val addOnProject = childProject(targetAddOnId)

        authToken.set(System.getenv("GITHUB_TOKEN"))
        repo.set(System.getenv("GITHUB_REPOSITORY"))
        tag.set(targetTag)

        title.set(addOnProject.map { "${it.zapAddOn.addOnName.get()} version ${it.zapAddOn.addOnVersion.get()}" })
        bodyFile.set(addOnProject.flatMap { it.tasks.named<ExtractLatestChangesFromChangelog>("extractLatestChanges").flatMap { it.latestChanges } })

        assets {
            register("add-on") {
                file.set(addOnProject.flatMap { it.tasks.named<Jar>(AddOnPlugin.JAR_ZAP_ADD_ON_TASK_NAME).flatMap { it.archiveFile } })
            }
        }

        doFirst {
            val addOnVersion = addOnProject.get().zapAddOn.addOnVersion.get()
            require(addOnVersion == targetAddOnVersion) {
                "Version of the tag $targetAddOnVersion does not match the version of the add-on $addOnVersion"
            }
        }
    }
}

fun childProject(addOnId: String): Provider<Project> =
    project.provider {
        val addOnProject = childProjects.values.firstOrNull { addOnId == it.zapAddOn.addOnId.get() }
        require(addOnProject != null) { "Add-on with ID $addOnId not found." }
        addOnProject
    }

fun childProjects(addOns: List<String>, action: (Project) -> Unit) =
    childProjects.values.filter { addOns.contains(it.name) }.forEach(action)

fun Project.java(configure: JavaPluginExtension.() -> Unit): Unit =
    (this as ExtensionAware).extensions.configure("java", configure)

fun Project.zapAddOn(configure: AddOnPluginExtension.() -> Unit): Unit =
    (this as ExtensionAware).extensions.configure("zapAddOn", configure)

val Project.zapAddOn: AddOnPluginExtension get() =
    (this as ExtensionAware).extensions.getByName("zapAddOn") as AddOnPluginExtension

fun AddOnPluginExtension.manifest(configure: ManifestExtension.() -> Unit): Unit =
    (this as ExtensionAware).extensions.configure("manifest", configure)
