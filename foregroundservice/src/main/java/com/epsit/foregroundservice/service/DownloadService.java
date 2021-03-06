package com.epsit.foregroundservice.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.epsit.foregroundservice.DownloadServiceStub;
import com.epsit.foregroundservice.KeepAliveApplication;
import com.epsit.foregroundservice.R;
import com.epsit.foregroundservice.util.ScreenManager;
import com.epsit.foregroundservice.util.ScreenReceiverUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 主服务，需要在后台运行着的
 */
public class DownloadService extends Service {
    public static final int NOTICE_ID = 100;
    private static final String TAG = DownloadService.class.getSimpleName();
    private DownloadBinder mDownloadBinder;
    private NotificationCompat.Builder mBuilderProgress;
    private NotificationManager mNotificationManager;

    private ScreenReceiverUtil mScreenListener;
    private ScreenManager mScreenManager;
    private Timer mRunTimer;

    private int mTimeSec;
    private int mTimeMin;
    private int mTimeHour;

    private ScreenReceiverUtil.SreenStateListener mScreenListenerer = new ScreenReceiverUtil.SreenStateListener() {
        @Override
        public void onSreenOn() {
            mScreenManager.finishActivity();
            Log.e(TAG, "关闭了1像素Activity");
        }

        @Override
        public void onSreenOff() {
            mScreenManager.startActivity();
            Log.e(TAG, "打开了1像素Activity");
        }

        @Override
        public void onUserPresent() {
        }
    };
    private OnTimeChangeListener mOnTimeChangeListener;

    @Override
    public void onCreate() {
        super.onCreate();

        //不启动这个播音频的service的话，华为麦芒5（android系统6.0的）还是会杀死应用进程，启动了就不会（即使手机的清屏应用里将我们的这个app设置为锁屏清理也不会杀死）
        Intent intent = new Intent(this, PlayerMusicService.class);
        startService(intent);

//        注册锁屏广播监听器
        mScreenListener = new ScreenReceiverUtil(this);
        mScreenManager = ScreenManager.getInstance(this);
        mScreenListener.setScreenReceiverListener(mScreenListenerer);

        mDownloadBinder = new DownloadBinder();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        startRunTimer();//发消息给主线程
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
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mDownloadBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        NotificationManager mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (mManager == null) {
            return;
        }
        mManager.cancel(NOTICE_ID);
        stopRunTimer();
//        mScreenListener.stopScreenReceiverListener();
    }


    private void startRunTimer() {
        TimerTask mTask = new TimerTask() {
            @Override
            public void run() {
                mTimeSec++;
                if (mTimeSec == 60) {
                    mTimeSec = 0;
                    mTimeMin++;
                }
                if (mTimeMin == 60) {
                    mTimeMin = 0;
                    mTimeHour++;
                }
                if (mTimeHour == 24) {
                    mTimeSec = 0;
                    mTimeMin = 0;
                    mTimeHour = 0;
                }
                String time = "时间为：" + mTimeHour + " : " + mTimeMin + " : " + mTimeSec;
                if (mOnTimeChangeListener != null) {
                    mOnTimeChangeListener.showTime(time);
                }
                Log.d(TAG, time);
            }
        };
        mRunTimer = new Timer();
        // 每隔1s更新一下时间
        mRunTimer.schedule(mTask, 1000, 1000);
    }

    private void stopRunTimer() {
        if (mRunTimer != null) {
            mRunTimer.cancel();
            mRunTimer = null;
        }
        mTimeSec = 0;
        mTimeMin = 0;
        mTimeHour = 0;
        Log.d(TAG, "时间为：" + mTimeHour + " : " + mTimeMin + " : " + mTimeSec);
    }

    public interface OnTimeChangeListener {
        void showTime(String time);
    }

    public class DownloadBinder extends Binder {
        public void setOnTimeChangeListener(OnTimeChangeListener onTimeChangeListener) {
            mOnTimeChangeListener = onTimeChangeListener;
        }
    }
}