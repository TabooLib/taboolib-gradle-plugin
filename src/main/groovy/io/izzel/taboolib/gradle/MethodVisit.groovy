package io.izzel.taboolib.gradle

import groovy.transform.EqualsAndHashCode
import groovy.transform.TupleConstructor

@TupleConstructor
@EqualsAndHashCode
class MethodVisit {

    String owner
    String name

    @Override
    String toString() {
        return owner.substring(owner.lastIndexOf("/") + 1) + "." + name + "()"
    }
}