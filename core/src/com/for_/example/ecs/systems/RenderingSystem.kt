package com.for_.example.ecs.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g2d.Batch
import com.for_.example.ecs.components.BodyComponent
import com.for_.example.ecs.components.TextureComponent
import com.for_.example.ecs.compareEntityByPosition
import com.for_.example.ecs.notNull

class RenderingSystem(
    private val batch: Batch,
    private val camera: Camera
) : SortedIteratingSystem(
    Family.all(BodyComponent::class.java, TextureComponent::class.java).get(),
    Comparator(::compareEntityByPosition)
) {

    private val bodyComponentMapper = ComponentMapper.getFor(BodyComponent::class.java)
    private val textureComponentMapper = ComponentMapper.getFor(TextureComponent::class.java)

    override fun update(deltaTime: Float) {
        camera.update()
        batch.projectionMatrix = camera.combined
        batch.enableBlending()
        batch.begin()

        super.update(deltaTime)

        batch.end()
    }

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        notNull(bodyComponentMapper[entity], textureComponentMapper[entity], ::render)
    }

    fun dispose() {
        batch.dispose()
        entities.forEach {
            textureComponentMapper[it]?.texture?.dispose()
        }
    }

    private fun render(bodyComponent: BodyComponent, textureComponent: TextureComponent) {
        val shape = bodyComponent.rectangle
        batch.draw(textureComponent.texture, shape.x, shape.y, shape.width, shape.height)
    }
}
