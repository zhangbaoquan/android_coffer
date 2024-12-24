package com.global.coffer.jetpack.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * 定义数据库的操作方法
 */

@Dao
public interface IHistoryDao {

    /**
     * 根据ID查询HistoryData对象
     * @param id id数据
     * @return 数据结果
     */
    @Query("SELECT * FROM coffer_room_history_table WHERE id = :id")
    HistoryEntity queryAction(int id);

    /**
     * 向数据库添加数据
     * @param data 数据
     */
    @Insert
    void insertData(List<HistoryEntity> data);

    /**
     * 更新数据库
     * @param data 数据
     */
    @Update
    void upDataAction(HistoryEntity data);

    /**
     * 删除单个数据
     * @param data 数据
     */
    @Delete
    void deleteAction(HistoryEntity data);

    /**
     * 删除所有数据
     * @param data 数据
     */
    @Query("DELETE FROM coffer_room_history_table")
    void deleteAllDataAction(HistoryEntity data);
}
