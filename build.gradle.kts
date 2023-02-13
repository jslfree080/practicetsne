import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.10"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("org.jetbrains.kotlinx:dataframe:0.9.1") // dependencies for dataframe library

    implementation("com.github.haifengl:smile-core:2.6.0")
    implementation("com.github.haifengl:smile-kotlin:2.6.0")
    implementation("com.github.haifengl:smile-math:2.6.0") // dependencies for smile library

    implementation("org.slf4j:slf4j-simple:2.0.6")

    implementation("org.jetbrains.lets-plot:lets-plot-batik:3.0.0")
    implementation("org.jetbrains.lets-plot:lets-plot-jfx:3.0.0")
    implementation("org.jetbrains.lets-plot:lets-plot-common:3.0.0")
    implementation("org.jetbrains.lets-plot:lets-plot-kotlin-jvm:4.2.0")
    implementation("org.jetbrains.lets-plot:lets-plot-image-export:3.0.0") // dependencies for lets-plot library
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("DigitsKt")
}