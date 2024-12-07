plugins {
  id("manager.library")
}

useARouter()
useAutoService()

dependencies {
  implementation(projects.libBase)
  implementation(projects.libUtils)
  implementation(projects.libConfig)
  implementation(projects.libCrash.apiCrash)

  // 这里面写只有自己模块才会用到的依赖
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.constraintlayout)
}
