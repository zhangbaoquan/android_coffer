package com.global.coffer.jetpack.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coffer_room_table_airports")
data class AirportEntity(
    val name:String,
    val age:Int,
    val country:String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,// 主键，自动生成
)
