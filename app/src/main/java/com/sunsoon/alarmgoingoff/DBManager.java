package com.sunsoon.alarmgoingoff;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DBManager {

    private MyDBHelper dbHelper;
    private String TBNAME;

    public DBManager(Context context){
        dbHelper = new MyDBHelper(context);
        TBNAME = MyDBHelper.TB_NAME;
    }

    public void add(TextItem textItem){
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("curtitle", textItem.getCurTitle());
        values.put("curdetail",textItem.getCurDetail());
        sqLiteDatabase.insert(TBNAME,null,values);
        sqLiteDatabase.close();
    }

    public List<TextItem> listAll(){
        List<TextItem> textList = null;
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(TBNAME,null,null,null,null,null,null,null);
        if (cursor!=null){
            textList = new ArrayList<TextItem>();
            while(cursor.moveToNext()){
                TextItem item = new TextItem();
                item.setId(cursor.getInt(cursor.getColumnIndex("ID")));
                item.setCurTitle(cursor.getString(cursor.getColumnIndex("CURTITLE")));
                item.setCurDetail(cursor.getString(cursor.getColumnIndex("CURDETAIL")));
                textList.add(item);
            }
            cursor.close();
        }
        sqLiteDatabase.close();
        return textList;
    }

    public void delete(long id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TBNAME,"ID=?", new String[]{String.valueOf(id)});
        db.close();
    }
}
