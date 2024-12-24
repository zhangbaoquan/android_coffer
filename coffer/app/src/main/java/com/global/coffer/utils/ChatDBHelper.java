package com.global.coffer.utils;

import static com.global.coffer.utils.ChatDBAdapter.DATABASE_NAME;
import static com.global.coffer.utils.ChatDBAdapter.DATABASE_VERSION;
import static com.global.coffer.utils.ChatDBAdapter.KEY_CHAT_ID;
import static com.global.coffer.utils.ChatDBAdapter.KEY_CONTENT;
import static com.global.coffer.utils.ChatDBAdapter.KEY_ID;
import static com.global.coffer.utils.ChatDBAdapter.KEY_TASK_ID;
import static com.global.coffer.utils.ChatDBAdapter.TABLE_NAME;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.global.coffer.CofferApplication;

import java.util.ArrayList;

public class ChatDBHelper extends SQLiteOpenHelper {
    public ChatDBHelper() {
        super(CofferApplication.Companion.getInstance(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("haha_tag","DBHelper onCreate");
        ArrayList<ChatDBAdapter.Field> fields = new ArrayList<>();
        fields.add(new ChatDBAdapter.Field(KEY_ID, "integer primary key autoincrement"));
        fields.add(new ChatDBAdapter.Field(KEY_TASK_ID, "text"));
        fields.add(new ChatDBAdapter.Field(KEY_CHAT_ID, "text"));
        fields.add(new ChatDBAdapter.Field(KEY_CONTENT, "text"));
        StringBuilder sb = new StringBuilder();
        sb.append("create table if not exists ");
        sb.append(TABLE_NAME);
        sb.append(" (");
        ChatDBAdapter.Field curField;
        for (int i = 0, j = fields.size(); i < j; i++) {
            curField = fields.get(i);
            if (curField != null) {
                sb.append(curField.FieldName);
                sb.append(" ");
                sb.append(curField.FieldType);
                if (i != j - 1) sb.append(",");
            }
        }
        sb.append(");");
        db.execSQL(sb.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 如果表已存在则删除，重新创建
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }
}
