
// FOR INTEGRATING WITH PROJECTS WHOSE JAVA VERSION IS < 17
// 1. Install JDK 17
// 2. Add the following to the build.gradle.kts file of the project
java {
  sourceCompatibility = JavaVersion.VERSION_17
  targetCompatibility = JavaVersion.VERSION_17
}
kotlin {
  jvmToolchain(17)
}

// required dependencies
dependencies {
  implementation("io.github.saumya-bhatt:karya-client:1.0.1")
  implementation("io.github.saumya-bhatt:karya-core:1.0.1")
}
