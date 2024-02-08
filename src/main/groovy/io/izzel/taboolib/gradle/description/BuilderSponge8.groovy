package io.izzel.taboolib.gradle.description

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.izzel.taboolib.gradle.TabooLibExtension
import org.gradle.api.Project

class BuilderSponge8 extends Builder {

    @Override
    byte[] build(Description description, Project project, TabooLibExtension tabooLibExt) {
        def json = new JsonObject()
        def plugins = new JsonArray()
        def info = new JsonObject()
        info.addProperty('loader', 'java_plain')
        info.addProperty('id', (description.name ?: project.name).toLowerCase())
        info.addProperty('name', description.name ?: project.name)
        info.addProperty('version', project.version.toString())

        if (tabooLibExt.version.skipTabooLibRelocate) {
            info.addProperty('main-class', "taboolib.platform.Sponge8Plugin")
        } else {
            info.addProperty('main-class', "${project.group}.taboolib.platform.Sponge8Plugin")
        }

        write(info, description.spongeDesc, 'description')
        // links
        if (description.lin.links.size() > 0) {
            def links = new JsonObject()
            description.lin.links.each {
                links.addProperty(it.key, it.value.url)
            }
            info.add('links', links)
        }
        // contributors
        if (description.con.contributors.size() > 0) {
            def contributors = new JsonArray()
            description.con.contributors.each {
                def con = new JsonObject()
                write(con, it.name, 'name')
                write(con, it.description, 'description')
                contributors.add(con)
            }
            info.add('contributors', contributors)
        }
        // dependencies
        if (description.dep.dependencies.size() > 0) {
            def dependencies = new JsonArray()
            description.dep.dependencies.findAll { it.with == null || it.with.equalsIgnoreCase('sponge8') }.each {
                def dep = new JsonObject()
                write(dep, it.name, 'id')
                write(dep, it.version, 'version')
                dep.addProperty('load-order', it.loadafter ? 'AFTER' : 'UNDEFINED')
                dep.addProperty('optional', it.optional)
                dependencies.add(dep)
            }
            info.add('dependencies', dependencies)
        }
        plugins.add(info)
        json.add('plugins', plugins)
        return bytes(json)
    }
}
