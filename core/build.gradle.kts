plugins {
  kotlin(Plugins.Serialization.KOTLINX) version Plugins.Serialization.VERSION
  id(Plugins.MAVEN_PUBLISH)
}

dependencies {
  implementation(Libs.Kotlinx.JSON_SERIALIZATION)
  implementation(Libs.Jackson.CORE)
  implementation(Libs.Jackson.YML_FORMAT)
}

publishing {
  publications {
    create<MavenPublication>("mavenJava") {
      from(components["java"])
      groupId = project.group.toString()
      artifactId = "core"
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
