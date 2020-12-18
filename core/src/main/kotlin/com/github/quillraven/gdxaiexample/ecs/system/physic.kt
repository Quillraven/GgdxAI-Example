package com.github.quillraven.gdxaiexample.ecs.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.World
import com.github.quillraven.gdxaiexample.Game
import com.github.quillraven.gdxaiexample.ecs.component.JumpComponent
import com.github.quillraven.gdxaiexample.ecs.component.MoveComponent
import com.github.quillraven.gdxaiexample.ecs.component.PhysicComponent
import com.github.quillraven.gdxaiexample.ecs.component.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.get
import kotlin.math.min

class PhysicSystem(
    private val world: World,
    private val interval: Float = Game.PHYSIC_TIME_STEP,
) : IteratingSystem(allOf(PhysicComponent::class, TransformComponent::class).get()) {
    private var accumulator = 0f

    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)
    }

    override fun update(deltaTime: Float) {
        accumulator += min(1 / 30f, deltaTime)
        while (accumulator >= interval) {
            updatePrevPositionAndApplyForces()
            world.step(interval, 6, 2)
            accumulator -= interval
        }
        world.clearForces()

        updateInterpolatedPosition(accumulator / interval)
    }

    private fun forAllEntities(block: (Entity, PhysicComponent, TransformComponent) -> Unit) {
        entities.forEach { entity ->
            val physic = entity[PhysicComponent.mapper]
            val transform = entity[TransformComponent.mapper]
            requireNotNull(physic, { "PhysicComponent missing for entity '$entity'" })
            requireNotNull(transform, { "TransformComponent missing for entity '$entity'" })

            block(entity, physic, transform)
        }
    }

    private fun updatePrevPositionAndApplyForces() {
        forAllEntities { entity, physic, transform ->
            transform.prevPosition.set(
                physic.body.position.x,
                physic.body.position.y,
            )

            // calculate impulse to apply
            entity[JumpComponent.mapper]?.let { jump ->
                physic.impulse.y = physic.body.mass * (jump.jumpVelocity - physic.body.linearVelocity.y)
                entity.remove(JumpComponent::class.java)
            }
            entity[MoveComponent.mapper]?.let { move ->
                physic.impulse.x = physic.body.mass * (move.moveVelocity - physic.body.linearVelocity.x)
            }

            if (!physic.impulse.isZero) {
                physic.body.applyLinearImpulse(physic.impulse, physic.body.worldCenter, true)
                physic.impulse.set(0f, 0f)
            }
        }
    }

    private fun updateInterpolatedPosition(alpha: Float) {
        forAllEntities { _, physic, transform ->
            transform.position.set(
                physic.body.position.x,
                physic.body.position.y,
                transform.position.z,
            )
            transform.interpolatedPosition.set(
                MathUtils.lerp(transform.prevPosition.x, transform.position.x, alpha),
                MathUtils.lerp(transform.prevPosition.y, transform.position.y, alpha),
            )
        }
    }

    override fun processEntity(entity: Entity?, deltaTime: Float) = Unit
}
