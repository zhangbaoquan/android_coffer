package com.global.coffer.opengl.page

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.global.coffer.R

class WebGLActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webgl_main)

        val webView = findViewById<WebView>(R.id.webView)
        webView.settings.javaScriptEnabled = true
//        webView.loadUrl("https://demo-1302416092.cos.ap-nanjing.myqcloud.com/demo/%E7%82%B9%E4%BA%91/dist/index.html")
        webView.loadUrl("https://demo-1302416092.cos.ap-nanjing.myqcloud.com/demo/%E7%82%B9%E4%BA%91/dist/%E7%82%B9%E4%BA%912.html")
    }
}