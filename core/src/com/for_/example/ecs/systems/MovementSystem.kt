package com.for_.example.ecs.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Rectangle
import com.for_.example.ecs.components.BodyComponent
import com.for_.example.ecs.components.PositionComponent
import com.for_.example.ecs.components.VelocityComponent
import com.for_.example.ecs.notNull

class MovementSystem(
    private val screenRect: Rectangle
) : IteratingSystem(
    Family.all(
        BodyComponent::class.java,
        PositionComponent::class.java,
        VelocityComponent::class.java
    ).get()
) {

    private val bodyComponentMapper = ComponentMapper.getFor(BodyComponent::class.java)
    private val positionComponentMapper = ComponentMapper.getFor(PositionComponent::class.java)
    private val velocityComponentMapper = ComponentMapper.getFor(VelocityComponent::class.java)

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        notNull(
            positionComponentMapper[entity],
            velocityComponentMapper[entity],
            bodyComponentMapper[entity],
            deltaTime.coerceAtMost(0.25f),
            ::move
        )
    }

    private fun move(
        positionComponent: PositionComponent,
        velocityComponent: VelocityComponent,
        bodyComponent: BodyComponent,
        deltaTime: Float
    ) {
        bodyComponent.rectangle.x += velocityComponent.x * deltaTime
        bodyComponent.rectangle.y += velocityComponent.y * deltaTime
        when {
            hasHorizontalCollision(bodyComponent) -> {
                velocityComponent.x *= -1
                bodyComponent.rectangle.x = positionComponent.x
            }
            hasVerticalCollision(bodyComponent) -> {
                velocityComponent.y *= -1
                bodyComponent.rectangle.y = positionComponent.y
            }
            else -> {
                positionComponent.x = bodyComponent.rectangle.x
                positionComponent.y = bodyComponent.rectangle.y
            }
        }
    }

    private fun hasHorizontalCollision(bodyComponent: BodyComponent) =
        bodyComponent.rectangle.x < screenRect.x ||
                (bodyComponent.rectangle.x + bodyComponent.rectangle.width) > (screenRect.x + screenRect.width)

    private fun hasVerticalCollision(bodyComponent: BodyComponent) =
        bodyComponent.rectangle.y < screenRect.y ||
                (bodyComponent.rectangle.y + bodyComponent.rectangle.height) > (screenRect.y + screenRect.height)
}