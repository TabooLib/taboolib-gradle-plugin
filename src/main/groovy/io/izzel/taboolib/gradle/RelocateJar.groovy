package io.izzel.taboolib.gradle

import groovy.transform.ToString
import io.izzel.taboolib.gradle.description.Platforms
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.commons.ClassRemapper

import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.stream.Collectors
import java.util.zip.ZipException

@ToString
class RelocateJar extends DefaultTask {

    @InputFile
    File inJar

    @Input
    Map<String, String> relocations

    @Optional
    @Input
    String classifier

    @Input
    Project project

    @Input
    TabooLibExtension tabooExt

    @TaskAction
    def relocate() {
        // 缓
        def optimize = []
        def isolated = new TreeMap<String, List<String>>()
        def methodVisits = new TreeMap<String, Set<MethodVisit>>()

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
                jarFile.entries().each { JarEntry jarEntry ->
                    def path = jarEntry.name
                    // 忽略用户定义的文件
                    if (tabooExt.exclude.stream().any { String e -> path.startsWith(e) }) {
                        return
                    }
                    def options = tabooExt.options
                    // 忽略模块文件
                    if (path.endsWith(".kotlin_module") && !options.contains("keep-kotlin-module")) {
                        return
                    }
                    // 忽略优化指示文件
                    if (path.startsWith("META-INF/tf") && path.endsWith(".json") && !options.contains("skip-minimize")) {
                        optimize.add(Bridge.newOptimizeFileReader(project, jarFile.getInputStream(jarEntry)))
                        return
                    }
                    // 忽略 Kotlin 依赖
                    if (path == "taboolib/common/env/KotlinEnv.class" && (options.contains("skip-kotlin") || options.contains("skip-kotlin-relocate"))) {
                        return
                    }
                    if (path == "taboolib/common/env/KotlinEnvNoRelocate.class" && !options.contains("skip-kotlin-relocate")) {
                        return
                    }
                    // 忽略启用隔离类加载器
                    if (path == "taboolib/common/SkipIsolatedClassLoader.class" && options.contains("enable-isolated-classloader")) {
                        return
                    }
                    // 忽略依赖加载部分代码
                    if (path.startsWith("taboolib/common/env") && options.contains("skip-env")) {
                        return
                    }
                    // 忽略 common 模块打包的第三方库
                    if (path.startsWith("taboolib/library/asm") || path.startsWith("taboolib/library/jarrelocator")) {
                        if (options.contains("skip-env") || options.contains("skip-env-relocate")) {
                            return
                        }
                    }
                    jarFile.getInputStream(jarEntry).withCloseable {
                        if (path.endsWith(".class")) {
                            def reader = new ClassReader(it)
                            def writer = new ClassWriter(0)
                            def visitor = new TabooLibClassVisitor(writer, project, tabooExt)
                            def rem = new ClassRemapper(visitor, remapper)
                            remapper.remapper = rem
                            reader.accept(rem, 0)
                            // 提取孤立类
                            isolated.putAll(visitor.isolated)
                            // 提取方法访问记录
                            methodVisits.put(relocate(project, jarEntry.name, tabooExt), visitor.methodVisits)
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
            }
        }

        // 数据整理
        // 类 -> 谁正在使用
        def use = new TreeMap<String, Set<String>>()
        remapper.use.each {
            it.value.each { e ->
                def key = relocate(project, getNameWithOutExtension(e), tabooExt)
                def value = relocate(project, getNameWithOutExtension(it.key), tabooExt)
                use.computeIfAbsent(key) { new HashSet() }.add(value)
            }
        }
        def visits = methodVisits[methodVisits.keySet().first()]
        if (visits != null) {
            visits.each {
                println(it)
            }
        }
        def transfer = new TreeMap()
        isolated.each {
            transfer[relocate(project, it.key, tabooExt)] = it.value.stream().map { i -> relocate(project, i, tabooExt) }.collect(Collectors.toList())
        }
        isolated = transfer

        // 第二次工作
        def tempOut2 = File.createTempFile(name, ".jar")
        new JarOutputStream(new FileOutputStream(tempOut2)).withCloseable { out ->
            int n
            def buf = new byte[32768]
            def del = new HashSet()
            def exclude = new HashSet()
            def options = tabooExt.options
            new JarFile(tempOut1).withCloseable { jarFile ->
                jarFile.entries().each { JarEntry jarEntry ->
                    if (optimize.any { it.exclude(jarEntry.name, use) }) {
                        return
                    }
                    jarFile.getInputStream(jarEntry).withCloseable {
                        if (jarEntry.name.endsWith(".class")) {
                            def nameWithOutExtension = getNameWithOutExtension(jarEntry.name)
                            if (use.containsKey(nameWithOutExtension.toString()) && !exclude.contains(nameWithOutExtension)) {
                                exclude.add(nameWithOutExtension)
                                if (!options.contains("skip-minimize") && isIsolated(use, use[nameWithOutExtension], isolated, nameWithOutExtension)) {
                                    del.add(nameWithOutExtension)
                                }
                            }
                        }
                        if (!del.contains(getNameWithOutExtension(jarEntry.name))) {
                            out.putNextEntry(new JarEntry(jarEntry.name))
                            while ((n = it.read(buf)) != -1) {
                                out.write(buf, 0, n)
                            }
                        }
                    }
                }
                if (!tabooExt.options.contains("skip-plugin-file")) {
                    Platforms.values().each {
                        if (tabooExt.modules.contains(it.module)) {
                            try {
                                out.putNextEntry(new JarEntry(it.file))
                                out.write(it.builder.build(tabooExt.des, project))
                            } catch (ZipException ignored) {
                                // ignore
                            }
                        }
                    }
                }
                null
            }
        }
        Files.copy(tempOut2.toPath(), outJar.toPath(), StandardCopyOption.REPLACE_EXISTING)
    }

    static String getNameWithOutExtension(name) {
        if (name.contains('$')) {
            return name.substring(0, name.indexOf('$')).replace('.', '/')
        } else if (name.contains('.')) {
            return name.substring(0, name.lastIndexOf('.')).replace('.', '/')
        } else {
            return name.replace('.', '/')
        }
    }

    static String relocate(Project project, String name, TabooLibExtension tabooExt) {
        if (name.startsWith("taboolib") && !tabooExt.options.contains("skip-taboolib-relocate")) {
            def root = tabooExt.rootPackage ?: project.group.toString()
            return root.replace('.', '/') + '/' + name.replace('.', '/')
        } else {
            return name.replace('.', '/')
        }
    }

    static boolean isIsolated(
            Map<String, Set<String>> use,
            Set<String> refer,
            Map<String, List<String>> isolated,
            String name,
            String exclude = null
    ) {
        if (isolated.containsKey(name)) {
            return refer.size() <= 1 || refer.stream()
                    .filter { it != exclude }
                    .allMatch {
                        name == it || isolated[name].contains(it) || isIsolated(use, use[it], isolated, it, name)
                    }
        } else {
            return false
        }
    }


    @Override
    String toString() {
        return "RelocateJar{}";
    }
}
