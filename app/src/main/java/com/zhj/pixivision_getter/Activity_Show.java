package com.zhj.pixivision_getter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * Created by Administrator on 2016/10/11 0011.
 */

public class Activity_Show extends Activity {
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_activity);
        init();
    }

    private void init() {
        Intent intent = getIntent();
        int id = intent.getIntExtra("id",1244);
        webView = (WebView) findViewById(R.id.WebView_Pixiv);
        webView.loadUrl("http://www.pixivision.net/zh/a/"+id);
    }
}
