package io.izzel.taboolib.gradle.description

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.gradle.api.Project

class BuilderVelocity extends Builder {

    @Override
    byte[] build(Description description, Project project) {
        def info = new JsonObject()
        info.addProperty('id', project.name.toLowerCase())
        info.addProperty('name', project.name)
        info.addProperty('main', "${project.group}.taboolib.platform.VelocityPlugin")
        info.addProperty('version', project.version.toString())
        // authors
        def con = description.con.contributors.collect { it.name }
        writeList(info, con, 'authors')
        // dependencies
        writeList(info, description.dep.dependencies
                .findAll { it.with == null || it.with.equalsIgnoreCase('velocity') }
                .collect { it.name }, 'dependencies')
        return bytes(info)
    }
}
