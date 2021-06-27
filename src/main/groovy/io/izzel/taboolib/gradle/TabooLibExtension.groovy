package io.izzel.taboolib.gradle

import groovy.transform.Canonical

@Canonical
class TabooLibExtension {

    String version = '6.0.0'

    String classifier = "all"

    List<String> modules = new ArrayList<>();

    Map<String, String> relocation = new LinkedHashMap<>()

    def install(String... name) {
        name.each { modules += it }
    }

    def relocate(String pre, String post) {
        relocation[pre] = post
    }
}
