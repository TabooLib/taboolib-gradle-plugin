package io.izzel.taboolib.gradle

@Deprecated("请使用 Basic", replaceWith = ReplaceWith("Basic"))
const val CONFIGURATION = "basic-configuration"

@Deprecated("请使用 Basic", replaceWith = ReplaceWith("Basic"))
const val EXPANSION_SUBMIT_CHAIN = "basic-submit-chain"

@Deprecated("请使用 MinecraftChat", replaceWith = ReplaceWith("MinecraftChat"))
const val CHAT = "minecraft-chat"

@Deprecated("请使用 MinecraftEffect", replaceWith = ReplaceWith("MinecraftEffect"))
const val EFFECT = "minecraft-effect"

@Deprecated("请使用 I18n", replaceWith = ReplaceWith("I18n"))
const val LANG = "minecraft-i18n"

@Deprecated("请使用 Kether", replaceWith = ReplaceWith("Kether"))
const val KETHER = "minecraft-kether"

@Deprecated("请使用 Metrics", replaceWith = ReplaceWith("Metrics"))
const val METRICS = "minecraft-metrics"

@Deprecated("请使用 Porticus", replaceWith = ReplaceWith("Porticus"))
const val PORTICUS = "minecraft-porticus"

@Deprecated("请使用 CommandHelper", replaceWith = ReplaceWith("CommandHelper"))
const val EXPANSION_COMMAND_HELPER = "minecraft-command-helper"

@Deprecated("请使用 BukkitNMS", replaceWith = ReplaceWith("BukkitNMS"))
const val NMS = "bukkit-nms"

@Deprecated("请使用 BukkitNMSUtil", replaceWith = ReplaceWith("BukkitNMSUtil"))
val NMS_UTIL = arrayOf("$NMS-legacy", "$NMS-stable", "$NMS-tag", "$NMS-tag-12005", "$NMS-tag-legacy", NMS)

@Deprecated("请使用 BukkitNMSEntityAI", replaceWith = ReplaceWith("BukkitNMSEntityAI"))
val AI = arrayOf("bukkit-nms-ai", NMS)

@Deprecated("请使用 BukkitHook", replaceWith = ReplaceWith("BukkitHook"))
const val BUKKIT_HOOK = "bukkit-hook"

@Deprecated("请使用 BukkitUtil", replaceWith = ReplaceWith("BukkitUtil"))
const val BUKKIT_UTIL = "bukkit-util"

@Deprecated("请使用 BukkitNavigation", replaceWith = ReplaceWith("BukkitNavigation"))
val NAVIGATION = arrayOf("bukkit-navigation", NMS)

@Deprecated("请使用 BukkitUI", replaceWith = ReplaceWith("BukkitUI"))
val UI = arrayOf("bukkit-ui", "bukkit-ui-12100", "bukkit-ui-legacy", CHAT, NMS)

@Deprecated("请使用 XSeries", replaceWith = ReplaceWith("XSeries"))
val BUKKIT_XSERIES = arrayOf("bukkit-xseries", "bukkit-xseries-item")

@Deprecated("请使用 BukkitFakeOp", replaceWith = ReplaceWith("BukkitFakeOp"))
const val EXPANSION_PLAYER_FAKE_OP = "bukkit-fake-op"

@Deprecated("请使用 Database", replaceWith = ReplaceWith("Database"))
const val DATABASE = "database"

@Deprecated("请使用 DatabaseAlkaidRedis", replaceWith = ReplaceWith("DatabaseAlkaidRedis"))
const val EXPANSION_REDIS = "database-alkaid-redis"

@Deprecated("请使用 DatabaseIoc", replaceWith = ReplaceWith("DatabaseIoc"))
const val EXPANSION_IOC = "database-ioc"

@Deprecated("请使用 DatabaseLettuceRedis", replaceWith = ReplaceWith("DatabaseLettuceRedis"))
const val EXPANSION_LETTUCE_REDIS = "database-lettuce-redis"

@Deprecated("请使用 DatabasePlayer", replaceWith = ReplaceWith("DatabasePlayer"))
const val EXPANSION_PLAYER_DATABASE = "database-player"

@Deprecated("请使用 DatabasePtc", replaceWith = ReplaceWith("DatabasePtc"))
const val EXPANSION_PTC = "database-ptc"

@Deprecated("请使用 DatabasePtcObject", replaceWith = ReplaceWith("DatabasePtcObject"))
const val EXPANSION_PTC_OBJECT = "database-ptc-object"

@Deprecated("请使用 Javascript", replaceWith = ReplaceWith("Javascript"))
const val EXPANSION_JAVASCRIPT = "script-javascript"

@Deprecated("请使用 Jexl", replaceWith = ReplaceWith("Jexl"))
const val EXPANSION_JEXL = "script-jexl"

@Deprecated("保留字段")
const val EXPANSION_GEEK_TOOL = "geek-tool"

@Deprecated("保留字段")
const val APPLICATION = "platform-application"

@Deprecated("保留字段")
const val BUKKIT = "platform-bukkit"

@Deprecated("保留字段")
const val BUNGEE = "platform-bungee"

@Deprecated("保留字段")
const val VELOCITY = "platform-velocity"

@Deprecated("保留字段")
const val AFYBROKER = "platform-afybroker"

@Deprecated("保留字段")
val BUKKIT_ALL = arrayOf(BUKKIT, BUKKIT_HOOK, BUKKIT_UTIL, *BUKKIT_XSERIES)

@Deprecated("保留字段")
val UNIVERSAL = arrayOf(CHAT, CONFIGURATION, LANG, EXPANSION_COMMAND_HELPER)

@Deprecated("保留字段")
val UNIVERSAL_DATABASE = arrayOf(DATABASE, EXPANSION_PTC_OBJECT)