plugins {
  id(Plugins.Kotlin.KAPT)
}

dependencies {

  implementation(project(Modules.CORE))
  implementation(project(Modules.Data.PSQL))
  implementation(project(Modules.Data.REDIS))

  implementation(Libs.Jackson.CORE)
  implementation(Libs.Jackson.YML_FORMAT)

  implementation(Libs.Dagger.DAGGER)
  kapt(Libs.Dagger.COMPILER)
}