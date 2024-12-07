plugins {
  id("manager.library")
}

useARouter(false) // lib_base 模块不包含实现类，不需要处理注解
useDataBinding(false) // lib_base 模块只依赖 DataBinding 但不开启 DataBinding

dependencies {
  implementation(projects.apiInit)
  implementation(projects.libUtils)
  implementation(projects.libConfig)
  implementation(projects.libAccount.apiAccount)

  implementation(libs.bundles.projectBase)
  implementation(libs.bundles.views)
  implementation(libs.bundles.network)
}