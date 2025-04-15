@file:JvmName("Lwjgl3Launcher")

package com.github.quillraven.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.github.quillraven.gdxaiexample.Game

fun main() {
    if (StartupHelper.startNewJvmIfRequired())
        return
    Lwjgl3Application(Game(), Lwjgl3ApplicationConfiguration().apply {
        setTitle("GgdxAI-Example")
        useVsync(true)
        setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1)

        setWindowedMode(1024, 576)
        setWindowIcon(*(arrayOf(128, 64, 32, 16).map { "libgdx$it.png" }.toTypedArray()))
    })
}
