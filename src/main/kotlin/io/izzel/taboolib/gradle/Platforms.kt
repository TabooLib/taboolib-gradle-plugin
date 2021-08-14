package io.izzel.taboolib.gradle

val platforms = listOf("Bukkit", "Nukkit", "Bungee", "Sponge7", "Sponge8", "Velocity")

fun List<String>.platformFlatten(): List<String> {
    return flatMap {
        if (it.contains("{platform}")) {
            platforms.map { p -> it.replace("{platform}", p) }
        } else {
            listOf(it)
        }
    }
}