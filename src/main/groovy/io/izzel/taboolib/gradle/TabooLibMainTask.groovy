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
import java.util.concurrent.Executors
import java.util.concurrent.Future
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
        def totalStart = System.currentTimeMillis()
        // 配置
        def mapping = relocations.collectEntries { [(it.key.replace('.', '/')), it.value.replace('.', '/')] }
        def remapper = new RelocateRemapper(relocations, mapping as Map<String, String>)

        // 文件
        def index = inJar.name.lastIndexOf('.')
        def name = inJar.name.substring(0, index) + (classifier == null ? "" : "-" + classifier) + inJar.name.substring(index)
        def outJar = new File(inJar.getParentFile(), name)
        def tempOut1 = File.createTempFile(name, ".jar")

        // 并行处理 class 文件
        def nThreads = Runtime.getRuntime().availableProcessors()
        def pool = Executors.newFixedThreadPool(nThreads)
        def classCount = 0
        def resourceCount = 0
        try {
            // 收集处理结果，保持顺序
            def results = Collections.synchronizedList(new ArrayList<>())
            def readStart = System.currentTimeMillis()
            new JarFile(inJar).withCloseable { jarFile ->
                def entries = Collections.list(jarFile.entries())
                def futures = new ArrayList<Future>()
                entries.each { JarEntry jarEntry ->
                    def path = jarEntry.name
                    // 忽略用户定义的文件
                    if (tabooExt.exclude.stream().any { String e -> path.startsWith(e) }) {
                        return
                    }
                    if (path.endsWith(".class")) {
                        classCount++
                        // 预读 class 字节，避免线程间共享 JarFile InputStream
                        def bytes = jarFile.getInputStream(jarEntry).withCloseable { it.bytes }
                        futures.add(pool.submit {
                            // 每个线程创建独立的 remapper 和 visitor
                            def threadRemapper = new RelocateRemapper(relocations, mapping as Map<String, String>)
                            def reader = new ClassReader(bytes)
                            def writer = new ClassWriter(0)
                            def visitor = new TabooLibClassVisitor(writer, project, tabooExt, api)
                            def rem = new ClassRemapper(visitor, threadRemapper)
                            threadRemapper.remapper = rem
                            reader.accept(rem, 0)
                            synchronized (results) {
                                results.add([name: threadRemapper.map(path), data: writer.toByteArray()])
                            }
                        })
                    } else {
                        resourceCount++
                        // 非 class 文件直接读取
                        def bytes = jarFile.getInputStream(jarEntry).withCloseable { it.bytes }
                        results.add([name: remapper.map(path), data: bytes])
                    }
                }
                // 等待所有 class 处理完成
                futures.each { it.get() }
            }
            def asmTime = System.currentTimeMillis() - readStart
            // 串行写入 jar
            def writeStart = System.currentTimeMillis()
            new JarOutputStream(new FileOutputStream(tempOut1)).withCloseable { out ->
                def written = new HashSet<String>()
                results.each { entry ->
                    if (written.add(entry.name)) {
                        try {
                            out.putNextEntry(new JarEntry(entry.name as String))
                            out.write(entry.data as byte[])
                        } catch (ZipException ex) {
                            println(ex)
                        }
                    }
                }
                // 描述文件
                if (!tabooExt.version.skipVersionFile) {
                    try {
                        out.putNextEntry(new JarEntry("META-INF/taboolib/env.properties"))
                        out.write(buildEnv())
                        out.putNextEntry(new JarEntry("META-INF/taboolib/version.properties"))
                        out.write(buildVersion())
                    } catch (ZipException ignored) {
                    }
                }
                // 插件文件
                if (!tabooExt.version.skipPlatformFile) {
                    Platforms.values().each {
                        if (tabooExt.env.modules.contains(it.module)) {
                            try {
                                out.putNextEntry(new JarEntry(it.file))
                                out.write(it.builder.build(tabooExt.des, project, tabooExt))
                            } catch (ZipException ignored) {
                            }
                        }
                    }
                }
            }
            def writeTime = System.currentTimeMillis() - writeStart
            def totalTime = System.currentTimeMillis() - totalStart
            println("[taboolibMainTask] ${classCount} classes, ${resourceCount} resources | ASM: ${asmTime}ms (${nThreads} threads), Write: ${writeTime}ms, Total: ${totalTime}ms")
        } finally {
            pool.shutdown()
        }
        Files.copy(tempOut1.toPath(), outJar.toPath(), StandardCopyOption.REPLACE_EXISTING)
    }

    byte[] buildEnv() {
        def modules = new HashSet<String>(tabooExt.env.modules)
        // 平台实现
        Platforms.values().each { p ->
            if (p.module in modules && p.hasImpl) {
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
                "disable-on-skipped-version=" + tabooExt.env.disableOnSkippedVersion,
                "disable-on-unsupported-version=" + tabooExt.env.disableOnUnsupportedVersion,
                "disable-when-primitive-loader-error=" + tabooExt.env.disableWhenPrimitiveLoaderError,
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
