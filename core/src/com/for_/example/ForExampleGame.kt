package com.for_.example

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.viewport.FitViewport
import com.for_.example.ecs.Engine
import com.for_.example.ecs.components.BodyComponent
import com.for_.example.ecs.components.PositionComponent
import com.for_.example.ecs.components.TextureComponent
import com.for_.example.ecs.components.VelocityComponent

class ForExampleGame : ApplicationAdapter() {
    private val screenRect = Rectangle(0f, 0f, WIDTH * 2, HEIGHT * 2)
    private val viewPort = FitViewport(WIDTH, HEIGHT)
    private val camera by lazy(viewPort::getCamera)
    private val engine by lazy {
        Engine(
            batch = SpriteBatch(),
            camera = camera,
            screenRect = screenRect
        )
    }

    override fun create() {
        (camera as? OrthographicCamera)?.zoom = 1f
        camera.position.x = WIDTH / 2f
        camera.position.y = HEIGHT / 2f
        engine.addEntity(createHero())
        engine.addEntity(createBG())
    }


    override fun render() {
        Gdx.gl.glClearColor(0.5f, 0f, 0.2f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        engine.update(Gdx.graphics.deltaTime)
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        viewPort.update(width, height)
        screenRect.width = viewPort.worldWidth
        screenRect.height = viewPort.worldHeight
    }

    override fun dispose() {
        engine.dispose()
    }

    private fun createBG() = engine.createEntity().also { entity ->
        entity.add(engine.createComponent(PositionComponent::class.java).apply {
            z = 0f
        })
        entity.add(engine.createComponent(BodyComponent::class.java).apply {
            shape.setWidth(WIDTH)
            shape.setHeight(HEIGHT)
        })
        entity.add(engine.createComponent(TextureComponent::class.java).apply {
            sprite = Texture("bg.png")
        })
    }

    private fun createHero() = engine.createEntity().also { entity ->
        entity.add(engine.createComponent(PositionComponent::class.java).apply {
            z = 1f
        })
        entity.add(engine.createComponent(BodyComponent::class.java).apply {
            shape.setWidth(100f)
            shape.setHeight(100f)
        })
        entity.add(engine.createComponent(TextureComponent::class.java).apply {
            sprite = Texture("badlogic.jpg")
        })
        entity.add(engine.createComponent(VelocityComponent::class.java).apply {
            x = -80f
            y = -100f
        })
    }

    companion object {
        private const val WIDTH = 640f
        private const val HEIGHT = 640f
    }
}