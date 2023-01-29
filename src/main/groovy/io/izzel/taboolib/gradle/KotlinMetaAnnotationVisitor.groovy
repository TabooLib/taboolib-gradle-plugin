package io.izzel.taboolib.gradle

import org.gradle.api.Project
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.Opcodes

class KotlinMetaAnnotationVisitor extends AnnotationVisitor {

    Project project

    KotlinMetaAnnotationVisitor(AnnotationVisitor annotationVisitor, project) {
        super(Opcodes.ASM9, annotationVisitor)
        this.project = project
    }

    @Override
    void visit(String name, Object value) {
        if (value instanceof String) {
            super.visit(name, value.replace("taboolib", "${project.group.replace('.', '/')}/taboolib"))
        } else {
            super.visit(name, value)
        }
    }

    @Override
    AnnotationVisitor visitArray(String name) {
        return new KotlinMetaAnnotationVisitor(super.visitArray(name), project)
    }
}
