package com.epsit.foregroundservice.util;

import android.content.Context;

public class SystemUtils {
    public static boolean isAppAlive(Context context, String packageName){
        return ServiceAliveUtils.isServiceAlice();
    }
}
