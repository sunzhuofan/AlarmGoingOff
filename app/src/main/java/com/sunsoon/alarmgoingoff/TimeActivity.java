package com.sunsoon.alarmgoingoff;

import android.app.Activity;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimeZone;

public class TimeActivity extends Activity implements AdapterView.OnItemClickListener,TextWatcher {

    private ListView listView;
    private HashMap<String, String> map = new HashMap<String, String>();
    private ArrayList<String> list = new ArrayList<String>();
    private EditText editText;
    private myadapter name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        listView = (ListView)findViewById(R.id.aabb);
        editText = (EditText)findViewById(R.id.ss);
        editText.addTextChangedListener(this);
        getdata();
        name = new myadapter();
        listView.setAdapter(name);
        listView.setOnItemClickListener(this);
    }

    public String getTime(String id) {
        TimeZone tz = TimeZone.getTimeZone(id);
        //String s = "TimeZone " + tz.getDisplayName(false, TimeZone.SHORT)
        //+ " Timezon id :: " + tz.getID();
        Time time = new Time(tz.getID());
        time.setToNow();
        int year = time.year;
        int month = time.month;
        int day = time.monthDay;
        int minute = time.minute;
        int hour = time.hour;
        int sec = time.second;
        return year + "年 " + (month+1) + "月 " + day + "日 " + hour + "时 " + minute + "分 " + sec + "秒";
    }

    public void getdata() {
        try {
            map.clear();
            list.clear();
            Resources res = getResources();
            XmlResourceParser xrp = res.getXml(R.xml.timezones);
            while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT) {
                if (xrp.getEventType() == XmlResourceParser.START_TAG) {
                    String name = xrp.getName();
                    if (name.equals("timezone")) {
                        if(xrp.getAttributeValue(1).indexOf(editText.getText().toString()) != -1) {
                            map.put(xrp.getAttributeValue(1),
                                    xrp.getAttributeValue(0));
                            list.add(xrp.getAttributeValue(1));
                        }
                    }
                }
                xrp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //适配器类
    class myadapter extends BaseAdapter {
        Holder holder;

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(int pos, View view, ViewGroup arg2) {
            holder = new Holder();
            if(view == null) {
                view = LayoutInflater.from(TimeActivity.this).inflate(R.layout.item, null);
                holder.view = (TextView)view.findViewById(R.id.aagg);
                view.setTag(holder);
            } else {
                holder = (Holder) view.getTag();
            }
            holder.view.setText(list.get(pos));
            return view;
        }
        class Holder{
            public TextView view;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
        //显示当前时区时间
        TextView textView = (TextView)view.findViewById(R.id.aagg);
        Toast.makeText(TimeActivity.this, getTime(map.get(textView.getText().toString())), Toast.LENGTH_LONG).show();
    }

    @Override
    public void afterTextChanged(Editable arg0) {
    }

    @Override
    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                  int arg3) {
    }

    @Override
    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        getdata();
        name.notifyDataSetChanged();
    }
}
