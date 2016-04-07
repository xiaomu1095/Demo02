package com.example.ll.demo02.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SettingDbHelper extends SQLiteOpenHelper{

    private static final int dbVersion = 1;

    public SettingDbHelper(Context context){
        super(context, "SettinfDB", null , 1);
    }

    public SettingDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    //严谨的单例模式
    private static volatile SettingDbHelper sInst = null;  // <<< 这里添加了 volatile
    public static SettingDbHelper getInstance(Context context) {
        SettingDbHelper inst = sInst;  // <<< 在这里创建临时变量
        if (inst == null) {
            synchronized (SettingDbHelper.class) {
                inst = sInst;
                if (inst == null) {
                    inst = new SettingDbHelper(context,"",null,dbVersion);
                    sInst = inst;
                }
            }
        }
        return inst;  // <<< 注意这里返回的是临时变量
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE  TABLE table_name(_id INTEGER PRIMARY KEY , filename VARCHAR, data TEXT)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }



}
