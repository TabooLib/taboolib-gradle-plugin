package io.izzel.taboolib.gradle

import groovy.transform.Canonical
import io.izzel.taboolib.gradle.description.Description
import org.gradle.api.Action

@Canonical
class TabooLibExtension {

    String version = '6.0.0'

    String classifier = "all"

    List<String> modules = []

    List<String> exclude = []

    Description des = new Description()

    Map<String, String> relocation = new LinkedHashMap<>()

    def install(String... name) {
        name.each { modules += it }
    }

    def exclude(String match) {
        exclude += match
    }

    def relocate(String pre, String post) {
        relocation[pre] = post
    }

    def description(Action<? super Description> action) {
        action.execute(des)
    }
}
