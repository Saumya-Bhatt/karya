object Plugins {

  const val MAVEN_PUBLISH = "maven-publish"

  object Kotlin {
    private const val KOTLIN_PREFIX = "org.jetbrains.kotlin"
    const val JVM = "$KOTLIN_PREFIX.jvm"
    const val KAPT = "kotlin-kapt"
    const val VERSION = "2.0.21"
  }

  object Serialization {
    const val KOTLINX = "plugin.serialization"
    const val VERSION = "2.0.20"
  }

  object Detekt {
    const val LIBRARY = "io.gitlab.arturbosch.detekt"
    const val VERSION = "1.23.4"
    const val FORMATTING = "$LIBRARY:detekt-formatting:$VERSION"
  }

  object Dokka {
    const val LIBRARY = "org.jetbrains.dokka"
    const val VERSION = "1.9.20"
  }

  object Shadow {
    const val LIBRARY = "com.gradleup.shadow"
    const val VERSION = "8.3.5"
  }
}
