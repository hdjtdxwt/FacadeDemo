package com.epsit.proxymodel;

import android.app.ActivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.epsit.proxymodel.bean.Weather;
import com.epsit.proxymodel.proxy.Callback;
import com.epsit.proxymodel.proxy.HttpProxy;

/**
 * 原本的项目用的是Volley，要是哪一天Volley不维护了或者倒闭不过期不用了，我们需要换一个实现方式，要怎么办呢，比如换成Okhttp来实现
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView result;
    String url = "https://www.tianqiapi.com/api/?version=v1&cityid=101280606&city=%E6%B7%B1%E5%9C%B3%E9%BE%99%E5%B2%97";
    String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.weather).setOnClickListener(this);
        result = findViewById(R.id.result);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.weather:
                HttpProxy.getmInstance().get(url, null, new Callback<Weather>() {
                    @Override
                    public void onSuccess(Weather response) {
                        if(response!=null){
                            result.setText(response.getData()!=null && response.getData().size()>0 ?
                                    response.getData().get(0).toString() : "null");
                        }

                    }

                    @Override
                    public void onFail(String error) {
                        Log.e(TAG, "thread="+Thread.currentThread().getName()+ " onErrorResponse="+ error);
                    }
                });
                break;
        }
    }
}
