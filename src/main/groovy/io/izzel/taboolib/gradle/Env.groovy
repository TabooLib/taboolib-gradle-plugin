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
    String repoTabooLib = "https://repo.tabooproject.org/repository/releases"

    /** Library 文件 */
    String fileLibs = "libraries"

    /** Assets 文件 */
    String fileAssets = "assets"

    /** 启用隔离加载器（完全隔离模式）*/
    boolean enableIsolatedClassloader = false

    /** 非开发者模式下是否在被跳过版本上关闭插件 */
    boolean disableOnSkippedVersion = true

    /** 非开发者模式下是否在不支持版本上关闭插件 */
    boolean disableOnUnsupportedVersion = true

    /** 在原始加载器初始化失败时是否强制关闭服务器 */
    boolean disableWhenPrimitiveLoaderError = false

    /** 强制使用旧版依赖处理工具 */
    boolean enableLegacyDependencyResolver = false

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
