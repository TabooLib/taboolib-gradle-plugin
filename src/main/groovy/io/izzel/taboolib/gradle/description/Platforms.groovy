package io.izzel.taboolib.gradle.description

enum Platforms {

    BUKKIT('Bukkit', 'platform-bukkit', 'plugin.yml', new BuilderBukkit()),

    NUKKIT('Nukkit', 'platform-nukkit', 'nukkit.yml', new BuilderNukkit()),

    BUNGEE('Bungee', 'platform-bungee', 'bungee.yml', new BuilderBungee()),

    VELOCITY('Velocity', 'platform-velocity', 'velocity-plugin.json', new BuilderVelocity()),

    CLOUDNETV3('CloudNetV3', 'platform-cloudnet-v3', 'module.json', new BuilderCloudNetV3()),

    SPONGE7('Sponge7', 'platform-sponge-api7', 'mcmod.info', new BuilderSponge7()),

    SPONGE8('Sponge8', 'platform-sponge-api8', 'META-INF/plugins.json', new BuilderSponge8()),

    AFYBROKER("AfyBroker", "platform-afybroker", "broker.yml", new BuilderAfyBroker(), false);

    String key
    String module
    String file
    Builder builder
    boolean hasImpl

    Platforms(key, module, file, builder) {
        this.key = key
        this.module = module
        this.file = file
        this.builder = builder
        this.hasImpl = true
    }

    Platforms(String key, String module, String file, Builder builder, boolean hasImpl) {
        this.key = key
        this.module = module
        this.file = file
        this.builder = builder
        this.hasImpl = hasImpl
    }
}