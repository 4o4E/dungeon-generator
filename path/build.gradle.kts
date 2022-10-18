plugins {
    kotlin("jvm")
}

group = "top.e404"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test", "1.7.20"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
    workingDir = File("run")
}