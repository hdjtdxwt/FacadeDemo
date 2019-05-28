package com.epsit.handlerdemo;
public class Message{
    public Handler target;
    public Object obj;
    private int msgId;
    public Message(){

    }
    public Message(int msgId){
        this.msgId = msgId;
    }
    public static Message obtain(){
        Message msg = null;
        msg = new Message();

        return msg;
    }
    public static Message obtain(int msgId){
        Message msg = null;
        msg = new Message();
        msg.msgId = msgId;
        return msg;
    }

    @Override
    public String toString() {
        return obj!=null ?obj.toString():"toString() message 的obj is null";
    }
}

