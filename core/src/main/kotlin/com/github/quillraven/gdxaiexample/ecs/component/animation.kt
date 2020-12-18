package com.github.quillraven.gdxaiexample.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

enum class AnimationType(
    val atlasKey: String,
    val playMode: Animation.PlayMode = Animation.PlayMode.LOOP,
    val offsetInPxY: Float = 0f,
) {
    NONE("error"),
    ADVENTURER_IDLE("idle", Animation.PlayMode.LOOP_PINGPONG, 1f),
    ADVENTURER_ATTACK_1("attack1", Animation.PlayMode.NORMAL, 1f),
    ADVENTURER_ATTACK_2("attack2", Animation.PlayMode.NORMAL, 1f),
    ADVENTURER_ATTACK_3("attack3", Animation.PlayMode.NORMAL, 1f),
    ADVENTURER_FALL("fall", Animation.PlayMode.LOOP_PINGPONG, 1f),
    ADVENTURER_START_JUMP("start_jump", Animation.PlayMode.NORMAL, 1f),
    ADVENTURER_JUMP("jump", Animation.PlayMode.LOOP, 1f),
    ADVENTURER_RUN("run", Animation.PlayMode.LOOP, 1f),
}

class AnimationComponent : Component, Pool.Poolable {
    var stateTime = 0f
    var dirty = true
    var type = AnimationType.NONE
        set(value) {
            dirty = true
            field = value
        }
    lateinit var gdxAnimation: Animation<TextureRegion>

    override fun reset() {
        stateTime = 0f
        dirty = true
        type = AnimationType.NONE
    }

    companion object {
        val mapper = mapperFor<AnimationComponent>()
    }
}
