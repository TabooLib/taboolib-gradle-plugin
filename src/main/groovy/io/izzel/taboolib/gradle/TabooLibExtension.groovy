package io.izzel.taboolib.gradle

import groovy.transform.Canonical

@Canonical
class TabooLibExtension {

    String tabooLibVersion = '5.+'

    String loaderVersion = '2.+'

    String classifier = 'all'

    Map<String, String> relocation = new LinkedHashMap<>()

    def relocate(String pre, String post) {
        this.relocation[pre] = post
    }
}
