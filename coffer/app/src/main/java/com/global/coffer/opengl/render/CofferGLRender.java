package com.global.coffer.opengl.render;

import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CofferGLRender implements GLSurfaceView.Renderer {

    //顶点数组
    private float[] mArrayVertex = { 0f, 0f, 0f };

    // 缓冲区
    private FloatBuffer mBuffer;

    public CofferGLRender() {
        //获取浮点形缓冲数据
        mBuffer = getFloatBuffer(mArrayVertex);
    }

    // Surface创建的时候调用
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // 设置清屏颜色为黑色（rgba）
        gl.glClearColor(0f, 0f, 0f, 0f);
    }

    // Surface改变的的时候调用
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // 设置OpenGL场景的大小
        gl.glViewport(width / 4, width / 2, width / 2, height / 2);
    }

    // 在Surface上绘制的时候调用
    @Override
    public void onDrawFrame(GL10 gl) {

        // 清除屏幕
        // GL_COLOR_BUFFER_BIT —— 表明颜色缓冲区
        // GL_DEPTH_BUFFER_BIT —— 表明深度缓冲
        // GL_STENCIL_BUFFER_BIT —— 表明模型缓冲区
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        // 允许设置顶点 // GL10.GL_VERTEX_ARRAY顶点数组
        // GL_COLOR_ARRAY —— 如果启用，颜色矩阵可以用来写入以及调用glDrawArrays方法或者glDrawElements方法时进行渲染。详见glColorPointer。
        // GL_NORMAL_ARRAY —— 如果启用，法线矩阵可以用来写入以及调用glDrawArrays方法或者glDrawElements方法时进行渲染。详见glNormalPointer。
        // GL_TEXTURE_COORD_ARRAY —— 如果启用，纹理坐标矩阵可以用来写入以及调用glDrawArrays方法或者glDrawElements方法时进行渲染。详见glTexCoordPointer。
        // GL_VERTEX_ARRAY —— 如果启用，顶点矩阵可以用来写入以及调用glDrawArrays方法或者glDrawElements方法时进行渲染。详见glVertexPointer。
        // GL_POINT_SIZE_ARRAY_OES(OES_point_size_arrayextension)——如果启用，点大小矩阵控制大小以渲染点和点sprites。这时由glPointSize定义的点大小将被忽略，由点大小矩阵 提供的大小将被用来渲染点和点sprites。详见glPointSize。
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        // 设置顶点
        // size —— 每个顶点的坐标维数，必须是2, 3 或者4，初始值是4。
        // type —— 指明每个顶点坐标的数据类型，允许的符号常量GL_BYTE, GL_SHORT,GL_FIXED 和GL_FLOAT，
        // 初始值为GL_FLOAT。
        // stride —— 指明连续顶点间的位偏移，如果为0，顶点被认为是紧密压入矩阵，初始值为0。
        // pointer —— 指明顶点坐标的缓冲区，如果为null，则没有设置缓冲区。
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mBuffer);

        //设置点的颜色为红色
        gl.glColor4f(1f, 0f, 0f, 0f);

        //设置点的大小
        gl.glPointSize(100f);

        // 绘制点
        // mode：有三种取值
        // GL_TRIANGLES：每三个顶之间绘制三角形，之间不连接
        // GL_TRIANGLE_FAN：以V0 V1 V2,V0 V2 V3,V0 V3 V4，……的形式绘制三角形
        // GL_TRIANGLE_STRIP：顺序在每三个顶点之间均绘制三角形。这个方法可以保证从相同的方向上所有三角形均被绘制。以V0 V1 V2 ,V1 V2 V3,V2 V3 V4,……的形式绘制三角形
        // first：从数组缓存中的哪一位开始绘制，一般都定义为0
        // count：顶点的数量
        gl.glDrawArrays(GL10.GL_POINTS, 0, 1);

        // 禁止顶点设置
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }

    /**
     * @param vertexes float 数组
     * @return 获取浮点形缓冲数据
     */
    public FloatBuffer getFloatBuffer(float[] vertexes) {
        FloatBuffer buffer;
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertexes.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        buffer = vbb.asFloatBuffer();
        //写入数组
        buffer.put(vertexes);
        //设置默认的读取位置
        buffer.position(0);
        return buffer;
    }

}
