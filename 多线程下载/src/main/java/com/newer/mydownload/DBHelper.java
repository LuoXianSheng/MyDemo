package com.newer.mydownload;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mr_LUO on 2016/4/5.
 */
public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "download.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table download_info (" +
                "_id integer primary key," +
                "thread_id integer, " +
                "start_pos integer, " +
                "end_pos integer, " +
                "download_size integer," +
                "url char)");
        System.out.println("数据库创建成功！");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
