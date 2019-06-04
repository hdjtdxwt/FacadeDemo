package com.epsit.foregroundservice.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.epsit.foregroundservice.R;

public class CancelNoticeService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
            Notification.Builder builder = new Notification.Builder(this);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            startForeground(DaemonService.NOTICE_ID, builder.build());//同样也发一个同样id的消息，会和之前发的消息合并，就在顶部状态栏就一个通知（和要保活的service发的notification合并成一个）
            new Thread(){
                @Override
                public void run() {
                    SystemClock.sleep(1000);

                    stopForeground(true);//当前service取消前台服务
                    //取消前台service的通知
                    NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                    manager.cancel(DaemonService.NOTICE_ID);
                    // 任务完成，终止自己
                    stopSelf();
                }

            }.start();
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
