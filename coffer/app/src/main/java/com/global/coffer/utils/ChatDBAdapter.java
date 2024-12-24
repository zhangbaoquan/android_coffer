package com.global.coffer.utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.global.coffer.bean.ChatMessage;

import java.util.ArrayList;


public class ChatDBAdapter {

    public static final String DATABASE_NAME = "mycofferdatabase.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "mycoffertable";

    //************************数据表 booklist**************************

    //-----------------------数据字段-------------------------
    public static final String KEY_ID = "id";
    public final static String KEY_TASK_ID				        = "taskId";			//任务id
    public final static String KEY_CHAT_ID						= "chatId";			//消息id
    public final static String KEY_CONTENT				        = "content";		//内容

    private ChatDBHelper mDBHelper;
    private SQLiteDatabase mDB;

    private ChatDBAdapter() {
        init();
    }

    public void init(){
        if (mDBHelper == null){
            mDBHelper = new ChatDBHelper();
            mDB = mDBHelper.getWritableDatabase();
        }
    }

    private static final class InstanceHolder {
        private static final ChatDBAdapter instance = new ChatDBAdapter();
    }

    public static ChatDBAdapter getInstance(){
        return InstanceHolder.instance;
    }

    /**
     * 插入消息
     * @param message 消息
     */
    public synchronized void insertChatMessage(ChatMessage message){
        if(message == null){
            return;
        }
        if(mDB == null){
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TASK_ID,message.taskId);
        contentValues.put(KEY_CHAT_ID,message.chatId);
        contentValues.put(KEY_CONTENT,message.content);
        try {
            mDB.beginTransaction();
            mDB.insert(TABLE_NAME, null, contentValues);
            mDB.setTransactionSuccessful(); // 设置事务成功
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            mDB.endTransaction(); // 结束事务
        }
    }

    /**
     * 更新消息
     * @param message 消息
     */
    public synchronized void updateChatMessage(ChatMessage message){
        if(message == null){
            return;
        }
        if(mDB == null){
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TASK_ID,message.taskId);
        contentValues.put(KEY_CHAT_ID,message.chatId);
        contentValues.put(KEY_CONTENT,message.content);
        try {
            mDB.beginTransaction();
            mDB.update(TABLE_NAME, contentValues,KEY_CHAT_ID + "=?",new String[]{message.chatId});
            mDB.setTransactionSuccessful(); // 设置事务成功
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            mDB.endTransaction(); // 结束事务
        }
    }

    /**
     * 查询消息
     * @return 消息
     */
    public synchronized ArrayList<ChatMessage> queryChatMessage(String taskId){
        String sql = "select * from " + TABLE_NAME + " where " + KEY_TASK_ID + "=" + taskId;
        if(TextUtils.isEmpty(taskId)){
            return null;
        }
        if(mDB == null){
            return null;
        }
        Cursor cursor = null;
        ArrayList<ChatMessage> list = new ArrayList<>();
        try {
            mDB.beginTransaction();
            String[] projection = {
                    "id",
                    KEY_TASK_ID,
                    KEY_CHAT_ID,
                    KEY_CONTENT
            };
            cursor = mDB.query(
                    TABLE_NAME,   // 表名
                    projection,   // 要返回的列
                    null,         // WHERE 子句
                    null,         // WHERE 子句的参数
                    null,         // GROUP BY 子句
                    null,         // HAVING 子句
                    null          // 排序方式
            );
            while (cursor.moveToNext()) {
                list.add(getBean(cursor));
            }
            mDB.setTransactionSuccessful(); // 设置事务成功
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            mDB.endTransaction(); // 结束事务
        }
        return list;
    }

    @SuppressLint("Range")
    private ChatMessage getBean(Cursor cursor) {
        if(cursor == null){
            return null;
        }
        ChatMessage message = new ChatMessage();
        message.taskId = cursor.getString(cursor.getColumnIndex(KEY_TASK_ID));
        message.chatId = cursor.getString(cursor.getColumnIndex(KEY_CHAT_ID));
        message.content = cursor.getString(cursor.getColumnIndex(KEY_CONTENT));
        return message;
    }

    /**
     * 数据库字段
     *
     * @author chenxingang
     */
    public static class Field {
        public String FieldName;
        public String FieldType;

        public Field(String name, String type) {
            FieldName = name;
            FieldType = type;
        }
    }
}
