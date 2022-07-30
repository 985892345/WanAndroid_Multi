plugins {
  `kotlin-dsl`
}

java {
  sourceCompatibility = JavaVersion.VERSION_1_8
  targetCompatibility = JavaVersion.VERSION_1_8
}

/*
* 注意：
* 1、classpath 在 build-logic 中要这样才能导入
* 2、如果出现了与 hilt 一样的情况，需要统一版本号，建议写在 gradle/libs.versions.toml 中，然后在 kt 文件中使用
*    Project.libsVersion() 这个高阶扩展来导入（可以参考 depend/Hilt.kt 中的写法）
* */
dependencies {
  implementation(libs.android.gradlePlugin)
  implementation(libs.kotlin.gradlePlugin)

  implementation(libs.hilt.gradlePlugin)

  // ARouter https://github.com/alibaba/ARouter
  // 可以去插件中搜索 ARouter Helper，用于实现一些快捷跳转的操作
  implementation("com.alibaba:arouter-register:1.0.2")

  // 很牛逼的检测工具 debug 模式下摇一摇手机触发 https://github.com/whataa/pandora
  implementation("com.github.whataa:pandora-plugin:1.0.0")

  // 一个轻量级 Android AOP 框架，在本项目中 CodeLocator 需要使用
  // CodeLocator：字节在用的强大的调试工具，请查看：https://github.com/bytedance/CodeLocator
  // 由于原 LanceX 项目没有适配新版本的 gradle，在 issues 中找到了适配的版本：https://github.com/liujianAndroid/lancet
  implementation("com.bytedance.tools.lancet:lancet-plugin-asm6:1.0.2")
}