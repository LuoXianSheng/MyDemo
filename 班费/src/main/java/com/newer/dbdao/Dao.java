package com.newer.dbdao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.newer.classfare.Person;
import com.newer.classfare.Rank;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Mr_LUO on 2016/2/4.
 */
public class Dao {

    public static final int CODE_NAME = 1;
    public static final int CODE_DATE = 2;
    public static final int CODE_ID = 3;
    public static final int CODE_ALL_DETAIL = 4;
    public static final int CODE_TYPE = 5;
    public static final int CODE_TYPE_IN = 6;
    public static final int CODE_TYPE_OUT = 7;
    public static final int CODE_PAY_FALSE = 8;

    public static final String TYPE_IN = "收入";
    public static final String TYPE_OUT = "支出";
    public static final String PAY_TRUE = "已缴费";
    public static final String PAY_FALSE = "未缴费";

    private static DBHelper dbHelper;
    private static SQLiteDatabase dbWrLite;
    private static Cursor cursor;

    //插入一条数据
    public static void insert(Context context, String name, String type, String money, String isPay, String remark) {
        dbHelper = new DBHelper(context);
        dbWrLite = dbHelper.getWritableDatabase();
        dbWrLite.execSQL("insert into tab_record values(null,?,?,?,?,?,?)",
                new Object[]{name, type, money, isPay, remark, new Timestamp(new Date().getTime())});
        if (isPay.equals(PAY_TRUE)) {
            //判断rank表中是否有name存在
            cursor = dbWrLite.rawQuery("select total from tab_rank where name = ?", new String[]{name});
            if (cursor.moveToFirst()) {
                dbWrLite.execSQL("update tab_rank set total = ? where name = ?",
                        new Object[]{cursor.getInt(0) + Integer.parseInt(money), name});
            } else {
                dbWrLite.execSQL("insert into tab_rank values(null,?,?)",
                        new Object[]{name, money});
            }
        }
        closeDB();
    }

    public static ArrayList<Person> getAllDetail(Context context, String data, int code) {
        dbHelper = new DBHelper(context);
        dbWrLite = dbHelper.getWritableDatabase();
        ArrayList<Person> list = new ArrayList<>();
        String sql = "select * from tab_record";
        switch (code) {
            case CODE_NAME:
                cursor = dbWrLite.rawQuery(sql + " where name = ? order by date desc",
                        new String[]{data});
                break;
            case CODE_DATE:
                cursor = dbWrLite.rawQuery(sql + " where date like ? order by date desc",
                        new String[]{data});
                break;
            case CODE_ID:
                cursor = dbWrLite.rawQuery(sql + " where id = ? order by date desc",
                        new String[]{data});
                break;
            case CODE_ALL_DETAIL:
                cursor = dbWrLite.rawQuery(sql + " order by date desc", null);
                break;
            case CODE_TYPE:
                cursor = dbWrLite.rawQuery(sql + " where type = ? order by date desc", new String[]{data});
                break;
            case CODE_PAY_FALSE:
                cursor = dbWrLite.rawQuery(sql + " where pay = ?", new String[]{PAY_FALSE});
                break;
        }
        getCursorData(cursor, list);
        cursor.close();
        closeDB();
        return list;
    }

    //获得游标里面的数据
    private static void getCursorData(Cursor cursor, ArrayList<Person> list) {
        if (cursor.moveToFirst()) {
            do {
                Person person = new Person();
                person.setId(cursor.getInt(0));
                person.setName(cursor.getString(1));
                person.setType(cursor.getString(2));
                person.setMoney(cursor.getInt(3));
                person.setPay(cursor.getString(4));
                person.setRemark(cursor.getString(5));
                person.setDate(cursor.getString(6));
                list.add(person);
            }while (cursor.moveToNext());
        }
    }

    //获取余额
    public static int getBalance(Context context, String type) {
        dbHelper = new DBHelper(context);
        dbWrLite = dbHelper.getWritableDatabase();
        if (type.equals(Dao.PAY_TRUE)) {
            cursor = dbWrLite.rawQuery("select sum(money) from tab_record where type = ?", new String[]{TYPE_IN});
        } else {
            cursor = dbWrLite.rawQuery("select sum(money) from tab_record where pay = ?", new String[]{PAY_FALSE});
        }
        if (cursor.moveToFirst()) {
            int sum = cursor.getInt(0);
            if (type.equals(PAY_TRUE)) {
                cursor = dbWrLite.rawQuery("select sum(money) from tab_record where pay = ? or type = ?", new String[]{PAY_FALSE, TYPE_OUT});
                if (cursor.moveToFirst()) {
                    sum -= cursor.getInt(0);
                }
            }
            return sum;
        }
        return 0;
    }

    //查询排行榜数据
    public static ArrayList<Rank> getRankAll(Context context) {
        dbHelper = new DBHelper(context);
        dbWrLite = dbHelper.getWritableDatabase();
        ArrayList<Rank> list = new ArrayList<>();
        cursor = dbWrLite.rawQuery("select * from tab_rank order by total desc", null);
        if (cursor.moveToFirst()) {
            do {
                Rank rank = new Rank();
                rank.setId(cursor.getInt(0));
                rank.setName(cursor.getString(1));
                rank.setTotal(cursor.getInt(2));
                list.add(rank);
            }while (cursor.moveToNext());
        }
        return list;
    }

    private static void closeDB() {
        dbWrLite.close();
        dbHelper.close();
    }
}
