package io.izzel.taboolib.gradle.description

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.gradle.api.Project

class BuilderSponge7 extends Builder {

    @Override
    byte[] build(Description description, Project project) {
        def json = new JsonArray()
        def info = new JsonObject()
        info.addProperty('modid', project.name.toLowerCase())
        info.addProperty('name', project.name)
        info.addProperty('version', project.version.toString())
        write(info, description.spongeDesc, 'description')
        write(info, description.lin.links['homepage'], 'url')
        // authors
        def con = description.con.contributors.collect { it.name }
        writeList(info, con, 'authorList')
        // dependencies
        writeList(info, description.dep.dependencies
                .findAll { it.with == null || it.with.equalsIgnoreCase('sponge7') }
                .findAll { !it.mod }
                .collect { it.fullyName() }, 'dependencies')
        writeList(info, description.dep.dependencies
                .findAll { it.with == null || it.with.equalsIgnoreCase('sponge7') }
                .findAll { it.mod }
                .collect { it.fullyName() }, 'requiredMods')
        json.add(info)
        return bytes(json)
    }
}
