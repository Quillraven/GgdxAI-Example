package com.github.quillraven.gdxaiexample.ecs.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.ai.msg.MessageManager
import com.github.quillraven.gdxaiexample.ecs.component.AIComponent
import com.github.quillraven.gdxaiexample.ecs.component.PlayerComponent
import ktx.app.KtxInputAdapter
import ktx.ashley.allOf
import ktx.ashley.get

enum class MsgCode {
    KEY_DOWN,
    KEY_UP,
    CHARACTER_INJURED,
}

sealed class KeyExtraInfo(var keycode: Int) {
    object Down : KeyExtraInfo(Input.Keys.UNKNOWN)
    object Up : KeyExtraInfo(Input.Keys.UNKNOWN)
}

sealed class CharacterExtraInfo(var entity: Entity) {
    object Injured : CharacterExtraInfo(NO_ENTITY)

    companion object {
        val NO_ENTITY = Entity()
    }
}

class AISystem : IteratingSystem(allOf(AIComponent::class).get()), KtxInputAdapter, EntityListener {
    private val messageDispatcher = MessageManager.getInstance()

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        Gdx.input.inputProcessor = this
        engine.addEntityListener(family, 1, this)
    }

    override fun removedFromEngine(engine: Engine) {
        super.removedFromEngine(engine)
        Gdx.input.inputProcessor = null
        engine.removeEntityListener(this)
    }

    override fun entityAdded(entity: Entity) {
        val ai = entity[AIComponent.mapper]
        requireNotNull(ai, { "AIComponent missing for entity '$entity'" })

        MsgCode.values().forEach {
            messageDispatcher.addListener(ai.stateMachine, it.ordinal)
        }

        ai.stateMachine.owner = entity
        ai.stateMachine.changeState(ai.state)
    }

    override fun entityRemoved(entity: Entity) {
        val ai = entity[AIComponent.mapper]
        requireNotNull(ai, { "AIComponent missing for entity '$entity'" })

        MsgCode.values().forEach {
            messageDispatcher.removeListener(ai.stateMachine, it.ordinal)
        }
    }

    override fun update(deltaTime: Float) {
        // update message dispatcher to send telegrams
        GdxAI.getTimepiece().update(deltaTime)
        messageDispatcher.update()

        super.update(deltaTime)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val ai = entity[AIComponent.mapper]
        requireNotNull(ai, { "AIComponent missing for entity '$entity'" })

        // update statemachine
        ai.stateTime += deltaTime
        ai.stateMachine.update()
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.I) {
            engine.entities.forEach { entity ->
                if (entity[PlayerComponent.mapper] != null) {
                    messageDispatcher.dispatchMessage(
                        2.5f,
                        MsgCode.CHARACTER_INJURED.ordinal,
                        CharacterExtraInfo.Injured.apply { this.entity = entity }
                    )
                    return true
                }
            }
        } else {
            messageDispatcher.dispatchMessage(
                MsgCode.KEY_DOWN.ordinal,
                KeyExtraInfo.Down.apply { this.keycode = keycode }
            )
        }
        return true
    }

    override fun keyUp(keycode: Int): Boolean {
        messageDispatcher.dispatchMessage(MsgCode.KEY_UP.ordinal, KeyExtraInfo.Up.apply { this.keycode = keycode })
        return true
    }
}
