package com.zhj.pixivision_getter;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.zhj.pixivision_getter.entity.CurrentIndex;
import com.zhj.pixivision_getter.entity.Pictures;
import com.zhj.pixivision_getter.entity.Pixivision;
import com.zhj.pixivision_getter.util.ConfigManeger;
import com.zhj.pixivision_getter.util.DownloadUtil;
import com.zhj.pixivision_getter.util.Urlutil;

import org.xutils.DbManager;
import org.xutils.config.DbConfigs;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    private static final int UPDATE_TEXT = 123;
    private static final int UPDATE_PROGRESS = 456;
    public static final int TOTAL_NUMBER = 1800;

    private static final String TAG = "MainActivity_TAG";
    @ViewInject(R.id.My_textView)
    private TextView textView;
    @ViewInject(R.id.button_in)
    private Button button_in ;
    @ViewInject(R.id.ImageView_background)
    private ImageView imageView;
    private Pixivision pv;
    private DbManager db;
    DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
            .setDbName("pixiv.db")
            .setDbDir(new File("/sdcard/HelloPixivABCDEF"))// 不设置dbDir时, 默认存储在app的私有目录.
            .setDbVersion(2)
            .setDbOpenListener(new DbManager.DbOpenListener() {
                @Override
                public void onDbOpened(DbManager db) {
                    // 开启WAL, 对写入加速提升巨大
                    db.getDatabase().enableWriteAheadLogging();
                }
            });
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_TEXT:
                    textView.setText("数据存储完成");
                    Picasso.with(getApplicationContext()).load(R.drawable.end).fit().centerCrop().into(imageView);
                    button_in.setVisibility(View.VISIBLE);
                    break;
                case UPDATE_PROGRESS:
                    String progress = msg.obj.toString();
                    textView.setText("数据存储完成了"+progress+"%");
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.Ext.init(getApplication());
        x.view().inject(this);
        init();
        if(!isGetData()){
            Toast.makeText(getApplicationContext(),"没有基础数据啊",Toast.LENGTH_SHORT).show();
        }else{
            //getUpDate();
            Log.d(TAG,"DataGet");
            Message msg = new Message();
            msg.what=UPDATE_TEXT;
            handler.sendMessage(msg);
            Log.d("TAG","finsh");
        }
    }
    @Event(R.id.button_init)
    private void doTest(View v) {
        doSave2();
        textView.setText("hello");
        Toast.makeText(getApplicationContext(),"start",Toast.LENGTH_SHORT).show();;
    }


    private boolean isGetData() {
        Pixivision sv = null;
        try {
            sv =  db.findFirst(Pixivision.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (sv!=null){
            return true;
        }else{
            return false;
        }
    }

    @Event(R.id.button_in)
    private void moveOn(View v){
        //DownloadUtil.downloadFile2();
        Log.d(TAG,"moveon");
        Intent intent = new Intent(getApplicationContext(),Main2Activity.class);
        startActivity(intent);
    }

    private void init() {
        Picasso.with(getApplicationContext()).load(R.drawable.start).fit().centerCrop().into(imageView);
        db = x.getDb(ConfigManeger.daoConfig);
        //db = x.getDb(new ConfigManeger().daoConfig);
   }

    private void doSave2(){
        ExecutorService executorService = Executors.newFixedThreadPool(30) ;

        //执行一个任务  该任务是 new Runnable() 对象
        for(int i = 0 ; i <TOTAL_NUMBER ; i ++) {
            final int index = i ;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    getAndSaveData(index);
                    if(index%(TOTAL_NUMBER/100)==0) {
                        Message msg = new Message();
                        msg.what = UPDATE_PROGRESS;
                        msg.obj = index/(TOTAL_NUMBER/100);
                        handler.sendMessage(msg);
                    }
                }
            });
        }
        //关闭线程池
        executorService.shutdown();
        if(executorService.isTerminated()){
            Message msg = new Message();
            msg.what=UPDATE_TEXT;
            handler.sendMessage(msg);
            Log.d("TAG","finsh");
        }
    }



    private void getAndSaveData(int i) {
        HttpURLConnection connection = null;
        List<String> name_list = new ArrayList<>();
        List<String> author_list = new ArrayList<>();
        List<String> url_list = new ArrayList<>();
        List<String> url_list2 = new ArrayList<>();
        List<String> url_list3 = new ArrayList<>();
        try {
            String ur = Urlutil.BASEURL + i;
            URL url = new URL(ur);
            connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("Accept-Language", Urlutil.CHINESE);
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            int count = 0;
            String title = "";
            String content = "";
            String date = "";
            String type = "";
            int pointer1 = -1 ;
            int pointer2 = -1 ;
            int pointer_date = -1 ;
            int pointer_type = -1 ;
            while ((line = reader.readLine()) != null) {
                count++;
               // Log.d("aaaaaaaaaaaaaaaaaa",line);
                if(line.contains(Urlutil.PIXIVER_ICON)){
                    url_list2.add(line);
                }
                if(line.contains(Urlutil.PIXIVER_INERLINK)){
                    url_list3.add(line);
                }
                if(line.contains(Urlutil.PICURL)){
                   url_list.add(getrealUrl(line));
                }
                if(line.contains(Urlutil.PICNAME)){
                    pointer1 = count+2;
                }
                if(count == pointer1){
                    name_list.add(line.split("<")[0]);
                }
                if(line.contains(Urlutil.DATE)){
                    Log.d("Date2",line);
                    pointer_date = count+1;
                }
                if(count==pointer_date){
                    date = line;
                    Log.d("Date",date);
                }
                if(line.contains(Urlutil.TYPE)){
                    pointer_type = count+1;
                }
                if(count==pointer_type){
                    type = line;
                }
                if(line.contains(Urlutil.PIXIVER)){
                    pointer2 = count+3;
                }
                if(count == pointer2){
                    author_list.add(line.split("<")[0]);
                }
                if (count == 14) {
                    Log.d("title", line);
                    title = getLine(line);
                }
                if (count == 15) {
                    Log.d("countent", line);
                    content = getLine(line);
                }
                response.append(line);
            }
            Log.d("list_url",url_list.toString());
            Log.d("list_url2",url_list.toString());
            Log.d("list_url3",url_list.toString());
            Log.d("list_author_url",author_list.toString());
            Log.d("list_name_url",name_list.toString());
            //Log.d("hello my friend",url_list.size()+"aa"+name_list.size()+"aa"+author_list.size());
            if(author_list.size()!=0&&url_list.size()!=0&&name_list.size()!=0) {
                List<Pictures> picturesList = getPictureData(url_list, author_list, name_list, i);
                db.save(picturesList);
                pv = new Pixivision();
                pv.setLocalIndex(i);
                pv.setName(title);
                pv.setDescription(content);
                pv.setIndex(i);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
                pv.setDate(sdf.parse(date));
                pv.setType(type);
                db.save(pv);
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(connection!=null)
                connection.disconnect();
        }
    }

    private List<Pictures> getPictureData(List<String> url_list, List<String> author_list, List<String> name_list, int index) {
        List<Pictures> list = new ArrayList<>();
        Pictures p ;
        Log.d("index",index+"");
        for(int i = 0 ; i <url_list.size();i++){
            p = new Pictures();
            p.setName(name_list.get(i));
            p.setAuthor(author_list.get(i));
            p.setPicUrl(url_list.get(i));
            p.setPixivsionIndex(index);
            list.add(p);
        }
        return list;
    }



    private String getLine(String line) {
        String str2 = line.split("content=\"")[1];
        String str3 = str2.split("\">")[0];
        return str3;
    }
    private String getrealUrl(String str){
        String url = str.split("768x1200_80/img-master/img/")[1];
        String url2 = url.split("jpg")[0];
        return url2;
    }
}
