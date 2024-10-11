package io.izzel.taboolib.gradle.description

import io.izzel.taboolib.gradle.TabooLibExtension
import org.gradle.api.Project

class BuilderAfyBroker extends Builder {

    @Override
    byte[] build(Description description, Project project, TabooLibExtension tabooLibExt) {
        def body = startBukkitFile()
        body += "name: ${description.name ?: project.name}"

        if (tabooLibExt.version.skipTabooLibRelocate) {
            body += "main: taboolib.platform.AfyBrokerPlugin"
        } else {
            body += "main: ${project.group}.taboolib.platform.AfyBrokerPlugin"
        }

        body += "version: ${project.version}"
        writeLine(body)
        // authors
        def con = description.con.contributors.collect { it.name }.join(', ')
        write(body, con, 'author')
        writeLine(body)
        // dependency
        writeList(body, description.dep.dependencies
                .findAll { it.with == null || it.with.equalsIgnoreCase('afybroker') }
                .findAll { it.forceDepend() }
                .collect { it.name }, 'depends')
        writeList(body, description.dep.dependencies
                .findAll { it.with == null || it.with.equalsIgnoreCase('afybroker') }
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
