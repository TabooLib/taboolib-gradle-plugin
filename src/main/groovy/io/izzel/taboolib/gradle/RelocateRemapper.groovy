package io.izzel.taboolib.gradle

import groovy.transform.Canonical
import org.objectweb.asm.commons.Remapper

@Canonical
class RelocateRemapper extends Remapper {

    Map<String, String> dot
    Map<String, String> slash

    @Override
    Object mapValue(Object value) {
        if (value instanceof String) {
            def match = dot.find { (value as String).startsWith(it.key) }
            if (match) {
                return (match.value + (value as String).substring(match.key.length())).toString()
            }
        }
        return super.mapValue(value)
    }

    @Override
    String map(String internalName) {
        def match = slash.find { internalName.startsWith(it.key) }
        if (match) {
            return match.value + internalName.substring(match.key.length())
        } else {
            return internalName
        }
    }
}
