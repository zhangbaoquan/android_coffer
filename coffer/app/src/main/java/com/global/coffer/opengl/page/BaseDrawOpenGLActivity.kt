package com.global.coffer.opengl.page

import android.app.ActivityManager
import android.opengl.GLSurfaceView
import android.os.Build
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.global.coffer.opengl.view.SquareGLSurfaceView


open class BaseDrawOpenGLActivity : AppCompatActivity() {

//    private var mGLSurfaceView: SquareGLSurfaceView? = null
    private var mGLSurfaceView: SquareGLSurfaceView? = null

    fun openGLDraw(renderer: GLSurfaceView.Renderer, container: FrameLayout) {
        // 判断设备是否支持OpenGl ES 2.0
        if (isSupported()) {
            // 先建GLSurfaceView实例
            mGLSurfaceView = SquareGLSurfaceView(this)
            // 设置渲染器
            mGLSurfaceView!!.setRenderer(renderer)
            container.addView(mGLSurfaceView)
        } else {
            Toast.makeText(this,"不支持OpenGL 2.0",Toast.LENGTH_LONG).show()
        }
    }

    private fun isSupported(): Boolean {
        val activityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val configurationInfo = activityManager.deviceConfigurationInfo
        var supportsEs2 = configurationInfo.reqGlEsVersion >= 0x2000

        val isEmulator = Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
                && (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86"))

        supportsEs2 = supportsEs2 || isEmulator
        return supportsEs2
    }

    override fun onPause() {
        super.onPause()
        mGLSurfaceView?.let {
            mGLSurfaceView!!.onPause()
        }
    }

    override fun onResume() {
        super.onResume()
        mGLSurfaceView?.let {
            mGLSurfaceView!!.onResume()
        }
    }

}