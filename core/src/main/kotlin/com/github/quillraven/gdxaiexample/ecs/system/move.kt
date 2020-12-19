package com.github.quillraven.gdxaiexample.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.github.quillraven.gdxaiexample.ecs.component.MoveComponent
import com.github.quillraven.gdxaiexample.ecs.component.MoveDirection
import com.github.quillraven.gdxaiexample.ecs.component.PlayerComponent
import com.github.quillraven.gdxaiexample.ecs.component.RenderComponent
import ktx.ashley.allOf
import ktx.ashley.get

class MoveSystem : IteratingSystem(allOf(MoveComponent::class).get()) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val move = entity[MoveComponent.mapper]
        requireNotNull(move, { "MoveComponent missing for entity '$entity'" })

        if (entity[PlayerComponent.mapper] != null) {
            when {
                Gdx.input.isKeyPressed(Input.Keys.LEFT) -> move.direction = MoveDirection.LEFT
                Gdx.input.isKeyPressed(Input.Keys.RIGHT) -> move.direction = MoveDirection.RIGHT
                else -> move.direction = MoveDirection.NONE
            }
        }

        when (move.direction) {
            MoveDirection.NONE -> move.moveVelocity = 0f
            MoveDirection.LEFT -> {
                move.moveVelocity = -move.maxSpeed
                entity[RenderComponent.mapper]?.sprite?.setFlip(true, false)
            }
            MoveDirection.RIGHT -> {
                move.moveVelocity = move.maxSpeed
                entity[RenderComponent.mapper]?.sprite?.setFlip(false, false)
            }
        }
    }
}
