plugins {
    id("com.diffplug.spotless") version "5.12.1"
}

allprojects {
    apply(plugin = "com.diffplug.spotless")

    repositories {
        mavenCentral()
        maven {
            url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
        }
    }

    spotless {
        kotlinGradle {
            ktlint()
        }
    }
}
