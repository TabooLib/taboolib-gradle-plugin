package io.izzel.taboolib.gradle.description

import org.gradle.api.Action

class Description {

    Contributors con = new Contributors()

    Dependencies dep = new Dependencies()

    Links lin = new Links()

    def bukkitNodes = new HashMap()

    def nukkitNodes = new HashMap()

    def bungeeNodes = new HashMap()

    String spongeDesc

    String name

    Description() {
        bukkitApi('1.13')
        nukkitApi('1.0.0')
    }

    def name(name) {
        this.name = name
    }

    def desc(desc) {
        bukkitNodes['description'] = desc
        nukkitNodes['description'] = desc
        bungeeNodes['description'] = desc
        spongeDesc = desc
    }

    def load(order) {
        bukkitNodes['load'] = order
        nukkitNodes['load'] = order
    }

    def bukkitApi(api) {
        bukkitNodes['api-version'] = api
    }

    def nukkitApi(api) {
        nukkitNodes['api'] = api
    }

    def prefix(prefix) {
        bukkitNodes['prefix'] = prefix
        nukkitNodes['prefix'] = prefix
    }

    def contributors(Action<? super Contributors> action) {
        action.execute(con)
    }

    def dependencies(Action<? super Dependencies> action) {
        action.execute(dep)
    }

    def links(Action<? super Links> action) {
        action.execute(lin)
    }
}
