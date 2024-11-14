plugins {
  application
  id(Plugins.Kotlin.KAPT)
}

dependencies {
  implementation(project(Modules.CORE))
  implementation(project(Modules.Data.FUSED))

  implementation(Libs.SLF4J)

  implementation(Libs.Log4j.API)
  implementation(Libs.Log4j.CORE)
  implementation(Libs.Log4j.KOTLIN_API)

  implementation(Libs.Ktor.KOTLINX)
  implementation(Libs.Ktor.Server.CORE)
  implementation(Libs.Ktor.Server.CIO)
  implementation(Libs.Ktor.Server.CALL_LOGGING)
  implementation(Libs.Ktor.Server.CONTENT_NEGOTIATION)

  implementation(Libs.Kotlinx.COROUTINES)
  implementation(Libs.Dagger.DAGGER)

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

application {
  mainClass.set("karya.servers.server.app.MainRunnerKt")
}