package com.for_.example.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite

class TextureComponent :Component {
    lateinit var texture: Texture
}
