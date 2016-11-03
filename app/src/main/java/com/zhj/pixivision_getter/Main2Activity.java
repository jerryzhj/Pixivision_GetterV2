package com.zhj.pixivision_getter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.zhj.pixivision_getter.R;
import com.zhj.pixivision_getter.entity.Pictures;
import com.zhj.pixivision_getter.entity.Pixivision;
import com.zhj.pixivision_getter.util.ConfigManeger;
import com.zhj.pixivision_getter.util.DownloadUtil;
import com.zhj.pixivision_getter.util.ListAdapter;
import com.zhj.pixivision_getter.util.Urlutil;

import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_main2)
public class Main2Activity extends AppCompatActivity {
    private static final String TAG = "Main2Activity";
    @ViewInject(R.id.ListView_simpleShow)
    private ListView listView;
    @ViewInject(R.id.button_all)
    private Button button_queryAll;
    @ViewInject(R.id.button_title)
    private Button button_querryTitle;
    @ViewInject(R.id.button_random)
    private Button button_random;
    @ViewInject(R.id.editText_query)
    private EditText edit;
    List<Pixivision> testList;
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
    private DbManager db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.Ext.init(getApplication());
        x.view().inject(this);

        init();
    }
    @Event(R.id.button_title)
    private void getData3(View v){
        Log.d(TAG,"getDataByTiTle");
        if(edit.getText()!=null){
            String text = edit.getText().toString();
            Log.d(TAG,text);
            testList = new ArrayList<>();
            try {
                testList = db.selector(Pixivision.class).where("name","LIKE","%"+text+"%").findAll();
            } catch (DbException e) {
                e.printStackTrace();
            }
            if(testList.size()==0){
                Toast.makeText(getApplicationContext(),"找不到啊",Toast.LENGTH_SHORT).show();
            }else{
                listView.setAdapter(new ListAdapter(this,testList));
                //listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,getDataByTitle(testList)));
            }
        }else{
            Toast.makeText(getApplicationContext(),"dsa",Toast.LENGTH_SHORT).show();
        }

    }

    private List<String> getDataByTitle(List<Pixivision> testList) {
        ArrayList<String> list = new ArrayList<>();
        for(int i = 0 ; i <testList . size() ;i ++){
            list.add(testList.get(i).getName());
        }
        return list;
    }


    private void init() {
        db = x.getDb(ConfigManeger.daoConfig);
        getData();
        listView.setAdapter(new ListAdapter(this, testList));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int id = testList.get(i).getIndex();
                Log.d("hello","aa");
                Toast.makeText(getApplicationContext(),"dsa",Toast.LENGTH_SHORT).show();
                try {
                    List<Pictures> list = db.selector(Pictures.class).where("pixivsionIndex","LIKE",id).findAll();
                    Log.d("hello",list.toString());
                    DoDownload(list,testList.get(i).getName());
                } catch (DbException e) {
                    e.printStackTrace();
                }
//                Intent intent = new Intent(Main2Activity.this,Activity_Show.class);
//                intent.putExtra("id",id);
//                startActivity(intent);
            }
        });
    }

    private List<Pixivision> getData2() {
        try {
           List<Pixivision> pixivList= db.selector(Pixivision.class).orderBy("date",true).where("type","LIKE","%il%").findAll();
           return pixivList;
            //testList = db.selector(Pixivision.class).where("id", "between", new String[]{random+"", (random+20)+""}).findAll();
        }catch (DbException e){
            e.printStackTrace();
        }
        return null;
    }

    private void DoDownload(List<Pictures> list, String name) {
        makeDir(name);
        for(int i = 0 ; i <list.size() ;i ++){
            Pictures pic = list.get(i);
            //DownloadUtil.downloadFile(pic,name);
            downloadFile2(pic,name);
            Log.d("aa","i="+i+name);
        }
    }


    private void downloadFile(Pictures pic, String name) {
        Log.d("name",name);
        String url = Urlutil.IMAGEURL2+pic.getPicUrl()+"jpg";
        String FileString = "/sdcard/PixivSionGeter/"+name+"/"+pic.getName()+".jpg";
        RequestParams requestParams = new RequestParams(url);
        requestParams.setSaveFilePath(FileString);
        x.http().get(requestParams, new org.xutils.common.Callback.CacheCallback<File>() {
            @Override
            public void onSuccess(File result) {
                Toast.makeText(x.app(), result.getName(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d(TAG,ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.d(TAG,cex.getMessage());
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
    private void makeDir(String name) {
        File dirFirstFolder = new File("/sdcard/PixivSionGeter/"+name);
        if(!dirFirstFolder.exists())
        { //如果该文件夹不存在，则进行创建
            dirFirstFolder.mkdirs();//创建文件夹
        }
    }

    public static void downloadFile2(Pictures pic, String name) {
        final Pictures pics = pic;
        final String namesss = name;
        //String url = "http://i2.pixiv.net/img-original/img/2012/10/04/00/31/37/30559170_p0.png";
        String url2 = "http://i2.pixiv.net/img-original/img/2013/11/14/01/07/15/39729073_p0.png";
        String url3 = "http://i2.pixiv.net/img-original/img/2013/12/05/23/29/22/40117720_p0.png";
        //String FileString = "/sdcard/PixivSionGeter/final2.jpg";
        String url = Urlutil.IMAGE_ORE_URL + pic.getPicUrl().split("p0")[0] + "p0.jpg";
        Log.d("aa",url);
        String FileString = "/sdcard/PixivSionGeter/" + name + "/" + pic.getName() + ".jpg";
        RequestParams requestParams = new RequestParams(url);
        requestParams.addHeader("Referer", url);
        requestParams.setSaveFilePath(FileString);
        x.http().get(requestParams, new org.xutils.common.Callback.CacheCallback<File>() {
            @Override
            public void onSuccess(File result) {
                Toast.makeText(x.app(), result.getName(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d(TAG,ex.getMessage());
                downloadFile3(pics,namesss);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.d(TAG,cex.getMessage());
                downloadFile3(pics,namesss);
            }

            @Override
            public void onFinished() {
                Log.d("hello", "finish");
            }

            @Override
            public boolean onCache(File result) {
                return false;
            }
        });
    }
    public static void downloadFile3(Pictures pic, String name) {
        //String url = "http://i2.pixiv.net/img-original/img/2012/10/04/00/31/37/30559170_p0.png";
        String url2 = "http://i2.pixiv.net/img-original/img/2013/11/14/01/07/15/39729073_p0.png";
        String url3 = "http://i2.pixiv.net/img-original/img/2013/12/05/23/29/22/40117720_p0.png";
        //String FileString = "/sdcard/PixivSionGeter/final2.jpg";
        String url = Urlutil.IMAGE_ORE_URL + pic.getPicUrl().split("p0")[0] + "p0.png";
        Log.d("aa",url);
        String FileString = "/sdcard/PixivSionGeter/" + name + "/" + pic.getName() + ".jpg";
        RequestParams requestParams = new RequestParams(url);
        requestParams.addHeader("Referer", url);
        requestParams.setSaveFilePath(FileString);
        x.http().get(requestParams, new org.xutils.common.Callback.CacheCallback<File>() {
            @Override
            public void onSuccess(File result) {
                Toast.makeText(x.app(), result.getName(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d(TAG,ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.d(TAG,cex.getMessage());
            }

            @Override
            public void onFinished() {
                Log.d("hello", "finish");
            }

            @Override
            public boolean onCache(File result) {
                return false;
            }
        });
    }
    private List<String> getData() {
        List<String> data = new ArrayList<String>();
        int random = (int)(Math.random()*(MainActivity.TOTAL_NUMBER-20));
        try {
            testList= db.selector(Pixivision.class).orderBy("id",true).findAll();
            //testList = db.selector(Pixivision.class).where("id", "between", new String[]{random+"", (random+20)+""}).findAll();
        }catch (DbException e){
            e.printStackTrace();
        }
        Log.d(TAG,testList.toString());
        for(int i=0;i<testList.size();i++){
            data.add(testList.get(i).getName());
        }
        return data;

    }


}
