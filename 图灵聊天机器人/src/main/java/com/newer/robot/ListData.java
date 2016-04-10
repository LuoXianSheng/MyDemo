package com.newer.robot;

import java.util.List;

/**
 * Created by Mr_LUO on 2016/3/15.
 */
public class ListData {

    public static final int LEFT = 0;
    public static final int RIGET = 1;

    private int direction;
    private int code;
    private String content;
    private String url;
    private List<News> newses;
    private List<Menus> menuses;

    public ListData(int direction, int code, String content, String url, List<News> newses, List<Menus> menuses) {
        this.direction = direction;
        this.code = code;
        this.content = content;
        this.url = url;
        this.newses = newses;
        this.menuses = menuses;
    }

    public int getCode() {
        return code;
    }

    public int getDirection() {
        return direction;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    public List<News> getNewses() {
        return newses;
    }

    public List<Menus> getMenuses() {
        return menuses;
    }
}
