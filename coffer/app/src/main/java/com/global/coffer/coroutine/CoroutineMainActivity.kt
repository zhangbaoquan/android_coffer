package com.global.coffer.coroutine

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.*

class CoroutineMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        testLaunch()
//        Thread.sleep(2000L)
    }


//    private fun testLaunch() {
//        val scope = CoroutineScope(Job())
//        scope.launch {
//            logX("Hello!")
//            delay (1000L)
//            logX ("World!")
//        }
//    }
//
//    fun logX(any: Any?) {
//        println(
//            """ ================================
//            $any Thread:${Thread.currentThread().name}
//            ================================"""
//                .trimIndent()
//        )
//    }

}