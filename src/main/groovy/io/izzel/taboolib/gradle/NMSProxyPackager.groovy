package io.izzel.taboolib.gradle

import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes

import java.util.jar.JarEntry
import java.util.jar.JarFile

class NMSProxyPackager {

    static final String PREFIX = 'META-INF/taboolib/nms-proxy/'
    static final String INDEX = PREFIX + 'index'
    private static final String ANNOTATION = 'Ltaboolib/module/nms/NMSProxy;'

    static Set<String> scan(JarFile jar, List<String> excludes, RelocateRemapper remapper) {
        def marked = new HashSet<String>()
        def available = new HashSet<String>()
        def descriptors = [ANNOTATION, remapper.mapDesc(ANNOTATION)] as Set<String>

        jar.entries().each { JarEntry entry ->
            if (!entry.name.endsWith('.class') || excludes.any { entry.name.startsWith(it) }) {
                return
            }
            def className = entry.name.substring(0, entry.name.length() - '.class'.length())
            available.add(className)
            jar.getInputStream(entry).withCloseable { input ->
                new ClassReader(input).accept(new ClassVisitor(Opcodes.ASM9) {
                    @Override
                    AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
                        if (descriptors.contains(descriptor)) {
                            marked.add(className)
                        }
                        return null
                    }
                }, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES)
            }
        }
        def selected = new HashSet<String>(marked)
        available.each { String className ->
            def parent = className
            while (parent.contains('$')) {
                parent = parent.substring(0, parent.lastIndexOf('$'))
                if (marked.contains(parent)) {
                    selected.add(className)
                    break
                }
            }
        }
        return selected
    }
}
