import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun properties(key: String) = project.findProperty(key).toString()

plugins {
    val kotlinVersion = "1.5.21"
    // Spring
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("org.springframework.boot") version "2.3.0.RELEASE" apply false
    // Docker
    id("com.palantir.docker") version "0.28.0" apply false
    // Java support
    id("java")
    // Kotlin support
    id("org.jetbrains.kotlin.jvm") version kotlinVersion
    // gradle-intellij-plugin - read more: https://github.com/JetBrains/gradle-intellij-plugin
    id("org.jetbrains.intellij") version "1.1.3"
    // gradle-changelog-plugin - read more: https://github.com/JetBrains/gradle-changelog-plugin
    id("org.jetbrains.changelog") version "1.1.2"

    kotlin("plugin.spring") version kotlinVersion
}

group = properties("pluginGroup")
version = properties("pluginVersion")

allprojects {
    group = group
}

subprojects {
    apply(plugin = "base")
    apply(plugin = "io.spring.dependency-management")

    // Configure project's dependencies
    repositories {
        mavenCentral()
        jcenter()
    }

    dependencyManagement {
        dependencies {
            dependency("io.github.microutils:kotlin-logging-jvm:2.0.11")
            dependency("com.squareup.okhttp3:okhttp:4.9.1")
            dependency("com.google.code.gson:gson:2.8.8")
            dependency("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.5")
            dependency("org.apache.lucene:lucene-analyzers-common:6.4.1")

            //test
            dependency("org.junit.jupiter:junit-jupiter-api:5.3.1")
            dependency("org.junit.jupiter:junit-jupiter-engine:5.3.1")
            dependency("org.mockito:mockito-core:3.12.4")
        }
    }
}

tasks {
    // Set the compatibility versions to 1.8
    withType<JavaCompile> {
        sourceCompatibility = "1.8"
        targetCompatibility = "1.8"
    }
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
}
