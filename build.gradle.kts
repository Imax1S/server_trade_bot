import io.ktor.plugin.features.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "1.8.20"
    id("io.ktor.plugin") version "2.3.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.0"
}

group = "com.ioline"
version = "0.0.1"
application {
    mainClass.set("com.ioline.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.suppressWarnings = true
    kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("ru.tinkoff.piapi:java-sdk-core:1.3")
    implementation("io.ktor:ktor-network-tls-certificates:$ktor_version")

    //json
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("com.google.protobuf:protobuf-java-util:3.0.0-beta-2")


    implementation("org.roboquant:roboquant-ibkr:1.4.0")
    implementation("org.roboquant:roboquant-ta:1.4.0")
//    implementation("org.slf4j:slf4j-log4j12:1.7.29")


    //log
//    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.slf4j:slf4j-api:2.0.7")
    implementation("org.slf4j:slf4j-simple:2.0.7")

    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
//    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}