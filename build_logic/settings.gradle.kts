@file:Suppress("UnstableApiUsage")

// 开启模块的简化依赖方式，例如：module.course.api.course
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
  repositories {
    includeBuild(".")
    gradlePluginPortal()
    mavenCentral()
    google()
    maven("https://maven.aliyun.com/repository/public")
    maven("https://maven.aliyun.com/repository/google")
    maven("https://jitpack.io")
  }
}

dependencyResolutionManagement {
  repositories {
    mavenCentral()
    google()
    maven("https://maven.aliyun.com/repository/public")
    maven("https://maven.aliyun.com/repository/google")
    maven("https://jitpack.io")
  }
  versionCatalogs {
    // 这个 libs 名字是固定的，搞了好久才解决这个问题
    create("libs") {
      // 这个 libs.versions.toml 名字也必须固定，不能改成其他的
      from(files("../gradle/libs.versions.toml"))
    }
  }
}

rootProject.name = "build_logic"
// 项目模块插件
include(":manager")
//其他业务插件
include(":plugin")
include(":plugin:cache")
include(":plugin:checker")