package com.chen.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class OkHttpCallbackFile extends OkHttpCallback implements Callback {

    private final String TAG = OkHttpCallbackFile.class.getSimpleName();
    public String url;
    public byte[] result;
    public int position;
    public OkHttpCallbackFile(int position){
        this.position = position;
    }
    public OkHttpCallbackFile(){}
    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {
        Log.d(TAG,"url:" + url);
        Log.d(TAG,"请求失败" + e.toString());
        onFinish("failure",e.toString().getBytes());
    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
        Log.d(TAG,"url:" + url);
        result = response.body().bytes();
        Log.d(TAG,"请求成功：" + result);
        onFinish("success",result,position);
        onFinish("success",result);
    }

    public void onFinish(String status,byte[] msg){
        Log.d(TAG,"url:" + url + "status:" + status);
    }
    public void onFinish(String status,byte[] msg,int position){
        Log.d(TAG,"url:" + url + "status:" + status);
    }
}
