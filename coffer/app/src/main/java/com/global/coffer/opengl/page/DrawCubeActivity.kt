package com.global.coffer.opengl.page

import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import com.global.coffer.R
//import com.global.coffer.opengl.render.CubeGLRender

class DrawCubeActivity : BaseDrawOpenGLActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opengl_cube_main)

        findViewById<Button>(R.id.b0).setOnClickListener({
            val container = findViewById<FrameLayout>(R.id.container)
//            openGLDraw(CubeGLRender(),container)
        })
    }
}