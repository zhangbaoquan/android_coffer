package com.global.coffer.jetpack.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * 创建数据库抽象类（Database）
 *
 * 在Room数据库框架中，你需要创建一个继承自RoomDatabase的抽象类，这个类将作为数据库的访问入口，并定义与实体类和DAO的关联
 * @Database注解定义了数据库包含的实体类（entities）和数据库版本（version）。
 * exportSchema属性用于控制是否导出数据库的schema文件，通常在开发阶段设置为true，而在生产环境中设置为false
 *
 * @TypeConverters注解用于指定一个或多个类，这些类包含自定义的类型转换器，如果需要将非标准类型（如Date或URL）存储在数据库中，这些转换器是必需的。
 */
@Database(entities = [AirportEntity::class], version = 1, exportSchema = false)
//@TypeConverters(YourTypeConverters::class) // 如果有自定义类型转换器，在这里指定
abstract class AirportRoomDatabase : RoomDatabase() {

    // 提供获取DAO实例的方法
    abstract fun getAirportDao(): IAirportDao

    companion object {
        @Volatile
        private var instance: AirportRoomDatabase? = null

        fun getInstance(context: Context): AirportRoomDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { inst ->
                    instance = inst
                }
            }
        }

        private fun buildDatabase(context: Context): AirportRoomDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AirportRoomDatabase::class.java,
                "airport_database"
            ).build()
        }
    }
}