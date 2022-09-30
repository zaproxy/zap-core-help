plugins {
    `java-library`
}

description = "Common files (e.g. images, map.jhm) used by all add-ons."

java {
    val javaVersion = JavaVersion.VERSION_11
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
}
