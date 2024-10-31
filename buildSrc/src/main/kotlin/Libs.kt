object Libs {

  const val KOTLIN_REFLECT = "org.jetbrains.kotlin:kotlin-reflect:1.8.10"
  const val KOTLINX_COROUTINES = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.0"
  const val SLF4J = "org.slf4j:slf4j-simple:2.0.0"
  const val POSTGRES = "org.postgresql:postgresql:42.7.1"
  const val HIKARI = "com.zaxxer:HikariCP:6.0.0"
  const val REDISSON = "org.redisson:redisson:3.17.6"
  const val FLYWAY = "org.flywaydb:flyway-core:9.2.0"

  object Log4j {
    private const val GROUP = "org.apache.logging.log4j"
    private const val VERSION = "2.24.0"

    const val API = "$GROUP:log4j-api:$VERSION"
    const val CORE = "$GROUP:log4j-core:$VERSION"
    const val KOTLIN_API = "$GROUP:log4j-api-kotlin:1.5.0"
  }

  object Dagger {
    private const val GROUP = "com.google.dagger"
    private const val VERSION = "2.51.1"

    const val DAGGER = "$GROUP:dagger:$VERSION"
    const val COMPILER = "$GROUP:dagger-compiler:$VERSION"
  }

  object Exposed {
    private const val GROUP = "org.jetbrains.exposed"
    private const val VERSION = "0.55.0"

    const val CORE = "$GROUP:exposed-core:$VERSION"
    const val JAVA_TIME = "$GROUP:exposed-java-time:$VERSION"
    const val JDBC = "$GROUP:exposed-jdbc:$VERSION"
  }

  object Ktor {
    private const val GROUP = "io.ktor"
    private const val VERSION = "3.0.0"

    const val JACKSON = "$GROUP:ktor-serialization-jackson:$VERSION"

    object Server {
      const val CORE = "$GROUP:ktor-server-core:$VERSION"
      const val CIO = "$GROUP:ktor-server-cio:$VERSION"
      const val CALL_LOGGING = "$GROUP:ktor-server-call-logging:$VERSION"
      const val CONTENT_NEGOTIATION = "$GROUP:ktor-server-content-negotiation:$VERSION"
    }
    object Client {
      const val CORE = "$GROUP:ktor-client-core:$VERSION"
      const val CIO = "$GROUP:ktor-client-cio:$VERSION"
      const val CONTENT_NEGOTIATION = "$GROUP:ktor-client-content-negotiation:$VERSION"
    }
  }

  object Jackson {
    private const val GROUP = "com.fasterxml.jackson"
    private const val VERSION = "2.14.0"

    const val CORE = "$GROUP.core:jackson-databind:$VERSION"
    const val YML_FORMAT = "$GROUP.dataformat:jackson-dataformat-yaml:$VERSION"
  }

}