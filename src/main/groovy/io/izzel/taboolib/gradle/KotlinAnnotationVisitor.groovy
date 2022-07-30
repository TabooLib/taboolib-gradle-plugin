package io.izzel.taboolib.gradle

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapperKt
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.Opcodes

class KotlinAnnotationVisitor extends AnnotationVisitor {

    Project project

    KotlinAnnotationVisitor(AnnotationVisitor annotationVisitor, project) {
        super(Opcodes.ASM7, annotationVisitor)
        this.project = project
    }

    @Override
    void visit(String name, Object value) {
        if (value instanceof String) {
            super.visit(name, value
                    .replace("@kotlin_version@", getKotlinVersion())
                    .replace("@kotlin_version_escape@", getKotlinVersionEscape())
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
        return new KotlinAnnotationVisitor(super.visitAnnotation(name, descriptor), project)
    }

    @Override
    AnnotationVisitor visitArray(String name) {
        return new KotlinAnnotationVisitor(super.visitArray(name), project)
    }

    @Override
    void visitEnd() {
        super.visitEnd()
    }

    String getKotlinVersion() {
        return KotlinPluginWrapperKt.getKotlinPluginVersion(project)
    }

    String getKotlinVersionEscape() {
        return getKotlinVersion().replaceAll("[._-]", "")
    }
}
