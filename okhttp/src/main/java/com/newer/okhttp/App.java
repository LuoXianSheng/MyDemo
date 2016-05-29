package com.newer.okhttp;

import android.app.Application;
import android.util.Log;

import org.xutils.DbManager;
import org.xutils.x;

/**
 * Created by Mr_LUO on 2016/4/12.
 */
public class App extends Application {

    public static final String TAG = "OKHttp";

    private static DbManager.DaoConfig daoConfig;

    public static DbManager.DaoConfig getDaoConfig() {
        return daoConfig;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);//Xutils初始化
        x.Ext.setDebug(true);

        daoConfig = new DbManager.DaoConfig()
                .setDbName("csu")
                .setDbVersion(1);

        Log.e(TAG, "初始化APP成功！");
    }
}
