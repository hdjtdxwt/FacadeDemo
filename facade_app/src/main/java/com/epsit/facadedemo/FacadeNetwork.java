package com.epsit.facadedemo;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.epsit.facadedemo.util.HttpUtil;
import com.google.gson.Gson;

import java.util.Map;

public class FacadeNetwork<T> {
    public interface Callback<T>{
        void onSuccess(T response);
        void onFail(String errorMessage);
    }
    public static RequestQueue requestQueue;
    private static FacadeNetwork instance;

    String TAG ="FacadeNetwork";
    Gson gson;
    Context mContext;

    private FacadeNetwork(Context context){
        mContext = context;
        gson = new Gson();
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }
    public static FacadeNetwork getInstance(Context context){
        if(instance ==null){
            synchronized (FacadeNetwork.class){
                if(instance == null){
                    instance = new FacadeNetwork(context);
                }
            }
        }
        return instance;
    }
    public <T> void get(String url, Map<String,Object> params, final Callback<T> callback){
        String finalUrl = HttpUtil.appendParams(url,params);
        Log.e(TAG,"finalUrl="+finalUrl);
        StringRequest request = new StringRequest( Request.Method.GET, finalUrl,new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(callback!=null && !TextUtils.isEmpty(s)){
                    Class cls = HttpUtil.getClassByClass(callback);
                    Log.e(TAG,"class="+cls.getName());
                    T object = (T)gson.fromJson(s,cls);
                    callback.onSuccess(object);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(volleyError!=null && callback!=null){
                    callback.onFail(volleyError.getMessage());
                }
            }
        });
        requestQueue.add(request);
    }
    public <T> void post(String url, Map<String,Object> params, final Callback<T> callback){
        String finalUrl = HttpUtil.appendParams(url,params);
        StringRequest request = new StringRequest( Request.Method.POST, finalUrl,new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(callback!=null && !TextUtils.isEmpty(s)){
                    Class cls = HttpUtil.getClassByClass(callback);
                    T object = (T)gson.fromJson(s,cls);
                    callback.onSuccess(object);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(volleyError!=null && callback!=null){
                    callback.onFail(new String(volleyError.networkResponse.data));
                }
            }
        });
        requestQueue.add(request);
    }
}
