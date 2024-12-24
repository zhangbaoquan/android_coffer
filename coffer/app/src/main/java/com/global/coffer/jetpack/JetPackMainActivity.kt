package com.global.coffer.jetpack

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.global.coffer.R
import com.global.coffer.jetpack.room.RoomActivity

class JetPackMainActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jetpack_main)

        findViewById<Button>(R.id.b0).setOnClickListener({
            this.startActivity(Intent(this, RoomActivity::class.java))
        })

        findViewById<Button>(R.id.b1).setOnClickListener({
            this.startActivity(Intent(this, PagingActivity::class.java))
        })

        findViewById<Button>(R.id.b2).setOnClickListener({
            this.startActivity(Intent(this, MVVMActivity::class.java))
        })

        findViewById<Button>(R.id.b3).setOnClickListener({
            this.startActivity(Intent(this, AndroidCoroutineActivity::class.java))
        })
    }


}