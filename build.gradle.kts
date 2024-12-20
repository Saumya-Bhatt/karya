plugins {
  id(Plugins.Kotlin.JVM) version Plugins.Kotlin.VERSION apply false
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
  group = "karya"
  version = "0.1.0"

  apply(plugin = Plugins.Kotlin.JVM)
  apply(plugin = Plugins.Dokka.LIBRARY)

  dependencies {
    detektPlugins(Plugins.Detekt.FORMATTING)
  }

}
