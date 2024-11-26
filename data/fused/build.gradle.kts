plugins {
  id(Plugins.Kotlin.KAPT)
}

dependencies {

  implementation(project(Modules.CORE))
  implementation(project(Modules.Data.PSQL))
  implementation(project(Modules.Data.REDIS))
  implementation(project(Modules.Data.RABBIT_MQ))

  implementation(Libs.Dagger.DAGGER)
  kapt(Libs.Dagger.COMPILER)
}
