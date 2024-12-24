package com.global.coffer

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.global.coffer.coroutine.CoroutineMainActivity
import com.global.coffer.demoPage.ConstraintActivity
import com.global.coffer.demoPage.GreenDaoActivity
import com.global.coffer.demoPage.IMItemStyleActivity
import com.global.coffer.opengl.page.OpenGLMainActivity
import com.global.coffer.rxjava.RxJavaOperationActivity
import com.global.coffer.demoPage.SQLiteOperationActivity
import com.global.coffer.demoPage.SelectImgVideoActivity
import com.global.coffer.jetpack.JetPackMainActivity
import com.global.coffer.map.MapMainActivity

/**
 * author       : coffer
 * date         : 2024/11/11
 * description  :
 */

class HomePageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_main)

        findViewById<Button>(R.id.b0).setOnClickListener({
            this.startActivity(Intent(this, SQLiteOperationActivity::class.java))
        })

        findViewById<Button>(R.id.b1).setOnClickListener({
            this.startActivity(Intent(this, RxJavaOperationActivity::class.java))
        })

        findViewById<Button>(R.id.b2).setOnClickListener({
            this.startActivity(Intent(this, IMItemStyleActivity::class.java))
        })

        findViewById<Button>(R.id.b3).setOnClickListener({
            this.startActivity(Intent(this, SelectImgVideoActivity::class.java))
        })
        findViewById<Button>(R.id.b4).setOnClickListener({
            this.startActivity(Intent(this, OpenGLMainActivity::class.java))
        })

        findViewById<Button>(R.id.b5).setOnClickListener({
            this.startActivity(Intent(this, CoroutineMainActivity::class.java))
        })

        findViewById<Button>(R.id.b6).setOnClickListener({
            this.startActivity(Intent(this, JetPackMainActivity::class.java))
        })

        findViewById<Button>(R.id.b7).setOnClickListener({
            this.startActivity(Intent(this, MapMainActivity::class.java))
        })

        findViewById<Button>(R.id.b8).setOnClickListener({
            this.startActivity(Intent(this, GreenDaoActivity::class.java))
        })

        // 使用ConstraintLayout 约束布局
        findViewById<Button>(R.id.b9).setOnClickListener({
            this.startActivity(Intent(this, ConstraintActivity::class.java))
        })
    }
}