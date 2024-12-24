package com.global.coffer.opengl.render
import android.opengl.GLES20.GL_COLOR_BUFFER_BIT
import android.opengl.GLES20.glClear
import android.opengl.GLES20.glClearColor
import android.opengl.GLES20.glViewport
import android.opengl.GLSurfaceView.Renderer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import com.global.coffer.opengl.utils.Square

class SquareGLRender : Renderer {

    private val square: Square

    init {
        square = Square()
    }
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // 设置清屏颜色为黑色
        glClearColor(0.0f, 0.5f, 0.0f, 0.0f)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        glViewport(0, 0, width, height);
    }

    override fun onDrawFrame(gl: GL10?) {
        // 清除屏幕
        glClear(GL_COLOR_BUFFER_BIT)
        square.draw()
    }


}