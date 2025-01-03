import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinJvm
import com.vanniktech.maven.publish.SonatypeHost

plugins {
  id(Plugins.Kotlin.KAPT)
  id(Plugins.MAVEN_PUBLISH)
  id(Plugins.PublishCentral.LIBRARY) version Plugins.PublishCentral.VERSION
}

val artifactId = "karya-client"
val clientVersion = project.extra.get("clientVersion").toString()

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

publishing {
  repositories {
    maven {
      name = "GitHubPackages"
      url = uri("https://maven.pkg.github.com/Saumya-Bhatt/karya")
      credentials {
        username = System.getenv("GITHUB_USERNAME")
        password = System.getenv("GITHUB_TOKEN")
      }
    }
  }
}

mavenPublishing {

  coordinates(
    groupId = project.group.toString(),
    artifactId = artifactId,
    version = clientVersion
  )

  configure(KotlinJvm(
    javadocJar = JavadocJar.Dokka("dokkaHtml"),
    sourcesJar = true
  ))

  pom {
    name.set("karya-client")
    description.set("Client module to interact with Karya Backend. Compiled on Java 17.")
    inceptionYear.set("2024")
    url.set("https://github.com/Saumya-Bhatt/karya")
    licenses {
      license {
        name.set("Apache License, Version 2.0")
        url.set("http://www.apache.org/licenses/LICENSE-2.0")
      }
    }
    developers {
      developer {
        id.set("Saumya-Bhatt")
        name.set("Saumya Bhatt")
        email.set("saumya.bhatt106@gmail.com")
      }
    }
    scm {
      connection.set("scm:git:git://github.com/Saumya-Bhatt/karya.git")
      developerConnection.set("scm:git:ssh://github.com:Saumya-Bhatt/karya.git")
      url.set("https://github.com/Saumya-Bhatt/karya")
    }
  }

  publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL, automaticRelease = true)
  signAllPublications()
}



