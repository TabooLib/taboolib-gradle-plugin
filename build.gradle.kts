import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `maven-publish`
    id("groovy")
    id("maven-publish")
    id("java-gradle-plugin")
    id("com.gradle.plugin-publish") version "1.3.1"
    kotlin("jvm") version "2.2.0"
}

group = "io.izzel.taboolib"
version = "2.0.27"

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
    compileOnly("org.codehaus.groovy:groovy:3.0.25")
    compileOnly(gradleApi())
    compileOnly(localGroovy())
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:2.2.0")
    "embed"("org.ow2.asm:asm:9.8")
    "embed"("org.ow2.asm:asm-commons:9.8")
    "embed"("com.google.code.gson:gson:2.13.1")
    "embed"(kotlin("stdlib"))
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(configurations.getByName("embed").map { if (it.isDirectory) it else zipTree(it) })
}

gradlePlugin {
    website = "https://github.com/TabooLib/taboolib-gradle-plugin"
    vcsUrl = "https://github.com/TabooLib/taboolib-gradle-plugin"

    plugins {
        create("taboolib") {
            id = "io.izzel.taboolib"
            displayName = "TabooLib Gradle Plugin"
            description = "TabooLib Gradle Plugin"
            implementationClass = "io.izzel.taboolib.gradle.TabooLibPlugin"
            tags = listOf("taboolib", "bukkit", "minecraft")
        }
    }
}

publishing {
    repositories {
        maven("/Users/sky/Desktop/repo")
    }
}

tasks.compileJava {
    targetCompatibility = "1.8"
}

tasks.compileKotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_1_8
    }
}

tasks.compileTestKotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_1_8
    }
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
