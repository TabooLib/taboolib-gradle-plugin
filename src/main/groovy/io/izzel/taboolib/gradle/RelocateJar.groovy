package io.izzel.taboolib.gradle

import groovy.transform.ToString
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

    @TaskAction
    def relocate() {
        def mapping = relocations.collectEntries { [(it.key.replace('.', '/')), it.value.replace('.', '/')] }
        def remapper = new RelocateRemapper(relocations, mapping as Map<String, String>)
        def index = inJar.name.lastIndexOf('.')
        def name = inJar.name.substring(0, index) + (classifier == null ? "" : "-" + classifier) + inJar.name.substring(index)
        def outJar = new File(inJar.getParentFile(), name)
        def tmpOut = File.createTempFile(name, ".jar")
        new JarOutputStream(new FileOutputStream(tmpOut)).withCloseable { out ->
            project.logger.info(outJar.getAbsolutePath())
            int n
            def buf = new byte[32768]
            new JarFile(inJar).withCloseable {jarFile ->
                for (def jarEntry : jarFile.entries()) {
                    jarFile.getInputStream(jarEntry).withCloseable {
                        if (jarEntry.name.endsWith(".class")) {
                            project.logger.info("Relocating " + jarEntry.name)
                            def reader = new ClassReader(it)
                            def writer = new ClassWriter(0)
                            def visitor = new TabooLibClassVisitor(writer, project)
                            reader.accept(new ClassRemapper(visitor, remapper), 0)
                            out.putNextEntry(new JarEntry(remapper.map(jarEntry.name)))
                            out.write(writer.toByteArray())
                        } else {
                            out.putNextEntry(new JarEntry(remapper.map(jarEntry.name)))
                            while ((n = it.read(buf)) != -1) {
                                out.write(buf, 0, n)
                            }
                        }
                    }
                }
            }
        }
        Files.copy(tmpOut.toPath(), outJar.toPath(), StandardCopyOption.REPLACE_EXISTING)
    }
}
