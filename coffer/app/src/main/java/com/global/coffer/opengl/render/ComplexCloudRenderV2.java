package com.global.coffer.opengl.render;

import static android.opengl.GLES20.glUseProgram;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.global.coffer.opengl.utils.ShaderHelper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class ComplexCloudRenderV2 implements GLSurfaceView.Renderer {


    // 顶点着色器代码
    private final String vertexShaderSource =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "attribute vec4 vColor;" + // 添加颜色属性
                    "varying vec4 fColor;" + // 传递到片段着色器的颜色
                    "void main() {" +
                    "  gl_PointSize = 5.0;" + // 点的大小
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "  fColor = vColor;" + // 将颜色传递给片段着色器
                    "}";

    // 片段着色器代码
    private final String fragmentShaderSource =
            "precision mediump float;" +
            "varying vec4 fColor;" + // 接收来自顶点着色器的颜色
                    "void main() {" +
                    "  gl_FragColor = fColor;" + // 使用传递的颜色
                    "}";

    private int pointCount;

    private FloatBuffer pointCloudBuffer;
    private FloatBuffer colorBuffer;

    // 变换矩阵
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    // 平移参数
    private float translateX = 0.0f;
    private float translateY = 0.0f;

    // 着色器程序
    private int program;
    private int positionHandle;
    private int colorHandle;
    private int mvpMatrixHandle;

    private float previousX;
    private float previousY;

    private float scale = 1.0f; // 缩放因子
    private float rotationAngle = 0.0f; // 旋转角度

    private ScaleGestureDetector scaleDetector;
    private GestureDetector gestureDetector;

    public ComplexCloudRenderV2(){

    }

    public void initData(float[] vertexArray,Context context){
        scaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        gestureDetector = new GestureDetector(context, new GestureListener());
        Matrix.setIdentityM(mViewMatrix, 0);
        initPointBuffer(vertexArray);
        initColorBuffer(vertexArray);
    }

    private void initPointBuffer(float[] vertexArray){
        pointCount = vertexArray.length;
        ByteBuffer bb = ByteBuffer.allocateDirect(vertexArray.length * 4);
        bb.order(ByteOrder.nativeOrder());
        pointCloudBuffer = bb.asFloatBuffer();
        pointCloudBuffer.put(vertexArray);
        pointCloudBuffer.position(0);
    }

    private void initColorBuffer(float[] vertexArray){
        ByteBuffer bb = ByteBuffer.allocateDirect(vertexArray.length * 4);
        bb.order(ByteOrder.nativeOrder());
        colorBuffer = bb.asFloatBuffer();
        colorBuffer.put(vertexArray);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); // 背景颜色
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        // 编译着色器
        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);

        program = ShaderHelper.linkProgram(vertexShader, fragmentShader);
        // 验证程序
        ShaderHelper.validateProgram(program);
        // 使用程序
        glUseProgram(program);
    }


    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        Matrix.orthoM(mProjectionMatrix, 0, -1, 1, -1, 1, -1, 1);
    }

    public boolean handleTouch(MotionEvent event) {
        // 处理缩放手势
        scaleDetector.onTouchEvent(event);
        // 处理旋转手势
//        gestureDetector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dx = (event.getX() - previousX) / 1000; // 调整平移因子
                float dy = (event.getY() - previousY) / 1000;
                setTranslation(dx, -dy);
                break;
        }
        previousX = event.getX();
        previousY = event.getY();
        return true;
    }

    @Override
    public void onDrawFrame(GL10 gl) {

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // 使用着色器程序
        GLES20.glUseProgram(program);

        // 获取属性和 uniform 句柄
        positionHandle = GLES20.glGetAttribLocation(program, "vPosition");
        colorHandle = GLES20.glGetAttribLocation(program, "vColor");
        mvpMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix");

        // 设置变换矩阵
        Matrix.setIdentityM(mMVPMatrix, 0);
        Matrix.translateM(mMVPMatrix, 0, translateX, translateY, 0);
        Matrix.scaleM(mMVPMatrix, 0, scale, scale, 1); // 应用缩放
        Matrix.rotateM(mMVPMatrix, 0, rotationAngle, 0, 0, 1); // 应用旋转

        // 传递点云数据
        pointCloudBuffer.position(0);
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 0, pointCloudBuffer);
        GLES20.glEnableVertexAttribArray(positionHandle);

        // 传递颜色数据
        colorBuffer.position(0);
        GLES20.glVertexAttribPointer(colorHandle, 3, GLES20.GL_FLOAT, false, 0, colorBuffer);
        GLES20.glEnableVertexAttribArray(colorHandle);

        // 传递变换矩阵
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mMVPMatrix, 0);

        // 绘制点
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, pointCount);

        // 禁用属性句柄
        GLES20.glDisableVertexAttribArray(positionHandle);
    }

    public void setTranslation(float x, float y) {
        translateX += x;
        translateY += y;
    }

    public void setScale(float scaleFactor) {
        scale *= scaleFactor; // 更新缩放因子
    }

    public void setRotation(float angle) {
        rotationAngle += angle; // 更新旋转角度
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();
            setScale(scaleFactor); // 更新缩放
            return true;
        }
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float angle = distanceX * 0.5f; // 调整旋转速度
            setRotation(angle); // 更新旋转
            return true;
        }
    }
}
