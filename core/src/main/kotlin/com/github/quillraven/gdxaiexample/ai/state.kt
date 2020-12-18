package com.github.quillraven.gdxaiexample.ai

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.msg.Telegram
import com.github.quillraven.gdxaiexample.Game
import com.github.quillraven.gdxaiexample.ecs.component.AIComponent
import com.github.quillraven.gdxaiexample.ecs.component.AnimationComponent
import com.github.quillraven.gdxaiexample.ecs.component.AnimationType
import com.github.quillraven.gdxaiexample.ecs.component.JumpComponent
import com.github.quillraven.gdxaiexample.ecs.component.PhysicComponent
import com.github.quillraven.gdxaiexample.ecs.system.KeyExtraInfo
import ktx.ashley.configureEntity
import ktx.ashley.get
import ktx.ashley.with
import ktx.log.info
import ktx.log.logger

interface EntityState : State<Entity> {
    private fun entityAi(entity: Entity): AIComponent {
        val ai = entity[AIComponent.mapper]
        requireNotNull(ai, { "AIComponent missing for entity '$entity'" })
        return ai
    }

    override fun enter(entity: Entity) {
        val ai = entityAi(entity)
        ai.stateTime = 0f
        ai.state = this
        enter(entity, ai)
    }

    override fun update(entity: Entity) {
        update(entity, entityAi(entity))
    }

    override fun exit(entity: Entity) {
        exit(entity, entityAi(entity))
    }

    override fun onMessage(entity: Entity, telegram: Telegram): Boolean {
        return onMessage(entity, entityAi(entity), telegram)
    }

    fun enter(entity: Entity, ai: AIComponent) = Unit
    fun update(entity: Entity, ai: AIComponent) = Unit
    fun exit(entity: Entity, ai: AIComponent) = Unit
    fun onMessage(entity: Entity, ai: AIComponent, telegram: Telegram): Boolean = false
}

object DefaultEntityState : EntityState

sealed class AdventurerState(
    private val animationType: AnimationType,
) : EntityState {
    fun entityAnimation(entity: Entity): AnimationComponent {
        val animation = entity[AnimationComponent.mapper]
        requireNotNull(animation, { "AnimationComponent missing for adventurer '$entity'" })
        return animation
    }

    override fun enter(entity: Entity, ai: AIComponent) {
        LOG.info { "Entity '$entity' is entering state '${this::class.simpleName}'" }

        val animation = entityAnimation(entity)
        animation.type = animationType
        // it is important to set the stateTime here as well because
        // the AnimationSystem is processed after the AISystem and therefore
        // isAnimationFinished might return true although the animation was not even
        // started yet
        animation.stateTime = 0f
    }

    object Idle : AdventurerState(AnimationType.ADVENTURER_IDLE) {
        override fun update(entity: Entity, ai: AIComponent) {
            entity[PhysicComponent.mapper]?.let { physic ->
                when {
                    physic.body.linearVelocity.y < 0f -> ai.stateMachine.changeState(Fall)
                    physic.body.linearVelocity.x != 0f -> ai.stateMachine.changeState(Run)
                }
            }
        }

        override fun onMessage(entity: Entity, ai: AIComponent, telegram: Telegram): Boolean {
            val extraInfo = telegram.extraInfo
            if (extraInfo is KeyExtraInfo.Down) {
                when (extraInfo.keycode) {
                    Input.Keys.A -> ai.stateMachine.changeState(Attack1)
                    Input.Keys.J -> ai.stateMachine.changeState(StartJump)
                }
                return true
            }
            return false
        }
    }

    object Run : AdventurerState(AnimationType.ADVENTURER_RUN) {
        override fun update(entity: Entity, ai: AIComponent) {
            entity[PhysicComponent.mapper]?.let { physic ->
                when {
                    physic.body.linearVelocity.y < 0f -> ai.stateMachine.changeState(Fall)
                    physic.body.linearVelocity.x == 0f -> ai.stateMachine.changeState(Idle)
                }
            }
        }

        override fun onMessage(entity: Entity, ai: AIComponent, telegram: Telegram): Boolean {
            val extraInfo = telegram.extraInfo
            if (extraInfo is KeyExtraInfo.Down) {
                when (extraInfo.keycode) {
                    Input.Keys.J -> ai.stateMachine.changeState(StartJump)
                }
                return true
            }
            return false
        }
    }

    object Attack1 : AdventurerState(AnimationType.ADVENTURER_ATTACK_1) {
        override fun update(entity: Entity, ai: AIComponent) {
            val animation = entityAnimation(entity)
            if (animation.gdxAnimation.isAnimationFinished(animation.stateTime)) {
                if (ai.state == Attack1) {
                    ai.stateMachine.changeState(Idle)
                } else {
                    ai.stateMachine.changeState(ai.state)
                }
            }
        }

        override fun onMessage(entity: Entity, ai: AIComponent, telegram: Telegram): Boolean {
            val extraInfo = telegram.extraInfo
            if (extraInfo is KeyExtraInfo.Down) {
                if (extraInfo.keycode == Input.Keys.A) {
                    ai.state = Attack2
                    return true
                }
            }
            return false
        }
    }

    object Attack2 : AdventurerState(AnimationType.ADVENTURER_ATTACK_2) {
        override fun update(entity: Entity, ai: AIComponent) {
            val animation = entityAnimation(entity)
            if (animation.gdxAnimation.isAnimationFinished(animation.stateTime)) {
                if (ai.state == Attack2) {
                    ai.stateMachine.changeState(Idle)
                } else {
                    ai.stateMachine.changeState(ai.state)
                }
            }
        }

        override fun onMessage(entity: Entity, ai: AIComponent, telegram: Telegram): Boolean {
            val extraInfo = telegram.extraInfo
            if (extraInfo is KeyExtraInfo.Down) {
                if (extraInfo.keycode == Input.Keys.A) {
                    ai.state = Attack3
                    return true
                }
            }
            return false
        }
    }

    object Attack3 : AdventurerState(AnimationType.ADVENTURER_ATTACK_3) {
        override fun update(entity: Entity, ai: AIComponent) {
            val animation = entityAnimation(entity)
            if (animation.gdxAnimation.isAnimationFinished(animation.stateTime)) {
                ai.stateMachine.changeState(Idle)
            }
        }
    }

    object StartJump : AdventurerState(AnimationType.ADVENTURER_START_JUMP) {
        override fun update(entity: Entity, ai: AIComponent) {
            val physic = entity[PhysicComponent.mapper]
            if (physic == null) {
                ai.stateMachine.changeState(Idle)
            } else if (ai.stateTime >= 0.35f) {
                ai.stateMachine.changeState(Jump)
            }
        }
    }

    object Jump : AdventurerState(AnimationType.ADVENTURER_JUMP) {
        override fun enter(entity: Entity, ai: AIComponent) {
            super.enter(entity, ai)
            ((Gdx.app.applicationListener) as Game).ecsEngine.configureEntity(entity) {
                with<JumpComponent> {
                    maxHeight = 2f
                }
            }
        }

        override fun update(entity: Entity, ai: AIComponent) {
            val physic = entity[PhysicComponent.mapper]
            if (physic == null) {
                ai.stateMachine.changeState(Idle)
            } else if (entity[JumpComponent.mapper] == null && physic.body.linearVelocity.y <= 0f) {
                ai.stateMachine.changeState(Fall)
            }
        }
    }

    object Fall : AdventurerState(AnimationType.ADVENTURER_FALL) {
        override fun update(entity: Entity, ai: AIComponent) {
            val physic = entity[PhysicComponent.mapper]
            if (physic == null) {
                ai.stateMachine.changeState(Idle)
            } else if (physic.body.linearVelocity.y >= 0f) {
                ai.stateMachine.changeState(Idle)
            }
        }
    }

    companion object {
        private val LOG = logger<AdventurerState>()
    }
}
