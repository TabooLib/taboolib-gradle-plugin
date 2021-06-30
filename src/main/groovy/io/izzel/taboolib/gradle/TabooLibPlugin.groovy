package io.izzel.taboolib.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar

class TabooLibPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.repositories.maven { url project.uri("https://repo2s.ptms.ink/repository/maven-releases/") }
        def tabooExt = project.extensions.create('taboolib', TabooLibExtension)
        def taboo = project.configurations.maybeCreate('taboo')
        def tabooTask = project.tasks.create('tabooRelocateJar', RelocateJar)

        project.afterEvaluate {
            project.configurations.compileClasspath.extendsFrom(taboo)
            // subprojects
            tabooExt.modules.each {
                project.configurations.taboo.dependencies.add(project.dependencies.create("io.izzel:taboolib:${tabooExt.version}:${it}"))
            }
            project.tasks.jar.finalizedBy(tabooTask)
            project.tasks.jar.configure { Jar task ->
                task.from(taboo.collect { it.isDirectory() ? it : project.zipTree(it) })
            }
            def jarTask = project.tasks.jar as Jar
            tabooTask.configure { RelocateJar task ->
                task.tabooExt = tabooExt
                task.project = project
                task.inJar = task.inJar ?: jarTask.archivePath
                task.relocations = tabooExt.relocation
                task.classifier = tabooExt.classifier
                task.relocations['taboolib'] = project.group.toString() + '.taboolib'
            }
        }
    }
}
