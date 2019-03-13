import org.zaproxy.gradle.addon.AddOnStatus

version = "9"
extra["language"] = "English"

zapAddOn {
    addOnStatus.set(AddOnStatus.RELEASE)

    manifest {
        url.set("https://github.com/zaproxy/zap-core-help/wiki/HelpIntro")
    }

    wikiGen {
        wikiDir.set(file("$rootDir/../zap-core-help-wiki/"))
    }
}
