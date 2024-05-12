package io.izzel.taboolib.gradle

const val CHAT = "module-chat"

const val CONFIGURATION = "module-configuration"

val LANG = arrayOf("module-lang", CONFIGURATION)

val KETHER = arrayOf("module-kether",  CONFIGURATION)

val METRICS = arrayOf("module-metrics", CONFIGURATION)

val DATABASE = arrayOf("module-database", CONFIGURATION)

const val NMS = "module-nms"

val NMS_UTIL = arrayOf("module-nms-util", NMS)

val NAVIGATION = arrayOf("module-navigation", NMS)

val AI = arrayOf("module-ai", NMS)

val UI = arrayOf("module-ui", CHAT, NMS)

const val EFFECT = "module-effect"

const val PORTICUS = "module-porticus"

const val EXPANSION_REDIS = "expansion-alkaid-redis"

const val EXPANSION_COMMAND_HELPER = "expansion-command-helper"

const val EXPANSION_GEEK_TOOL = "expansion-geek-tool"

const val EXPANSION_IOC = "expansion-ioc"

const val EXPANSION_JAVASCRIPT = "expansion-javascript"

const val EXPANSION_PLAYER_DATABASE = "expansion-player-database"

const val EXPANSION_PLAYER_FAKE_OP = "expansion-player-fake-op"

const val EXPANSION_SUBMIT_CHAIN = "expansion-submit-chain"

const val EXPANSION_PTC = "expansion-persistent-container"

const val EXPANSION_PTC_OBJECT = "expansion-persistent-container-object"

const val EXPANSION_JEXL = "expansion-jexl"

const val EXPANSION_FOLIA = "expansion-folia"

const val EXPANSION_LETTUCE_REDIS = "expansion-lettuce-redis"

const val APPLICATION = "platform-application"

const val BUKKIT = "platform-bukkit"

const val BUKKIT_HOOK = "module-bukkit-hook"

const val BUKKIT_UTIL = "module-bukkit-util"

const val BUKKIT_XSERIES = "module-bukkit-xseries"

const val BUNGEE = "platform-bungee"

const val VELOCITY = "platform-velocity"

const val AFYBROKER = "platform-afybroker"

/** Bukkit 完整模块 */
val BUKKIT_ALL = arrayOf(BUKKIT, BUKKIT_HOOK, BUKKIT_UTIL, BUKKIT_XSERIES)

/** 泛用模块 */
val UNIVERSAL = arrayOf(CHAT, CONFIGURATION, *LANG, EXPANSION_COMMAND_HELPER)

/** 通用数据储存 */
val UNIVERSAL_DATABASE = arrayOf(*DATABASE, EXPANSION_PTC_OBJECT)