plugins {
  id("manager.libraryApp")
}

useARouter()
useDataBinding() // dataBinding 需要进行模块依赖之间的传递，所以需要开启
// 比如 A -> B -> C
// A 和 C 都开启了 dataBinding，则 B 模块也必须开启才能正常注册 C 模块的 dataBinding
// module_single 模块作为中间者即使没有使用也需要开启 dataBinding

dependencies {
  implementation(projects.libBase)
  implementation(projects.libConfig)
  implementation(projects.libUtils)
  implementation(projects.libAccount.apiAccount)
  implementation(projects.moduleLogin.apiLogin)

  implementation(libs.bundles.projectBase)
  implementation(libs.bundles.views)
  implementation(libs.bundles.network)
}
