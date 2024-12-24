package com.global.coffer.demoPage;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.global.coffer.R;
import com.global.coffer.bean.ChatMessage;
import com.global.coffer.utils.ChatDBAdapter;

import java.util.List;


public class SQLiteOperationActivity extends AppCompatActivity {

    private int positionCur;

    private EditText editText1;
    private EditText editText2;

    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ChatDBAdapter.getInstance().init();
        setContentView(R.layout.activity_sqlite_main);
        editText1 = findViewById(R.id.edit1);
        editText2 = findViewById(R.id.edit2);
        textView = findViewById(R.id.content);
        findViewById(R.id.b0).setOnClickListener(v -> {
            Log.i("haha_tag","数据库插入");
            // 插入
            positionCur ++;
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.taskId = "coffer1";
            chatMessage.chatId = "cc"+positionCur;
            chatMessage.content = editText1.getText().toString();
            ChatDBAdapter.getInstance().insertChatMessage(chatMessage);
            Toast.makeText(SQLiteOperationActivity.this,"插入成功",Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.b1).setOnClickListener(v -> {
            // 更新
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.taskId = "coffer1";
            chatMessage.chatId = "cc"+1;
            chatMessage.content = editText2.getText().toString();
            ChatDBAdapter.getInstance().updateChatMessage(chatMessage);
            Toast.makeText(this,"更新成功",Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.b2).setOnClickListener(v -> {
            // 查询
            List<ChatMessage> list = ChatDBAdapter.getInstance().queryChatMessage("coffer1");
            StringBuilder sb = new StringBuilder();
            if(list != null){
                for (int i = 0; i < list.size(); i++) {
                    sb.append(list.get(i).content).append('\n');
                }
            }
            textView.setText(sb.toString());
        });

    }
}
