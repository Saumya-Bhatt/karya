plugins {
    id(Plugins.Kotlin.KAPT)
}

dependencies {

    implementation(project(Modules.CORE))

    implementation(Libs.SLF4J)
    implementation(Libs.SQS)

    implementation(Libs.Dagger.LIBRARY)
    implementation(Libs.Kotlinx.COROUTINES)
    implementation(Libs.Kotlinx.JSON_SERIALIZATION)

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
    }
}
tasks.named("processResources") {
    dependsOn("copyConfigs")
}
