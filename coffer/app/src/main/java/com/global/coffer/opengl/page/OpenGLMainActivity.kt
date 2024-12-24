package com.global.coffer.opengl.page

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.global.coffer.R

class OpenGLMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opengl_main)

        findViewById<Button>(R.id.b0).setOnClickListener({
            this.startActivity(Intent(this, DrawSquareActivity::class.java))
        })

        findViewById<Button>(R.id.b1).setOnClickListener({
            this.startActivity(Intent(this, DrawCubeActivity::class.java))
        })

        findViewById<Button>(R.id.b2).setOnClickListener({
            this.startActivity(Intent(this, DrawGameActivity::class.java))
        })

        findViewById<Button>(R.id.b3).setOnClickListener({
            this.startActivity(Intent(this, DrawSimplePointCloudActivity::class.java))
        })
        findViewById<Button>(R.id.b4).setOnClickListener({
            this.startActivity(Intent(this, DrawComplexPointCloudActivity::class.java))
        })
        findViewById<Button>(R.id.b5).setOnClickListener({
            this.startActivity(Intent(this, WebGLActivity::class.java))
        })
    }
}
