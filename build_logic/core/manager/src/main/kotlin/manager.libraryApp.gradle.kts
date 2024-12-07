import com.android.build.gradle.internal.dsl.BaseAppModuleExtension

// 是否允许执行单模块调试
val isAllowDebugModule = !project.gradle.startParameter.taskNames.any {
  // 注意：这里面的是取反，即满足下面条件的不执行单模块调试
  /**
   * 如果出现:
   * AAPT2 aapt2-8.0.2-9289358-osx Daemon #0: Unexpected error during link, attempting to stop daemon.
   *
   * 这个是因为打包时存在多个 application 模块导致
   * 可能跟单模块有关，请在下面添加你的 gradle task name
   */
  it.contains("assembleRelease")
      || it.contains("assembleDebug") && !it.contains(project.name)
      || it == "publishModuleCachePublicationToMavenRepository" // 本地缓存任务
      || it == "cacheToLocalMaven"
      || it == "channelRelease"
      || it == "channelDebug"
      || it == "cyxbsRelease"
} && !name.startsWith("api_") // api 模块不开启

if (!isAllowDebugModule) {
  println("${project.name} 的单模块调试被取消！")
  apply(plugin = "manager.library")
} else {
  apply(plugin = "manager.application")
  extensions.configure<BaseAppModuleExtension>("android") {
    // 设置 debug 的源集
    sourceSets {
      getByName("main") {
        // 将 debug 加入编译环境，单模块需要的代码放这里面
        java.srcDir("src/main/debug")
        res.srcDir("src/main/debug-res")
        // 如果 debug 下存在 AndroidManifest 文件，则重定向 AndroidManifest 文件
        // 可参考 lib_crash 模块
        if (projectDir.resolve("src")
            .resolve("main")
            .resolve("debug")
            .resolve("AndroidManifest.xml").exists()) {
          manifest.srcFile("src/main/debug/AndroidManifest.xml")
        }
      }
    }
    defaultConfig {
      // 设置单模块安装包名字
      manifestPlaceholders["single_module_app_name"] = project.name
    }
  }
  // 依赖 lib_single 用于设置单模块入口
  dependencies {
    "implementation"(project(":lib_single"))
  }
}