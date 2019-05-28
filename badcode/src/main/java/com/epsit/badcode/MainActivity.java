package com.epsit.badcode;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.epsit.badcode.bean.Weather;
import com.google.gson.Gson;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView result;
    String url = "https://www.tianqiapi.com/api/?version=v1&cityid=101280606&city=%E6%B7%B1%E5%9C%B3%E9%BE%99%E5%B2%97";
    RequestQueue requestQueue ;
    String TAG = "MainActivity";
    Button weatherBtn;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weatherBtn = findViewById(R.id.weather);
        findViewById(R.id.weather).setOnClickListener(this);
        result = findViewById(R.id.result);
        requestQueue=Volley.newRequestQueue(getApplicationContext());
        handler.sendEmptyMessage(1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG,"thread-->"+Thread.currentThread().getName());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                weatherBtn.setText("获取天气");
            }
        }).start();
        new LooperThread().start();
    }
    private class LooperThread extends Thread {

        @Override
        public void run() {
            Thread.currentThread().setName("OtherThread");
            weatherBtn.setText("other thread获取天气");
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.weather:
                StringRequest strRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if(!TextUtils.isEmpty(s)){
                            Log.e(TAG, "thread="+Thread.currentThread().getName()+ " onResponse="+s);
                            Weather weather = new Gson().fromJson(s, Weather.class);
                            if(weather!=null){
                                List<Weather.DataBean> list = weather.getData();
                                if(list!=null && list.size()>0){
                                    Weather.DataBean bean = list.get(0);
                                    result.setText(bean.toString());
                                }
                            }
                        }else{
                            Log.e(TAG, "thread="+Thread.currentThread().getName()+ " s is null");
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e(TAG, "thread="+Thread.currentThread().getName()+ " onErrorResponse="+volleyError.getMessage());
                    }
                });
                requestQueue.add(strRequest);
                break;
        }
    }
}
