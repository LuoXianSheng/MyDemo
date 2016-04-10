package com.newer.dbdao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mr_LUO on 2016/1/31.
 */
public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "classfare.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table tab_record (" +
                "id integer primary key, " +
                "name text, " +
                "type text, " +
                "money integer, " +
                "pay text, " +
                "remark text, " +
                "date timestamp)");
        db.execSQL("create table tab_rank (" +
                "id integer primary key, " +
                "name text, " +
                "total integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
