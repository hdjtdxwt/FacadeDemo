package com.epsit.proxymodel.proxy;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.epsit.proxymodel.util.HttpUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class OkhttpModel<T> implements IHttp<T> {
    String TAG ="OkhttpModel";
    private OkhttpModel(){}
    Gson gson = new Gson();
    OkHttpClient client = new OkHttpClient();

    private static OkhttpModel instance = new OkhttpModel();

    Handler myHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    public static OkhttpModel getInstance(){
        return instance;
    }
    @Override
    public void get(String url, Map<String, Object> params, final Callback<T> callback) {

        if(TextUtils.isEmpty(url)){
            return;
        }
        String finalUrl = HttpUtil.appendParams(url,params);
        Log.e(TAG, "get() returned: " + finalUrl+"------------");
        Request request = new Request.Builder().url(finalUrl).build();
        //创建Call请求队列
        //请求都是放到一个队列里面的
        Call call = client.newCall(request);

        //开始请求
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                if(!TextUtils.isEmpty(json) && callback!=null){
                    Class<T> cls = HttpUtil.getClassByClass(callback);
                    final T t = gson.fromJson(json, cls);
                    myHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onSuccess(t);
                        }
                    });
                }
            }
            //       失败，成功的方法都是在子线程里面，不能直接更新UI
            @Override
            public void onFailure(Call call, final IOException e) {
                myHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFail(e.getMessage());
                    }
                });
            }


        });
    }

    @Override
    public void post(String url, Map<String, Object> params, final Callback<T> callback) {
        if(TextUtils.isEmpty(url)){
            return;
        }
        String finalUrl = HttpUtil.appendParams(url,params);
        Log.e(TAG, "get() returned: " + finalUrl+"------------");

        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String,Object> e : params.entrySet()) {
            String key = e.getKey();
            String value = e.getValue()!=null ? e.getValue().toString() :"";
            builder.add(key,value);
        }
        FormBody paramsBody= builder.build();
        Request request = new Request.Builder().url(finalUrl).post(paramsBody).build();
        //创建Call请求队列
        //请求都是放到一个队列里面的
        Call call = client.newCall(request);

        //开始请求
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                if(!TextUtils.isEmpty(json) && callback!=null){
                    Class<T> cls = HttpUtil.getClassByClass(callback);
                    final T t = gson.fromJson(json, cls);
                    myHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onSuccess(t);
                        }
                    });
                }
            }
            //       失败，成功的方法都是在子线程里面，不能直接更新UI
            @Override
            public void onFailure(Call call, final IOException e) {
                myHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFail(e.getMessage());
                    }
                });
            }


        });
    }
}
