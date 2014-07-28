package com.nilhcem.xebia.recipes;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class XebiaApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault("fonts/Roboto-Regular.ttf", R.attr.fontPath);
    }
}
