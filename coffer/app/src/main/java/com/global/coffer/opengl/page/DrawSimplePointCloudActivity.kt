package com.global.coffer.opengl.page

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.FrameLayout
import com.global.coffer.R
import com.global.coffer.opengl.render.SimpleCloudRender
import com.global.coffer.opengl.utils.PCDParser
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class DrawSimplePointCloudActivity : BaseDrawOpenGLActivity() {

    private val mUIHandler: Handler = Handler(Looper.getMainLooper())

    private val render = SimpleCloudRender()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opengl_simple_cloud_main)

        findViewById<Button>(R.id.b0).setOnClickListener({
            val container = findViewById<FrameLayout>(R.id.container)

            val singleThreadExecutor: ExecutorService = Executors.newSingleThreadExecutor()
            singleThreadExecutor.execute {
                val t1 = System.currentTimeMillis()
                // 在子线程解析pcd文件，然后切换到主线程进行渲染
                val data = PCDParser()
                    .parsePcdToFloat("happy.pcd")
                val t2 = System.currentTimeMillis() - t1
                Log.i("haha_tag", "读文件耗时 ：$t2")
                Log.i("haha_tag", "points size : ${data.size}")
                mUIHandler.post({
                    render.initData(data)
                    openGLDraw(render, container)
                })
            }

        })

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return render.handleTouch(event) || super.onTouchEvent(event)
    }
}