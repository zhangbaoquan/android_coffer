package com.global.coffer.opengl.render

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLES20.GL_FLOAT
import android.opengl.GLES20.GL_LINES
import android.opengl.GLES20.GL_POINTS
import android.opengl.GLES20.GL_TRIANGLES
import android.opengl.GLES20.glDrawArrays
import android.opengl.GLES20.glEnableVertexAttribArray
import android.opengl.GLES20.glGetAttribLocation
import android.opengl.GLES20.glGetUniformLocation
import android.opengl.GLES20.glUniform4f
import android.opengl.GLES20.glUseProgram
import android.opengl.GLES20.glVertexAttribPointer
import android.opengl.GLES20.glViewport
import android.opengl.GLSurfaceView.Renderer
import com.global.coffer.opengl.utils.ShaderHelper
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class GameGLRender(context: Context) : Renderer {

    private val U_COLOR = "u_Color"
    private val A_POSITION = "a_Position"

    private val vertexData: FloatBuffer

    private val POSITION_COMPONENT_COUNT = 2

    private val BYTES_PER_FLOAT: Int = 4

    private val context:Context

    // 用来存储链接的程序ID
    private var program = 0

    private var uColorLocation = 0
    private var aPositionLocation = 0

    private val vertexShaderSource = "attribute vec4 a_Position;" +
            "void main() {" +
            "gl_Position = a_Position;" +
            "gl_PointSize = 10.0;" +
            "}"

    private val fragmentShaderSource = "precision mediump float;" +
            "uniform vec4 u_Color;" +
            "void main() {" +
            "gl_FragColor = u_Color;" +
            "}"

    // 构建游戏中桌子的顶点。长方形由两个三角形拼接而成，因为openGL 仅支持绘制点、线、三角形。
    var tableVerticesWithTriangles: FloatArray = floatArrayOf(
        // Triangle 1
        -0.5f, -0.5f,
        0.5f, 0.5f,
        -0.5f, 0.5f,

        // Triangle 2
        -0.5f, -0.5f,
        0.5f, -0.5f,
        0.5f, 0.5f,

        // Line 1 相当于木槌
        -0.5f, 0f,
        0.5f, 0f,

        // Mallets
        0f, -0.25f,
        0f, 0.25f
    )

    init {
        this.context = context
        vertexData = ByteBuffer
            .allocateDirect(tableVerticesWithTriangles.size * BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()

        vertexData.put(tableVerticesWithTriangles)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f) // 背景颜色
//        // 读取顶点着色器
//        val vertexShaderSource = TextResourceReader
//            .readTextFileFromResource(context, R.raw.simple_vertex_shader)
//        // 读取片着色器
//        val fragmentShaderSource = TextResourceReader
//            .readTextFileFromResource(context, R.raw.simple_fragment_shader)

        // 编译着色器
        val vertexShader: Int = ShaderHelper.compileVertexShader(vertexShaderSource)
        val fragmentShader: Int = ShaderHelper.compileFragmentShader(fragmentShaderSource)

        program = ShaderHelper.linkProgram(vertexShader, fragmentShader)
        // 验证程序
        ShaderHelper.validateProgram(program)
        // 使用程序
        glUseProgram(program)

        uColorLocation = glGetUniformLocation(program, U_COLOR)

        aPositionLocation = glGetAttribLocation(program, A_POSITION)

        // Bind our data, specified by the variable vertexData, to the vertex
        // attribute at location A_POSITION_LOCATION.
        // 关联顶点数组的属性
        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT,
            false, 0, vertexData)

        glEnableVertexAttribArray(aPositionLocation)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        // Set the OpenGL viewport to fill the entire surface.
        glViewport(0, 0, width, height);
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        // Draw the table.
        glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f);
        glDrawArrays(GL_TRIANGLES, 0, 6)

        // Draw the center dividing line.
        glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        glDrawArrays(GL_LINES, 6, 2)

        // Draw the first mallet blue.
        glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
        glDrawArrays(GL_POINTS, 8, 1)

        // Draw the second mallet red.
        glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        glDrawArrays(GL_POINTS, 9, 1)
    }
}