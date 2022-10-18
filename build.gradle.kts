import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.20"
}

group = "top.e404"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test", "1.7.20"))
}

tasks.test {
    useJUnitPlatform()
    workingDir = File("run")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}