package com.epsit.foregroundservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.epsit.foregroundservice.service.DownloadService;
import com.epsit.foregroundservice.util.ScreenManager;
import com.epsit.foregroundservice.util.SystemUtils;

public class SinglePixelActivity extends AppCompatActivity {
    private static final String TAG = SinglePixelActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window mWindow = getWindow();
        mWindow.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams attrParams = mWindow.getAttributes();
        attrParams.x = 0;
        attrParams.y = 0;
        attrParams.height = 1;
        attrParams.width = 1;
        mWindow.setAttributes(attrParams);
        ScreenManager.getInstance(this).setSingleActivity(this);
    }

    @Override
    protected void onDestroy() {
        if (!SystemUtils.isAppAlive(this, null)) {
            Intent intentAlive = new Intent(this, DownloadService.class);
            startService(intentAlive);
        }
        super.onDestroy();
    }
}
