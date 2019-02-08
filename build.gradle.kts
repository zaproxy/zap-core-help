plugins {
    id("com.diffplug.gradle.spotless") version "3.18.0"
}

allprojects {
    apply(plugin = "com.diffplug.gradle.spotless")

    repositories {
        mavenCentral()
    }

    spotless {
        kotlinGradle {
            ktlint()
        }
    }
}