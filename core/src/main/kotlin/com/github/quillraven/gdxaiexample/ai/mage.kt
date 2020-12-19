package com.github.quillraven.gdxaiexample.ai

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ai.msg.Telegram
import com.github.quillraven.gdxaiexample.Game
import com.github.quillraven.gdxaiexample.ecs.component.AIComponent
import com.github.quillraven.gdxaiexample.ecs.component.AnimationType
import com.github.quillraven.gdxaiexample.ecs.component.MoveToComponent
import com.github.quillraven.gdxaiexample.ecs.system.CharacterExtraInfo
import ktx.ashley.configureEntity
import ktx.ashley.get
import ktx.ashley.with
import ktx.log.debug
import ktx.log.info
import ktx.log.logger

sealed class MageState(animationType: AnimationType) : AnimatedState(animationType) {
    override fun enter(entity: Entity, ai: AIComponent) {
        super.enter(entity, ai)
        LOG.info { "Mage '$entity' is entering state '${this::class.simpleName}'" }
    }

    object Idle : MageState(AnimationType.MAGE_IDLE) {
        override fun onMessage(entity: Entity, ai: AIComponent, telegram: Telegram): Boolean {
            val extraInfo = telegram.extraInfo
            if (extraInfo is CharacterExtraInfo.Injured) {
                LOG.debug { "Entity '${extraInfo.entity}' is injured. Entity '$entity' will heal it." }
                ((Gdx.app.applicationListener) as Game).ecsEngine.configureEntity(entity) {
                    with<MoveToComponent> {
                        leashRange = 1.25f
                        targetEntity = extraInfo.entity
                    }
                }
                ai.stateMachine.changeState(Run)
                return true
            }
            return false
        }
    }

    object Run : MageState(AnimationType.MAGE_RUN) {
        override fun update(entity: Entity, ai: AIComponent) {
            if (entity[MoveToComponent.mapper] == null) {
                ai.stateMachine.changeState(StartCast)
            }
        }
    }

    object StartCast : MageState(AnimationType.MAGE_START_CAST) {
        override fun update(entity: Entity, ai: AIComponent) {
            val animation = entityAnimation(entity)
            if (animation.gdxAnimation.isAnimationFinished(animation.stateTime)) {
                ai.stateMachine.changeState(Cast)
            }
        }
    }

    object Cast : MageState(AnimationType.MAGE_CAST) {
        override fun enter(entity: Entity, ai: AIComponent) {
            super.enter(entity, ai)
            LOG.debug { "Entity '$entity' is healing" }
        }

        override fun update(entity: Entity, ai: AIComponent) {
            val animation = entityAnimation(entity)
            if (animation.gdxAnimation.isAnimationFinished(animation.stateTime)) {
                ai.stateMachine.changeState(Idle)
            }
        }
    }

    companion object {
        private val LOG = logger<MageState>()
    }
}
