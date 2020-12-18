package com.github.quillraven.gdxaiexample.ecs.system

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.viewport.Viewport

class PhysicDebugRenderSystem(
    private val world: World,
    private val viewport: Viewport,
    private val camera: Camera = viewport.camera,
    private val renderer: Box2DDebugRenderer = Box2DDebugRenderer()
) : EntitySystem() {
    init {
        setProcessing(System.getProperty("dev-mode", "false") == "true")
    }

    override fun update(deltaTime: Float) {
        viewport.apply()
        renderer.render(world, camera.combined)
    }
}

