plugins {
  id("manager.library")
}

useARouter()

dependencies {
  implementation(projects.libBase)
  implementation(projects.libUtils)
  implementation(projects.libConfig)
  implementation(projects.libAccount.apiAccount)

  implementation(libs.bundles.network)

  implementation("com.github.franmontiel:PersistentCookieJar:v1.0.1")
}
