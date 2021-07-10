package io.izzel.taboolib.gradle.description

class Contributors {

    List<Contributor> contributors = []

    Contributor name(name) {
        def con = new Contributor(name)
        contributors += con
        return con
    }

    class Contributor {

        def name
        def description

        Contributor(name) {
            this.name = name
        }

        Contributor description(description) {
            this.description = description
            return this
        }
    }
}