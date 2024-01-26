package io.izzel.taboolib.gradle

import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

class Version {

    /** Coroutines 版本 */
    @Nullable String coroutines = "1.7.3"

    /** TabooLib 版本 */
    @NotNull String taboolib = "6.1.0-dev"

    /** 跳过 Kotlin */
    boolean skipKotlin = false

    /** 跳过 Kotlin 重定向 */
    boolean skipKotlinRelocate = false

    /** 跳过 TabooLib 重定向 */
    boolean skipTabooLibRelocate = false
}
