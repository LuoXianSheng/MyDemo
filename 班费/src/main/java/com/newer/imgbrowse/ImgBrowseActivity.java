package com.newer.imgbrowse;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.widget.GridView;

import com.newer.classfare.R;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImgBrowseActivity extends Activity {

    private GridView gridView;
//    private DisplayImageOptions options;    // 设置图片显示相关参数
    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.img_browse_activity);
        gridView = (GridView) findViewById(R.id.gridview);
//        initImageOptions();
        initData();
//        gridView.setAdapter(new GridAdapter(this, list, options));
    }

    private void initData() {
        list = new ArrayList<>();
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        getpicpath(path);
        /*File file = new File(path);
        System.out.println(path);
        File[] allfiles = file.listFiles();
        if (allfiles == null) {
            return;
        }
        for (int k = 0; k < allfiles.length; k++) {
            File fi = allfiles[k];
            if (fi.isFile()) {
                int idx = fi.getPath().lastIndexOf(".");
                if (idx <= 0) {
                    continue;
                }
                String suffix = fi.getPath().substring(idx);
                if (suffix.toLowerCase().equals(".jpg") ||
                        suffix.toLowerCase().equals(".jpeg") ||
                        suffix.toLowerCase().equals(".bmp") ||
                        suffix.toLowerCase().equals(".png") ||
                        suffix.toLowerCase().equals(".gif")) {
                    list.add(fi.getPath());
                }
            } else {
                System.out.println(fi.getName());
            }
        }*/
    }
//
//    private void initImageOptions() {
//        options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.friends_sends_pictures_no)// 设置图片Uri为空或是错误的时候显示的图片
//                .showImageForEmptyUri(R.drawable.friends_sends_pictures_no)
//                .showImageOnFail(R.drawable.friends_sends_pictures_no)
//                .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
//                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
//                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
//                .bitmapConfig(Bitmap.Config.RGB_565)
//                .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
//                .build();
//    }

    private void getpicpath(String sdpath) {
        //打开SD卡目录
        File file = new File(sdpath);
        //获取SD卡目录列表
        File[] files = file.listFiles();
        for (int z = 0; z < files.length; z++) {
            File f = files[z];
            if (f.isFile()) {
                isfile(f);
            } else {
                notfile(f);
            }
        }
    }

    private void isfile(File file) {
        String filename = file.getName();
        int idx = filename.lastIndexOf(".");
        if (idx <= 0) {
            return;
        }
        String suffix = filename.substring(idx);
        if (suffix.toLowerCase().equals(".jpg") ||
                suffix.toLowerCase().equals(".jpeg") ||
                suffix.toLowerCase().equals(".bmp") ||
                suffix.toLowerCase().equals(".png") ||
                suffix.toLowerCase().equals(".gif")) {
            list.add(file.getPath());
        }
    }

    public void notfile(File file) {
        File[] files = file.listFiles();
        if (files == null) {
            return;
        }
        for (int i = 0; i < files.length; i++) {
            File fis = files[i];
            if (fis.isFile()) {
                isfile(fis);
            } else {
                String SDpath = fis.getPath();
                File fileSD = new File(SDpath);
                File[] filess = fileSD.listFiles();
                if (filess == null) {
                    return;
                }
                for (int j = 0; j < filess.length; j++) {
                    getpicpath(fileSD.toString());
                }
            }
        }
    }
}
