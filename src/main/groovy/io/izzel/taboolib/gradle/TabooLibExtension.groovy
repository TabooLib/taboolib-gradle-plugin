//file:noinspection unused
package io.izzel.taboolib.gradle

import groovy.transform.Canonical
import io.izzel.taboolib.gradle.description.Description
import org.gradle.api.Action

@Canonical
class TabooLibExtension {

    /**
     * 是否为子模块（不进行重定向，不产生描述文件）
     */
    boolean subproject = false

    /** 描述文件 */
    Description des = new Description()

    /** 环境文件 */
    Env env = new Env()

    /** 版本文件 */
    Version version = new Version()

    /** 排除文件 */
    List<String> exclude = []

    /** 重定向 */
    Map<String, String> relocation = new LinkedHashMap<>()

    /** 设置 */
    List<String> options = []

    /** 根包名 */
    String rootPackage = null

    /** 分类 */
    String classifier = null

    /** 排除文件 */
    def exclude(String match) {
        exclude += match
    }

    /** 重定向 */
    def relocate(String pre, String post) {
        relocation[pre] = post
    }

    /** 描述文件构造器 */
    def description(Action<? super Description> action) {
        action.execute(des)
    }

    /** 环境文件构造器 */
    def env(Action<? super Env> action) {
        action.execute(env)
    }

    /** 版本文件构造器 */
    def version(Action<? super Version> action) {
        action.execute(version)
    }
}