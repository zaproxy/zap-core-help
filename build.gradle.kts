plugins {
    id("com.diffplug.spotless") version "6.14.1"
}

allprojects {
    apply(plugin = "com.diffplug.spotless")

    repositories {
        mavenCentral()
    }

    spotless {
        kotlinGradle {
            ktlint()
        }
    }
}
