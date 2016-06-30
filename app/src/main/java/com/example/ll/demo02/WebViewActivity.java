package com.example.ll.demo02;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class WebViewActivity extends AppCompatActivity {

    private WebView webview;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_web_view);

        webview = (WebView) findViewById(R.id.webview);
        progress = (ProgressBar) findViewById(R.id.progress);
        if (webview != null) {
//            WebSettings webSettings = webview.getSettings();
//            webSettings.setJavaScriptEnabled(true);
//            webview.loadUrl("http://images.entertainment.ie/images_content/rectangle/620x372/success-kid.jpg");
            webview.loadUrl("http://www.baidu.com");
            webview.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    progress.setVisibility(View.GONE);
                    super.onPageFinished(view, url);
                }
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    progress.setVisibility(View.VISIBLE);
                    progress.setProgress(webview.getProgress());
                    super.onPageStarted(view, url, favicon);
                }
            });

            webview.setWebChromeClient(new WebChromeClient(){
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    super.onProgressChanged(view, newProgress);
                }
            });
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



    //在 Activity 销毁的时候，可以先让 WebView 加载null内容，然后移除 WebView，再销毁 WebView，最后置空。
    @Override
    protected void onDestroy() {
        if (webview != null) {
            webview.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webview.clearHistory();

            ((ViewGroup) webview.getParent()).removeView(webview);
            webview.destroy();
            webview = null;
        }
        super.onDestroy();
    }
}
