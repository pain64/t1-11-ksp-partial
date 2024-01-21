plugins {
    kotlin("jvm")
}

// Versions are declared in 'gradle.properties' file
val kspVersion: String by project

dependencies {
    implementation(project(":annotations"))
    // implementation("org.postgresql:postgresql:42.7.1")
    implementation("com.google.devtools.ksp:symbol-processing-api:$kspVersion")
}