dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("com.badlogicgames.gdx:gdx:${project.property("gdxVersion")}")
    implementation("com.badlogicgames.gdx:gdx-ai:${project.property("aiVersion")}")
    implementation("com.badlogicgames.ashley:ashley:${project.property("ashleyVersion")}")
    implementation("com.badlogicgames.gdx:gdx-box2d:${project.property("gdxVersion")}")
    // ktx-app needs to be exposed for the launcher classes so that they know that KtxGame implements ApplicationListener
    api("io.github.libktx:ktx-app:${project.property("ktxVersion")}")
    implementation("io.github.libktx:ktx-ashley:${project.property("ktxVersion")}")
    implementation("io.github.libktx:ktx-assets:${project.property("ktxVersion")}")
    implementation("io.github.libktx:ktx-box2d:${project.property("ktxVersion")}")
    implementation("io.github.libktx:ktx-graphics:${project.property("ktxVersion")}")
    implementation("io.github.libktx:ktx-log:${project.property("ktxVersion")}")
}
