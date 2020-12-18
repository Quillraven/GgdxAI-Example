package com.github.quillraven.gdxaiexample.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class PhysicComponent : Component, Pool.Poolable {
    lateinit var body: Body
    val impulse = Vector2()

    override fun reset() {
        body.world.destroyBody(body)
        body.userData = null
        impulse.set(0f, 0f)
    }

    companion object {
        val mapper = mapperFor<PhysicComponent>()
    }
}
