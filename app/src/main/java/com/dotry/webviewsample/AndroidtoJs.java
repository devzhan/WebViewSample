package com.dotry.webviewsample;

import android.webkit.JavascriptInterface;

public class AndroidtoJs extends Object{

    // 定义JS需要调用的方法
    // 被JS调用的方法必须加入@JavascriptInterface注解
    @JavascriptInterface
    public void nativeJavaMethod(String msg) {//msg 由js传入

        System.out.println("JS调用了Android的nativeJavaMethod方法==="+msg);
    }
}
