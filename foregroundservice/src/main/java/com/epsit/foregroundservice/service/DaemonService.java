package com.epsit.foregroundservice.service;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.epsit.foregroundservice.KeepAliveApplication;
import com.epsit.foregroundservice.R;

/**
 * 前台Service，使用startForeground
 * 这个Service尽量要轻，不要占用过多的系统资源，否则
 * 系统在资源紧张时，照样会将其杀死
 */
public class DaemonService extends Service {
    private static final String TAG = "DaemonService";

    public static final int NOTICE_ID = 100;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "DaemonService---->onCreate被调用，启动前台service");
        //如果API大于18，需要弹出一个可见通知
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) { //18 版本之上的手机系统，也就是4.4之后的系统
            Notification.Builder builder = new Notification.Builder(KeepAliveApplication.getApplication());
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentTitle("KeepAppAlive");
            builder.setContentText("DaemonService is running...");
            startForeground(NOTICE_ID, builder.build()); //显示一个通知在状态栏

            //下面的这个service
            Intent cancelIntent = new Intent(KeepAliveApplication.getApplication(), CancelNoticeService.class);
            startService(cancelIntent);
        }

        // 如果Service被终止
        // 当资源允许情况下，重启service
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        // 如果Service被杀死，干掉通知
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
            NotificationManager mManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            mManager.cancel(NOTICE_ID);
        }
            Log.d(TAG,"DaemonService---->onDestroy，前台service被杀死");
        // 重启自己
        Intent intent = new Intent(getApplicationContext(),DaemonService.class);
        startService(intent);
        super.onDestroy();
    }
}