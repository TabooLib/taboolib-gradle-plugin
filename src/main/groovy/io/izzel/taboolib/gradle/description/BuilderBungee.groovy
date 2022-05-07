package io.izzel.taboolib.gradle.description

import org.gradle.api.Project

class BuilderBungee extends Builder {

    @Override
    byte[] build(Description description, Project project) {
        def body = startBukkitFile()
        body += "name: ${description.name ?: project.name}"
        body += "main: ${project.group}.taboolib.platform.BungeePlugin"
        body += "version: ${project.version}"
        write(body, description.lin.links['homepage'], 'website')
        writeLine(body)
        // authors
        def con = description.con.contributors.collect { it.name }.join(', ')
        write(body, con, 'author')
        writeLine(body)
        // dependency
        writeList(body, description.dep.dependencies
                .findAll { it.with == null || it.with.equalsIgnoreCase('bungee') }
                .findAll { it.forceDepend() }
                .collect { it.name }, 'depends')
        writeList(body, description.dep.dependencies
                .findAll { it.with == null || it.with.equalsIgnoreCase('bungee') }
                .findAll { it.optional }
                .collect { it.name }, 'softDepends')
        writeLine(body)
        // custom nodes
        description.bungeeNodes.each {
            if (it.value instanceof List) {
                writeList(body, it.value, it.key)
            } else {
                write(body, it.value, it.key)
            }
        }
        return bytes(body)
    }
}
