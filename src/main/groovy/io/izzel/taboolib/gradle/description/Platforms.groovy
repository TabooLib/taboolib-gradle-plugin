package io.izzel.taboolib.gradle.description

enum Platforms {

    BUKKIT('Bukkit', 'platform-bukkit', 'plugin.yml', new BuilderBukkit()),

    NUKKIT('Nukkit', 'platform-nukkit', 'nukkit.yml', new BuilderNukkit()),

    BUNGEE('Bungee', 'platform-bungee', 'bungee.yml', new BuilderBungee()),

    VELOCITY('Velocity', 'platform-velocity', 'velocity-plugin.json', new BuilderVelocity()),

    SPONGE7('Sponge7', 'platform-sponge-api7', 'mcmod.info', new BuilderSponge7()),

    SPONGE8('Sponge8', 'platform-sponge-api8', 'META-INF/plugins.json', new BuilderSponge8());

    String key
    String module
    String file
    Builder builder

    Platforms(key, module, file, builder) {
        this.key = key
        this.module = module
        this.file = file
        this.builder = builder
    }
}