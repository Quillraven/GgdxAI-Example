package com.github.quillraven.gdxaiexample.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.github.quillraven.gdxaiexample.Game

fun main() {
    Lwjgl3Application(
        Game(),
        Lwjgl3ApplicationConfiguration().apply {
            setTitle("GdxAI-Example")
            setWindowedMode(1024, 576)
            setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png")
        },
    )
}
