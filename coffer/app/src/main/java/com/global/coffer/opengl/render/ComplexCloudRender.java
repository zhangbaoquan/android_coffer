package com.global.coffer.opengl.render;

import static android.opengl.GLES20.glUseProgram;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.view.MotionEvent;

import com.global.coffer.opengl.utils.ShaderHelper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class ComplexCloudRender implements GLSurfaceView.Renderer {

//    // 顶点着色器代码
//    private final String vertexShaderSource =
//            "attribute vec4 vPosition;" +
//                    "void main() {" +
//                    "  gl_PointSize = 5.0;" + // 点的大小
//                    "  gl_Position = vPosition;" +
//                    "}";

//    private final String vertexShaderSource =
//            "uniform mat4 uMVPMatrix;" +
//                    "attribute vec4 vPosition;" +
//                    "void main() {" +
//                    "  gl_PointSize = 5.0;" + // 点的大小
//                    "  gl_Position = uMVPMatrix * vPosition;" +
//                    "}";

    // 顶点着色器代码
    private final String vertexShaderSource =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "varying vec4 vColor;" +
                    "void main() {" +
                    "  gl_PointSize = 5.0;" + // 点的大小
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "  vColor = vec4(1.0, 0.0, 0.0, 1.0);" + // 默认颜色
                    "}";

    // 片段着色器代码
    private final String fragmentShaderSource =
            "precision mediump float;" +
                    "void main() {" +
                    "  gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);" + // 红色
                    "}";

    private FloatBuffer vertexBuffer;
    private int program;
    private int pointCount;

    /////////////////////////
    private float[] mViewMatrix = new float[16];
    private float[] mProjectionMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];

    private float[] currentTranslation = new float[3]; // x, y, z 平移
    private float previousX, previousY;
    private boolean isDragging = false;

    public ComplexCloudRender(){

    }

    public void initData(float[] vertexArray){
        Matrix.setIdentityM(mViewMatrix, 0);

        pointCount = vertexArray.length / 3;
        ByteBuffer bb = ByteBuffer.allocateDirect(vertexArray.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertexArray);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); // 背景颜色
        // 编译着色器
        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);

        program = ShaderHelper.linkProgram(vertexShader, fragmentShader);
        // 验证程序
        ShaderHelper.validateProgram(program);
        // 使用程序
        glUseProgram(program);
        vertexBuffer.position(0);
    }


    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }

    public boolean handleTouch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                previousX = event.getX();
                previousY = event.getY();
                isDragging = true;
                break;

            case MotionEvent.ACTION_MOVE:
                if (isDragging) {
                    float dx = event.getX() - previousX; // 计算X轴移动
                    float dy = event.getY() - previousY; // 计算Y轴移动

                    // 更新平移
                    currentTranslation[0] += dx * 0.01f; // 根据移动距离调整平移
                    currentTranslation[1] -= dy * 0.01f; // Y轴反向

                    previousX = event.getX();
                    previousY = event.getY();
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isDragging = false;
                break;
        }
        return true;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // 设置变换矩阵
        Matrix.setIdentityM(mMVPMatrix, 0);
        Matrix.translateM(mMVPMatrix, 0, currentTranslation[0], currentTranslation[1], currentTranslation[2]);

        // 获取变换矩阵句柄
        int mvpMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix");

        // 设置变换矩阵
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mMVPMatrix, 0);

        // 获取顶点位置句柄
        int positionHandle = GLES20.glGetAttribLocation(program, "vPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);

        // 准备坐标数据
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);

        // 绘制点云
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, pointCount);

        // 禁用顶点数组
        GLES20.glDisableVertexAttribArray(positionHandle);
    }
}
