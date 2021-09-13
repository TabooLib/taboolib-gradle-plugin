package io.izzel.taboolib.gradle.description

class Links {

    def links = new HashMap<String, Link>()

    Link name(name) {
        def link = new Link(name)
        links[link.name] = link
        return link
    }

    class Link {

        String name
        String url

        Link(name) {
            this.name = name
        }

        Link url(url) {
            this.url = url
            return this
        }

        @Override
        String toString() {
            return url
        }
    }
}