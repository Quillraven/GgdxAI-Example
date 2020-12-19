package com.github.quillraven.gdxaiexample.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class MoveToComponent : Component, Pool.Poolable {
    var leashRange = 0f
    lateinit var targetEntity: Entity

    override fun reset() {
        leashRange = 0f
    }

    companion object {
        val mapper = mapperFor<MoveToComponent>()
    }
}
