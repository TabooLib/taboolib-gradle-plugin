package io.izzel.taboolib.gradle

import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes

class TabooLibClassVisitor extends ClassVisitor {

    TabooLibClassVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM5, classVisitor);
    }

    @Override
    AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        if (descriptor == "Lorg/spongepowered/api/plugin/Plugin;") {
            return new SpongeAnnotationVisitor(super.visitAnnotation(descriptor, visible))
        } else {
            return super.visitAnnotation(descriptor, visible)
        }
    }
}
