package io.izzel.taboolib.gradle

import com.google.gson.GsonBuilder
import org.gradle.api.Project
import com.google.gson.JsonArray
import com.google.gson.JsonObject

import java.nio.charset.StandardCharsets

class Description {

    def authors
    def contributors
    def depend
    def softdepend
    def loadbefore
    def website
    def prefix
    def load
    def api = "1.0.0"
    def apiVersion = '1.13'
    def libraries
    def dependencies
    def requiredMods = "spongeapi@7.2.0"
    def custom = new HashMap<String, Object>()

    static List<String> buildFile() {
        def str = []
        str += ""
        str += ""
        str += "#         Powered by TabooLib 6.0         #"
        str += ""
        str += ""
        return str
    }

    static def appendName(str, any, key) {
        if (any != null) {
            str.add("$key: $any")
        }
    }

    static def appendNameList(str, any, key) {
        if (any instanceof List<String>) {
            str.add("$key:")
            for (i in any) {
                str.add("  - '${i}'")
            }
        } else if (any != null) {
            str.add("$key:")
            str.add("  - '${any}'")
        }
    }

    byte[] buildBukkitFile(Project project) {
        def str = buildFile()
        str += "name: ${project.name}"
        str += "main: ${project.group}.taboolib.platform.BukkitPlugin"
        appendName(str, load, "load")
        str += "version: ${project.version}"
        if (apiVersion != null) {
            str += "api-version: ${apiVersion}"
        }
        appendName(str, prefix, "prefix")
        appendName(str, website, "website")
        appendNameList(str, authors, "authors")
        appendNameList(str, contributors, "contributors")
        appendNameList(str, depend, "depend")
        appendNameList(str, softdepend, "softdepend")
        appendNameList(str, loadbefore, "loadbefore")
        appendNameList(str, libraries, "libraries")
        custom.entrySet().each {
            if (it.value instanceof List) {
                appendNameList(str, it.value, it.key)
            } else {
                appendName(str, it.value, it.key)
            }
        }
        return str.join("\n").getBytes(StandardCharsets.UTF_8)
    }

    byte[] buildBungeeFile(Project project) {
        def str = buildFile()
        str += "name: ${project.name}"
        str += "main: ${project.group}.taboolib.platform.BungeePlugin"
        str += "version: ${project.version}"
        appendName(str, authors, "author")
        appendNameList(str, depend, "depends")
        appendNameList(str, softdepend, "softDepends")
        appendNameList(str, libraries, "libraries")
        custom.entrySet().each {
            if (it.value instanceof List) {
                appendNameList(str, it.value, it.key)
            } else {
                appendName(str, it.value, it.key)
            }
        }
        return str.join("\n").getBytes(StandardCharsets.UTF_8)
    }

    byte[] buildNukkitFile(Project project) {
        def str = buildFile()
        str += "name: ${project.name}"
        str += "main: ${project.group}.taboolib.platform.NukkitPlugin"
        appendName(str, load, "load")
        str += "version: ${project.version}"
        if (api != null) {
            str += "api: ${api}"
        }
        appendName(str, prefix, "prefix")
        appendName(str, website, "website")
        appendNameList(str, authors, "authors")
        appendNameList(str, depend, "depend")
        appendNameList(str, softdepend, "softdepend")
        appendNameList(str, loadbefore, "loadbefore")
        appendNameList(str, libraries, "libraries")
        custom.entrySet().each {
            if (it.value instanceof List) {
                appendNameList(str, it.value, it.key)
            } else {
                appendName(str, it.value, it.key)
            }
        }
        return str.join("\n").getBytes(StandardCharsets.UTF_8)
    }

    byte[] buildSpongeFile(Project project) {
        def base = new JsonArray()
        def info = new JsonObject()
        info.addProperty("modid", project.name.toLowerCase())
        info.addProperty("name", project.name)
        info.addProperty("version", project.version.toString())
        if (website != null) {
            info.addProperty("url", website.toString())
        }
        if (authors instanceof List<String>) {
            def arr = new JsonArray()
            authors.each { arr.add(it) }
            info.add("authorList", arr)
        } else if (authors != null) {
            def arr = new JsonArray()
            arr.add(authors.toString())
            info.add("authorList", arr)
        }
        if (dependencies instanceof List<String>) {
            def arr = new JsonArray()
            if (dependencies.none { "spongeapi" in it }) {
                arr.add("spongeapi@7.2.0")
            }
            dependencies.each { arr.add(it) }
            info.add("dependencies", arr)
        } else if (dependencies != null) {
            def arr = new JsonArray()
            arr.add(dependencies.toString())
            info.add("dependencies", arr)
        } else {
            def arr = new JsonArray()
            arr.add("spongeapi@7.2.0")
            info.add("dependencies", arr)
        }
        if (requiredMods instanceof List<String>) {
            def arr = new JsonArray()
            requiredMods.each { arr.add(it) }
            info.add("requiredMods", arr)
        } else if (requiredMods != null) {
            def arr = new JsonArray()
            arr.add(requiredMods.toString())
            info.add("requiredMods", arr)
        }
        base.add(info)
        return new GsonBuilder().setPrettyPrinting().create().toJson(base).getBytes(StandardCharsets.UTF_8)
    }

    byte[] buildVelocityFile(Project project) {
        def base = new JsonArray()
        def info = new JsonObject()
        info.addProperty("id", project.name.toLowerCase())
        info.addProperty("name", project.name)
        info.addProperty("main", "${project.group}.taboolib.platform.VelocityPlugin")
        info.addProperty("version", project.version.toString())
        if (authors instanceof List<String>) {
            def arr = new JsonArray()
            authors.each { arr.add(it) }
            info.add("authors", arr)
        } else if (authors != null) {
            def arr = new JsonArray()
            arr.add(authors.toString())
            info.add("authors", arr)
        }
        if (dependencies instanceof List<String>) {
            def arr = new JsonArray()
            dependencies.each { arr.add(it) }
            info.add("dependencies", arr)
        } else if (dependencies != null) {
            def arr = new JsonArray()
            arr.add(dependencies.toString())
            info.add("dependencies", arr)
        }
        base.add(info)
        return new GsonBuilder().setPrettyPrinting().create().toJson(base).getBytes(StandardCharsets.UTF_8)
    }

    def author(author) {
        this.authors = author
    }

    def contributor(contributor) {
        this.contributors = contributor
    }

    def authors(author) {
        this.authors = author
    }

    def contributors(contributor) {
        this.contributors = contributor
    }

    def depend(depend) {
        this.depend = depend
    }

    def softdepend(softdepend) {
        this.softdepend = softdepend
    }

    def loadbefore(loadbefore) {
        this.loadbefore = loadbefore
    }

    def website(website) {
        this.website = website
    }

    def prefix(prefix) {
        this.prefix = prefix
    }

    def load(load) {
        this.load = load
    }

    def api(api) {
        this.api = api
    }

    def apiVersion(apiVersion) {
        this.apiVersion = apiVersion
    }

    def libraries(libraries) {
        this.libraries = libraries
    }

    def dependencies(dependencies) {
        this.dependencies = dependencies
    }

    def requiredMods(requiredMods) {
        this.requiredMods = requiredMods
    }

    def custom(String key, value) {
        this.custom[key] = value
    }
}
