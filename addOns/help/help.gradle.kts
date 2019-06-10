import org.zaproxy.gradle.addon.AddOnStatus

version = "10"
extra["language"] = "English"

zapAddOn {
    addOnStatus.set(AddOnStatus.RELEASE)

    manifest {
        url.set("https://github.com/zaproxy/zap-core-help/wiki/HelpIntro")
        notBeforeVersion.set("2.8.0")
    }

    wikiGen {
        wikiDir.set(file("$rootDir/../zap-core-help-wiki/"))
    }
}
