package com.epsit.foregroundservice.util;

import android.app.ActivityManager;
import android.content.Context;

import java.util.ArrayList;

public class ServiceAliveUtils {
    static Context context;
    public static void init(Context ctx){
        context = ctx;
    }

    public static boolean isServiceAlice() {
        boolean isServiceRunning = false;
        ActivityManager manager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (manager == null) {
            return true;
        }
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.epsit.foregroundservice.service.DownloadService".equals(service.service.getClassName())) {
                isServiceRunning = true;
            }
        }
        return isServiceRunning;
    }

    /*public static boolean isServiceAlice() {
        ActivityManager myManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>)
                myManager.getRunningServices(30);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString().equals("com.epsit.foregroundservice.service.DownloadService")) {
                return true;
            }
        }
        return false;
    }*/
}
