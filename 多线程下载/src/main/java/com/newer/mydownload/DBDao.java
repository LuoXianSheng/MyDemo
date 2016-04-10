package com.newer.mydownload;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr_LUO on 2016/4/5.
 */
public class DBDao {

    private static DBDao dbDao = null;
    private Context context;

    private DBDao(Context context) {
        this.context = context;
    }

    public static DBDao getInstance(Context context) {//获取一个操作类的实例
        if (dbDao == null) {
            dbDao = new DBDao(context);
        }
        return dbDao;
    }

    public SQLiteDatabase getConnection() {
        SQLiteDatabase sqliteDatabase = null;
        try {
            sqliteDatabase = new DBHelper(context).getWritableDatabase();
        } catch (Exception e) {
        }
        return sqliteDatabase;
    }

    /**
     * 查看数据库中是有已经有了下载记录
     *
     * @param url 下载的url
     * @return
     */
    public synchronized boolean getDownloadRecord(String url) {
        SQLiteDatabase db = getConnection();
        String sql = "select count(*) from download_info where url = ?";
        int count = 0;//下载记录数
        Cursor cursor = db.rawQuery(sql, new String[]{url});
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count == 0;
    }

    /**
     * 保存下载信息
     * @param list 各个线程下载的信息的集合
     */
    public synchronized void saveDownloadinfos(List<DownloadInfo> list) {
        SQLiteDatabase db = getConnection();
        String sql = "insert into download_info values(null, ?, ?, ?, ?, ?)";
        for (DownloadInfo downloadInfo : list) {
            db.execSQL(sql, new Object[]{downloadInfo.getThreadID(), downloadInfo.getStartPosition(),
                    downloadInfo.getEndPosition(),downloadInfo.getDownloadSize(), downloadInfo.getUrl()});
        }
        db.close();
    }

    /**
     * 更新下载信息
     * @param threadID
     * @param downloadSize
     * @param url
     */
    public synchronized void updateDownloadinfos(int threadID, int downloadSize, String url) {
        SQLiteDatabase db = getConnection();
        String sql = "update download_info set download_size = ? where thread_id = ? and url = ?";
        db.execSQL(sql, new Object[]{downloadSize, threadID, url});
        db.close();
    }

    /**
     * 获取所有线程下载信息
     * @param url
     * @return
     */
    public synchronized List<DownloadInfo> getDownloadInfos(String url) {
        List<DownloadInfo> list = new ArrayList<>();
        SQLiteDatabase db = getConnection();
        String sql = "select * from download_info where url = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{url});
        while (cursor.moveToNext()) {
            DownloadInfo downloadInfo = new DownloadInfo(cursor.getInt(1), cursor.getInt(2), cursor.getInt(3),
                    cursor.getInt(4), url);
            list.add(downloadInfo);
        }
        cursor.close();
        db.close();
        return list;
    }

    /**
     * 下载完后删除记录
     * @param url
     */
    public synchronized void deleteDownloadInfos(String url) {
        SQLiteDatabase db = getConnection();
        String sql = "delete from download_info where url = ?";
        db.execSQL(sql, new Object[]{url});
        db.close();
    }
}
