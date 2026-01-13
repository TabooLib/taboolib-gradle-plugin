package io.izzel.taboolib.gradle.description

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.izzel.taboolib.gradle.TabooLibExtension
import org.gradle.api.Project

class BuilderHytale extends Builder {

    @Override
    byte[] build(Description description, Project project, TabooLibExtension tabooLibExt) {
        def manifest = new JsonObject()

        // Group - 使用项目的 group
        def group = description.hytaleNodes['Group'] ?: project.group.toString()
        manifest.addProperty('Group', group)

        // Name - 插件名称
        manifest.addProperty('Name', description.name ?: project.name)

        // Version - 版本号
        manifest.addProperty('Version', project.version.toString())

        // Description - 插件描述
        def desc = description.hytaleNodes['Description']
        if (desc != null) {
            manifest.addProperty('Description', desc.toString())
        }

        // Authors - 作者列表
        def authors = new JsonArray()
        description.con.contributors.each { contributor ->
            def author = new JsonObject()
            author.addProperty('Name', contributor.name)
            if (contributor.description != null) {
                author.addProperty('Contact', contributor.description)
            }
            authors.add(author)
        }
        if (authors.size() > 0) {
            manifest.add('Authors', authors)
        }

        // Website - 主页链接
        def website = description.lin.links['homepage']
        if (website != null) {
            manifest.addProperty('Website', website.toString())
        }

        // Main - 主类
        if (tabooLibExt.version.skipTabooLibRelocate) {
            manifest.addProperty('Main', "taboolib.platform.HytalePlugin")
        } else {
            manifest.addProperty('Main', "${project.group}.taboolib.platform.HytalePlugin")
        }

        // ServerVersion - 服务器版本要求
        def serverVersion = description.hytaleNodes['ServerVersion'] ?: '>=0.0.1'
        manifest.addProperty('ServerVersion', serverVersion.toString())

        // Dependencies - 强依赖 (格式: "Group:Name": "version")
        def dependencies = new JsonObject()
        description.dep.dependencies
                .findAll { it.with == null || it.with.equalsIgnoreCase('hytale') }
                .findAll { it.forceDepend() }
                .each { dep ->
                    def key = dep.group ? "${dep.group}:${dep.name}" : dep.name
                    def ver = dep.version ?: '*'
                    dependencies.addProperty(key, ver)
                }
        manifest.add('Dependencies', dependencies)

        // OptionalDependencies - 可选依赖
        def optionalDependencies = new JsonObject()
        description.dep.dependencies
                .findAll { it.with == null || it.with.equalsIgnoreCase('hytale') }
                .findAll { it.optional }
                .each { dep ->
                    def key = dep.group ? "${dep.group}:${dep.name}" : dep.name
                    def ver = dep.version ?: '*'
                    optionalDependencies.addProperty(key, ver)
                }
        manifest.add('OptionalDependencies', optionalDependencies)

        // LoadBefore - 加载顺序
        def loadBefore = new JsonObject()
        description.dep.dependencies
                .findAll { it.with == null || it.with.equalsIgnoreCase('hytale') }
                .findAll { it.loadbefore }
                .each { dep ->
                    def key = dep.group ? "${dep.group}:${dep.name}" : dep.name
                    def ver = dep.version ?: '*'
                    loadBefore.addProperty(key, ver)
                }
        manifest.add('LoadBefore', loadBefore)

        // DisabledByDefault - 默认禁用
        def disabledByDefault = description.hytaleNodes['DisabledByDefault'] ?: false
        manifest.addProperty('DisabledByDefault', disabledByDefault as Boolean)

        // IncludesAssetPack - 包含资源包
        def includesAssetPack = description.hytaleNodes['IncludesAssetPack'] ?: false
        manifest.addProperty('IncludesAssetPack', includesAssetPack as Boolean)

        // SubPlugins - 子插件
        manifest.add('SubPlugins', new JsonArray())

        return bytes(manifest)
    }
}
