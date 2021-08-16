import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.springframework.boot")
    id("com.palantir.docker")

    kotlin("plugin.spring")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")

    // Spring boot starter kit, update version by updating org.springframework.boot plugin version
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-web-services")
    // Logging
    implementation("io.github.microutils:kotlin-logging")
    implementation("com.google.code.gson:gson")
    // Jackson
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}
