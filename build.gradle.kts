plugins {
    id("com.diffplug.spotless")
    id("org.zaproxy.common")
}

allprojects {
    apply(plugin = "com.diffplug.spotless")

    spotless {
        kotlinGradle {
            ktlint()
        }
    }
}
