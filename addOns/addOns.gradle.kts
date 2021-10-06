import java.util.regex.Pattern
import org.zaproxy.gradle.addon.AddOnPluginExtension
import org.zaproxy.gradle.addon.internal.GitHubReleaseExtension
import org.zaproxy.gradle.addon.internal.model.AddOnRelease
import org.zaproxy.gradle.addon.internal.model.ProjectInfo
import org.zaproxy.gradle.addon.internal.model.ReleaseState
import org.zaproxy.gradle.addon.internal.tasks.CreatePullRequest
import org.zaproxy.gradle.addon.internal.tasks.CreateTagAndGitHubRelease
import org.zaproxy.gradle.addon.internal.tasks.GenerateReleaseStateLastCommit
import org.zaproxy.gradle.addon.internal.tasks.HandleRelease
import org.zaproxy.gradle.addon.manifest.ManifestExtension
import org.zaproxy.gradle.addon.misc.ConvertMarkdownToHtml

plugins {
    eclipse
    id("org.zaproxy.add-on") version "0.7.0" apply false
    id("org.zaproxy.crowdin") version "0.1.0"
}

eclipse {
    project {
        // Prevent collision with zap-extensions' addOns project.
        name = "addOnsHelp"
    }
}

crowdin {
    credentials {
        token.set(System.getenv("CROWDIN_AUTH_TOKEN"))
    }

    configuration {
        file.set(file("$rootDir/gradle/crowdin.yml"))
    }
}

description = "Common configuration of the add-ons."

val ghReleaseDataProvider = provider {
    subprojects.first().zapAddOn.gitHubRelease
}
val generateReleaseStateLastCommit by tasks.registering(GenerateReleaseStateLastCommit::class)

val handleRelease by tasks.registering(HandleRelease::class) {
    user.set(ghReleaseDataProvider.map { it.user.get() })
    repo.set(ghReleaseDataProvider.map { it.marketplaceRepo.get() })
}

val prepareNextDevIter by tasks.registering {
    mustRunAfter(handleRelease)
}

val releasedProjects = mutableListOf<Project>()
val createPullRequestNextDevIter by tasks.registering(CreatePullRequest::class) {
    user.set(ghReleaseDataProvider.map { it.user.get() })
    repo.set(ghReleaseDataProvider.map { it.repo.get() })
    branchName.set("bump-version")

    commitSummary.set("Prepare next dev iteration(s)")
    commitDescription.set(provider {
        "Update version and changelog for:\n" + releasedProjects.map {
            " - ${it.zapAddOn.addOnName.get()}"
        }.sorted().joinToString("\n")
    })

    dependsOn(prepareNextDevIter)
}

val releaseAddOn by tasks.registering

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
        zapVersion.set("2.11.0")

        releaseLink.set(project.provider { "https://github.com/zaproxy/zap-core-help/releases/${zapAddOn.addOnId.get()}-v@CURRENT_VERSION@" })

        manifest {
            author.set("ZAP Crowdin Team")
            repo.set("https://github.com/zaproxy/zap-core-help/")
            changesFile.set(tasks.named<ConvertMarkdownToHtml>("generateManifestChanges").flatMap { it.html })
        }
    }

    dependencies {
        "zap"("org.zaproxy:zap:2.11.0-20210929.165234-4")
        "implementation"(project(":commonFiles"))
    }

    afterEvaluate {
        val language = ext["language"]

        description = "$language version of the ZAP help file."

        zapAddOn {
            addOnName.set("Help - $language")
        }
    }

    val projectInfo = ProjectInfo.from(project)
    generateReleaseStateLastCommit {
        projects.add(projectInfo)
    }

    if (ReleaseState.read(projectInfo).isNewRelease()) {
        releasedProjects.add(project)

        val versionProvider = project.zapAddOn.addOnVersion
        val tagProvider = versionProvider.map { "${project.zapAddOn.addOnId.get()}-v$it" }
        val createReleaseAddOn by project.tasks.named<CreateTagAndGitHubRelease>("createRelease") {
            tag.set(tagProvider)
            val message = versionProvider.map { "${project.zapAddOn.addOnName.get()} version $it" }
            tagMessage.set(message)
            title.set(message)
        }
        val zapAddOnExt = project.zapAddOn
        releaseAddOn {
            dependsOn(createReleaseAddOn)

            dependsOn(handleRelease)
            dependsOn(createPullRequestNextDevIter)
            if (zapAddOnExt.addOnId.get() == "help") {
                dependsOn(":addOns:crowdinUploadSourceFiles")
            }
        }

        val addOnRelease = AddOnRelease.from(project)
        addOnRelease.downloadUrl.set(addOnRelease.addOn.map { it.asFile.name }.map {
            "https://github.com/${ghReleaseDataProvider.get().repo.get()}/releases/download/${tagProvider.get()}/$it"
        })

        handleRelease {
            addOns.add(addOnRelease)

            mustRunAfter(createReleaseAddOn)
        }

        val prepareNextDevIterAddOn by project.tasks.named("prepareNextDevIter") {
            mustRunAfter(handleRelease)
        }
        prepareNextDevIter {
            dependsOn(prepareNextDevIterAddOn)
        }
    }
}

val createPullRequestRelease by tasks.registering(CreatePullRequest::class) {
    System.getenv("ADD_ON_IDS")?.let {
        val projects = it.split(Pattern.compile(" *, *")).map { name ->
            val project = subprojects.find { it.name == name }
            require(project != null) { "Add-on with project name $name not found." }
            project
        }

        projects.forEach {
            dependsOn(it.tasks.named("prepareRelease"))
        }

        user.set(ghReleaseDataProvider.map { it.user.get() })
        repo.set(ghReleaseDataProvider.map { it.repo.get() })
        branchName.set("release")

        commitSummary.set("Release add-on(s)")
        commitDescription.set(provider {
            "Release the following add-ons:\n" + projects.map {
                " - ${it.zapAddOn.addOnName.get()} version ${it.zapAddOn.addOnVersion.get()}"
            }.sorted().joinToString("\n")
        })
    }
}

fun Project.java(configure: JavaPluginExtension.() -> Unit): Unit =
    (this as ExtensionAware).extensions.configure("java", configure)

fun Project.zapAddOn(configure: AddOnPluginExtension.() -> Unit): Unit =
    (this as ExtensionAware).extensions.configure("zapAddOn", configure)

val Project.zapAddOn: AddOnPluginExtension get() =
    (this as ExtensionAware).extensions.getByName("zapAddOn") as AddOnPluginExtension

val AddOnPluginExtension.gitHubRelease: GitHubReleaseExtension get() =
    (this as ExtensionAware).extensions.getByName("gitHubRelease") as GitHubReleaseExtension

fun AddOnPluginExtension.manifest(configure: ManifestExtension.() -> Unit): Unit =
    (this as ExtensionAware).extensions.configure("manifest", configure)
