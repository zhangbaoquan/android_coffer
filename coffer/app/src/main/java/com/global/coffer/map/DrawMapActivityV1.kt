package com.global.coffer.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.global.coffer.R
import com.global.coffer.map.utils.DrawState
import com.global.coffer.map.view.DrawingView

class DrawMapActivityV1 : AppCompatActivity() {

    private var currentStatus: DrawState = DrawState.Default

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draw_map_main)
        val drawingView = findViewById<DrawingView>(R.id.drawing_view)
        findViewById<AppCompatButton>(R.id.b0).setOnClickListener({ v->
            val button :AppCompatButton = v as AppCompatButton
            // 开始绘制或者完成
            if(currentStatus == DrawState.Default || currentStatus == DrawState.Finish){
                currentStatus = DrawState.Start
                button.text = "完成绘制"
            } else if(currentStatus == DrawState.Start){
                currentStatus = DrawState.Finish
                button.text = "开始绘制"
            }
            drawingView.setDrawStatus(currentStatus)
        })

        findViewById<AppCompatButton>(R.id.b2).setOnClickListener({
            drawingView.cancelLastDrawAction()
        })

    }
}