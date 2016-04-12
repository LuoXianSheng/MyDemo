package com.newer.okhttp;

import android.app.Application;
import android.util.Log;

import org.xutils.x;

/**
 * Created by Mr_LUO on 2016/4/12.
 */
public class App extends Application {

    public static final String TAG = "OKHttp";

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true);
        Log.e(TAG, "初始化APP成功！");
    }
}
