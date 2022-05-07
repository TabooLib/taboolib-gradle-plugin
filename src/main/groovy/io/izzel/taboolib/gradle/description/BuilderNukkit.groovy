package io.izzel.taboolib.gradle.description

import org.gradle.api.Project

class BuilderNukkit extends Builder {

    @Override
    byte[] build(Description description, Project project) {
        def body = startBukkitFile()
        body += "name: ${description.name ?: project.name}"
        body += "main: ${project.group}.taboolib.platform.NukkitPlugin"
        body += "version: ${project.version}"
        write(body, description.lin.links['homepage'], 'website')
        writeLine(body)
        // authors
        def con = description.con.contributors.collect { it.name }
        writeList(body, con, 'authors')
        writeLine(body)
        // dependency
        writeList(body, description.dep.dependencies
                .findAll { it.with == null || it.with.equalsIgnoreCase('nukkit') }
                .findAll { it.forceDepend() }
                .collect { it.name }, 'depend')
        writeList(body, description.dep.dependencies
                .findAll { it.with == null || it.with.equalsIgnoreCase('nukkit') }
                .findAll { it.optional }
                .collect { it.name }, 'softdepend')
        writeList(body, description.dep.dependencies
                .findAll { it.with == null || it.with.equalsIgnoreCase('nukkit') }
                .findAll { it.loadbefore }
                .collect { it.name }, 'loadbefore')
        writeLine(body)
        // custom nodes
        description.nukkitNodes.each {
            if (it.value instanceof List) {
                writeList(body, it.value, it.key)
            } else {
                write(body, it.value, it.key)
            }
        }
        return bytes(body)
    }
}
