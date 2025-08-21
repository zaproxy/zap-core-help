import org.zaproxy.gradle.addon.AddOnStatus

extra["language"] = "English"
extra["url"] = "https://www.zaproxy.org/docs/desktop/"

zapAddOn {
    addOnStatus.set(AddOnStatus.RELEASE)
}
