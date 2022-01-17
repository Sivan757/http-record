val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project
val version: String by project

plugins {
    application
    kotlin("jvm") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "cn.kapukapu"
application {
    mainClass.set("cn.kapukapu.ApplicationKt")
}

repositories {
    maven(url = "https://maven.aliyun.com/repository/public/")
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-host-common:$ktorVersion")
    implementation("io.ktor:ktor-server-cio:$ktorVersion")
    implementation("io.ktor:ktor-jackson:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.ktor:ktor-html-builder:$ktorVersion")

    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
}

tasks {
    shadowJar {
        manifest {
            attributes(Pair("Main-Class", "cn.kapukapu.ApplicationKt"))
        }
    }
}
