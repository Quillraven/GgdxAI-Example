package com.github.quillraven.gdxaiexample.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

enum class MoveDirection {
    NONE,
    LEFT,
    RIGHT,
}

class MoveComponent : Component, Pool.Poolable {
    var direction = MoveDirection.NONE
    var maxSpeed = 1f
    var moveVelocity = 0f

    override fun reset() {
        direction = MoveDirection.NONE
        moveVelocity = 0f
        maxSpeed = 1f
    }

    companion object {
        val mapper = mapperFor<MoveComponent>()
    }
}
