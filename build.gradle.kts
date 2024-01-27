plugins {
    `maven-publish`
    id("groovy")
    id("maven-publish")
    id("java-gradle-plugin")
    id("com.gradle.plugin-publish") version "0.12.0"
    kotlin("jvm") version "1.7.10"
}

group = "io.izzel.taboolib"
version = "2.0.2"

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
    compileOnly("org.codehaus.groovy:groovy:3.0.11")
    compileOnly(gradleApi())
    compileOnly(localGroovy())
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
    "embed"("org.ow2.asm:asm:9.3")
    "embed"("org.ow2.asm:asm-commons:9.3")
    "embed"("com.google.code.gson:gson:2.9.0")
    "embed"(kotlin("stdlib"))
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(configurations.getByName("embed").map { if (it.isDirectory) it else zipTree(it) })
}

pluginBundle {
    website = "https://github.com/TabooLib/taboolib-gradle-plugin"
    vcsUrl = "https://github.com/TabooLib/taboolib-gradle-plugin"
    tags = listOf("taboolib", "bukkit", "minecraft")
}

gradlePlugin {
    plugins {
        create("taboolib") {
            id = "io.izzel.taboolib"
            displayName = "TabooLib Gradle Plugin"
            description = "TabooLib Gradle Plugin"
            implementationClass = "io.izzel.taboolib.gradle.TabooLibPlugin"
        }
    }
}

publishing {
    repositories {
        maven("/Users/sky/Desktop/repo")
    }
}

tasks.compileKotlin {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

tasks.compileTestKotlin {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
