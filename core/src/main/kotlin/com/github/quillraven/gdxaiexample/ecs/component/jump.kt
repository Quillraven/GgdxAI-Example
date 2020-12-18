package com.github.quillraven.gdxaiexample.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class JumpComponent : Component, Pool.Poolable {
    var maxHeight = 1f
    var jumpVelocity = 0f

    override fun reset() {
        maxHeight = 1f
        jumpVelocity = 0f
    }

    companion object {
        val mapper = mapperFor<JumpComponent>()
    }
}
