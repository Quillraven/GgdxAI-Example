plugins {
    application
}

application {
    mainClass.set("com.github.quillraven.gdxaiexample.lwjgl3.launcherKt")
}

sourceSets {
    main {
        resources {
            srcDir(rootProject.files("assets"))
        }
    }
}

dependencies {
    implementation(project(":core"))
    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:${project.property("gdxVersion")}")
    implementation("com.badlogicgames.gdx:gdx-platform:${project.property("gdxVersion")}:natives-desktop")
    implementation("com.badlogicgames.gdx:gdx-box2d-platform:${project.property("gdxVersion")}:natives-desktop")
}

tasks {
    jar {
        from(files(sourceSets.main.get().output.classesDirs))
        from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })

        archiveBaseName.set("GgdxAI-Example")

        manifest {
            attributes["Main-Class"] = application.mainClass.get()
        }
    }
}
