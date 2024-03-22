//file:noinspection unused
package io.izzel.taboolib.gradle

class Env {

    /** 调试模式 */
    boolean debug = false

    /** 开发模式强制下载 */
    boolean forceDownloadInDev = true

    /** 中央仓库地址 */
    String repoCentral = "https://maven.aliyun.com/repository/central"

    /** TabooLib 仓库地址 */
    String repoTabooLib = "http://sacredcraft.cn:8081/repository/releases"

    /** Library 文件 */
    String fileLibs = "libraries"

    /** Assets 文件 */
    String fileAssets = "assets"

    /** 启用隔离加载器（完全隔离模式）*/
    boolean enableIsolatedClassloader = false

    /** 安装模块 */
    Set<String> modules = ["common", "common-env", "common-util", "common-legacy-api", "common-platform-api", "common-reflex"]

    /** 安装模块 */
    def install(Object... name) {
        name.each {
            if (it instanceof String[]) modules.addAll(it)
            else modules += it.toString()
        }
    }
}
