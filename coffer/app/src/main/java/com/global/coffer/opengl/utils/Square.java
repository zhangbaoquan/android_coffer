package com.global.coffer.opengl.utils;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Square {
    // 正方形的四个顶点坐标
    private final float[] coordinates = {
            -0.5f,  0.5f, 0.0f, // 左上角
            0.5f,  0.5f, 0.0f, // 右上角
            0.5f, -0.5f, 0.0f, // 右下角
            -0.5f, -0.5f, 0.0f  // 左下角
    };

    private final FloatBuffer vertexBuffer;

    // 定义颜色（红色）
    private final float[] color = {1.0f, 0.0f, 0.0f, 1.0f}; // RGBA

    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private final int mProgram;

    public Square() {
        // 初始化顶点缓冲区
        ByteBuffer bb = ByteBuffer.allocateDirect(coordinates.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(coordinates);
        vertexBuffer.position(0);

        // 编译着色器
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        // 创建 OpenGL 程序
        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);
    }

    public void draw() {
        // 使用程序
        GLES20.glUseProgram(mProgram);

        // 获取顶点位置句柄
        int positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // 启用顶点位置句柄
        GLES20.glEnableVertexAttribArray(positionHandle);

        // 准备坐标数据
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);

        // 获取颜色句柄
        int colorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // 设置颜色
        GLES20.glUniform4fv(colorHandle, 1, color, 0);

        // 绘制正方形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4);

        // 禁用顶点数组
        GLES20.glDisableVertexAttribArray(positionHandle);
    }

    private int loadShader(int type, String shaderCode) {
        // 创建顶点或片段着色器
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }
}

