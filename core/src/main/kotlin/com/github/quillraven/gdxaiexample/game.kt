package com.github.quillraven.gdxaiexample

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.quillraven.gdxaiexample.ecs.createDefaultEngine
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.box2d.earthGravity
import ktx.log.debug
import ktx.log.logger

class Game : KtxGame<KtxScreen>() {
    private val batch: Batch by lazy { SpriteBatch(512) }
    private val viewport: Viewport = FitViewport(16f, 9f)
    val ecsEngine: Engine by lazy { createDefaultEngine(batch, viewport, world, assets) }
    private val world: World by lazy {
        World(earthGravity, true).apply {
            autoClearForces = false
        }
    }
    private val assets: AssetManager by lazy { AssetManager() }

    override fun create() {
        if (System.getProperty("dev-mode", "false") == "true") {
            Gdx.app.logLevel = Application.LOG_DEBUG
        }

        addScreen(MainScreen(ecsEngine, world))
        setScreen<MainScreen>()
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        viewport.update(width, height, true)
    }

    override fun dispose() {
        LOG.debug { "Maximum sprites in batch: ${(batch as SpriteBatch).maxSpritesInBatch}" }

        batch.dispose()
        world.dispose()
        assets.dispose()
        super.dispose()
    }

    companion object {
        private val LOG = logger<Game>()
        const val UNIT_SCALE = 1 / 32f
        const val PHYSIC_TIME_STEP = 1 / 45f
    }
}
