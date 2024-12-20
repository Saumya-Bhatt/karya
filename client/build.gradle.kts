plugins {
  id(Plugins.Kotlin.KAPT)
  id(Plugins.MAVEN_PUBLISH)
  id(Plugins.Shadow.LIBRARY) version Plugins.Shadow.VERSION
}

dependencies {
  implementation(project(Modules.CORE))

  implementation(Libs.SLF4J)

  implementation(Libs.Log4j.API)
  implementation(Libs.Log4j.CORE)
  implementation(Libs.Log4j.KOTLIN_API)

  implementation(Libs.Ktor.KOTLINX)
  implementation(Libs.Ktor.Client.CORE)
  implementation(Libs.Ktor.Client.CONTENT_NEGOTIATION)
  implementation(Libs.Ktor.Client.CIO)

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
  archiveBaseName.set("${project.group}-client")
  archiveVersion.set(project.version.toString())
  archiveClassifier.set("all")
}

publishing {
  publications {
    create<MavenPublication>("mavenJava") {
      from(components["java"])

      artifact(tasks.named("shadowJar").get()) {
        classifier = "all"
      }

      groupId = project.group.toString()
      artifactId = "client"
      version = project.version.toString()
    }
  }

  repositories {
    maven {
      name = "GitHubPackages"
      url = uri("https://maven.pkg.github.com/Saumya-Bhatt/karya") // replace with your GitHub repo
      credentials {
        username = System.getenv("GITHUB_USERNAME")
        password = System.getenv("GITHUB_TOKEN")
      }
    }
  }
}
