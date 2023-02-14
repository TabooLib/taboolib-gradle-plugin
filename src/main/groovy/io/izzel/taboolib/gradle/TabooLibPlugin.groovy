package io.izzel.taboolib.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.jvm.tasks.Jar
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapperKt

class TabooLibPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.repositories.maven {
            url project.uri("http://ptms.ink:8081/repository/releases/")
            allowInsecureProtocol true
        }
        project.repositories.maven {
            url project.uri("https://repo.spongepowered.org/maven")
        }
        def tabooExt = project.extensions.create('taboolib', TabooLibExtension)
        def taboo = project.configurations.maybeCreate('taboo')
        def tabooTask = project.tasks.create('tabooRelocateJar', RelocateJar)
        project.afterEvaluate {
            project.configurations.compileClasspath.extendsFrom(taboo)
            // subprojects
            tabooExt.modules.each {
                project.configurations.taboo.dependencies.add(project.dependencies.create("io.izzel.taboolib:${it}:${tabooExt.version}"))
            }
            // com.mojang:datafixerupper:4.0.26
            project.dependencies.add('compileOnly', 'com.mojang:datafixerupper:4.0.26')

            project.tasks.jar.finalizedBy(tabooTask)
            project.tasks.jar.configure { Jar task ->
                task.from(taboo.collect { it.isDirectory() ? it : project.zipTree(it) })
                task.duplicatesStrategy = DuplicatesStrategy.EXCLUDE
            }

            def kv = KotlinPluginWrapperKt.getKotlinPluginVersion(project).replaceAll("[._-]", "")
            def jarTask = project.tasks.jar as Jar
            tabooTask.configure { RelocateJar task ->
                task.tabooExt = tabooExt
                task.project = project
                task.inJar = task.inJar ?: jarTask.archiveFile.get().getAsFile()
                task.relocations = tabooExt.relocation
                task.classifier = tabooExt.classifier
                if (!tabooExt.options.contains("skip-taboolib-relocate")) {
                    task.relocations['taboolib'] = project.group.toString() + '.taboolib'
                }
                if (!tabooExt.options.contains("skip-kotlin") && !tabooExt.options.contains("skip-kotlin-relocate")) {
                    task.relocations['kotlin.'] = 'kotlin' + kv + '.'
                }
            }
        }
    }
}
