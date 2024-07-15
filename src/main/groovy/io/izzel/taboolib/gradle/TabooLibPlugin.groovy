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
            url project.uri("http://sacredcraft.cn:8081/repository/releases/")
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
        def tabooTask = project.tasks.maybeCreate('taboolibMainTask', TabooLibMainTask)
        tabooTask.group = "taboolib"
        // 注册任务 - 刷新依赖
        project.tasks.maybeCreate('taboolibRefreshDependencies')
        project.tasks.taboolibRefreshDependencies.group = "taboolib"
        project.tasks.taboolibRefreshDependencies.doLast {
            def taboolibFile = new File("../../caches/modules-2/files-2.1/io.izzel.taboolib").canonicalFile
            taboolibFile.listFiles()?.each { module ->
                def file = new File(taboolibFile, "${module.name}/${tabooExt.version.taboolib}")
                if (file.exists()) {
                    file.deleteDir()
                    System.out.println("Delete $file")
                }
            }
        }
        // 注册任务 - 构建 API 版本
        project.tasks.maybeCreate('taboolibBuildApi')
        project.tasks.taboolibBuildApi.group = "taboolib"

        // 添加依赖以及重定向配置
        project.afterEvaluate {
            def api = false
            try {
                project.tasks.taboolibBuildApi.dependsOn(project.tasks.build)
                api = project.gradle.startParameter.taskRequests.args[0][0].toString() == "taboolibBuildApi"
            } catch (Throwable ignored) {
            }

            // 继承 "taboo" 配置
            project.configurations.implementation.extendsFrom(taboo)

            // com.mojang:datafixerupper:4.0.26
            project.dependencies.add('implementation', 'com.mojang:datafixerupper:4.0.26')
            // org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3
            if (tabooExt.version.coroutines != null) {
                project.dependencies.add('implementation', 'org.jetbrains.kotlinx:kotlinx-coroutines-core:' + tabooExt.version.coroutines)
            }

            // subprojects
            tabooExt.env.modules.each {
                def dep = project.dependencies.create("io.izzel.taboolib:${it}:${tabooExt.version.taboolib}")
                if (api || isIncludeModule(it) && !tabooExt.subproject) {
                    project.configurations.taboo.dependencies.add(dep)
                } else {
                    project.configurations.implementation.dependencies.add(dep)
                }
            }

            project.tasks.jar.finalizedBy(tabooTask)
            project.tasks.jar.configure { Jar task ->
                task.from(taboo.collect {
                    if (it.isDirectory()) {
                        it
                    } else if (it.name.endsWith(".jar")) {
                        project.zipTree(it)
                    } else {
                        project.files(it)
                    }
                })
                task.duplicatesStrategy = DuplicatesStrategy.EXCLUDE
                if (api) {
                    task.getArchiveClassifier().set("api")
                }
            }

            def kotlinVersion = KotlinPluginWrapperKt.getKotlinPluginVersion(project).replaceAll("[._-]", "")

            def jarTask = project.tasks.jar as Jar
            tabooTask.configure { TabooLibMainTask task ->
                task.tabooExt = tabooExt
                task.project = project
                task.inJar = task.inJar ?: jarTask.archivePath
                task.relocations = tabooExt.relocation
                task.classifier = tabooExt.classifier
                task.api = api

                // 重定向
                if (!tabooExt.version.isSkipTabooLibRelocate()) {
                    def root = tabooExt.rootPackage ?: project.group.toString()
                    task.relocations['taboolib'] = root + '.taboolib'
                }
                if (!tabooExt.version.isSkipKotlinRelocate()) {
                    task.relocations['kotlin.'] = 'kotlin' + kotlinVersion + '.'
                    if (tabooExt.version.coroutines != null) {
                        def coroutinesVersion = tabooExt.version.coroutines.replaceAll("[._-]", "")
                        task.relocations['kotlinx.coroutines.'] = 'kotlin' + kotlinVersion + 'x.coroutines' + coroutinesVersion + '.'
                    }
                }
            }
        }
    }

    static def isIncludeModule(String module) {
        return module == "common" || module == "platform-application" || Platforms.values().any { p -> p.module == module }
    }
}
