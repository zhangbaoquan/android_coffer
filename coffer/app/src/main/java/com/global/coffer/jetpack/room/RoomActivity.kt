package com.global.coffer.jetpack.room

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.global.coffer.R
import kotlinx.coroutines.launch


class RoomActivity : AppCompatActivity() {

    private lateinit var editText1: EditText
    private lateinit var editText2: EditText

    private lateinit var textView: TextView

    private lateinit var airportViewModel: AirportViewModel

    private var index = 0
    private var age = 20

    private var data:AirportEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_map_main)
        textView = findViewById(R.id.content)
        editText1 = findViewById(R.id.edit1)
        editText2 = findViewById(R.id.edit2)
        airportViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            AirportViewModel::class.java
        )
        findViewById<View>(R.id.b0).setOnClickListener { v: View? ->

            lifecycleScope.launch {
                Log.i("haha_tag", "数据库插入")
                val str = if(TextUtils.isEmpty(editText1.text.toString())){
                    "lala"
                } else {
                    editText1.text.toString()
                }
                // 启动协程来插入数据库
                airportViewModel.insertAirport(
                    AirportEntity(
                        str,
                        age++,
                        "China"
                    )
                )
                index++
            }
        }

        findViewById<View>(R.id.b1).setOnClickListener { v: View? ->
            // 更新
            val str = if(TextUtils.isEmpty(editText2.text.toString())){
                "haha"
            } else {
                editText2.text.toString()
            }
            // 更新
            airportViewModel.update(AirportEntity(
                str,
                age++,
                "China",
                0
            ))
            Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show()
        }

        airportViewModel.getAirportQueryByIdData().observe(this, object : Observer<AirportEntity?> {
            override fun onChanged(value: AirportEntity?) {
                data = value
                value?.let {
                    textView.text = it.name
                }
            }
        })

        findViewById<View>(R.id.b2).setOnClickListener { v: View? ->
            // 查询
//            airportViewModel.queryByIdData(index)
            airportViewModel.queryByIdDataToRx(index)
        }

        findViewById<View>(R.id.b3).setOnClickListener { v: View? ->
            // 删除单个
            data?.let {
                airportViewModel.deleteSingleData(it)
            }

        }

        findViewById<View>(R.id.b4).setOnClickListener { v: View? ->
            // 全部删除
            airportViewModel.deleteAllData()
        }

    }


}