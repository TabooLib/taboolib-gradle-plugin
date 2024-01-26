package io.izzel.taboolib.gradle

import io.izzel.taboolib.gradle.description.Platforms
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.jvm.tasks.Jar
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapperKt

class TabooLibPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        // 添加仓库
        project.repositories.maven {
            url project.uri("http://ptms.ink:8081/repository/releases/")
            allowInsecureProtocol true
        }
        project.repositories.maven {
            url project.uri("https://repo.spongepowered.org/maven")
        }
        // 注册扩展
        def tabooExt = project.extensions.create('taboolib', TabooLibExtension)
        // 注册配置
        def taboo = project.configurations.maybeCreate('taboo')
        // 注册任务
        def tabooTask = project.tasks.create('taboolibMainTask', TabooLIbMainTask)
        project.afterEvaluate {
            project.configurations.compileClasspath.extendsFrom(taboo)
            // com.mojang:datafixerupper:4.0.26
            project.dependencies.add('compileOnly', 'com.mojang:datafixerupper:4.0.26')
            // org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3
            if (tabooExt.version.coroutines != null) {
                project.dependencies.add('compileOnly', 'org.jetbrains.kotlinx:kotlinx-coroutines-core:' + tabooExt.version.coroutines)
            }

            // subprojects
            tabooExt.env.modules.each {
                def dep = project.dependencies.create("io.izzel.taboolib:${it}:${tabooExt.version.taboolib}")
                if (isIncludeModule(it) && !tabooExt.subproject) {
                    project.configurations.taboo.dependencies.add(dep)
                } else {
                    project.configurations.compileOnly.dependencies.add(dep)
                }
            }

            project.tasks.jar.finalizedBy(tabooTask)
            project.tasks.jar.configure { Jar task ->
                task.from(taboo.collect { it.isDirectory() ? it : project.zipTree(it) })
                task.duplicatesStrategy = DuplicatesStrategy.EXCLUDE
            }

            def kotlinVersion = KotlinPluginWrapperKt.getKotlinPluginVersion(project).replaceAll("[._-]", "")
            def jarTask = project.tasks.jar as Jar
            tabooTask.configure { TabooLIbMainTask task ->
                task.tabooExt = tabooExt
                task.project = project
                task.inJar = task.inJar ?: jarTask.archivePath
                task.relocations = tabooExt.relocation
                task.classifier = tabooExt.classifier

                // 重定向
                if (!tabooExt.version.isSkipTabooLibRelocate()) {
                    def root = tabooExt.rootPackage ?: project.group.toString()
                    task.relocations['taboolib'] = root + '.taboolib'
                }
                if (!tabooExt.version.isSkipKotlinRelocate()) {
                    task.relocations['kotlin.'] = 'kotlin' + kotlinVersion + '.'
                }
            }
        }
    }

    static def isIncludeModule(String module) {
        return module == "common" || Platforms.values().any { p -> p.module == module }
    }
}
