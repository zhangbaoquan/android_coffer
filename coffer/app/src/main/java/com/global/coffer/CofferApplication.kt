package com.global.coffer
import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex

/**
 * author      : coffer
 * date        : 10/14/21
 * description :
 */
open class CofferApplication : Application() {

    companion object {
        const val TAG = "CofferApplication_tag"
        lateinit var mInstance: CofferApplication

        fun getInstance(): CofferApplication {
            return mInstance
        }
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        mInstance = this
        MultiDex.install(this)
    }
}
