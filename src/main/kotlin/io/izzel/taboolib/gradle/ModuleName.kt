package io.izzel.taboolib.gradle

/**
 * 基础模块：配置文件，任务链
 */
val Basic = arrayOf(
    "basic-configuration",
    "basic-submit-chain",
)

/**
 * Bukkit 虚拟 OP 工具
 */
val BukkitFakeOp = arrayOf(
    "bukkit-fake-op",
    "bukkit-nms"
)

/**
 * Bukkit 与 Vault、PlaceholderAPI 等插件交互
 */
val BukkitHook = arrayOf(
    "bukkit-hook",
)

/**
 * Bukkit 寻路工具
 */
val BukkitNavigation = arrayOf(
    "bukkit-navigation",
    "bukkit-nms"
)

/**
 * Bukkit 箱子菜单
 */
val BukkitUI = arrayOf(
    "bukkit-ui",
    "bukkit-ui-12100",
    "bukkit-ui-legacy",
    "bukkit-util",
    "bukkit-xseries",
    "bukkit-xseries-item",
    "bukkit-nms",
    "minecraft-chat",
)

/**
 * Bukkit 扩展工具
 */
val BukkitUtil = arrayOf(
    "bukkit-util",
    "bukkit-xseries",
    "minecraft-chat",
    "minecraft-i18n",
    "basic-configuration"
)

/**
 * XSeries
 */
val XSeries = arrayOf(
    "bukkit-xseries",
)

/**
 * XSeries（XSkull 及 ItemBuilder）
 */
val XSeriesItem = arrayOf(
    "bukkit-xseries",
    "bukkit-xseries-item",
)

/**
 * Bukkit NMS
 */
val BukkitNMS = arrayOf(
    "bukkit-nms",
)

/**
 * Bukkit NMS 扩展工具
 */
val BukkitNMSUtil = arrayOf(
    "bukkit-nms-legacy",
    "bukkit-nms-stable",
    "bukkit-nms-tag",
    "bukkit-nms-tag-12005",
    "bukkit-nms-tag-legacy",
    "bukkit-nms",
    *BukkitUtil,
)

/**
 * Bukkit NMS ItemTag 工具
 */
val BukkitNMSItemTag = arrayOf(
    "bukkit-nms-tag",
    "bukkit-nms-tag-12005",
    "bukkit-nms-tag-legacy",
    "bukkit-nms",
    "minecraft-chat",
)

/**
 * Bukkit NMS 数据序列化工具
 */
val BukkitNMSDataSerializer = arrayOf(
    "bukkit-nms-data-serializer",
    "bukkit-nms"
)

/**
 * Bukkit NMS 实体 AI
 */
val BukkitNMSEntityAI = arrayOf(
    "bukkit-nms-ai",
    "bukkit-nms"
)

/**
 * 数据库
 */
val Database = arrayOf(
    "database",
    "basic-configuration",
)

/**
 * Alkaid Redis
 */
val DatabaseAlkaidRedis = arrayOf(
    "database-alkaid-redis",
    "basic-configuration",
)

/**
 * IOC
 */
val DatabaseIoc = arrayOf(
    "database-ioc",
    "basic-configuration",
)

/**
 * Lettuce Redis
 */
val DatabaseLettuceRedis = arrayOf(
    "database-lettuce-redis",
    "basic-configuration",
)

/**
 * 玩家数据库
 */
val DatabasePlayer = arrayOf(
    "database-player",
    *Database
)

/**
 * Persistent Container
 */
val DatabasePtc = arrayOf(
    "database-ptc",
    *Database
)

/**
 * Persistent Container With Object
 */
val DatabasePtcObject = arrayOf(
    "database-ptc-object",
    *Database
)

/**
 * Minecraft 文本工具
 */
val MinecraftChat = arrayOf(
    "minecraft-chat",
)

/**
 * Minecraft 效果工具
 */
val MinecraftEffect = arrayOf(
    "minecraft-effect",
)

/**
 * 指令帮助
 */
val CommandHelper = arrayOf(
    "minecraft-command-helper",
    "minecraft-chat",
    "minecraft-i18n",
)

/**
 * 国际化接口
 */
val I18n = arrayOf(
    "minecraft-i18n",
    "minecraft-chat",
    "basic-configuration",
)

/**
 * Kether 脚本引擎
 */
val Kether = arrayOf(
    "minecraft-kether",
    "minecraft-chat",
    "minecraft-i18n",
    "bukkit-nms",
    "bukkit-nms-stable",
    "basic-configuration",
)

/**
 * BStats 数据统计
 */
val Metrics = arrayOf(
    "minecraft-metrics",
    "basic-configuration",
)

/**
 * BungeeCord 通讯
 */
val Porticus = arrayOf(
    "minecraft-porticus",
    "basic-configuration",
)

/**
 * Javascript 脚本环境
 */
val JavaScript = arrayOf("script-javascript")

/**
 * Jexl 脚本环境
 */
val Jexl = arrayOf("script-jexl")

/**
 * Afybroker 平台
 */
val AfyBroker = arrayOf("platform-afybroker")

/**
 * 独立程序
 */
val App = arrayOf("platform-application")

/**
 * Bukkit 平台
 */
val Bukkit = arrayOf("platform-bukkit")

/**
 * BungeeCord 平台
 */
val BungeeCord = arrayOf("platform-bungee")

/**
 * Velocity 平台
 */
val Velocity = arrayOf("platform-velocity")