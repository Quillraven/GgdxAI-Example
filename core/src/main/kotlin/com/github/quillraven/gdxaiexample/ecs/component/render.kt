package com.github.quillraven.gdxaiexample.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class RenderComponent : Component, Pool.Poolable {
    val sprite = Sprite()
    val offsetInPx = Vector2()

    override fun reset() {
        offsetInPx.set(0f, 0f)
    }

    companion object {
        val mapper = mapperFor<RenderComponent>()
    }
}
