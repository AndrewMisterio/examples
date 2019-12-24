package com.for_.example.ecs

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Rectangle
import com.for_.example.ecs.systems.MovementSystem
import com.for_.example.ecs.systems.RenderingSystem

class Engine(
    batch: Batch,
    camera: Camera,
    screenRect: Rectangle
) : PooledEngine() {

    private val renderingSystem = RenderingSystem(batch, camera)

    init {
        addSystem(MovementSystem(screenRect))
        addSystem(renderingSystem)
    }

    fun dispose() {
        renderingSystem.dispose()
    }
}