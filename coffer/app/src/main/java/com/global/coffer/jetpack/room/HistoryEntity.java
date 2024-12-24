package com.global.coffer.jetpack.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entity：数据库中表对应的Java实体
 * tableName: 指定表名。
 * primaryKeys: 指定主键字段。
 * indices: 定义索引。
 */
@Entity(tableName = "coffer_room_history_table")
public class HistoryEntity {

    // autoGenerate: 是否自动生成主键值。
    @PrimaryKey(autoGenerate = true)
    public int id;

    // name: 指定字段名，也就是表的列名
    @ColumnInfo(name = "user_name")
    public String userName;


    // defaultValue：设置默认值，未指定值时的默认值
    @ColumnInfo(name = "user_age",defaultValue = "18")
    public int userAge;


    // typeAffinity: 指定字段类型。通过 typeAffinity 属性,可以指定字段的数据类型,如 TEXT、INTEGER 等。
    @ColumnInfo(name = "user_hobby",typeAffinity = ColumnInfo.TEXT)
    public String userHobby;
}
