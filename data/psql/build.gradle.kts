plugins {
  id(Plugins.Kotlin.KAPT)
}

dependencies {

  implementation(project(Modules.CORE))

  implementation(Libs.Exposed.CORE)
  implementation(Libs.Exposed.JAVA_TIME)
  implementation(Libs.Exposed.JDBC)
  implementation(Libs.Exposed.JSON)

  implementation(Libs.POSTGRES)
  implementation(Libs.MYSQL)

  implementation(Libs.Dagger.LIBRARY)
  implementation(Libs.FLYWAY)
  implementation(Libs.HIKARI)
  implementation(Libs.SLF4J)

  implementation(Libs.Log4j.API)
  implementation(Libs.Log4j.CORE)
  implementation(Libs.Log4j.KOTLIN_API)

  kapt(Libs.Dagger.COMPILER)
}

tasks.register("copyConfigs") {
  doLast {
    val configPath = File("src/main/resources")
    delete(configPath)
    copy {
      from(project.rootDir.resolve("configs/common"))
      into(configPath.resolve(""))
    }
    copy {
      from(project.rootDir.resolve("configs/migrations"))
      into(configPath.resolve("db/migrations"))
    }
  }
}
tasks.named("processResources") {
  dependsOn("copyConfigs")
}
