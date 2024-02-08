package io.izzel.taboolib.gradle

import groovy.transform.ToString
import io.izzel.taboolib.gradle.description.Builder
import io.izzel.taboolib.gradle.description.Platforms
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapperKt
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.commons.ClassRemapper

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipException

@ToString
class TabooLibMainTask extends DefaultTask {

    @InputFile
    File inJar

    @Input
    Map<String, String> relocations

    @Optional
    @Input
    String classifier

    @Input
    boolean api;

    @Input
    Project project

    @Input
    TabooLibExtension tabooExt

    @TaskAction
    def relocate() {
        if (tabooExt.subproject) {
            return
        }
        // 配置
        def mapping = relocations.collectEntries { [(it.key.replace('.', '/')), it.value.replace('.', '/')] }
        def remapper = new RelocateRemapper(relocations, mapping as Map<String, String>)

        // 文件
        def index = inJar.name.lastIndexOf('.')
        def name = inJar.name.substring(0, index) + (classifier == null ? "" : "-" + classifier) + inJar.name.substring(index)
        def outJar = new File(inJar.getParentFile(), name)
        def tempOut1 = File.createTempFile(name, ".jar")

        // 第一次工作
        new JarOutputStream(new FileOutputStream(tempOut1)).withCloseable { out ->
            int n
            def buf = new byte[32768]
            new JarFile(inJar).withCloseable { jarFile ->
                // region 重定向
                jarFile.entries().each { JarEntry jarEntry ->
                    def path = jarEntry.name
                    // 忽略用户定义的文件
                    if (tabooExt.exclude.stream().any { String e -> path.startsWith(e) }) {
                        return
                    }
                    jarFile.getInputStream(jarEntry).withCloseable {
                        if (path.endsWith(".class")) {
                            def reader = new ClassReader(it)
                            def writer = new ClassWriter(0)
                            def visitor = new TabooLibClassVisitor(writer, project, tabooExt, api)
                            def rem = new ClassRemapper(visitor, remapper)
                            remapper.remapper = rem
                            reader.accept(rem, 0)
                            // 写回文件
                            // 拦截报错防止文件名称重复导致编译终止
                            try {
                                out.putNextEntry(new JarEntry(remapper.map(path)))
                            } catch (ZipException zipException) {
                                println(zipException)
                                return true
                            }
                            out.write(writer.toByteArray())
                        } else {
                            try {
                                out.putNextEntry(new JarEntry(remapper.map(path)))
                            } catch (ZipException ex) {
                                println(ex)
                                return true
                            }
                            while ((n = it.read(buf)) != -1) {
                                out.write(buf, 0, n)
                            }
                        }
                        null
                    }
                }
                // endregion
                // 描述文件
                try {
                    out.putNextEntry(new JarEntry("META-INF/taboolib/env.properties"))
                    out.write(buildEnv())
                    out.putNextEntry(new JarEntry("META-INF/taboolib/version.properties"))
                    out.write(buildVersion())
                } catch (ZipException ignored) {
                }
                // 插件文件
                Platforms.values().each {
                    if (tabooExt.env.modules.contains(it.module)) {
                        try {
                            out.putNextEntry(new JarEntry(it.file))
                            out.write(it.builder.build(tabooExt.des, project, tabooExt))
                        } catch (ZipException ignored) {
                        }
                    }
                }
                null
            }
        }
        // api mode
        Files.copy(tempOut1.toPath(), outJar.toPath(), StandardCopyOption.REPLACE_EXISTING)
    }

    byte[] buildEnv() {
        def modules = new HashSet<String>(tabooExt.env.modules)
        // 平台实现
        Platforms.values().each { p ->
            if (p.module in modules) {
                modules += p.module + "-impl"
            }
        }
        modules.removeIf { i -> i.startsWith("common") }
        def file = Builder.startBukkitFile()
        file.addAll([
                "debug=" + tabooExt.env.debug,
                "force-download-in-dev=" + tabooExt.env.forceDownloadInDev,
                "repo-central=" + tabooExt.env.repoCentral,
                "repo-taboolib=" + tabooExt.env.repoTabooLib,
                "file-libs=" + tabooExt.env.fileLibs,
                "file-assets=" + tabooExt.env.fileAssets,
                "enable-isolated-classloader=" + tabooExt.env.enableIsolatedClassloader,
                "module=" + modules.join(',')
        ])
        return file.join('\n').getBytes(StandardCharsets.UTF_8)
    }

    byte[] buildVersion() {
        def kotlinVersion = KotlinPluginWrapperKt.getKotlinPluginVersion(project)
        def file = Builder.startBukkitFile()
        file.addAll([
                "kotlin=" + (tabooExt.version.skipKotlin ? "null" : kotlinVersion),
                "kotlin-coroutines=" + tabooExt.version.coroutines,
                "taboolib=" + tabooExt.version.taboolib,
                "skip-kotlin-relocate=" + tabooExt.version.skipKotlinRelocate,
                "skip-taboolib-relocate=" + tabooExt.version.skipTabooLibRelocate
        ])
        return file.join('\n').getBytes(StandardCharsets.UTF_8)
    }

    @Override
    String toString() {
        return "TabooLibMainTask{}";
    }
}
