package com.global.coffer.map

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.global.coffer.R

class MapMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_main)

        findViewById<Button>(R.id.b0).setOnClickListener({
            this.startActivity(Intent(this, PixelLevelActivity::class.java))
        })

        findViewById<Button>(R.id.b1).setOnClickListener({
            this.startActivity(Intent(this, AnimMapActivity::class.java))
        })

        findViewById<Button>(R.id.b2).setOnClickListener({
            this.startActivity(Intent(this, DrawMapActivityV1::class.java))
        })

        findViewById<Button>(R.id.b3).setOnClickListener({
            this.startActivity(Intent(this, DrawMapActivityV2::class.java))
        })
    }
}