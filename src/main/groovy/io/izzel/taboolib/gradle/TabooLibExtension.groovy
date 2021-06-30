package io.izzel.taboolib.gradle

import groovy.transform.Canonical
import org.codehaus.groovy.runtime.DefaultGroovyMethods

@Canonical
class TabooLibExtension {

    String version = '6.0.0'

    String classifier = "all"

    List<String> modules = []

    Description description = new Description()

    Map<String, String> relocation = new LinkedHashMap<>()

    def install(String... name) {
        name.each { modules += it }
    }

    def relocate(String pre, String post) {
        relocation[pre] = post
    }

    def descriptionFile(@DelegatesTo(Description.class) Closure<Description> closure) {
        DefaultGroovyMethods.with(description, closure)
    }
}
