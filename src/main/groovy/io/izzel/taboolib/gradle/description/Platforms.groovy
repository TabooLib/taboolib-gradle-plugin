package io.izzel.taboolib.gradle.description

enum Platforms {

    BUKKIT('platform-bukkit', 'plugin.yml', new BuilderBukkit()),

    NUKKIT('platform-nukkit', 'nukkit.yml', new BuilderNukkit()),

    BUNGEE('platform-bungee', 'bungee.yml', new BuilderBungee()),

    VELOCITY('platform-velocity', 'velocity-plugin.json', new BuilderVelocity()),

    SPONGE7('platform-sponge-api7', 'mcmod.info', new BuilderSponge7()),

    SPONGE8('platform-sponge-api8', 'META-INF/plugins.json', new BuilderSponge8());

    String module
    String file
    Builder builder

    Platforms(module, file, builder) {
        this.module = module
        this.file = file
        this.builder = builder
    }
}