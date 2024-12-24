package com.global.coffer.jetpack.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
 * 定义数据库的操作方法
 */
@Dao
interface IAirportDao {
    /**
     * 插入单个Airport对象
     */
    @Insert
    suspend fun insertAction(data: AirportEntity)

    /**
     * 插入单个Airport对象
     */
    @Insert
    suspend fun insertAllAction(data: MutableList<AirportEntity>)

    /**
     * 根据ID查询Airport对象
     */
    @Query("SELECT * FROM coffer_room_table_airports WHERE id = :id")
    suspend fun queryAirportById(id: Int): AirportEntity?

    /**
     * 根据ID查询Airport对象（使用RxJava）
     */
    @Query("SELECT * FROM coffer_room_table_airports WHERE id = :id")
    fun queryAirportByIdRx(id: Int): AirportEntity?

    /**
     * 更新数据库数据
     */
    @Update
    suspend fun updateAction(data: AirportEntity)

    /**
     * 删除单个对象
     */
    @Delete
    suspend fun deleteAction(data: AirportEntity)

    /**
     * 删除所有对象
     */
    @Query("DELETE FROM coffer_room_table_airports")
    suspend fun deleteAllAction()

}