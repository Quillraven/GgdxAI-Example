plugins {
    kotlin("jvm") version "2.1.10" apply false
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    repositories {
        mavenCentral()
        google()
    }

    extensions.configure<JavaPluginExtension> {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }

    extensions.configure<org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension> {
        jvmToolchain(17)
    }

    version = "0.0.1-SNAPSHOT"
}
