package io.izzel.taboolib.gradle.description

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.gradle.api.Project

/**
 * @project taboolib-gradle-plugin
 *
 * @author Score2
 * @since 2021/09/29 12:20
 */
class BuilderCloudNetV3 extends Builder {
    @Override
    byte[] build(Description description, Project project) {
        def info = new JsonObject()
        info.addProperty('group', project.group.toString())
        info.addProperty('name', description.name ?: project.name)
        info.addProperty('main', "${project.group}.taboolib.platform.CloudNetV3Plugin")
        info.addProperty('version', project.version.toString())
        // authors
        def con = description.con.contributors.collect { it.name }
        if (con.size() >= 1) {
            info.addProperty('author', con.get(0).toString())
        } else {
            writeList(info, con, 'authors')
        }
        // dependencies
        def depends = new JsonArray()
        description.dep.dependencies.forEach { it ->
            def depend = new JsonObject()
            depend.addProperty('group', it.group)
            depend.addProperty('name', it.name)
            depend.addProperty('version', it.version)
            depends.add(depend)
        }
        return bytes(info)
    }
}
