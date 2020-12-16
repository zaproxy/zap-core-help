import org.zaproxy.gradle.addon.AddOnStatus

version = "11"
extra["language"] = "English"

zapAddOn {
    addOnStatus.set(AddOnStatus.RELEASE)

    manifest {
        url.set("https://www.zaproxy.org/docs/desktop/")
        notBeforeVersion.set("2.10.0")
    }
}
