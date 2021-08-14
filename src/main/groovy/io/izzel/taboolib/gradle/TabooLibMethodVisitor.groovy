package io.izzel.taboolib.gradle


import groovy.transform.EqualsAndHashCode
import groovy.transform.TupleConstructor
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.Attribute
import org.objectweb.asm.Handle
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.TypePath

class TabooLibMethodVisitor extends MethodVisitor {

    TabooLibClassVisitor classVisitor

    TabooLibMethodVisitor(MethodVisitor methodVisitor, TabooLibClassVisitor classVisitor) {
        super(Opcodes.ASM7, methodVisitor)
        this.classVisitor = classVisitor
    }

    @Override
    void visitParameter(String name, int access) {
        super.visitParameter(name, access)
    }

    @Override
    AnnotationVisitor visitAnnotationDefault() {
        return super.visitAnnotationDefault()
    }

    @Override
    AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        return super.visitAnnotation(descriptor, visible)
    }

    @Override
    AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return super.visitTypeAnnotation(typeRef, typePath, descriptor, visible)
    }

    @Override
    void visitAnnotableParameterCount(int parameterCount, boolean visible) {
        super.visitAnnotableParameterCount(parameterCount, visible)
    }

    @Override
    AnnotationVisitor visitParameterAnnotation(int parameter, String descriptor, boolean visible) {
        return super.visitParameterAnnotation(parameter, descriptor, visible)
    }

    @Override
    void visitAttribute(Attribute attribute) {
        super.visitAttribute(attribute)
    }

    @Override
    void visitCode() {
        super.visitCode()
    }

    @Override
    void visitFrame(int type, int numLocal, Object[] local, int numStack, Object[] stack) {
        super.visitFrame(type, numLocal, local, numStack, stack)
    }

    @Override
    void visitInsn(int opcode) {
        super.visitInsn(opcode)
    }

    @Override
    void visitIntInsn(int opcode, int operand) {
        super.visitIntInsn(opcode, operand)
    }

    @Override
    void visitVarInsn(int opcode, int i) {
        super.visitVarInsn(opcode, i)
    }

    @Override
    void visitTypeInsn(int opcode, String type) {
        super.visitTypeInsn(opcode, type)
    }

    @Override
    void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        super.visitFieldInsn(opcode, owner, name, descriptor)
    }

    @Override
    void visitMethodInsn(int opcode, String owner, String name, String descriptor) {
        super.visitMethodInsn(opcode, owner, name, descriptor)
    }

    @Override
    void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        classVisitor.methodVisits.add(new MethodVisit(owner, name))
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface)
    }

    @Override
    void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
        super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments)
    }

    @Override
    void visitJumpInsn(int opcode, Label label) {
        super.visitJumpInsn(opcode, label)
    }

    @Override
    void visitLabel(Label label) {
        super.visitLabel(label)
    }

    @Override
    void visitLdcInsn(Object value) {
        super.visitLdcInsn(value)
    }

    @Override
    void visitIincInsn(int i, int increment) {
        super.visitIincInsn(i, increment)
    }

    @Override
    void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
        super.visitTableSwitchInsn(min, max, dflt, labels)
    }

    @Override
    void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        super.visitLookupSwitchInsn(dflt, keys, labels)
    }

    @Override
    void visitMultiANewArrayInsn(String descriptor, int numDimensions) {
        super.visitMultiANewArrayInsn(descriptor, numDimensions)
    }

    @Override
    AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return super.visitInsnAnnotation(typeRef, typePath, descriptor, visible)
    }

    @Override
    void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        super.visitTryCatchBlock(start, end, handler, type)
    }

    @Override
    AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return super.visitTryCatchAnnotation(typeRef, typePath, descriptor, visible)
    }

    @Override
    void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
        super.visitLocalVariable(name, descriptor, signature, start, end, index)
    }

    @Override
    AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String descriptor, boolean visible) {
        return super.visitLocalVariableAnnotation(typeRef, typePath, start, end, index, descriptor, visible)
    }

    @Override
    void visitLineNumber(int line, Label start) {
        super.visitLineNumber(line, start)
    }

    @Override
    void visitMaxs(int maxStack, int maxLocals) {
        super.visitMaxs(maxStack, maxLocals)
    }

    @Override
    void visitEnd() {
        super.visitEnd()
    }
}
