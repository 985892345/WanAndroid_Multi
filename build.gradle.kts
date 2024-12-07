buildscript {
  dependencies {
    classpath(libs.vasdolly.gradlePlugin)
  }
}

plugins {
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.android.library) apply false
  alias(libs.plugins.kotlin.android) apply false
  alias(libs.plugins.kotlin.compose) apply false
  alias(libs.plugins.kotlin.serialization) apply false
  alias(libs.plugins.ksp) apply false
}

// 管理 git 提交规范的脚本
apply(from = "git-hook.gradle.kts")