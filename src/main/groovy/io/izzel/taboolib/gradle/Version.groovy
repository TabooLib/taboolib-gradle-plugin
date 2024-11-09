package io.izzel.taboolib.gradle


import org.jetbrains.annotations.Nullable

class Version {

    /** Coroutines 版本 */
    @Nullable
    String coroutines = "1.7.3"

    /** TabooLib 版本 */
    @Nullable
    String taboolib = null

    /** 跳过 Kotlin */
    boolean skipKotlin = false

    /** 跳过 Kotlin 重定向 */
    boolean skipKotlinRelocate = false

    /** 跳过 TabooLib 重定向 */
    boolean skipTabooLibRelocate = false

    /** 跳过版本描述文件 */
    boolean skipVersionFile = false

    /** 跳过平台描述文件 */
    boolean skipPlatformFile = false
}
