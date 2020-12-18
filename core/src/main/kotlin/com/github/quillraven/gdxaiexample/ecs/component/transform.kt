package com.github.quillraven.gdxaiexample.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class TransformComponent : Component, Pool.Poolable, Comparable<TransformComponent> {
    val position = Vector3()
    val prevPosition = Vector2()
    val interpolatedPosition = Vector2()
    val size = Vector2(1f, 1f)

    fun moveImmediately(x: Float, y: Float) {
        position.set(x, y, position.z)
        prevPosition.set(x, y)
        interpolatedPosition.set(x, y)
    }

    override fun reset() {
        position.set(0f, 0f, 0f)
        prevPosition.set(0f, 0f)
        interpolatedPosition.set(0f, 0f)
        size.set(1f, 1f)
    }

    override fun compareTo(other: TransformComponent): Int {
        val zDiff = other.position.z.compareTo(position.z)
        return if (zDiff == 0) other.position.y.compareTo(position.y) else zDiff
    }

    companion object {
        val mapper = mapperFor<TransformComponent>()
    }
}
