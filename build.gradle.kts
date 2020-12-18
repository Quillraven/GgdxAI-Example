plugins {
    kotlin("jvm") apply false
}

subprojects {
    repositories {
        jcenter()
        google()
    }

    apply {
        plugin("org.jetbrains.kotlin.jvm")
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = "1.8"
            incremental = true
        }
    }

    version = "0.0.1-SNAPSHOT"
}
