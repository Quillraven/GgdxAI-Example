package com.github.quillraven.gdxaiexample.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.github.quillraven.gdxaiexample.ecs.component.MoveComponent
import com.github.quillraven.gdxaiexample.ecs.component.MoveDirection
import com.github.quillraven.gdxaiexample.ecs.component.MoveToComponent
import com.github.quillraven.gdxaiexample.ecs.component.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.get

class MoveToSystem : IteratingSystem(allOf(MoveToComponent::class, MoveComponent::class).get()) {
    private fun isWithinRange(
        transformSelf: TransformComponent,
        transformTarget: TransformComponent,
        range: Float,
    ): Boolean {
        val diffX = transformSelf.position.x - transformTarget.position.x
        return diffX * diffX <= range * range
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val moveTo = entity[MoveToComponent.mapper]
        val move = entity[MoveComponent.mapper]
        requireNotNull(moveTo, { "MoveToComponent missing for entity '$entity'" })
        requireNotNull(move, { "MoveComponent missing for entity '$entity'" })

        val transformTarget = moveTo.targetEntity[TransformComponent.mapper]
        val transformSelf = entity[TransformComponent.mapper]

        if (transformTarget == null || transformSelf == null
            || isWithinRange(transformSelf, transformTarget, moveTo.leashRange)
        ) {
            // at least one of the entities has no transformation to check against
            // OR entity is within range of target
            // -> stop moveTo
            move.direction = MoveDirection.NONE
            entity.remove(MoveToComponent::class.java)
        } else {
            if (transformTarget.position.x > transformSelf.position.x) {
                move.direction = MoveDirection.RIGHT
            } else {
                move.direction = MoveDirection.LEFT
            }
        }
    }
}
