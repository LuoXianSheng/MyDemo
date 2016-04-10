package com.newer.player;

/**
 * Created by Mr_LUO on 2016/3/23.
 */
public class Config {

    public static final int GONE = 0;//隐藏
    public static final int VISIBILITY = 1;//显示

    public static final int INIT_STATUS = -3;//初始化状态
    public static final int BACKGROUND_STATUS = -2;//停止
    public static final int PLAY_STATUS = 1;//播放
    public static final int PAUSE_STATUS = 0;//暂停
    public static final int NEXT_STATUS = 2;//下一首
    public static final int CONTINUE_STATUS = 3;//继续播放

    public static final String COMPLETION_ACTION = "com.newer.action.COMPLETION_ACTION";  //播放完成
    public static final String MUSIC_CURRENT = "com.newer.action.MUSIC_CURRENT";  //音乐当前时间改变动作
    public static final String MUSIC_DURATION = "com.newer.action.MUSIC_DURATION";//音乐播放长度改变动作
    public static final String BACKGROUND_MUSIC = "com.newer.action.BACKGROUND_MUSIC";//后台播放
    public static final String INIT_ACTION = "com.newer.action.INIT_ACTION";//初始化
    public static final String PAUSE_ACTION = "com.newer.action.PAUSE_ACTION";//暂停
    public static final String PLAY_ACTION = "com.newer.action.PLAY_ACTION";//播放
    public static final String CONTINUE_ACTION = "com.newer.action.CONTINUE_ACTION";//继续
    public static final String RESUME_ACTION = "com.newer.action.RESUME_ACTION";//恢复到暂停状态

    public static final int NOTIFY_ID = 0;
    public static final int PLAY_CODE = 1;//播放请求码
    public static final int NEXT_CODE = 2;//下一曲请求码
    public static final int DELETE_CODE = 0;//清除请求码
    public static final String NOTIFY_NEXT_ACTION = "com.newer.action.NOTIFY_NEXT_ACTION";//通知栏下一曲
    public static final String NOTIFY_PLAY_ACTION = "com.newer.action.NOTIFY_PLAY_ACTION";//通知栏播放
    public static final String NOTIFY_CONTINUE_ACTION = "com.newer.action.NOTIFY_CONTINUE_ACTION";//通知栏继续
    public static final String NOTIFY_PAUSE_ACTION = "com.newer.action.NOTIFY_PAUSE_ACTION";//通知栏暂停
    public static final String NOTIFY_UPDATE_ACTION = "com.newer.action.NOTIFY_UPDATE_ACTION";//通知栏更新
    public static final String NOTIFY_INIT_ACTION = "com.newer.action.NOTIFY_INIT_ACTION";//通知栏初始化
    public static final String NOTIFY_DELETE_ACTION = "com.newer.action.NOTIFY_DELETE_ACTION";//通知栏被清除

}
