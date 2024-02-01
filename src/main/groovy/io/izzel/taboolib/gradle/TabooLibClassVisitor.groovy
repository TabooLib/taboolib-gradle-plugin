package io.izzel.taboolib.gradle

import org.gradle.api.Project
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class TabooLibClassVisitor extends ClassVisitor {

    String name

    Project project

    TabooLibExtension tabooExt

    boolean api

    List<String> pluginAnnotations = [
            "Lorg/spongepowered/api/plugin/Plugin;",
            "Lorg/spongepowered/plugin/jvm/Plugin;",
            "Lcom/velocitypowered/api/plugin/Plugin;"
    ]

    TabooLibClassVisitor(ClassVisitor classVisitor, Project project, TabooLibExtension tabooExt, boolean api) {
        super(Opcodes.ASM9, classVisitor);
        this.project = project
        this.tabooExt = tabooExt
        this.api = api
    }

    @Override
    void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.name = name
        super.visit(version, access, name, signature, superName, interfaces)
    }

    @Override
    MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        if (project.hasProperty("DeleteCode")) return new EmptyMethodVisitor(super.visitMethod(access, name, descriptor, signature, exceptions))
        return super.visitMethod(access, name, descriptor, signature, exceptions)
    }

    @Override
    AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        // 插件注解
        if (descriptor in pluginAnnotations) {
            return new PluginAnnotationVisitor(super.visitAnnotation(descriptor, visible), project)
        }
        // Metadata
        if (!tabooExt.version.skipTabooLibRelocate && descriptor == "Lkotlin/Metadata;") {
            return new KotlinMetaAnnotationVisitor(super.visitAnnotation(descriptor, visible), project, tabooExt)
        }
        // 其他
        return super.visitAnnotation(descriptor, visible)
    }

    @Override
    void visitEnd() {
        super.visitEnd()
    }
}
