package io.izzel.taboolib.gradle

import org.gradle.api.Project
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type

class IsolatedAnnotationVisitor extends AnnotationVisitor {

    String name
    Project project
    TabooLibClassVisitor parent

    IsolatedAnnotationVisitor(AnnotationVisitor annotationVisitor, project, name, parent) {
        super(Opcodes.ASM9, annotationVisitor)
        this.name = name
        this.project = project
        this.parent = parent
        this.parent.isolated[name] = new ArrayList<>()
    }

    @Override
    AnnotationVisitor visitArray(String name) {
        return new IsolatedExcludeAnnotationVisitor(super.visitArray(name), this)
    }

    class IsolatedExcludeAnnotationVisitor extends AnnotationVisitor {

        IsolatedAnnotationVisitor parent

        IsolatedExcludeAnnotationVisitor(AnnotationVisitor annotationVisitor, parent) {
            super(Opcodes.ASM9, annotationVisitor)
            this.parent = parent
        }

        @Override
        void visit(String name, Object value) {
            parent.parent.isolated[parent.name] += (value as Type).className
            super.visit(name, value)
        }
    }
}
