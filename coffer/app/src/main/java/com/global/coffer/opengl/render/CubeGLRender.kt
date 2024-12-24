//package com.global.coffer.opengl.render
//
//import android.opengl.GLES20
//import android.opengl.GLSurfaceView.Renderer
//import android.opengl.Matrix
//import android.view.MotionEvent
//import com.global.coffer.opengl.utils.Cube
//import javax.microedition.khronos.egl.EGLConfig
//import javax.microedition.khronos.opengles.GL10
//import kotlin.math.sqrt
//
//
//class CubeGLRender: Renderer {
//    private var cube: Cube? = null
//    private val mMVPMatrix = FloatArray(16)
//    private val mProjectionMatrix = FloatArray(16)
//    private val mViewMatrix = FloatArray(16)
//    private val mRotationMatrix = FloatArray(16)
//    private val mTranslationMatrix = FloatArray(16)
//    private val mScaleMatrix = FloatArray(16)
//
//    private val touchStart = FloatArray(2)
//    private val currentRotation = FloatArray(3) // x, y, z 旋转角度
//    private val currentTranslation = FloatArray(3) // x, y, z 平移
//    private var currentScale = 1.0f
//
//
//    init {
//        cube = Cube()
//        Matrix.setIdentityM(mRotationMatrix, 0)
//        Matrix.setIdentityM(mTranslationMatrix, 0)
//        Matrix.setIdentityM(mScaleMatrix, 0)
//    }
//
//    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
//        gl?.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
//        cube!!.loadShaders()
//    }
//
//    override fun onDrawFrame(gl: GL10?) {
//        gl?.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
//
//        // 计算变换矩阵
//        Matrix.setIdentityM(mMVPMatrix, 0)
//        Matrix.translateM(
//            mMVPMatrix, 0,
//            currentTranslation[0], currentTranslation[1], currentTranslation[2]
//        )
//        Matrix.scaleM(mMVPMatrix, 0, currentScale, currentScale, currentScale)
//        Matrix.multiplyMM(mMVPMatrix, 0, mMVPMatrix, 0, mRotationMatrix, 0)
//
//        cube!!.draw(mMVPMatrix)
//    }
//
//    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
//        gl?.glViewport(0, 0, width, height)
//        val ratio = width.toFloat() / height
//        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)
//        Matrix.setLookAtM(mViewMatrix, 0, 0f, 0f, -5f, 0f, 0f, 0f, 0f, 1f, 0f)
//    }
//
//    fun handleTouch(event: MotionEvent): Boolean {
//        val x = event.x
//        val y = event.y
//
//        when (event.action) {
//            MotionEvent.ACTION_DOWN -> {
//                touchStart[0] = x
//                touchStart[1] = y
//                return true
//            }
//
//            MotionEvent.ACTION_MOVE -> {
//                val dx = x - touchStart[0]
//                val dy = y - touchStart[1]
//
//                // 旋转
//                currentRotation[0] += dy * 0.5f // 绕 X 轴旋转
//                currentRotation[1] += dx * 0.5f // 绕 Y 轴旋转
//
//                // 更新旋转矩阵
//                Matrix.setRotateM(mRotationMatrix, 0, currentRotation[0], 1f, 0f, 0f)
//                Matrix.rotateM(mRotationMatrix, 0, currentRotation[1], 0f, 1f, 0f)
//
//                touchStart[0] = x
//                touchStart[1] = y
//                return true
//            }
//
//            MotionEvent.ACTION_POINTER_DOWN -> {
//                // 处理缩放
//                if (event.pointerCount == 2) {
//                    val distance = spacing(event)
//                    if (distance > 10f) { // 确保有足够的距离
//                        currentScale *= 1.1f // 放大
//                    }
//                }
//                return true
//            }
//
//            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP ->                 // 处理指针抬起
//                return true
//        }
//        return false
//    }
//
//    private fun spacing(event: MotionEvent): Float {
//        val x = event.getX(0) - event.getX(1)
//        val y = event.getY(0) - event.getY(1)
//        return sqrt((x * x + y * y).toDouble()).toFloat()
//    }
//}