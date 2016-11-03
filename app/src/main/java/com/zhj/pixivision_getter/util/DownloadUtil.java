package com.zhj.pixivision_getter.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.squareup.picasso.Downloader;
import com.squareup.picasso.Picasso;
import com.zhj.pixivision_getter.entity.Pictures;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ZHJ on 2016/11/1 0001.
 * Purpose:
 */

public class DownloadUtil {
    public static void downloadFile(Pictures pic, String name) {
        String url = Urlutil.IMAGEURL2+pic.getPicUrl()+"jpg";
        String FileString = "/sdcard/PixivSionGeter/"+name+"/"+pic.getName()+".jpg";
        RequestParams requestParams = new RequestParams(url);
        requestParams.setSaveFilePath(FileString);
        x.http().get(requestParams, new org.xutils.common.Callback.CacheCallback<File>() {
            @Override
            public void onSuccess(File result) {
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
                Log.d("hello","finish");
            }

            @Override
            public boolean onCache(File result) {
                return false;
            }
        });

    }
    public static void downloadFile2(Pictures pic, String name) {
        //String url = "http://i2.pixiv.net/img-original/img/2012/10/04/00/31/37/30559170_p0.png";
        String url2 = "http://i2.pixiv.net/img-original/img/2013/11/14/01/07/15/39729073_p0.png";
        //String FileString = "/sdcard/PixivSionGeter/final2.jpg";
        String url = Urlutil.IMAGE_ORE_URL+pic.getPicUrl()+"png";
        String FileString = "/sdcard/PixivSionGeter/"+name+"/"+pic.getName()+".jpg";
        RequestParams requestParams = new RequestParams(url);
        requestParams.addHeader("Referer", url);
        requestParams.setSaveFilePath(FileString);
        x.http().get(requestParams, new org.xutils.common.Callback.CacheCallback<File>() {
            @Override
            public void onSuccess(File result) {
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
                Log.d("hello","finish");
            }

            @Override
            public boolean onCache(File result) {
                return false;
            }
        });

    }

    public static void downAsynFile(Pictures pic, final String name) {
        String url = Urlutil.IMAGEURL2+pic.getPicUrl()+"jpg";
        final String FileString = "/sdcard/PixivSionGeter/"+name+"/"+pic.getName()+".jpg";
        Log.d("aa",url);
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) {
                InputStream inputStream = response.body().byteStream();
                FileOutputStream fileOutputStream = null;
                try {
                    fileOutputStream = new FileOutputStream(new File(FileString));
                    byte[] buffer = new byte[2048];
                    int len = 0;
                    while ((len = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, len);
                    }
                    fileOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.d("wangshu", "文件下载成功"+name);
            }
        });
    }
}
