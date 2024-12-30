plugins {
  application
  id(Plugins.Kotlin.KAPT)
  id(Plugins.Shadow.LIBRARY) version Plugins.Shadow.VERSION
}

dependencies {
  implementation(project(Modules.CORE))
  implementation(project(Modules.Data.FUSED))

  implementation(Libs.SLF4J)
  implementation(Libs.CAFFEINE)
  implementation(Libs.MICROMETER_PROMETHEUS)

  implementation(Libs.Log4j.API)
  implementation(Libs.Log4j.CORE)
  implementation(Libs.Log4j.KOTLIN_API)

  implementation(Libs.Ktor.KOTLINX)
  implementation(Libs.Ktor.Server.CORS)
  implementation(Libs.Ktor.Server.CORE)
  implementation(Libs.Ktor.Server.CIO)
  implementation(Libs.Ktor.Server.CALL_LOGGING)
  implementation(Libs.Ktor.Server.MICROMETER)
  implementation(Libs.Ktor.Server.CONTENT_NEGOTIATION)

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
  archiveBaseName.set("${project.group}-server")
  archiveVersion.set(project.version.toString())
  archiveClassifier.set("all")
}


application {
  mainClass.set("karya.servers.server.app.MainRunnerKt")
}
