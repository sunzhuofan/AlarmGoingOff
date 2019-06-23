package com.sunsoon.alarmgoingoff;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SetTextActivity extends AppCompatActivity {
    public final String TAG = "SetTextActivity";

    EditText titleText;
    EditText detailText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_text);
        Intent intent = getIntent();
        String title = intent.getStringExtra("text_title");
        String detail = intent.getStringExtra("text_detail");


        Log.i(TAG, "onCreate: title:" + title);
        Log.i(TAG, "onCreate: detail:" + detail);

        titleText = (EditText)findViewById(R.id.text_title);
        detailText = (EditText)findViewById(R.id.text_detail);
    }

    public void save(View btn){
        Log.i(TAG, "save: ");
        //获取新的值
        String newTitle = titleText.getText().toString();
        String newDetail = detailText.getText().toString();

        Log.i(TAG, "save: 取到新的值");
        Log.i(TAG, "save: newTitle:" + newTitle);
        Log.i(TAG, "save: newDetail:" + newDetail);

        //保存到Bundle或放入到extra
        Intent intent = getIntent();
        Bundle bdl = new Bundle();
        bdl.putString("key_title",newTitle);
        bdl.putString("key_detail",newDetail);
        intent.putExtras(bdl);
        setResult(2,intent);


        //测试数据库
        TextItem item1 = new TextItem(newTitle,newDetail);
        DBManager manager = new DBManager(this);
        manager.add(item1);
        Log.i(TAG, "onOptionsItemSelected: 写入数据完毕");

        //查询所有数据
        List<TextItem> testList = manager.listAll();
        for (TextItem i: testList){
            Log.i(TAG, "save: 取出数据Id=" + i.getId());
           Log.i(TAG, "save: 取出数据Title=" + i.getCurTitle());
            Log.i(TAG, "save: 取出数据Detail=" + i.getCurDetail());
        }

        //回到调用页面
        finish();
    }

}
