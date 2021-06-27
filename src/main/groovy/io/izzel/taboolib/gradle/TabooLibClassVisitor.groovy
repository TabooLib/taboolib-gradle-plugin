package io.izzel.taboolib.gradle

import org.gradle.api.Project
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes

class TabooLibClassVisitor extends ClassVisitor {

    Project project

    TabooLibClassVisitor(ClassVisitor classVisitor, Project project) {
        super(Opcodes.ASM7, classVisitor);
        this.project = project
    }

    @Override
    AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        if (descriptor == "Lorg/spongepowered/api/plugin/Plugin;") {
            return new SpongeAnnotationVisitor(super.visitAnnotation(descriptor, visible), project)
        } else {
            return super.visitAnnotation(descriptor, visible)
        }
    }
}
