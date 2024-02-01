package io.izzel.taboolib.gradle

import org.gradle.api.Project
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.Opcodes

class KotlinMetaAnnotationVisitor extends AnnotationVisitor {

    Project project
    TabooLibExtension tabooExt

    KotlinMetaAnnotationVisitor(AnnotationVisitor annotationVisitor, project, tabooExt) {
        super(Opcodes.ASM9, annotationVisitor)
        this.project = project
        this.tabooExt = tabooExt
    }

    @Override
    void visit(String name, Object value) {
        if (value instanceof String) {
            def group = project.group.toString().replace('.', '/')
            def rep = value.replace("Ltaboolib", "L$group/taboolib")
            tabooExt.relocation.each { k, v ->
                rep = rep.replace("L${k.replace('.', '/')}", "L${v.replace('.', '/')}")
            }
            super.visit(name, rep)
        } else {
            super.visit(name, value)
        }
    }

    @Override
    AnnotationVisitor visitArray(String name) {
        return new KotlinMetaAnnotationVisitor(super.visitArray(name), project, tabooExt)
    }
}
