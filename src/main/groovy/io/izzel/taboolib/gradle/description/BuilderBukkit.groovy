package io.izzel.taboolib.gradle.description

import io.izzel.taboolib.gradle.TabooLibExtension
import org.gradle.api.Project

class BuilderBukkit extends Builder {

    @Override
    byte[] build(Description description, Project project, TabooLibExtension tabooLibExt) {
        def body = startBukkitFile()
        body += "name: ${description.name ?: project.name}"

        if (tabooLibExt.version.skipTabooLibRelocate) {
            body += "main: taboolib.platform.BukkitPlugin"
        } else {
            body += "main: ${project.group}.taboolib.platform.BukkitPlugin"
        }

        body += "version: ${project.version}"
        write(body, description.lin.links['homepage'], 'website')
        writeLine(body)
        // authors
        def con = description.con.contributors.collect { it.name }
        writeList(body, con, 'authors')
        writeLine(body)
        // dependency
        writeList(body, description.dep.dependencies
                .findAll { it.with == null || it.with.equalsIgnoreCase('bukkit') }
                .findAll { it.forceDepend() }
                .collect { it.name }, 'depend')
        writeList(body, description.dep.dependencies
                .findAll { it.with == null || it.with.equalsIgnoreCase('bukkit') }
                .findAll { it.optional }
                .collect { it.name }, 'softdepend')
        writeList(body, description.dep.dependencies
                .findAll { it.with == null || it.with.equalsIgnoreCase('bukkit') }
                .findAll { it.loadbefore }
                .collect { it.name }, 'loadbefore')
        writeLine(body)
        // custom nodes
        description.bukkitNodes.each {
            if (it.value instanceof List) {
                writeList(body, it.value, it.key)
            } else {
                write(body, it.value, it.key)
            }
        }
        return bytes(body)
    }
}
