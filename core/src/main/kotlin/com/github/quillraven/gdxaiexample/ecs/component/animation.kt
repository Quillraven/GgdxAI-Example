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
    ADVENTURER_IDLE("adventurer-idle", Animation.PlayMode.LOOP_PINGPONG, 1f),
    ADVENTURER_ATTACK_1("adventurer-attack1", Animation.PlayMode.NORMAL, 1f),
    ADVENTURER_ATTACK_2("adventurer-attack2", Animation.PlayMode.NORMAL, 1f),
    ADVENTURER_ATTACK_3("adventurer-attack3", Animation.PlayMode.NORMAL, 1f),
    ADVENTURER_FALL("adventurer-fall", Animation.PlayMode.LOOP_PINGPONG, 1f),
    ADVENTURER_START_JUMP("adventurer-start_jump", Animation.PlayMode.NORMAL, 1f),
    ADVENTURER_JUMP("adventurer-jump", Animation.PlayMode.LOOP, 1f),
    ADVENTURER_RUN("adventurer-run", Animation.PlayMode.LOOP, 1f),
    MAGE_IDLE("mage-idle"),
    MAGE_RUN("mage-run"),
    MAGE_START_CAST("mage-start-cast", Animation.PlayMode.NORMAL),
    MAGE_CAST("mage-cast", Animation.PlayMode.NORMAL),
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
