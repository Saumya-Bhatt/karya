import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinJvm
import com.vanniktech.maven.publish.SonatypeHost

plugins {
  id(Plugins.MAVEN_PUBLISH)
  id(Plugins.PublishCentral.LIBRARY) version Plugins.PublishCentral.VERSION
  kotlin(Plugins.Serialization.KOTLINX) version Plugins.Serialization.VERSION
}

val artifactId = "karya-core"
val clientVersion = project.extra.get("clientVersion").toString()

dependencies {
  implementation(Libs.Kotlinx.JSON_SERIALIZATION)
  implementation(Libs.Jackson.CORE)
  implementation(Libs.Jackson.YML_FORMAT)
}

// Rename the artifact for publication
tasks.withType<Jar> {
  archiveBaseName.set(artifactId)
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
    name.set("karya-core")
    description.set("Karya Core module containing the common data classes and utility functions. Compiled on Java 17.")
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

