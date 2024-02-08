package io.izzel.taboolib.gradle.description

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.izzel.taboolib.gradle.TabooLibExtension
import org.gradle.api.Project

class BuilderVelocity extends Builder {

    @Override
    byte[] build(Description description, Project project, TabooLibExtension tabooLibExt) {
        def info = new JsonObject()
        info.addProperty('id', (description.name ?: project.name).toLowerCase())
        info.addProperty('name', description.name ?: project.name)

        if (tabooLibExt.version.skipTabooLibRelocate) {
            info.addProperty('main', "taboolib.platform.VelocityPlugin")
        } else {
            info.addProperty('main', "${project.group}.taboolib.platform.VelocityPlugin")
        }

        info.addProperty('version', project.version.toString())
        // authors
        def con = description.con.contributors.collect { it.name }
        writeList(info, con, 'authors')
        // dependencies
        def dependencies = new JsonArray()
        description.dep.dependencies.forEach(dep -> {
            def dependency = new JsonObject()
            dependency.addProperty('id', dep.name)
            dependency.addProperty('optional', dep.optional)
            dependencies.add(dependency)
        })
        info.add('dependencies', dependencies)
        return bytes(info)
    }
}
