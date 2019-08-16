package com.dotry.webviewsample;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private WebView mWebView;
    private WebSettings mWebSettings;
    private Button mButtonJavaJS;
    private Button mButtonEvaluateJavascript;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }


    private void init() {
        mButtonJavaJS = findViewById(R.id.java_js);
        mButtonJavaJS.setOnClickListener(this);
        mButtonEvaluateJavascript = findViewById(R.id.java_evaluateJavascript);
        mButtonEvaluateJavascript.setOnClickListener(this);
        mWebView = findViewById(R.id.web_view);
        mWebSettings = mWebView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        // 通过addJavascriptInterface()将Java对象映射到JS对象
        //参数1：Javascript对象名
        //参数2：Java对象名
        mWebView.addJavascriptInterface(new AndroidtoJs(), "test");//AndroidtoJS类对象映射到js的test对象

        mWebView.loadUrl("file:///android_asset/javascript.html");
        //接收js弹框
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
                b.setTitle("Alert");
                b.setMessage(message);
                b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                b.setCancelable(false);
                b.create().show();
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {


                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                Uri uri = Uri.parse(url);
                // 如果url的协议 = 预先约定的 js 协议
                // 就解析往下解析参数
                if (uri.getScheme().equals("js")) {

                    // 如果 authority  = 预先约定协议里的 webview，即代表都符合约定的协议
                    // 所以拦截url,下面JS开始调用Android需要的方法
                    if (uri.getAuthority().equals("webview")) {

                        //  步骤3：
                        // 执行JS所需要调用的逻辑
                        Log.i(TAG, "拦截到相关url，去做其他事宜。");

                    }

                    return true;
                }
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Uri uri = Uri.parse(url);
                // 如果url的协议 = 预先约定的 js 协议
                // 就解析往下解析参数
                if (uri.getScheme().equals("js")) {

                    // 如果 authority  = 预先约定协议里的 webview，即代表都符合约定的协议
                    // 所以拦截url,下面JS开始调用Android需要的方法
                    if (uri.getAuthority().equals("webview")) {

                        //  步骤3：
                        // 执行JS所需要调用的逻辑
                        Log.i(TAG, "拦截到相关url，去做其他事宜。");

                    }

                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });


    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.java_js:
                //java 执行js 可以传入参数。
                mWebView.loadUrl("javascript:callJS(1)");
                break;
            case R.id.java_evaluateJavascript:
                mWebView.evaluateJavascript("javascript:callJS(1)", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {
                        Log.i(TAG, "s is ===" + s);
                    }
                });
                break;

        }
    }
}
