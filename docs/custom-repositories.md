# 自定义仓库配置

TabooLib Gradle Plugin 支持添加自定义 Maven 仓库，以便从私有仓库或其他第三方仓库获取依赖。

## 基本用法

在你的 `build.gradle` 或 `build.gradle.kts` 文件中，可以通过 `taboolib` 配置块添加自定义仓库：

### Groovy DSL

```groovy
taboolib {
    // 添加自定义仓库
    repository("my-repo", "https://my-custom-repo.com/maven")
    repository("private-repo", "https://private.company.com/nexus")
    
    // 其他配置...
    version {
        taboolib = "6.0.12"
    }
}
```

### Kotlin DSL

```kotlin
taboolib {
    // 添加自定义仓库
    repository("my-repo", "https://my-custom-repo.com/maven")
    repository("private-repo", "https://private.company.com/nexus")
    
    // 其他配置...
    version {
        taboolib = "6.0.12"
    }
}
```

## 方法说明

### `repository(name, url)`

添加一个自定义仓库到项目中。

**参数：**
- `name` (String): 仓库名称，用于标识仓库
- `url` (String): 仓库的 URL 地址

**示例：**
```groovy
repository("aliyun", "https://maven.aliyun.com/repository/central")
```

## 常见用例

### 1. 使用阿里云镜像加速

```groovy
taboolib {
    repository("aliyun-central", "https://maven.aliyun.com/repository/central")
    repository("aliyun-public", "https://maven.aliyun.com/repository/public")
}
```

### 2. 添加私有企业仓库

```groovy
taboolib {
    repository("company-nexus", "https://nexus.company.com/repository/maven-public/")
    repository("company-releases", "https://nexus.company.com/repository/maven-releases/")
}
```

### 3. 使用 JitPack 仓库

```groovy
taboolib {
    repository("jitpack", "https://jitpack.io")
}
```

### 4. 添加 Sonatype 快照仓库

```groovy
taboolib {
    repository("sonatype-snapshots", "https://oss.sonatype.org/content/repositories/snapshots/")
}
```

## 仓库优先级

自定义仓库会在项目评估（`afterEvaluate`）阶段添加到项目中，因此它们的优先级会高于在 `taboolib` 配置块之后添加的仓库。

默认情况下，插件会自动添加以下仓库：
1. `https://repo.tabooproject.org/repository/releases/` (TabooLib 官方仓库)
2. `https://repo.spongepowered.org/maven` (Sponge 仓库)

自定义仓库会在这些默认仓库之后添加。

## 认证配置

对于需要认证的私有仓库，需要在 Gradle 的全局配置中设置凭据：

### 方式一：在 `~/.gradle/gradle.properties` 中配置

```properties
myRepoUsername=your-username
myRepoPassword=your-password
```

然后在 `build.gradle` 中引用：

```groovy
repositories {
    maven {
        url = "https://private.company.com/nexus"
        credentials {
            username = project.findProperty("myRepoUsername") ?: System.getenv("REPO_USERNAME")
            password = project.findProperty("myRepoPassword") ?: System.getenv("REPO_PASSWORD")
        }
    }
}
```

### 方式二：使用环境变量

```bash
export REPO_USERNAME=your-username
export REPO_PASSWORD=your-password
```

**注意：** 当前 `repository()` 方法只支持添加简单的 Maven 仓库 URL。如果需要认证或其他高级配置，请直接在 `repositories` 块中配置。

## 完整示例

```groovy
plugins {
    id 'io.izzel.taboolib' version '2.0.23'
}

taboolib {
    // 添加自定义仓库
    repository("aliyun-central", "https://maven.aliyun.com/repository/central")
    repository("jitpack", "https://jitpack.io")
    
    // 版本配置
    version {
        taboolib = "6.0.12"
        coroutines = "1.7.3"
    }
    
    // 环境配置
    env {
        install("common", "common-util", "platform-bukkit")
    }
    
    // 描述配置
    description {
        name = "MyPlugin"
        version = "1.0.0"
        main = "com.example.MyPlugin"
        author = "YourName"
        depend = ["TabooLib"]
    }
}
```

## 故障排除

### 1. 仓库无法访问

如果遇到仓库无法访问的问题，请检查：
- 网络连接是否正常
- 仓库 URL 是否正确
- 是否需要认证凭据

### 2. 依赖解析失败

如果依赖解析失败，请确保：
- 自定义仓库中确实包含所需的依赖
- 依赖的坐标（group:artifact:version）是否正确
- 仓库的访问权限是否正确配置

### 3. 构建缓慢

如果构建速度较慢，建议：
- 使用地理位置更近的镜像仓库
- 检查网络连接速度
- 考虑使用企业级 Maven 仓库管理器

## 更多信息

- [TabooLib 官方文档](https://docs.tabooproject.org/)
- [Gradle 仓库配置文档](https://docs.gradle.org/current/userguide/declaring_repositories.html)