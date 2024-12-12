plugins {
  id(Plugins.Kotlin.KAPT)
}

dependencies {

  implementation(project(Modules.CORE))
  implementation(project(Modules.Data.SQS))
  implementation(project(Modules.Data.PSQL))
  implementation(project(Modules.Data.REDIS))
  implementation(project(Modules.Data.RABBIT_MQ))

  implementation(Libs.Dagger.LIBRARY)
  kapt(Libs.Dagger.COMPILER)
}
