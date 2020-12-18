package com.github.quillraven.gdxaiexample.ecs

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.quillraven.gdxaiexample.ecs.system.AISystem
import com.github.quillraven.gdxaiexample.ecs.system.AnimationSystem
import com.github.quillraven.gdxaiexample.ecs.system.JumpSystem
import com.github.quillraven.gdxaiexample.ecs.system.MoveSystem
import com.github.quillraven.gdxaiexample.ecs.system.PhysicDebugRenderSystem
import com.github.quillraven.gdxaiexample.ecs.system.PhysicSystem
import com.github.quillraven.gdxaiexample.ecs.system.RenderSystem
import ktx.assets.loadOnDemand

fun createDefaultEngine(
    batch: Batch,
    viewport: Viewport,
    world: World,
    assets: AssetManager,
) = PooledEngine().apply {
    addSystem(AISystem())
    addSystem(MoveSystem())
    addSystem(JumpSystem(world))
    addSystem(PhysicSystem(world))
    addSystem(AnimationSystem(assets.loadOnDemand<TextureAtlas>("assets.atlas").asset))
    addSystem(
        RenderSystem(
            assets.loadOnDemand<TextureAtlas>("assets.atlas").asset.findRegion("background"),
            batch,
            viewport
        )
    )
    addSystem(PhysicDebugRenderSystem(world, viewport))
}
