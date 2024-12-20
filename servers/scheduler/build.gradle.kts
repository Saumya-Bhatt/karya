plugins {
  application
  id(Plugins.Kotlin.KAPT)
  id(Plugins.Shadow.LIBRARY) version Plugins.Shadow.VERSION
}

dependencies {

  implementation(project(Modules.CORE))
  implementation(project(Modules.Data.FUSED))

  implementation(Libs.SLF4J)
  implementation(Libs.KOTLIN_REFLECT)

  implementation(Libs.Log4j.API)
  implementation(Libs.Log4j.CORE)
  implementation(Libs.Log4j.KOTLIN_API)

  implementation(Libs.Prometheus.HTTP_SERVER)
  implementation(Libs.Kotlinx.COROUTINES)
  implementation(Libs.Dagger.LIBRARY)

  kapt(Libs.Dagger.COMPILER)
}

tasks.register("copyConfigs") {
  doLast {
    val configPath = File("src/main/resources")
    delete(configPath)
    copy {
      from(project.rootDir.resolve("configs/commons"))
      into(configPath.resolve(""))
    }
  }
}

tasks.named("processResources") {
  dependsOn("copyConfigs")
}

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
  archiveBaseName.set("${project.group}-scheduler")
  archiveVersion.set(project.version.toString())
  archiveClassifier.set("all")
}

application {
  mainClass.set("karya.servers.scheduler.app.MainRunnerKt")
}
