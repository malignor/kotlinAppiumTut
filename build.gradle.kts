import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.21"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.mikemybytes:junit5-formatted-source-parent:0.2.0")
    implementation("com.mikemybytes:junit5-formatted-source-tests:0.2.0")
    implementation("com.mikemybytes:junit5-formatted-source:0.2.0")
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    testImplementation("io.appium:java-client:7.6.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.1.0")
}

tasks.test {
    useJUnitPlatform()
    reports {
        junitXml.outputLocation.set(layout.buildDirectory.dir("test-junit-xml"))
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}