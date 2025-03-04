import utils.config.Config
import rule.ModuleNamespaceCheckRule

plugins {
  id("com.android.application")
  kotlin("android")
}

AndroidProjectChecker.config(project) // 项目检查工具

android {
  namespace = ModuleNamespaceCheckRule.getCorrectNamespace(project)
  compileSdk = libsEx.versions.`android-compileSdk`.requiredVersion.toInt()
  defaultConfig {
    applicationId = Config.getApplicationId(project)
    minSdk = libsEx.versions.`android-minSdk`.requiredVersion.toInt()
    targetSdk = libsEx.versions.`android-targetSdk`.requiredVersion.toInt()
    versionCode = Config.versionCode
    versionName = Config.versionName
  }
  buildTypes {
    release {
      isMinifyEnabled = true
      isShrinkResources = true
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        rootDir.resolve("build_logic")
          .resolve("manager")
          .resolve("proguard-rules.pro")
      )
    }
    debug {
      isMinifyEnabled = false
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        rootDir.resolve("build_logic")
          .resolve("manager")
          .resolve("proguard-rules.pro")
      )
    }
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
  }
  // 命名规范设置，因为多模块相同资源名在打包时会合并，所以必须强制开启
  resourcePrefix = project.name.substringAfter("_")
  buildFeatures {
    dataBinding = true // application 模块必须开启 databinding，因为编译期需要关联其他模块的 databinding
    buildConfig = true
  }
}


