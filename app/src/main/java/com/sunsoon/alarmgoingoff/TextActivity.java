package com.sunsoon.alarmgoingoff;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LauncherActivity;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TextActivity extends AppCompatActivity implements Runnable,AdapterView.OnItemLongClickListener {
    private static final String TAG = "TextActivity";
    String textTitle;
    String textDetail;
    String data[];
    Handler handler;
    SharedPreferences sharedPreferences;
    private List<HashMap<String,String>> listItem;
    private SimpleAdapter adapter;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        initListView();

        //获取SP里保存的数据
        sharedPreferences = getSharedPreferences("owntext",Activity.MODE_PRIVATE);
        textTitle = sharedPreferences.getString("text_title",null);
        textDetail = sharedPreferences.getString("text_detail",null);
        Log.i("SharedPreferences", "onCreate: sp my_title=" + textTitle);
        Log.i("SharedPreferences", "onCreate: sp my_detail=" + textDetail);

        Thread thread = new Thread(this);
        thread.start();


        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {

                if (msg.what == 5){
                    listItem = (List<HashMap<String, String>>)msg.obj;
                    adapter = new SimpleAdapter(TextActivity.this,listItem,R.layout.list_item,
                            new String[]{"ItemTitle","ItemDetail"},
                            new int[]{R.id.itemTitle,R.id.itemDetail});
                    listView = (ListView)findViewById(R.id.listView);
                    listView.setAdapter(adapter);
                    listView.setOnItemLongClickListener(TextActivity.this);
                }
                super.handleMessage(msg);
            }
        };
    }

    private  void initListView(){
        listItem = new ArrayList<HashMap<String, String>>();
        for(int i = 0; i<10 ; i++){
            HashMap<String,String> map = new HashMap<String, String>();
            map.put("ItemTitle","Rate:"+i);//标题文字
            map.put("ItemDetail","detail:"+i);//详情描述
            listItem.add(map);
        }
        //生成适配器的ITEM和动态数组对应的元素
        adapter = new SimpleAdapter(this,listItem,//listItems 数据源
                R.layout.list_item,//ListItem的xml布局实现
                new String[]{"ItemTitle","ItemDetail"},
                new int[]{R.id.itemTitle,R.id.itemDetail}
        );
    }

    public void openSetText(View btn){openNewText();}

    private void openNewText(){
        Intent newText = new Intent(this,SetTextActivity.class);
        newText.putExtra("key_title",textTitle);
        newText.putExtra("key_detail",textDetail);

        Log.i("TextActivity", "openNewText: textTitle = "+textTitle);
        Log.i("TextActivity", "openNewText: textDetail = "+textDetail);

        startActivityForResult(newText,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == 2) {
            Bundle bundle = data.getExtras();
            textTitle = bundle.getString("key_title");
            textDetail = bundle.getString("key_detail");
            Log.i("result", "onActivityResult: textTitle:" + textTitle);
            Log.i("result", "onActivityResult: textDetail:" + textDetail);


            Thread thread = new Thread(this);
            thread.start();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void run() {
        Log.i("run", "running");
        List<HashMap<String, String>> retlist = new ArrayList<>();
        try{
            Thread.sleep(1000);
            //把数据写入到数据库中
            DBManager dbManager = new DBManager(this);
            for (TextItem item : dbManager.listAll()){
                //获取数据，带入到list中
                Log.i("run", "run: 取出数据Id=" + item.getId());
                Log.i("run", "run: 取出数据Title=" + item.getCurTitle());
                Log.i("run", "run: 取出数据Detail=" + item.getCurDetail());
                HashMap<String,String> map = new HashMap<String, String>();
                map.put("ItemTitle",item.getCurTitle());
                map.put("ItemDetail",item.getCurDetail());
                retlist.add(map);
            }

        } catch (InterruptedException e){
            e.printStackTrace();
        }
        Message msg = handler.obtainMessage(5);
        msg.obj = retlist;
        handler.sendMessage(msg);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, final long id) {
        Log.i("onItemLongClick", "onItemLongClick: 长按列表项position="+position);
        //删除操作
        //listItems.remove(position);
        //listItemAdapter.notifyDataSetChanged();
        //构造对话框进行确认操作
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示").setMessage("请确认是否删除当前备忘事件").setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("onItemLongClick", "onClick: 对话框事件处理");
                DBManager manager = new DBManager(TextActivity.this);
                manager.delete(id+1);
                listItem.remove(position);
                adapter.notifyDataSetChanged();
            }
        }).setNegativeButton("否",null);
        builder.create().show();
        Log.i("onItemLongClick", "onItemLongClick: size="+listItem.size());
        return true;
    }



}
