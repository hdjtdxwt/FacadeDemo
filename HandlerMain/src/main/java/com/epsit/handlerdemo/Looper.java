package com.epsit.handlerdemo;
public class Looper{
    public MessageQueue mQueue;
    private static ThreadLocal<Looper> sThreadLocal  = new ThreadLocal<>();
    private Looper(){
        mQueue = new MessageQueue();
    }

    //准备MessageQueue
    public static void prepare(){
        Looper looper = sThreadLocal.get();
        if(looper!=null){
            throw new RuntimeException("一个线程只能有一个Looper对象");
        }
        sThreadLocal.set(new Looper());
    }
    public static void prepareMainLooper(){
        
    }
    public static Looper myLooper(){
        return sThreadLocal.get();
    }

    public void loop(){
        final Looper me = myLooper();
        final MessageQueue queue = me.mQueue;//唯一的looper确定mQueue也是唯一的
        for(;;){
            Message msg = queue.next();
            if(msg!=null){
                msg.target.dispatchMessage(msg);
                return;
            }
        }
    }
}