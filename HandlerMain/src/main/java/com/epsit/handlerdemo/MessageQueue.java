package com.epsit.handlerdemo;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 生产者消费者模式
 */
public class MessageQueue{
    BlockingQueue<Message>queue;//仓库的阻塞对象
    private static final int MAXCOUNT = 64;//仓库大小

    public MessageQueue(){
        queue = new ArrayBlockingQueue<Message>(MAXCOUNT) ;
    }
    //放消息进去
    public void enqueueMessage(Message msg){
        try {
            queue.put(msg);//这是阻塞的操作，放不进去会阻塞等待
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    //消息取出
    public Message next(){
        Message msg = null;
        msg = queue.peek();
        return msg;
    }

}