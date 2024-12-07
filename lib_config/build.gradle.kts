plugins {
  id("manager.library")
}

useAutoService()

dependencies {
  implementation(projects.apiInit)
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.constraintlayout)
  implementation(libs.material)
}