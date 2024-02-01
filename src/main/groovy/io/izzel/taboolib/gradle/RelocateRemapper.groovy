package io.izzel.taboolib.gradle

import groovy.transform.Canonical
import org.objectweb.asm.commons.ClassRemapper
import org.objectweb.asm.commons.Remapper

@Canonical
class RelocateRemapper extends Remapper {

    Map<String, String> dot
    Map<String, String> slash
    ClassRemapper remapper

    List<String> skipKotlinAnnotations = [
            "kotlin/annotation/Repeatable",
            "kotlin/annotation/Retention",
            "kotlin/annotation/Target",
            "kotlin/jvm/JvmField",
            "kotlin/jvm/JvmInline",
            "kotlin/jvm/JvmStatic",
            "kotlin/jvm/PurelyImplements",
            "kotlin/Metadata",
            "kotlin/Deprecated",
            "kotlin/ReplaceWith",
    ]

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

    @SuppressWarnings('GroovyAccessibility')
    @Override
    String map(String internalName) {
        if (skipKotlinAnnotations.any { internalName.startsWith(it) }) {
            return internalName
        }
        def match = slash.find { internalName.startsWith(it.key) }
        if (match) {
            if (match.value.startsWith("!")) {
                def index = internalName.lastIndexOf('/')
                return match.value.substring(1) + "/" + internalName.substring(index + 1, internalName.length())
            }
            return match.value + internalName.substring(match.key.length())
        } else {
            return internalName
        }
    }
}
