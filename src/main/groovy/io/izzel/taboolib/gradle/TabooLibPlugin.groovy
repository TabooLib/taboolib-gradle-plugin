package io.izzel.taboolib.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar

class TabooLibPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.repositories.maven { url "http://repo.ptms.ink/repository/codemc-nms/" }
        project.repositories.maven { url "http://repo.ptms.ink/repository/maven-releases/" }
        def tabooExt = project.extensions.create('taboolib', TabooLibExtension)
        def taboo = project.configurations.maybeCreate('taboo')
        project.configurations.compileClasspath.extendsFrom(taboo)
        def tabooTask = project.tasks.create('tabooRelocateJar', RelocateJar)

        project.afterEvaluate {
            println(project.group.toString())
            project.configurations.compileClasspath.dependencies.add(project.dependencies.create("io.izzel.taboolib:TabooLib:${tabooExt.tabooLibVersion}:all"))
            taboo.dependencies.add(project.dependencies.create("io.izzel.taboolib:TabooLibLoader:${tabooExt.loaderVersion}:all"))

            def shadowPresent = project.plugins.hasPlugin('com.github.johnrengelman.shadow')
            if (!shadowPresent) {
                project.tasks.jar.finalizedBy(tabooTask)
                project.tasks.jar.configure { Jar task ->
                    task.from(taboo.collect { it.isDirectory() ? it : project.zipTree(it) })
                }
                def jarTask = project.tasks.jar as Jar
                tabooTask.configure { RelocateJar task ->
                    task.inJar = task.inJar ?: jarTask.archivePath
                    task.relocations = tabooExt.relocation
                    task.classifier = tabooExt.classifier
                    task.relocations['io.izzel.taboolib.loader'] = task.relocations['io.izzel.taboolib.loader'] ?: project.group.toString() + '.boot'
                    task.relocations['org.bstats.bukkit'] = task.relocations['org.bstats.bukkit'] ?: project.group.toString() + '.metrics'
                }
            } else {
                def shadowJar = project.tasks.getByName('shadowJar')
                if (shadowJar) {
                    shadowJar.configurations.add(taboo)
                }
            }
        }
    }
}
