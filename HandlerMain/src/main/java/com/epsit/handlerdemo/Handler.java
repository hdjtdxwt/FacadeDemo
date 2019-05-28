package com.epsit.handlerdemo;

public class Handler{
    final Looper mLooper;
    MessageQueue mQueue;

    public Handler(){
        mLooper = Looper.myLooper();
        mQueue = mLooper.mQueue;
    }
    public void sendMessage(Message message){
        message.target = this;
        enqueueMessage(message);
    }
    public void enqueueMessage(Message msg){
        mQueue.enqueueMessage(msg);
    }
    //handler的queue是一块内存区域，new Thread()创建的子线程，可以拿到主线程的handler对象，可以往MessageQueue发消息，然后让消息在主线程里头执行，就跨越了线程通信
    public void dispatchMessage(Message msg){
        handleMessage(msg);
    }

    public void handleMessage(Message msg){

    }
}