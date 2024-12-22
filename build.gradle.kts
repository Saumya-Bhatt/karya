plugins {
  id(Plugins.Kotlin.JVM) version Plugins.Kotlin.VERSION apply false
  id(Plugins.PublishCentral.LIBRARY) version Plugins.PublishCentral.VERSION apply false
  id(Plugins.Detekt.LIBRARY) version Plugins.Detekt.VERSION
  id(Plugins.Dokka.LIBRARY) version Plugins.Dokka.VERSION
}

allprojects {
  apply(plugin = Plugins.Detekt.LIBRARY)

  repositories {
    mavenCentral()
    gradlePluginPortal()
  }

  detekt {
    toolVersion = Plugins.Detekt.VERSION
    config.setFrom(files("$rootDir/configs/detekt.yml"))
    buildUponDefaultConfig = false
  }
}

subprojects {
  group = "io.github.saumya-bhatt"

  // update this only when updating the servers docker image.
  version = "1.1.1"

  apply(plugin = Plugins.Kotlin.JVM)
  apply(plugin = Plugins.Dokka.LIBRARY)

  dependencies {
    detektPlugins(Plugins.Detekt.FORMATTING)
  }
}

gradle.beforeProject {
  extra["clientVersion"] = "1.1.1"
}

