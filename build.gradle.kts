import org.gradle.api.tasks.compile.GroovyCompile
import org.gradle.api.tasks.compile.JavaCompile
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("groovy")
    `maven-publish`
    id("java-gradle-plugin")
    id("com.gradle.plugin-publish") version "2.0.0"
    kotlin("jvm") version "1.9.24" // Keep this for compatibility
}

group = "io.izzel.taboolib"
version = "2.0.36"

configurations {
    create("embed") {
        implementation.get().extendsFrom(this)
    }
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    compileOnly(gradleApi())
    compileOnly(localGroovy())
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.24")
    "embed"("org.ow2.asm:asm:9.7.1")
    "embed"("org.ow2.asm:asm-commons:9.7.1")
    "embed"("com.google.code.gson:gson:2.9.0")
    "embed"(kotlin("stdlib"))
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(configurations.getByName("embed").map { if (it.isDirectory) it else zipTree(it) })
}

gradlePlugin {
    website.set("https://github.com/TabooLib/taboolib-gradle-plugin")
    vcsUrl.set("https://github.com/TabooLib/taboolib-gradle-plugin")

    plugins {
        create("taboolib") {
            id = "io.izzel.taboolib"
            displayName = "TabooLib Gradle Plugin"
            description = "TabooLib Gradle Plugin"
            implementationClass = "io.izzel.taboolib.gradle.TabooLibPlugin"
            tags.set(listOf("taboolib", "bukkit", "minecraft"))
        }
    }
}

publishing {
    repositories {
        maven("/Users/sky/Desktop/repo")
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_1_8)
        freeCompilerArgs.add("-Xskip-metadata-version-check")
    }
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
