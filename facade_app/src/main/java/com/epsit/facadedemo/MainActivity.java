package com.epsit.facadedemo;

import android.app.ActivityManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.epsit.facadedemo.bean.Weather;

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
        startActivity(new Intent(""));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.weather:
                //好处是业务代码和第三方的请求代码耦合低了，还是有耦合的，要是哪一天，Volley的框架稍微改一个地方，我们只需要改我们的FacadeNetwork这一个地方，整个基本还可以运行，如果有什么变化，我们需要改的地方就少了
                //FacadeNetwork 有点像工具类的作用
                FacadeNetwork.getInstance(getApplicationContext()).get(url, null, new FacadeNetwork.Callback<Weather>() {
                    @Override
                    public void onSuccess(Weather response) {
                        if(response!=null){
                            result.setText(response.getData()!=null && response.getData().size()>0 ?
                                    response.getData().get(0).toString() : "null");
                        }

                    }

                    @Override
                    public void onFail(String errorMessage) {
                        Log.e(TAG, "thread="+Thread.currentThread().getName()+ " onErrorResponse="+ errorMessage);
                    }
                });
                break;
        }
    }
}
