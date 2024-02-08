package io.izzel.taboolib.gradle.description

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import io.izzel.taboolib.gradle.TabooLibExtension
import org.gradle.api.Project

import java.nio.charset.StandardCharsets

abstract class Builder {

    abstract byte[] build(Description description, Project project, TabooLibExtension tabooLibExt)

    static List<String> startBukkitFile() {
        def str = []
        str += ''
        str += ''
        str += '#         Powered by TabooLib 6.1         #'
        str += ''
        str += ''
        return str
    }

    static def writeLine(body) {
        body.add("")
    }

    static def write(List<String> body, data, key) {
        if (data != null) {
            body.add("$key: $data")
        }
    }

    static def write(JsonObject body, data, key) {
        if (data != null) {
            body.addProperty("$key", "$data")
        }
    }

    static def writeList(List<String> body, data, key) {
        if (data instanceof List<String>) {
            if (data.size() > 0) {
                body.add("$key:")
                for (i in data) {
                    body.add("  - '${i}'")
                }
            }
        } else if (data != null) {
            body.add("$key:")
            body.add("  - '${data}'")
        }
    }

    static def writeList(JsonObject body, data, key) {
        def arr = new JsonArray()
        if (data instanceof List<String>) {
            if (data.size() > 0) {
                data.each { arr.add(it) }
                body.add(key, arr)
            }
        } else if (data != null) {
            arr.add(data.toString())
            body.add(key, arr)
        }
    }

    static byte[] bytes(List<String> body) {
        return body.join('\n').getBytes(StandardCharsets.UTF_8)
    }

    static byte[] bytes(JsonElement body) {
        return new GsonBuilder().setPrettyPrinting().create().toJson(body).getBytes(StandardCharsets.UTF_8)
    }
}