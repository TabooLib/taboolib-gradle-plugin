package io.izzel.taboolib.gradle.description

class Dependencies {

    List<Dependency> dependencies = []

    Dependency name(name) {
        def dep = new Dependency(name)
        dependencies += dep
        return dep
    }

    class Dependency {

        // 适应 CloudNet 依赖
        String group
        String name
        String with
        String version

        def loadafter = false
        def loadbefore = false
        def optional = false
        def mod = false

        Dependency(name) {
            this.name = name
        }

        Dependency(group, name) {
            this.group = group
            this.name = name
        }

        def fullyName(spec = '@') {
            return version == null ? name : name + spec + version
        }

        def forceDepend() {
            return !loadafter && !loadbefore && !optional
        }

        Dependency with(description) {
            this.with = description
            return this
        }

        Dependency group(group) {
            this.group = group
            return this
        }

        Dependency version(version) {
            this.version = version
            return this
        }

        Dependency loadafter(loadafter) {
            this.loadafter = loadafter
            return this
        }

        Dependency loadbefore(loadbefore) {
            this.loadbefore = loadbefore
            return this
        }

        Dependency optional(optional) {
            this.optional = optional
            return this
        }

        Dependency mod(mod) {
            this.mod = mod
            return this
        }
    }
}