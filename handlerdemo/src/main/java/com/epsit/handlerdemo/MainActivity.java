package com.epsit.handlerdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    String TAG ="MainActivity";
    Handler handler ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Looper.prepare();
        Looper mainLooper = Looper.myLooper();
        Log.e(TAG,"mainLooper=null ? "+(mainLooper==null ? "true" :"false"));
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.e(TAG, "handleMessage");
            }
        };

        //Looper.prepareMainLooper(Looper.getMainLooper());
        setContentView(R.layout.activity_main);

        Message msg = Message.obtain();
        msg.obj="主线程给主线程发的消息";
        handler.sendMessage(msg);

        findViewById(R.id.text).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "测试点击", Toast.LENGTH_SHORT).show();
                Message msg = Message.obtain();
                msg.obj="主线程点击给主线程发的消息";
                handler.sendMessage(msg);
            }
        });
        new Thread(){
            @Override
            public void run() {
                super.run();
                Message msg = Message.obtain();
                msg.obj = "子线程发给主线程的消息";
                handler.sendMessage(msg); //子线程给主线程发送消息
            }
        }.start();
        if(Looper.myLooper()!=null) {
            Log.e(TAG,"loop执行了");
            //Looper.myLooper().loop();//死循环卡住了界面  activityThread也有

        }else{
            Log.e(TAG,"Looper.myLooper() 返回空对象");
        }
    }
}
