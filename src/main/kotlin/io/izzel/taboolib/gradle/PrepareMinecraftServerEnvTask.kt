package io.izzel.taboolib.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.*
import org.gradle.api.tasks.*
import org.gradle.api.tasks.options.*
import java.io.File

abstract class PrepareMinecraftServerEnvTask : DefaultTask() {

    init {
        group = "TabooLibLoom"
    }

    @get:Input
    @get:Option(option = "jarUrl", description = "To download the server jar.")
    abstract val jarUrl: Property<String>

    @get:Input
    @get:Optional
    @get:Option(option = "jarName", description = "The name of the downloaded Jar file")
    abstract val jarName: Property<String>

    private val jarNameOrDefault
        get() = jarName.orElse("server.jar")

    @get:Input
    @get:Optional
    @get:Option(option = "serverDirectory", description = "For storing server data.")
    abstract val serverDirectory: DirectoryProperty

    private val serverDirectoryOrDefault
        get() = serverDirectory.orElse(project.layout.buildDirectory.dir("MinecraftServer"))

    @get:Input
    @get:Optional
    @get:Option(option = "agreeEula", description = "Agree to the Minecraft EULA")
    abstract val agreeEula: Property<Boolean>

    private val agreeEulaOrDefault
        get() = agreeEula.orElse(false)

    @TaskAction
    fun launchServer() {
        val jarUrl = jarUrl.get()
        val serverDirectory = serverDirectoryOrDefault.get().asFile.apply(File::mkdirs)
        val jarFile = serverDirectory.resolve(jarNameOrDefault.get())
        if (jarFile.exists().not()) {
            logger.lifecycle(
                """
                    Downloading Jar
                        url : $jarUrl
                        dest : ${jarFile.absolutePath}
                """.trimIndent()
            )
            downloadFile(jarUrl, jarFile)
        }
        if (agreeEulaOrDefault.get()) {
            val eulaFile = serverDirectory.resolve("eula.txt")
            if (eulaFile.exists().not() || eulaFile.readText().contains("eula=true").not()) {
                eulaFile.writeText("eula=true")
            }
        }
    }

    private fun downloadFile(url: String, dest: File) {
        ant.invokeMethod("get", mapOf("src" to url, "dest" to dest))
    }
}
