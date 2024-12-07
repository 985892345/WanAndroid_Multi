import rule.ModuleNamespaceCheckRule

plugins {
  id("com.android.library")
  kotlin("android")
}

AndroidProjectChecker.config(project) // 项目检查工具

android {
  namespace = ModuleNamespaceCheckRule.getCorrectNamespace(project)
  compileSdk = libsEx.versions.`android-compileSdk`.requiredVersion.toInt()
  defaultConfig {
    minSdk = libsEx.versions.`android-minSdk`.requiredVersion.toInt()
  }
  compileOptions {
    val javaVersion = libsEx.versions.javaTarget.requiredVersion
    sourceCompatibility = JavaVersion.toVersion(javaVersion)
    targetCompatibility = JavaVersion.toVersion(javaVersion)
  }
  kotlinOptions {
    jvmTarget = libsEx.versions.kotlinJvmTarget.requiredVersion
  }
  lint {
    abortOnError = false // 编译遇到错误不退出，可以一次检查多个错误，并且已执行的 task 下次执行会直接走缓存
    targetSdk = libsEx.versions.`android-targetSdk`.requiredVersion.toInt()
  }
  // 命名规范设置，因为多模块相同资源名在打包时会合并，所以必须强制开启
  resourcePrefix = project.name.substringAfter("_")
  buildFeatures {
    buildConfig = true
  }
}