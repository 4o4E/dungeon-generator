import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.7.20"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "top.e404"
version = "1.0.0"
val epluginVersion = "1.0.5"

fun kotlinx(id: String, version: String) = "org.jetbrains.kotlinx:kotlinx-$id:$version"
fun eplugin(id: String, version: String = epluginVersion) = "top.e404:eplugin-$id:$version"

repositories {
    mavenLocal()
    // spigot
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/groups/public/")
    // engine hub
    maven("https://maven.enginehub.org/repo/")
    // lumine
    maven("https://mvn.lumine.io/repository/maven/")
    maven("https://mvn.lumine.io/repository/maven-public/")
    // jitpack
    maven("https://jitpack.io")
    mavenCentral()
}

dependencies {
    // spigot
    compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")
    // serialization
    implementation(kotlinx("serialization-core-jvm", "1.3.3"))
    implementation(kotlinx("serialization-json", "1.3.3"))
    // path
    implementation(project(":path"))
    // eplugin
    implementation(eplugin("core"))
    implementation(eplugin("serialization"))
    implementation(eplugin("hook-worldedit"))
    implementation(eplugin("hook-mmoitems"))
    implementation(eplugin("hook-mythicmobs"))
    implementation(eplugin("hook-itemsadder"))
    // world edit
    compileOnly("com.sk89q.worldedit:worldedit-core:7.2.7")
    compileOnly("com.sk89q.worldedit:worldedit-bukkit:7.2.7")
    // mi
    compileOnly("net.Indyuce:MMOItems:6.7.3")
    // mythic lib
    compileOnly("io.lumine:MythicLib-dist:1.3.1")
    // mm
    compileOnly("io.lumine:Mythic-Dist:5.1.4")
    // itemsadder
    compileOnly("com.github.LoneDev6:api-itemsadder:3.0.0")
    // test
    testImplementation(kotlin("test", "1.7.20"))
}

tasks {
    withType<KotlinCompile>() {
        kotlinOptions.jvmTarget = "1.8"
    }

    processResources {
        filesMatching("plugin.yml") {
            expand(project.properties)
        }
    }

    shadowJar {
        archiveFileName.set("DungeonGenerator.jar")
        exclude("META-INF/*")
        relocate("kotlin", "top.e404.dungeon_generator.relocate.kotlin")
        relocate("top.e404.eplugin", "top.e404.dungeon_generator.relocate.eplugin")

        doFirst {
            project.projectDir
                .resolve("jar")
                .listFiles()
                ?.forEach { it.deleteRecursively() }
        }

        doLast {
            project.projectDir.resolve("jar").mkdirs()
            for (file in project.projectDir.resolve("build/libs").listFiles() ?: arrayOf()) {
                println("正在复制`${file.name}`")
                file.copyTo(project.projectDir.resolve("jar/${file.name}"), true)
            }
        }
    }
}