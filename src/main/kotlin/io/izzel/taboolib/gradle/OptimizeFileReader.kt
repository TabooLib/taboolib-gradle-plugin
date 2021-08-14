package io.izzel.taboolib.gradle

import com.google.gson.JsonParser
import org.gradle.api.Project
import java.io.InputStream
import java.nio.charset.StandardCharsets

/**
 * taboolib-gradle-plugin
 * io.izzel.taboolib.gradle.OptimizeFileReader
 *
 * @author sky
 * @since 2021/8/14 1:18 下午
 */
class OptimizeFileReader(project: Project, input: InputStream) {

    val optimize = ArrayList<Optimize>()
    val group = ArrayList<Group>()
    val exclude = ArrayList<String>()

    init {
        try {
            val jsonSource = input.readBytes().toString(StandardCharsets.UTF_8)
            val json = JsonParser.parseString(jsonSource).asJsonObject
            if (json.has("optimize")) {
                json.getAsJsonArray("optimize").forEach { ele ->
                    optimize += Optimize(
                        relocate(project, ele.asJsonObject["class"].asString),
                        Optimize.Type.valueOf(ele.asJsonObject["type"].asString)
                    )
                }
            }
            if (json.has("group")) {
                json.getAsJsonArray("group").forEach { ele ->
                    var depend: Group.Depend? = null
                    if (ele.asJsonObject.has("depend")) {
                        depend = Group.Depend(
                            ele.asJsonObject["depend"].asJsonObject["name"].asJsonArray.map { relocate(project, it.asString) },
                            ele.asJsonObject["depend"].asJsonObject["exclude"].asJsonArray.map { relocate(project, it.asString) }
                        )
                    }
                    group += Group(
                        ele.asJsonObject["check"].asJsonArray.map { relocate(project, it.asString) },
                        ele.asJsonObject["member"].asJsonArray.map { relocate(project, it.asString) },
                        ele.asJsonObject["exclude"]?.asJsonArray?.map { relocate(project, it.asString) }?.toList() ?: emptyList(),
                        depend,
                        Group.Mode.valueOf(ele.asJsonObject["mode"].asString)
                    )
                }
            }
            if (json.has("exclude")) {
                exclude += json.getAsJsonArray("exclude").map { relocate(project, it.asString) }
            }
        } catch (ex: Throwable) {
            ex.printStackTrace()
        }
    }

    fun relocate(project: Project, name: String): String {
        return if (name.startsWith("taboolib")) {
            project.group.toString().replace('.', '/') + '/' + name.replace('.', '/')
        } else {
            name.replace('.', '/')
        }
    }

    fun exclude(name: String, use: Map<String, Set<String>>): Boolean {
        if (exclude.any { name.startsWith(it) }) {
            return true
        }
        if (group.any { it.exclude(name, use) }) {
            return true
        }
        return false
    }

    class Optimize(val path: String, val type: Type) {

        enum class Type {

            METHOD
        }
    }

    class Group(check: List<String>, member: List<String>, exclude: List<String>, val depend: Depend? = null, val mode: Mode) {

        val check = check.platformFlatten()

        val member = member.platformFlatten()

        val exclude = exclude.platformFlatten()

        class Depend(name: List<String>, exclude: List<String>) {

            val name = name.platformFlatten()

            val exclude = exclude.platformFlatten()
        }

        enum class Mode {

            REMOVE
        }

        fun exclude(name: String, use: Map<String, Set<String>>): Boolean {
            if (member.any { name.startsWith(it) }) {
                if (depend != null) {
                    val fail = depend.name.any { n ->
                        val set = use[n]!!.toMutableList()
                        set.remove(n)
                        set.removeAll(depend.exclude)
                        set.isNotEmpty()
                    }
                    if (fail) {
                        return false
                    }
                }
                for (s in check) {
                    val set = use[s]!!.toMutableList()
                    set.remove(s)
                    set.removeAll(check)
                    set.removeAll(member)
                    set.removeAll(exclude)
                    if (set.isNotEmpty()) {
                        return false
                    }
                }
                return true
            } else {
                return false
            }
        }
    }
}