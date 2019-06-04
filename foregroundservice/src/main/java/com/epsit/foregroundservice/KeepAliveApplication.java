package com.epsit.foregroundservice;

import android.app.Application;

import com.epsit.foregroundservice.util.ServiceAliveUtils;

public class KeepAliveApplication extends Application {
    private String TAG = "KeepAliveApplication";
    private static KeepAliveApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        ServiceAliveUtils.init(this);
    }

    public static KeepAliveApplication getApplication() {
        return instance;
    }

}
