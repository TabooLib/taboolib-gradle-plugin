package io.izzel.taboolib.gradle

import kotlin.jvm.internal.Intrinsics
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import org.objectweb.asm.Handle
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class EmptyMethodVisitor extends MethodVisitor {

    EmptyMethodVisitor(@NotNull final MethodVisitor visitor) {
        super(Opcodes.ASM9, visitor)
    }

    void visitIincInsn(final int var, final int increment) {
    }

    void visitInsn(final int opcode) {
    }

    void visitIntInsn(final int opcode, final int operand) {
    }

    void visitVarInsn(final int opcode, final int var) {
    }

    void visitTypeInsn(final int opcode, @Nullable final String type) {
    }

    void visitJumpInsn(final int opcode, @Nullable final Label label) {
    }

    void visitLabel(@Nullable final Label label) {
    }

    void visitLdcInsn(@Nullable final Object value) {
    }

    void visitTableSwitchInsn(final int min, final int max, @Nullable final Label dflt, @NotNull final Label... labels) {
        Intrinsics.checkNotNullParameter((Object) labels, "labels")
    }

    void visitLookupSwitchInsn(@Nullable final Label dflt, @Nullable final int[] keys, @Nullable final Label[] labels) {
    }

    void visitMultiANewArrayInsn(@Nullable final String descriptor, final int numDimensions) {
    }

    void visitTryCatchBlock(@Nullable final Label start, @Nullable final Label end, @Nullable final Label handler, @Nullable final String type) {
    }

    void visitLocalVariable(@Nullable final String name, @Nullable final String descriptor, @Nullable final String signature, @Nullable final Label start, @Nullable final Label end, final int index) {
    }

    void visitLineNumber(final int line, @Nullable final Label start) {
    }

    void visitFrame(final int type, final int numLocal, @Nullable final Object[] local, final int numStack, @Nullable final Object[] stack) {
    }

    void visitFieldInsn(final int opcode, @Nullable final String owner, @Nullable final String name, @Nullable final String descriptor) {
    }

    void visitMethodInsn(final int opcode, @Nullable final String owner, @Nullable final String name, @Nullable final String descriptor) {
    }

    void visitMethodInsn(final int opcode, @Nullable final String owner, @Nullable final String name, @Nullable final String descriptor, final boolean isInterface) {
    }

    void visitInvokeDynamicInsn(@Nullable final String name, @Nullable final String descriptor, @Nullable final Handle bootstrapMethodHandle, @NotNull final Object... bootstrapMethodArguments) {
    }
}
