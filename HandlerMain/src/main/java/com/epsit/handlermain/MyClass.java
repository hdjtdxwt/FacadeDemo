package com.epsit.handlermain;

import com.epsit.handlerdemo.*;

public class MyClass {
    static Handler handler;

    public static void main(String[] args) {
        Looper.prepare();


        Looper mainLooper = Looper.myLooper();
        System.out.println("mainLooper=null ? " + (mainLooper == null ? "true" : "false"));
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                System.out.println("handleMessage-->"+msg.toString());
            }
        };


        new Thread() {
            @Override
            public void run() {
                super.run();
                Message msg = Message.obtain();
                msg.obj = "子线程发给主线程的消息";
                handler.sendMessage(msg); //子线程给主线程发送消息
            }
        }.start();

        if (Looper.myLooper() != null) {
            System.out.println("loop执行了");
            Looper.myLooper().loop();//死循环卡住了界面  activityThread也有

        } else {
            System.out.println("Looper.myLooper() 返回空对象");
        }
    }
}
