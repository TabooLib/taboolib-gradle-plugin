package io.izzel.taboolib.gradle

import org.gradle.api.Project
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.Opcodes

class SpongeAnnotationVisitor extends AnnotationVisitor {

    Project project

    SpongeAnnotationVisitor(AnnotationVisitor annotationVisitor) {
        super(Opcodes.ASM5, annotationVisitor)
    }

    @Override
    void visit(String name, Object value) {
        if (value instanceof String) {
            super.visit(name, value
                    .replace("@plugin_id@", project.name.toLowerCase())
                    .replace("@plugin_name@", project.name)
                    .replace("@plugin_version@", project.version.toString())
            )
        } else {
            super.visit(name, value)
        }
    }

    @Override
    void visitEnum(String name, String descriptor, String value) {
        super.visitEnum(name, descriptor, value)
    }

    @Override
    AnnotationVisitor visitAnnotation(String name, String descriptor) {
        return super.visitAnnotation(name, descriptor)
    }

    @Override
    AnnotationVisitor visitArray(String name) {
        return super.visitArray(name)
    }

    @Override
    void visitEnd() {
        super.visitEnd()
    }
}
