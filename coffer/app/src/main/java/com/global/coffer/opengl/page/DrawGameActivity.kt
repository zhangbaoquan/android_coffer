package com.global.coffer.opengl.page

import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import com.global.coffer.R
import com.global.coffer.opengl.render.GameGLRender

class DrawGameActivity : BaseDrawOpenGLActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opengl_game_main)
        findViewById<Button>(R.id.b0).setOnClickListener({
            val container = findViewById<FrameLayout>(R.id.container)
            openGLDraw(GameGLRender(this),container)
        })
    }
}